package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForMacro

abstract class NuXmvForMacroMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvForMacro {
    fun getLoopVariableName(): String {
        return identifier?.text ?: throw Exception("No loop variable name found")
    }

    fun getLoopRange(): IntRange {
        val wholeNumbers = rangeConstant?.wholeNumberList
        return if (wholeNumbers == null || wholeNumbers.size < 2) {
            IntRange.EMPTY
        } else {
            val from = wholeNumbers[0].text.toInt()
            val to = wholeNumbers[1].text.toInt()
            IntRange(from, to)
        }
    }
}
