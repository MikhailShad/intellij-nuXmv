package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.impl.NuXmvNamedElementImpl

abstract class ModuleNameMixin(node: ASTNode) : NuXmvNamedElementImpl(node), NuXmvModuleName {
    override fun getNameIdentifier(): PsiElement? {
        return this.simpleIdentifier
    }
}
