package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionNameUsage

abstract class NuXmvFunctionNameUsageMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvFunctionNameUsage {
    override fun getReference(): PsiReference? {
        return super.getReference()
    }
}
