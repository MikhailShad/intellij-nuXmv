package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.ModelGraph
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.StateVariable
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.Transition
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

class ModelStateAnalyzer(private val project: Project) {
    fun analyzeModelFile(file: VirtualFile): ModelGraph {
        val psiFile = PsiManager.getInstance(project).findFile(file) as? NuXmvFile
            ?: throw Exception("Not a nuXmv file")

        val stateVariables = findStateVariables(psiFile)
        analyzeTransitionsInNextExpressions(psiFile, stateVariables)
        analyzeInitialStates(psiFile, stateVariables)

        return ModelGraph(stateVariables.filter { it.value.transitions.isNotEmpty() })
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

                val conditionVisitor = ConditionVisitor(stateVariable)
                condition.accept(conditionVisitor)
                val conditionText = conditionVisitor.conditionText

                val transitions = sourceStates.map {
                    val transition = Transition(
                        from = it,
                        to = targetState,
                    )
                    transition.condition = conditionText
                    transition
                }

                stateVariable.transitions.addAll(transitions)
            }
        }
    }

    private fun analyzeInitialStates(psiFile: NuXmvFile, stateVariables: Map<String, StateVariable>) {
        // INIT constranit
        val initConstraints = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvInitConstraint::class.java)
        for (initConstraint in initConstraints) {
            val expr = initConstraint.expr ?: continue
            analyzeInitExpr(expr, stateVariables)
        }

        // init(<variable>) := <expr>
        val initAssignExprs = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvInitAssignExpr::class.java)
        for (initAssign in initAssignExprs) {
            val varName = initAssign.identifierUsage?.text ?: continue
            val stateVariable = stateVariables[varName] ?: continue
            val valueExpr = initAssign.expr

            if (valueExpr != null) {
                stateVariable.initialValue = valueExpr.text
            }
        }
    }

    private fun analyzeInitExpr(expr: NuXmvExpr, stateVariables: Map<String, StateVariable>) {
        when (expr) {
            is NuXmvEqualityBasicExpr -> {
                val operands = expr.exprList
                if (operands.size == 2) {
                    val leftOperand = operands[0].text
                    val rightOperand = operands[1].text
                    val stateVariable = stateVariables[leftOperand]
                    if (stateVariable != null) {
                        stateVariable.initialValue = rightOperand
                    }
                }
            }

            is NuXmvLogicalNotBasicExpr -> {
                val innerExpr = expr.expr
                if (innerExpr is NuXmvReferenceBasicExpr) {
                    val varName = innerExpr.text
                    val stateVariable = stateVariables[varName]
                    if (stateVariable != null && stateVariable.type == NuXmvBuiltInType.BOOLEAN) {
                        stateVariable.initialValue = "FALSE"
                    }
                }
            }

            is NuXmvReferenceBasicExpr -> {
                val varName = expr.text
                val stateVariable = stateVariables[varName]
                if (stateVariable != null && stateVariable.type == NuXmvBuiltInType.BOOLEAN) {
                    stateVariable.initialValue = "TRUE"
                }
            }

            is NuXmvAndBasicExpr,
            is NuXmvOrBasicExpr -> {
                val exprList = when (expr) {
                    is NuXmvAndBasicExpr -> expr.exprList
                    is NuXmvOrBasicExpr -> expr.exprList
                    else -> return
                }

                for (subExpr in exprList) {
                    analyzeInitExpr(subExpr, stateVariables)
                }
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

    private inner class ConditionVisitor(private val stateVariable: StateVariable) : NuXmvVisitor() {
        private val stringBuilder = StringBuilder()

        val conditionText: String get() = stringBuilder.toString()

        override fun visitElement(element: PsiElement) {
            for (child in element.children) {
                if (child is NuXmvReferenceBasicExpr || child is NuXmvLiteralBasicExpr) {
                    continue
                }

                if (child.text.contains(stateVariable.name)) {
                    child.accept(this)
                } else {
                    stringBuilder.append(child.text)
                }
            }
        }
    }
}
