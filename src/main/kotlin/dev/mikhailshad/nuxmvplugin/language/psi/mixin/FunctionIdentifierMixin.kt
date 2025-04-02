package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionIdentifier
import dev.mikhailshad.nuxmvplugin.language.psi.impl.NuXmvNamedElementImpl

abstract class FunctionIdentifierMixin(node: ASTNode) : NuXmvNamedElementImpl(node), NuXmvFunctionIdentifier {
    override fun getNameIdentifier(): PsiElement? {
        return this.simpleIdentifier ?: this
    }
}
