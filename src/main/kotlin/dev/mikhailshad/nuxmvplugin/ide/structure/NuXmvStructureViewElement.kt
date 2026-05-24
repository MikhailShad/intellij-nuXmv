package dev.mikhailshad.nuxmvplugin.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes

class NuXmvStructureViewElement(private val element: NavigatablePsiElement) :
    StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getAlphaSortKey(): String {
        return element.elementType.toString()
    }

    override fun getPresentation(): ItemPresentation {
        return element.presentation
            ?: PresentationData(
                element.firstChild?.text ?: "",
                element.elementType.toString(),
                null,
                null
            )
    }

    override fun getChildren(): Array<TreeElement> {
        val result = ArrayList<PsiElement>()

        when (element) {
            is NuXmvFile -> {
                result.addAll(NuXmvScopes.modulesIn(element).values)
            }

            is NuXmvModule -> {
                val moduleBody = element.moduleBody
                if (moduleBody != null) {
                    result.addAll(moduleBody.varDeclarationList)
                    result.addAll(moduleBody.ivarDeclarationList)
                    result.addAll(moduleBody.frozenVarDeclarationList)
                    result.addAll(moduleBody.defineDeclarationList)
                    result.addAll(moduleBody.constantsDeclarationList)
                    result.addAll(moduleBody.ctlSpecificationList)
                    result.addAll(moduleBody.ltlSpecificationList)
                    result.addAll(moduleBody.invarSpecificationList)
                }
            }

            is NuXmvVarDeclaration -> {
                result.addAll(element.singleVarDeclarationList)
            }

            is NuXmvIvarDeclaration -> {
                result.addAll(element.singleIvarDeclarationList)
            }

            is NuXmvFrozenVarDeclaration -> {
                result.addAll(element.singleIvarDeclarationList)
            }

            is NuXmvDefineDeclaration -> {
                result.addAll(element.defineBodyList)
            }

            is NuXmvConstantsDeclaration -> {
                result.addAll(element.constantList)
            }
        }

        if (result.isEmpty()) return emptyArray()

        val array = arrayOfNulls<TreeElement>(result.size)
        for (i in result.indices) {
            array[i] = NuXmvStructureViewElement(result[i] as NavigatablePsiElement)
        }
        @Suppress("UNCHECKED_CAST")
        return array as Array<TreeElement>
    }
}
