package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypeSpecifier

abstract class AbstractDeclarationMixin(node: ASTNode) : ASTWrapperPsiElement(node) {
    fun getVariableType(): String? {
        val typeSpecifier = PsiTreeUtil.getChildOfType(this, NuXmvTypeSpecifier::class.java)
        return typeSpecifier?.text
    }

    fun getContainingModule(): NuXmvModule? {
        return PsiTreeUtil.getParentOfType(this, NuXmvModule::class.java)
    }
}
