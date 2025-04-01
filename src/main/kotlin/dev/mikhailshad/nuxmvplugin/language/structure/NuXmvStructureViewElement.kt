package dev.mikhailshad.nuxmvplugin.language.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvStructureViewElement(private val element: NavigatablePsiElement) :
    StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getAlphaSortKey(): String {
        val name = when {
            element is PsiNamedElement -> (element as PsiNamedElement).name ?: ""
            element is NuXmvNuXmvModule -> {
                val moduleName = element.moduleDeclaration.moduleName?.text ?: "unnamed"
                "module_$moduleName"
            }

            element is NuXmvSingleVarDeclaration -> {
                val varName = element.varName.text ?: "unnamed"
                "var_$varName"
            }

            element is NuXmvSingleIvarDeclaration -> {
                val varName = element.varName.text ?: "unnamed"
                "ivar_$varName"
            }

            element is NuXmvDefineBody -> {
                val defineName = element.complexIdentifier.text ?: "unnamed"
                "define_$defineName"
            }

            element is NuXmvInitAssignExpr || element is NuXmvNextAssignExpr || element is NuXmvSimpleAssignExpr -> {
                val identifier = when (val assignExpr = element as PsiElement) {
                    is NuXmvInitAssignExpr -> assignExpr.complexIdentifier?.text
                    is NuXmvNextAssignExpr -> assignExpr.complexIdentifier?.text
                    is NuXmvSimpleAssignExpr -> assignExpr.complexIdentifier.text
                    else -> null
                }
                "assign_${identifier ?: "unnamed"}"
            }

            else -> element.text.take(20)
        }
        return name.lowercase()
    }

    override fun getPresentation(): ItemPresentation {
        return when (element) {
            is NuXmvFile -> {
                PresentationData(
                    element.name,
                    "NuXmv File",
                    null,
                    null
                )
            }

            is NuXmvNuXmvModule -> {
                val moduleName = element.moduleDeclaration.moduleName?.text ?: "unnamed"
                PresentationData(
                    "MODULE $moduleName",
                    "Module",
                    null,
                    null
                )
            }

            is NuXmvSingleVarDeclaration -> {
                val varName = element.varName.text ?: "unnamed"
                val typeText = element.typeSpecifier?.text ?: "unknown type"
                PresentationData(
                    "$varName : $typeText",
                    "Variable",
                    null,
                    null
                )
            }

            is NuXmvSingleIvarDeclaration -> {
                val varName = element.varName.text ?: "unnamed"
                val typeText = element.simpleTypeSpecifier?.text ?: "unknown type"
                PresentationData(
                    "$varName : $typeText",
                    "Input Variable",
                    null,
                    null
                )
            }

            is NuXmvDefineBody -> {
                val defineName = element.complexIdentifier.text ?: "unnamed"
                PresentationData(
                    "$defineName := ...",
                    "Define",
                    null,
                    null
                )
            }

            is NuXmvLtlSpecification -> {
                PresentationData(
                    "LTLSPEC ...",
                    "LTL Specification",
                    null,
                    null
                )
            }

            is NuXmvCtlSpecification -> {
                PresentationData(
                    "CTLSPEC ...",
                    "CTL Specification",
                    null,
                    null
                )
            }

            is NuXmvInvarSpecification -> {
                PresentationData(
                    "INVARSPEC ...",
                    "Invariant Specification",
                    null,
                    null
                )
            }

            is NuXmvInitAssignExpr -> {
                val identifier = element.complexIdentifier?.text ?: "unnamed"
                PresentationData(
                    "init($identifier) := ...",
                    "Init Assignment",
                    null,
                    null
                )
            }

            is NuXmvNextAssignExpr -> {
                val identifier = element.complexIdentifier?.text ?: "unnamed"
                PresentationData(
                    "next($identifier) := ...",
                    "Next Assignment",
                    null,
                    null
                )
            }

            is NuXmvSimpleAssignExpr -> {
                val identifier = element.complexIdentifier.text ?: "unnamed"
                PresentationData(
                    "$identifier := ...",
                    "Assignment",
                    null,
                    null
                )
            }

            else -> {
                PresentationData(
                    element.text.take(20) + if (element.text.length > 20) "..." else "",
                    element.javaClass.simpleName,
                    null,
                    null
                )
            }
        }
    }

    override fun getChildren(): Array<TreeElement> {
        if (element !is NuXmvFile && element !is NuXmvNuXmvModule) {
            return emptyArray()
        }

        val result = ArrayList<TreeElement>()

        when (element) {
            is NuXmvFile -> {
                // Get all modules in the file
                val modules = PsiTreeUtil.findChildrenOfType(element, NuXmvNuXmvModule::class.java)
                modules.forEach {
                    result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                }
            }

            is NuXmvNuXmvModule -> {
                // Get module body elements
                val moduleBody = element.moduleBody
                if (moduleBody != null) {
                    // Add variables
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleVarDeclaration::class.java).forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }

                    // Add input variables
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleIvarDeclaration::class.java).forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }

                    // Add defines
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvDefineBody::class.java).forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }

                    // Add LTL specs
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvLtlSpecification::class.java).forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }

                    // Add CTL specs
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvCtlSpecification::class.java).forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }

                    // Add INVAR specs
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvInvarSpecification::class.java).forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }

                    // Add assignments
                    val assignExprs = ArrayList<PsiElement>()
                    assignExprs.addAll(PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSimpleAssignExpr::class.java))
                    assignExprs.addAll(PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvInitAssignExpr::class.java))
                    assignExprs.addAll(PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvNextAssignExpr::class.java))

                    assignExprs.forEach {
                        result.add(NuXmvStructureViewElement(it as NavigatablePsiElement))
                    }
                }
            }
        }

        return result.toArray(arrayOf<TreeElement>())
    }
}
