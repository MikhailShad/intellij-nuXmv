package dev.mikhailshad.nuxmvplugin.language.psi.impl.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleIdentifier
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes
import dev.mikhailshad.nuxmvplugin.language.psi.impl.NuXmvNamedElementImpl

class NuXmvModuleNameMixinImpl(node: ASTNode) : NuXmvNamedElementImpl(node), NuXmvModuleName {
    override fun getNameIdentifier(): PsiElement? {
        return findChildByType<NuXmvSimpleIdentifier>(NuXmvTypes.IDENTIFIER)
    }

    override fun getIdentifier(): PsiElement {
        return nameIdentifier!!
    }
}
