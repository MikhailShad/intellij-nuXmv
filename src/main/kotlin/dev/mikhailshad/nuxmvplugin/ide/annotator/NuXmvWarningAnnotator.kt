package dev.mikhailshad.nuxmvplugin.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import java.math.BigInteger
import dev.mikhailshad.nuxmvplugin.language.psi.*
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvIdentifierReference
import dev.mikhailshad.nuxmvplugin.language.psi.scope.isaModules
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

private val WORD_CONSTANT = Regex("0([us]?)([bBoOdDhH])([0-9]*)_([0-9a-fA-F_]+)")
private val MAX_WORD_WIDTH: BigInteger = BigInteger.valueOf(Int.MAX_VALUE.toLong())
private val RADIX_NAMES = mapOf(2 to "a binary", 8 to "an octal", 10 to "a decimal", 16 to "a hexadecimal")

class NuXmvWarningAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!element.isValid) {
            return
        }

        checkValidReferences(element, holder)
        checkTimeDomain(element, holder)
        checkTimedConstructs(element, holder)
        checkNextUsage(element, holder)
        checkWordConstant(element, holder)
        checkIsa(element, holder)
//        checkValidTypes(element, holder)
    }

    private fun checkValidTypes(element: PsiElement, holder: AnnotationHolder) {
        if (element !is NuXmvExpr) {
            return
        }

        // TODO: type validation
//        holder.newAnnotation(HighlightSeverity.WARNING, "FIXME: type validation")
//            .range(element)
//            .create()
    }

    private fun checkValidReferences(element: PsiElement, holder: AnnotationHolder) {
        if (element !is NuXmvIdentifierUsage) {
            return
        }
        if (PsiTreeUtil.getParentOfType(element, NuXmvPslBody::class.java) != null) {
            return
        }
        if (element.text == NuXmvFile.TIME_VARIABLE && element.isInTimedModel) {
            return
        }

        val unresolved = element.references.firstOrNull { it.resolve() == null } ?: return
        if (unresolved is NuXmvIdentifierReference && unresolved.isQualified && unresolved.module == null) {
            return
        }
        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference: ${unresolved.canonicalText}")
            .range(unresolved.absoluteRange)
            .create()
    }

    private fun checkNextUsage(element: PsiElement, holder: AnnotationHolder) {
        val message = when {
            NuXmvNextOperator.isNextCall(element) -> {
                val section = NuXmvNextOperator.forbiddingSection(element)
                when {
                    section != null -> "'next' is not allowed in $section"
                    NuXmvNextOperator.isInsideNextCall(element) -> "Nested 'next'"
                    else -> null
                }
            }

            element is NuXmvIdentifierUsage -> {
                val define = element.reference?.resolve() as? NuXmvDefineName
                if (define == null || !NuXmvNextOperator.defineUsesNext(define)) {
                    null
                } else {
                    val section = NuXmvNextOperator.forbiddingSection(element)
                    when {
                        section != null -> "'${element.text}' uses 'next' (via DEFINE), which is not allowed in $section"
                        NuXmvNextOperator.isInsideNextCall(element) -> "'${element.text}' uses 'next' (via DEFINE) inside 'next'"
                        else -> null
                    }
                }
            }

            else -> null
        } ?: return
        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private fun checkIsa(element: PsiElement, holder: AnnotationHolder) {
        if (element !is NuXmvIsaModuleName) return
        val module = element.parentOfType<NuXmvModule>() ?: return
        val target = (element.reference?.resolve() as? NuXmvModuleName)?.parentOfType<NuXmvModule>()
        val message = when {
            target == null -> "Unknown module '${element.text}'"
            target.moduleDeclaration.moduleParameterList.isNotEmpty() ->
                "ISA cannot include module '${element.text}' with parameters"

            includesThroughIsa(target, module, HashSet()) ->
                "Module '${module.moduleDeclaration.moduleName?.name}' is recursively defined through ISA"

            else -> null
        } ?: return
        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private fun includesThroughIsa(from: NuXmvModule, target: NuXmvModule, visited: MutableSet<NuXmvModule>): Boolean {
        if (from === target) return true
        if (!visited.add(from)) return false
        return from.isaModules().any { includesThroughIsa(it, target, visited) }
    }

    private fun checkWordConstant(element: PsiElement, holder: AnnotationHolder) {
        if (element !is NuXmvWordConstant) return
        val message = wordConstantProblem(element.text) ?: return
        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private fun wordConstantProblem(text: String): String? {
        val match = WORD_CONSTANT.matchEntire(text) ?: return null
        val (_, base, widthText, valueText) = match.destructured
        val radix = when (base.lowercase()) {
            "b" -> 2
            "o" -> 8
            "d" -> 10
            else -> 16
        }
        val digits = valueText.replace("_", "")
        digits.firstOrNull { Character.digit(it, radix) < 0 }?.let {
            return "Digit '$it' is not allowed in ${RADIX_NAMES.getValue(radix)} word constant"
        }
        if (widthText.isEmpty()) {
            return if (radix == 10) "Decimal word constant requires a width" else null
        }
        val width = widthText.toBigInteger()
        if (width < BigInteger.ONE || width > MAX_WORD_WIDTH) {
            return "Width of a word constant must be in 1..$MAX_WORD_WIDTH"
        }
        return if (BigInteger(digits, radix).bitLength() > width.toInt()) {
            "Value of word constant $text does not fit in $width bits"
        } else {
            null
        }
    }

    private fun checkTimeDomain(element: PsiElement, holder: AnnotationHolder) {
        if (element !is NuXmvTimeDomainAnnotation) {
            return
        }
        val domain = element.identifier ?: return
        if (domain.text != NuXmvFile.CONTINUOUS_TIME_DOMAIN && domain.text != NuXmvFile.NONE_TIME_DOMAIN) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Unknown time domain '${domain.text}', expected '${NuXmvFile.NONE_TIME_DOMAIN}' or '${NuXmvFile.CONTINUOUS_TIME_DOMAIN}'"
            )
                .range(domain)
                .create()
        }
    }

    private fun checkTimedConstructs(element: PsiElement, holder: AnnotationHolder) {
        val construct = when {
            element is NuXmvUrgentConstraint -> element.firstChild
            element is NuXmvTimedLtlExpr -> element.timeLtlOp
            element.elementType == NuXmvTypes.LTL_TIMED_NEXT || element.elementType == NuXmvTypes.LTL_TIMED_PREVIOUS -> element
            element is NuXmvSimpleTypeSpecifier && element.resolveType() == NuXmvBuiltInType.CLOCK -> element
            else -> null
        } ?: return
        if (element.isInTimedModel) {
            return
        }
        holder.newAnnotation(
            HighlightSeverity.WARNING,
            "'${construct.text}' is available only in timed models (@TIME_DOMAIN ${NuXmvFile.CONTINUOUS_TIME_DOMAIN})"
        )
            .range(construct)
            .create()
    }
}
