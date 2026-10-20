package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName

abstract class NuXmvVarNameMixin(node: ASTNode) : NuXmvNamedElementMixin(node), NuXmvVarName {
    private val isQuoted: Boolean get() = firstChild.elementType == NuXmvTypes.QUOTES

    override val nameFirstLeaf: PsiElement? get() = if (isQuoted) firstChild.nextSibling else firstChild
    override val nameLastLeaf: PsiElement? get() = if (isQuoted) lastChild?.prevSibling else lastChild
}
