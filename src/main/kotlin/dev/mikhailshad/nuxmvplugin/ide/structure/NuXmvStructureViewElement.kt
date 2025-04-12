package dev.mikhailshad.nuxmvplugin.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.containers.toArray
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
                result.addAll(PsiTreeUtil.findChildrenOfType(element, NuXmvModule::class.java))
            }

            is NuXmvModule -> {
                val moduleBody = element.moduleBody
                if (moduleBody != null) {
                    result.addAll(
                        sequenceOf(
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvVarDeclaration::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvIvarDeclaration::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvFrozenVarDeclaration::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvDefineDeclaration::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvConstantsDeclaration::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvCtlSpecification::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvLtlSpecification::class.java),
                            PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvInvarSpecification::class.java)
                        ).flatten()
                    )
                }
            }

            is NuXmvVarDeclaration -> {
                result.addAll(PsiTreeUtil.findChildrenOfType(element, NuXmvSingleVarDeclaration::class.java))
            }

            is NuXmvIvarDeclaration, is NuXmvFrozenVarDeclaration -> {
                result.addAll(PsiTreeUtil.findChildrenOfType(element, NuXmvSingleIvarDeclaration::class.java))
            }

            is NuXmvDefineDeclaration -> {
                result.addAll(PsiTreeUtil.findChildrenOfType(element, NuXmvDefineBody::class.java))
            }

            is NuXmvConstantsDeclaration -> {
                result.addAll(PsiTreeUtil.findChildrenOfType(element, NuXmvConstant::class.java))
            }
        }

        return if (result.isNotEmpty())
            result.map { NuXmvStructureViewElement(it as NavigatablePsiElement) }
                .toArray(arrayOf())
        else
            emptyArray<TreeElement>()
    }
}
