package dev.mikhailshad.nuxmvplugin.language.resolve

import com.intellij.psi.PsiElement

class NuXmvNamedElementReference(psiElement: PsiElement) : NuXmvReferenceBase(psiElement, psiElement.textRange) {
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> = listOf(psiElement)
}
