package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleParameter
import dev.mikhailshad.nuxmvplugin.language.psi.impl.NuXmvNamedElementImpl

abstract class ModuleParameterMixin(node: ASTNode) : NuXmvNamedElementImpl(node), NuXmvModuleParameter {
    override fun getNameIdentifier(): PsiElement? {
        return this.simpleIdentifier
    }
}
