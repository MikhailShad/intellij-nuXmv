package dev.mikhailshad.nuxmvplugin.language.psi.scope

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import dev.mikhailshad.nuxmvplugin.language.psi.*

/**
 * Cached lookup tables for module-scoped declarations and file-scoped module names.
 *
 * All caches are invalidated on `PsiModificationTracker.MODIFICATION_COUNT`, which is the
 * documented dependency for subtree-scoped PSI computations. Callers should never call
 * `PsiTreeUtil.findChildrenOfType` over a module body — go through here instead.
 */
object NuXmvScopes {
    private val MODULE_SCOPE_KEY: Key<CachedValue<NuXmvModuleScope>> =
        Key.create("nuxmv.module.scope")
    private val FILE_MODULES_KEY: Key<CachedValue<Map<String, NuXmvModule>>> =
        Key.create("nuxmv.file.modules")
    private val FILE_FOR_LOOP_MACROS_KEY: Key<CachedValue<List<NuXmvForLoopMacro>>> =
        Key.create("nuxmv.file.forLoopMacros")

    /** Returns a cached `NuXmvModuleScope` for [module]; safe to call from any thread inside a read action. */
    fun of(module: NuXmvModule): NuXmvModuleScope =
        CachedValuesManager.getCachedValue(module, MODULE_SCOPE_KEY) {
            CachedValueProvider.Result.create(
                buildScope(module),
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }

    /** Cached `moduleName -> NuXmvModule` table for [file]. */
    fun modulesIn(file: NuXmvFile): Map<String, NuXmvModule> =
        CachedValuesManager.getCachedValue(file, FILE_MODULES_KEY) {
            val map: MutableMap<String, NuXmvModule> = LinkedHashMap()
            for (child in file.children) {
                if (child is NuXmvModule) {
                    val name = child.moduleDeclaration.moduleName?.name ?: continue
                    map.putIfAbsent(name, child)
                }
            }
            CachedValueProvider.Result.create(map.toMap(), PsiModificationTracker.MODIFICATION_COUNT)
        }

    /** All FOR-loop macros declared anywhere in [file], cached. */
    fun forLoopMacrosIn(file: NuXmvFile): List<NuXmvForLoopMacro> =
        CachedValuesManager.getCachedValue(file, FILE_FOR_LOOP_MACROS_KEY) {
            val macros = ArrayList<NuXmvForLoopMacro>()
            collectForLoopMacros(file, macros)
            CachedValueProvider.Result.create(
                macros.toList(),
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }

    private fun collectForLoopMacros(element: PsiElement, sink: MutableList<NuXmvForLoopMacro>) {
        var child = element.firstChild
        while (child != null) {
            if (child is NuXmvForLoopMacro) {
                sink.add(child)
            }
            collectForLoopMacros(child, sink)
            child = child.nextSibling
        }
    }

    private fun buildScope(module: NuXmvModule): NuXmvModuleScope {
        val declarations = LinkedHashMap<String, NuXmvNamedElement>()
        val variables = LinkedHashMap<String, NuXmvVarName>()
        val defines = LinkedHashMap<String, NuXmvDefineName>()
        val parameters = LinkedHashMap<String, NuXmvModuleParameter>()
        val submoduleEdges = LinkedHashMap<String, String>()
        val assignmentsByVar = LinkedHashMap<String, MutableList<NuXmvSingleAssignConstraint>>()
        val ambiguous = HashSet<String>()

        fun register(name: String?, element: NuXmvNamedElement) {
            if (name.isNullOrEmpty()) return
            val previous = declarations.putIfAbsent(name, element)
            if (previous != null && previous !== element) {
                ambiguous.add(name)
            }
        }

        for (param in module.moduleDeclaration.moduleParameterList) {
            val name = param.name ?: continue
            parameters.putIfAbsent(name, param)
            register(name, param)
        }

        val body = module.moduleBody
        val assignConstraints = body?.assignConstraintList.orEmpty()

        if (body != null) {
            for (decl in body.varDeclarationList) {
                for (single in decl.singleVarDeclarationList) {
                    registerVar(single, variables, ::register)
                    val moduleType = single.typeSpecifier?.moduleTypeSpecifier?.identifier?.text
                    if (moduleType != null) {
                        submoduleEdges.putIfAbsent(single.varName.name ?: continue, moduleType)
                    }
                    collectEnumValues(single.typeSpecifier, ::register)
                }
            }

            for (decl in body.ivarDeclarationList) {
                for (single in decl.singleIvarDeclarationList) {
                    val varName = single.varName
                    val name = varName.name ?: continue
                    variables.putIfAbsent(name, varName)
                    register(name, varName)
                    collectEnumValues(single.simpleTypeSpecifier, ::register)
                }
            }

            for (decl in body.frozenVarDeclarationList) {
                for (single in decl.singleIvarDeclarationList) {
                    val varName = single.varName
                    val name = varName.name ?: continue
                    variables.putIfAbsent(name, varName)
                    register(name, varName)
                    collectEnumValues(single.simpleTypeSpecifier, ::register)
                }
            }

            for (decl in body.defineDeclarationList) {
                for (define in decl.defineBodyList) {
                    val name = define.defineName.name ?: continue
                    defines.putIfAbsent(name, define.defineName)
                    register(name, define.defineName)
                }
            }

            for (decl in body.constantsDeclarationList) {
                for (constant in decl.constantList) {
                    register(constant.name, constant)
                }
            }

            for (decl in body.functionDeclarationList) {
                for (spec in decl.functionSpecificationList) {
                    register(spec.functionName.name, spec.functionName)
                }
            }

            for (constraint in assignConstraints) {
                for (single in constraint.singleAssignConstraintList) {
                    val lhsName = assignedVariableName(single) ?: continue
                    assignmentsByVar.getOrPut(lhsName) { ArrayList() }.add(single)
                }
            }
        }

        return NuXmvModuleScope(
            module = module,
            declarations = declarations,
            variables = variables,
            defines = defines,
            parameters = parameters,
            submoduleEdges = submoduleEdges,
            assignmentsByVar = assignmentsByVar.mapValues { it.value as List<NuXmvSingleAssignConstraint> },
            ambiguous = ambiguous,
            assignConstraints = assignConstraints
        )
    }

    private inline fun registerVar(
        single: NuXmvSingleVarDeclaration,
        variables: MutableMap<String, NuXmvVarName>,
        register: (String?, NuXmvNamedElement) -> Unit
    ) {
        val varName = single.varName
        val name = varName.name ?: return
        variables.putIfAbsent(name, varName)
        register(name, varName)
    }

    private fun collectEnumValues(
        typeSpecifier: PsiElement?,
        register: (String?, NuXmvNamedElement) -> Unit
    ) {
        if (typeSpecifier == null) return
        walkForEnumValues(typeSpecifier, register)
    }

    private fun walkForEnumValues(
        element: PsiElement,
        register: (String?, NuXmvNamedElement) -> Unit
    ) {
        var child: PsiElement? = element.firstChild
        while (child != null) {
            if (child is NuXmvEnumerationTypeValue) {
                register(child.name, child)
            } else {
                walkForEnumValues(child, register)
            }
            child = child.nextSibling
        }
    }

    private fun assignedVariableName(single: NuXmvSingleAssignConstraint): String? {
        val usage = when (val expr = single.expr) {
            is NuXmvSimpleAssignExpr -> expr.identifierUsage
            is NuXmvInitAssignExpr -> expr.identifierUsage
            is NuXmvNextAssignExpr -> expr.identifierUsage
            else -> null
        } ?: return null
        val text = usage.text ?: return null
        val bracket = text.indexOf('[')
        return if (bracket >= 0) text.substring(0, bracket) else text
    }
}

/** Cached lookup of declarations, variables, defines, parameters, etc. inside this module. */
fun NuXmvModule.scope(): NuXmvModuleScope = NuXmvScopes.of(this)

/** Resolve a name within the module's combined namespace. O(1) on cache hit. */
fun NuXmvModule.findDeclaration(name: String): NuXmvNamedElement? = scope().declarations[name]

/** The submodule type instantiated by [varName], if any. */
fun NuXmvModule.submoduleTypeOf(varName: String): String? = scope().submoduleEdges[varName]
