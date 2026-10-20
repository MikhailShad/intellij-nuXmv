package dev.mikhailshad.nuxmvplugin.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvIdentifierUsageMixin
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

class NuXmvWarningAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!element.isValid) {
            return
        }

        checkValidReferences(element, holder)
        checkTimeDomain(element, holder)
        checkTimedConstructs(element, holder)
        checkNextUsage(element, holder)
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

        val identifierUsage = element as NuXmvIdentifierUsageMixin

        if (identifierUsage.reference?.resolve() == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference: ${element.text}")
                .range(element)
                .create()
        }
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
