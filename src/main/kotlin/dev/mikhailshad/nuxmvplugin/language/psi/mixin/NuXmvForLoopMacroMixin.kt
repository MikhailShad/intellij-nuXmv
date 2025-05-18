package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForLoopMacro
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor

abstract class NuXmvForLoopMacroMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvForLoopMacro {
    companion object {
        const val LOOP_VARIABLE_REPLACEMENT = "$"
    }

    val loopVariableName: String
        get() {
            return forLoopVariable?.name ?: throw Exception("No loop variable name found")
        }

    val loopRange: IntRange
        get() {
            val wholeNumbers = rangeConstant?.wholeNumberList
            return if (wholeNumbers == null || wholeNumbers.size < 2) {
                IntRange.EMPTY
            } else {
                val from = wholeNumbers[0].text.toInt()
                val to = wholeNumbers[1].text.toInt()
                IntRange(from, to)
            }
        }

    val loopBody: List<PsiElement>
        get() =
            listOf(
                assignConstraintList,
                compassionConstraintList,
                computeSpecificationList,
                constantsDeclarationList,
                ctlSpecificationList,
                defineDeclarationList,
                fairnessConstraintList,
                forLoopMacroList,
                frozenVarDeclarationList,
                functionDeclarationList,
                initConstraintList,
                invarConstraintList,
                invarSpecificationList,
                isaDeclarationList,
                ivarDeclarationList,
                justiceConstraintList,
                ltlSpecificationList,
                mirrorDeclarationList,
                parameterSynthProblemDeclarationList,
                predDeclarationList,
                transConstraintList,
                varDeclarationList
            ).filter {
                it.isNotEmpty()
            }.flatten()

    override fun expand(): String {
        return innerExpand(mutableSetOf(loopVariableName))
    }

    private fun innerExpand(nestedLoopVariableNames: Set<String>): String {
        if (loopRange.isEmpty() || loopBody.isEmpty()) {
            return ""
        }

        val bodyExpandedStringBuilder = StringBuilder()
        for (element in loopBody) {
            if (element is NuXmvForLoopMacro) {
                val nestedForMacro = element as NuXmvForLoopMacroMixin
                val nestedLoopVariableName = nestedForMacro.forLoopVariable?.name ?: ""
                val nestedExpanded = nestedForMacro.innerExpand(nestedLoopVariableNames + nestedLoopVariableName)
                bodyExpandedStringBuilder.append(nestedExpanded)
            } else {
                val visitor = LoopVariableReplacementVisitor(nestedLoopVariableNames)
                element.accept(visitor)
                bodyExpandedStringBuilder.append(visitor.text)
            }
        }
        val bodyExpandedString = bodyExpandedStringBuilder.toString()

        val macroExpandedStringBuilder = StringBuilder()
        for (i in loopRange) {
            macroExpandedStringBuilder.append(
                bodyExpandedString.replace(
                    "$LOOP_VARIABLE_REPLACEMENT$loopVariableName",
                    i.toString()
                )
            )
            macroExpandedStringBuilder.appendLine()
        }
        return macroExpandedStringBuilder.toString()
    }

    private inner class LoopVariableReplacementVisitor(private val variableNames: Set<String>) : NuXmvVisitor() {
        private val stringBuilder = StringBuilder()

        val text: String
            get() = stringBuilder.toString()

        override fun visitElement(element: PsiElement) {
            if (element.children.isNotEmpty()) {
                element.acceptChildren(this)
            } else {
                when (element) {
                    is NuXmvNamedElement -> expandIdentifierIfNeeded(element.name)
                    is NuXmvIdentifierUsage -> expandIdentifierIfNeeded(element.text)
                    else -> stringBuilder.append(element.text)
                }
            }
        }

        private fun expandIdentifierIfNeeded(identifier: String?) {
            if (variableNames.contains(identifier)) {
                stringBuilder.append(LOOP_VARIABLE_REPLACEMENT)
            }
            stringBuilder.append(identifier)
        }
    }
}
