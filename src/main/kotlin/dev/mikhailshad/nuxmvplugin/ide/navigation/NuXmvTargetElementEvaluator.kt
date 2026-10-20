package dev.mikhailshad.nuxmvplugin.ide.navigation

import com.intellij.codeInsight.TargetElementEvaluatorEx2
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

/**
 * Treats every segment of a dotted declaration name (`con.s : real;`) as the declaration itself,
 * so that Ctrl+click on `s` shows usages instead of jumping to the start of the name.
 */
class NuXmvTargetElementEvaluator : TargetElementEvaluatorEx2() {
    override fun getNamedElement(element: PsiElement): PsiElement? {
        if (element.elementType != NuXmvTypes.IDENTIFIER) return null
        val owner = element.parent as? NuXmvNamedElement ?: return null
        val nameIdentifier = owner.nameIdentifier ?: return null
        return if (nameIdentifier.textRange.contains(element.textRange)) owner else null
    }
}
