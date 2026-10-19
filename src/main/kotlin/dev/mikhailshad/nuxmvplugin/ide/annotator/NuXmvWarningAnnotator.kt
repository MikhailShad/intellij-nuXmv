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
