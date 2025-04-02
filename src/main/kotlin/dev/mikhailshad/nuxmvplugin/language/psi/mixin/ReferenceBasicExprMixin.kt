package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvReferenceBasicExpr
import dev.mikhailshad.nuxmvplugin.language.psi.impl.NuXmvNamedElementImpl

abstract class ReferenceBasicExprMixin(node: ASTNode) : NuXmvNamedElementImpl(node), NuXmvReferenceBasicExpr {
    override fun getNameIdentifier(): PsiElement? {
        return this.complexIdentifier ?: this.simpleIdentifier
    }
}
