package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class NuXmvNamedElementReference(psiElement: PsiElement) :
    NuXmvReferenceBase(psiElement, TextRange.from(0, psiElement.textLength)) {
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> = listOf(psiElement)
}
