package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName
import dev.mikhailshad.nuxmvplugin.language.psi.impl.NuXmvNamedElementImpl

abstract class VarNameMixin(node: ASTNode) : NuXmvNamedElementImpl(node), NuXmvVarName {
    override fun getNameIdentifier(): PsiElement? {
        return this.complexIdentifier
    }
}
