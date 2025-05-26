package dev.mikhailshad.nuxmvplugin.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExpr
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvIdentifierUsageMixin

class NuXmvWarningAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!element.isValid) {
            return
        }

        checkValidReferences(element, holder)
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

        val identifierUsage = element as NuXmvIdentifierUsageMixin

        if (identifierUsage.reference?.resolve() == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference: ${element.text}")
                .range(element)
                .create()
        }
    }
}
