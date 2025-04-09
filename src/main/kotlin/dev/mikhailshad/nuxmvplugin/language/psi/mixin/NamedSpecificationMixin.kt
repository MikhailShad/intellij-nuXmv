package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedSpecification

abstract class NamedSpecificationMixin(node: ASTNode) : NuXmvNamedElementMixin(node), NuXmvNamedSpecification {
    override fun getNameIdentifier(): PsiElement? {
        return firstChild.nextSibling
    }
}
