package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiWhiteSpace
import com.intellij.util.IncorrectOperationException
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvElementFactory
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvNamedElementReference

abstract class NuXmvNamedElementMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvNamedElement {
    protected open val nameFirstLeaf: PsiElement? get() = firstChild
    protected open val nameLastLeaf: PsiElement? get() = lastChild

    override fun getNameIdentifier(): PsiElement? {
        val first = nameFirstLeaf ?: return null
        return if (first === nameLastLeaf) first else this
    }

    override fun getName(): String? {
        val first = nameFirstLeaf ?: return null
        val last = nameLastLeaf ?: return null
        val text = StringBuilder()
        var leaf: PsiElement? = first
        while (leaf != null) {
            if (leaf !is PsiWhiteSpace && leaf !is PsiComment) text.append(leaf.text)
            if (leaf === last) break
            leaf = leaf.nextSibling
        }
        return text.toString()
    }

    override fun getNavigationElement(): PsiElement = nameFirstLeaf ?: this
    override fun getTextOffset(): Int = nameFirstLeaf?.textOffset ?: super.getTextOffset()
    override fun getReference(): PsiReference? = NuXmvNamedElementReference(this)
    override fun setName(name: String): PsiElement {
        val first = nameFirstLeaf ?: throw IncorrectOperationException("No name identifier found")
        val last = nameLastLeaf ?: first
        val replacement = NuXmvElementFactory.createVarName(project, name)
        addRangeBefore(replacement.firstChild, replacement.lastChild, first)
        deleteChildRange(first, last)
        return this
    }
}
