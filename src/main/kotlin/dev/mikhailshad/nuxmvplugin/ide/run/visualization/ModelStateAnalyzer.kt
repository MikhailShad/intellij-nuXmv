package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

class ModelStateAnalyzer(private val project: Project) {
    data class StateVariable(
        val name: String,
        val type: NuXmvBuiltInType,
        val transitions: MutableList<Transition> = mutableListOf()
    )

    data class Transition(
        val from: String,
        val to: String,
    ) {
        lateinit var condition: String
    }

    fun analyzeModelFile(file: VirtualFile): List<StateVariable> {
        val psiFile = PsiManager.getInstance(project).findFile(file) ?: return emptyList()
        if (psiFile !is NuXmvFile) return emptyList()

        val stateVariables = findStateVariables(psiFile)
        analyzeTransitionsInNextExpressions(psiFile, stateVariables)

        return stateVariables.values.toList()
    }

    private fun findStateVariables(psiFile: NuXmvFile): Map<String, StateVariable> {
        val stateVariables = mutableMapOf<String, StateVariable>()
        val varDeclarations = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvSingleVarDeclaration::class.java)

        for (varDecl in varDeclarations) {
            val typeSpecifier = varDecl.typeSpecifier?.simpleTypeSpecifier
            val type = typeSpecifier?.resolveType()
            when (type) {
                NuXmvBuiltInType.BOOLEAN,
                NuXmvBuiltInType.SET,
                NuXmvBuiltInType.WORD -> {
                    val varName = varDecl.varName.name ?: continue
                    stateVariables[varName] = StateVariable(varName, type)
                }

                else -> continue
            }
        }

        return stateVariables
    }

    private fun analyzeTransitionsInNextExpressions(psiFile: NuXmvFile, stateVariables: Map<String, StateVariable>) {
        val nextExpressions = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvNextAssignExpr::class.java)
        for (nextExpression in nextExpressions) {
            val identifier = nextExpression.identifierUsage?.text ?: continue
            val stateVariable = stateVariables[identifier]
            if (stateVariable != null) {
                val rhs = nextExpression.expr
                when (rhs) {
                    is NuXmvCaseBasicExpr -> extractTransitionsFromCaseExpr(rhs, stateVariable)
                    else -> continue // TODO: maybe analyze init+next pairs?
                }
            }
        }
    }

    private fun extractTransitionsFromCaseExpr(
        caseExpr: NuXmvCaseBasicExpr, stateVariable: StateVariable
    ) {
        for (caseBody in caseExpr.regularCaseBodyList) {
            val exprList = caseBody.exprList
            if (exprList.size == 2) {
                val condition = exprList[0]
                val targetExpr = exprList[1]

                val sourceStates = extractSourceStateFromCondition(condition, stateVariable)
                val targetState = targetExpr.text

                val transitions = sourceStates.map {
                    val transition = Transition(
                        from = it,
                        to = targetState,
                    )
                    transition.condition = condition.text
                    transition
                }

                stateVariable.transitions.addAll(transitions)
            }
        }
    }

    private fun extractSourceStateFromCondition(
        condition: NuXmvExpr,
        stateVariable: StateVariable
    ): Set<String> {
        return PsiTreeUtil.findChildrenOfType(condition, NuXmvReferenceBasicExpr::class.java)
            .filter {
                it.text == stateVariable.name
            }.map {
                val parentExpr = it.parent
                var state: String? = null
                when (parentExpr) {
                    is NuXmvEqualityBasicExpr -> {
                        val operands = parentExpr.exprList
                        if (operands.size == 2) {
                            if (operands[0].text == stateVariable.name) {
                                state = operands[1].text
                            } else if (operands[1].text == stateVariable.name) {
                                state = operands[0].text
                            }
                        }
                    }

                    is NuXmvLogicalNotBasicExpr -> {
                        state = "FALSE"
                    }

                    is NuXmvReferenceBasicExpr -> {
                        if (parentExpr.text == stateVariable.name && stateVariable.type == NuXmvBuiltInType.BOOLEAN) {
                            state = "TRUE"
                        }
                    }
                }

                return@map state
            }.filter { it != null }
            .map { it!! }
            .toSet()
    }
}
