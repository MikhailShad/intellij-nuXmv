package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIsaModuleName
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvModuleReference

abstract class NuXmvIsaModuleNameMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvIsaModuleName {
    override fun getReference(): PsiReference = NuXmvModuleReference(this)
}
