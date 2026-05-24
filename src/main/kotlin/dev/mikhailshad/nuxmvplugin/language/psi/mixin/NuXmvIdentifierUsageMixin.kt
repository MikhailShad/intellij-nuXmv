package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvForMacroLoopVarReference
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvIdentifierReference

abstract class NuXmvIdentifierUsageMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvIdentifierUsage {
    override fun getReference(): PsiReference? = references.firstOrNull()

    override fun getReferences(): Array<PsiReference> {
        if (NuXmvForMacroLoopVarReference.isLoopVarReference(this)) {
            NuXmvForMacroLoopVarReference.getContainingForMacro(this)?.let {
                return arrayOf(NuXmvForMacroLoopVarReference(this, it))
            }
        }
        return NuXmvIdentifierReference.referencesFor(this)
    }
}
