package dev.mikhailshad.nuxmvplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement
import dev.mikhailshad.nuxmvplugin.language.resolve.NuXmvNamedElementReference

abstract class NuXmvNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvNamedElement {
    override fun getName(): String? = nameIdentifier?.text
    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this
    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()
    override fun getReference(): PsiReference? = NuXmvNamedElementReference(this)
}
