package dev.mikhailshad.nuxmvplugin.ide.structure

import com.intellij.ide.navigationToolbar.AbstractNavBarModelExtension
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import javax.swing.Icon

class NuXmvNavBarModelExtension : AbstractNavBarModelExtension() {
    override fun getPresentableText(any: Any?): String? = when (any) {
        is NuXmvPresentableElement -> any.presentation?.presentableText
        is NuXmvModule -> any.moduleDeclaration.moduleName?.name
        else -> null
    }

    override fun getIcon(any: Any?): Icon? = when (any) {
        is NuXmvPresentableElement -> any.presentation?.getIcon(false)
        is NuXmvModule -> NuXmvIcons.MODULE
        else -> null
    }

    override fun adjustElement(psiElement: PsiElement): PsiElement {
        if (psiElement.language != NuXmvLanguage) {
            return psiElement
        }
        return navBarElementOf(psiElement) ?: psiElement
    }

    override fun getParent(psiElement: PsiElement): PsiElement? {
        if (psiElement.language != NuXmvLanguage || psiElement is PsiFile) {
            return null
        }
        return navBarElementOf(psiElement.parent) ?: psiElement.containingFile
    }

    private fun navBarElementOf(element: PsiElement?): PsiElement? =
        PsiTreeUtil.getNonStrictParentOfType(element, NuXmvPresentableElement::class.java, NuXmvModule::class.java)
}
