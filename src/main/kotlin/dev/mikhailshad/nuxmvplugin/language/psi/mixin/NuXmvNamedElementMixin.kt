package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.util.IncorrectOperationException
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvElementFactory
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvNamedElementReference

abstract class NuXmvNamedElementMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvNamedElement {
    override fun getNameIdentifier(): PsiElement? = firstChild
    override fun getName(): String? = nameIdentifier?.text
    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this
    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()
    override fun getReference(): PsiReference? = NuXmvNamedElementReference(this)
    override fun setName(name: String): PsiElement {
        val identifier = nameIdentifier ?: throw IncorrectOperationException("No name identifier found")
        val newIdentifier = NuXmvElementFactory.createIdentifier(project, name)
        identifier.replace(newIdentifier)
        return this
    }
}
