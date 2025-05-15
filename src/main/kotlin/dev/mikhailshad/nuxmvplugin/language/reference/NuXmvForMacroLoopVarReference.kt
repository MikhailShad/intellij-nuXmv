package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForLoopMacro
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage

class NuXmvForMacroLoopVarReference(
    element: NuXmvIdentifierUsage,
    private val forLoopMacro: NuXmvForLoopMacro
) : NuXmvReferenceBase(element, TextRange.from(0, element.textLength)) {

    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val loopVarElement = forLoopMacro.forLoopVariable ?: return emptyList()
        val identifier = element.text

        if (identifier == loopVarElement.text) {
            return listOf(loopVarElement)
        }

        return emptyList()
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // Rename is not supported for loop variables
        return element
    }

    companion object {
        fun isLoopVarReference(element: NuXmvIdentifierUsage): Boolean {
            val containingForMacro = PsiTreeUtil.getParentOfType(element, NuXmvForLoopMacro::class.java) ?: return false
            return element.text == containingForMacro.forLoopVariable?.text
        }

        fun getContainingForMacro(element: NuXmvIdentifierUsage): NuXmvForLoopMacro? {
            return PsiTreeUtil.getParentOfType(element, NuXmvForLoopMacro::class.java)
        }
    }
}
