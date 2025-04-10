package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement

class NuXmvNamedElementReference(psiElement: PsiElement) :
    NuXmvReferenceBase(psiElement, TextRange.from(0, psiElement.textLength)) {
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> = listOf(psiElement)

    override fun handleElementRename(newElementName: String): PsiElement {
        if (psiElement is NuXmvNamedElement) {
            psiElement.setName(newElementName)
            return psiElement
        }
        return super.handleElementRename(newElementName)
    }
}
