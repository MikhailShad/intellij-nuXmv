package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvIdentifierReference

abstract class NuXmvIdentifierUsageMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvIdentifierUsage {
    override fun getReference(): PsiReference? {
        return NuXmvIdentifierReference(this)
    }
}
