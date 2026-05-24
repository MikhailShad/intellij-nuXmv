package dev.mikhailshad.nuxmvplugin.language.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope

class NuXmvDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        // Handle module documentation
        if (element is NuXmvModule) {
            return generateModuleDoc(element)
        }

        // Handle variable documentation
        if (element is NuXmvVarName) {
            return when (val parent = element.parent) {
                is NuXmvSingleVarDeclaration -> generateVarDoc(parent)
                is NuXmvSingleIvarDeclaration -> generateIvarDoc(parent)
                else -> null
            }
        }

        // Handle define documentation
        if (element is NuXmvDefineName) {
            return generateDefineDoc(element.parent as NuXmvDefineBody)
        }

        return null
    }

    private fun generateModuleDoc(module: NuXmvModule): String {
        val moduleName = module.moduleDeclaration.moduleName?.name ?: "module_name"
        val moduleBody = module.moduleBody

        val builder = StringBuilder()

        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("MODULE ")
        builder.append(moduleName)

        val params = module.moduleDeclaration.moduleParameterList
        if (params.isNotEmpty()) {
            builder.append(params.map { it.name }.joinToString(", ", "(", ")"))
        }
        builder.append(DocumentationMarkup.DEFINITION_END)

        builder.append(DocumentationMarkup.CONTENT_START)

        if (moduleBody != null) {
            val stateVars = moduleBody.varDeclarationList.flatMap { it.singleVarDeclarationList }
            if (stateVars.isNotEmpty()) {
                builder.append("<b>State Variables:</b><br>")
                for (v in stateVars) {
                    builder.append("${v.varName.text ?: "unnamed"} : ${v.typeSpecifier?.text ?: "unknown type"}<br>")
                }
                builder.append("<br>")
            }

            val inputVars = moduleBody.ivarDeclarationList.flatMap { it.singleIvarDeclarationList } +
                    moduleBody.frozenVarDeclarationList.flatMap { it.singleIvarDeclarationList }
            if (inputVars.isNotEmpty()) {
                builder.append("<b>Input Variables:</b><br>")
                for (v in inputVars) {
                    builder.append("${v.varName.text ?: "unnamed"} : ${v.simpleTypeSpecifier?.text ?: "unknown type"}<br>")
                }
                builder.append("<br>")
            }

            val defines = moduleBody.defineDeclarationList.flatMap { it.defineBodyList }
            if (defines.isNotEmpty()) {
                builder.append("<b>Defines:</b><br>")
                for (d in defines) {
                    builder.append("${d.defineName.name ?: "unnamed"}<br>")
                }
            }
        }

        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }

    private fun generateVarDoc(varDecl: NuXmvSingleVarDeclaration): String {
        val varName = varDecl.varName.name ?: "unnamed"
        val typeText = varDecl.typeSpecifier?.text ?: "unknown type"

        val builder = StringBuilder()

        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("State Variable: ")
        builder.append(varName)
        builder.append(DocumentationMarkup.DEFINITION_END)

        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append("<b>Type:</b> $typeText<br>")

        val module = varDecl.parentOfType<NuXmvModule>()
        val assigns = module?.scope()?.assignmentsByVar?.get(varName).orEmpty()
        if (assigns.isNotEmpty()) {
            val initAssigns = ArrayList<String>()
            val nextAssigns = ArrayList<String>()
            val simpleAssigns = ArrayList<String>()
            for (assign in assigns) {
                when (val expr = assign.expr) {
                    is NuXmvInitAssignExpr -> initAssigns.add(expr.expr?.text.orEmpty())
                    is NuXmvNextAssignExpr -> nextAssigns.add(expr.expr?.text.orEmpty())
                    is NuXmvSimpleAssignExpr -> simpleAssigns.add(expr.expr?.text.orEmpty())
                    else -> Unit
                }
            }

            if (initAssigns.isNotEmpty()) {
                builder.append("<b>Initial Value:</b><br>")
                for (text in initAssigns) builder.append("init($varName) := $text<br>")
                builder.append("<br>")
            }
            if (nextAssigns.isNotEmpty()) {
                builder.append("<b>Next Value:</b><br>")
                for (text in nextAssigns) builder.append("next($varName) := $text<br>")
                builder.append("<br>")
            }
            if (simpleAssigns.isNotEmpty()) {
                builder.append("<b>Assignments:</b><br>")
                for (text in simpleAssigns) builder.append("$varName := $text<br>")
            }
        }

        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }

    private fun generateIvarDoc(ivarDecl: NuXmvSingleIvarDeclaration): String {
        val varName = ivarDecl.varName.text ?: "unnamed"
        val typeText = ivarDecl.simpleTypeSpecifier?.text ?: "unknown type"

        val builder = StringBuilder()

        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("Input Variable: ")
        builder.append(varName)
        builder.append(DocumentationMarkup.DEFINITION_END)

        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append("<b>Type:</b> $typeText<br>")
        builder.append("<br>")
        builder.append("Input variables are used to model external inputs to the system.")
        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }

    private fun generateDefineDoc(defineBody: NuXmvDefineBody): String {
        val defineName = defineBody.defineName.name ?: "unnamed"
        val exprText = defineBody.expr?.text?.take(100) ?: "..."

        val builder = StringBuilder()

        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("Define: ")
        builder.append(defineName)
        builder.append(DocumentationMarkup.DEFINITION_END)

        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append("<b>Expression:</b><br>")
        builder.append("$defineName := $exprText")
        if (exprText.length >= 100) {
            builder.append("...")
        }
        builder.append("<br><br>")
        builder.append("DEFINEs in nuXmv are used to create macros or shortcuts for expressions.")
        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }
}
