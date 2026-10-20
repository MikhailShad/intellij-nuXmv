package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.openapi.util.RecursionManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType

/**
 * Where nuXmv accepts the `next` operator. It is rejected in sections that expect a simple expression
 * (INIT, INVAR, FAIRNESS, JUSTICE, COMPASSION, CTLSPEC, COMPUTE, PRED, `x :=` and `init(x) :=` assignments),
 * inside another `next`, and also when it reaches such a place through a DEFINE.
 */
object NuXmvNextOperator {
    /** True for a `next(…)` call expression; the `next(x) :=` assignment target is not a call. */
    fun isNextCall(element: PsiElement): Boolean =
        element is NuXmvFunctionCallBasicExpr && element.functionNameUsage.firstChild.elementType == NuXmvTypes.NEXT_FUN

    /** The section that forbids `next` at [element], or null if `next` is allowed there. */
    fun forbiddingSection(element: PsiElement): String? {
        var parent = element.parent
        while (parent != null && parent !is NuXmvModule) {
            when (parent) {
                is NuXmvInitConstraint -> return "INIT"
                is NuXmvInvarConstraint -> return "INVAR"
                is NuXmvFairnessConstraint -> return "FAIRNESS"
                is NuXmvJusticeConstraint -> return "JUSTICE"
                is NuXmvCompassionConstraint -> return "COMPASSION"
                is NuXmvCtlSpecification -> return "CTLSPEC"
                is NuXmvComputeSpecification -> return "COMPUTE"
                is NuXmvPredDeclaration -> return "PRED"
                is NuXmvSimpleAssignExpr -> return "ASSIGN"
                is NuXmvInitAssignExpr -> return "ASSIGN init(…)"
                is NuXmvTransConstraint, is NuXmvInvarSpecification, is NuXmvLtlSpecification,
                is NuXmvDefineBody, is NuXmvNextAssignExpr, is NuXmvUrgentConstraint,
                is NuXmvParameterList, is NuXmvPslSpecification -> return null
            }
            parent = parent.parent
        }
        return null
    }

    /** True when [element] is an argument (at any depth) of another `next(…)` call. */
    fun isInsideNextCall(element: PsiElement): Boolean {
        var parent = element.parent
        while (parent != null && parent !is NuXmvModule) {
            if (isNextCall(parent)) return true
            parent = parent.parent
        }
        return false
    }

    /** True when the body of [define] uses `next` directly or through other DEFINEs. */
    fun defineUsesNext(define: NuXmvDefineName): Boolean {
        val body = define.parent as? NuXmvDefineBody ?: return false
        return CachedValuesManager.getCachedValue(body) {
            val usesNext = RecursionManager.doPreventingRecursion(body, false) {
                val content: PsiElement? = body.arrayLiteral ?: body.expr
                content != null && containsNext(content)
            } ?: false
            CachedValueProvider.Result.create(usesNext, PsiModificationTracker.MODIFICATION_COUNT)
        }
    }

    private fun containsNext(root: PsiElement): Boolean {
        if (isNextCall(root)) return true
        if (root is NuXmvIdentifierUsage) {
            val target = root.reference?.resolve() as? NuXmvDefineName
            return target != null && defineUsesNext(target)
        }
        return PsiTreeUtil.getChildrenOfType(root, PsiElement::class.java)?.any { containsNext(it) } ?: false
    }
}
