package dev.mikhailshad.nuxmvplugin.language.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvReference

class NuXmvWarningAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!element.isValid) {
            return
        }

//        checkValidReferences(element, holder)
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
        if (element !is NuXmvNamedElement) {
            return
        }

        val parent = element.parent
        if (parent is NuXmvVarDeclaration || parent is NuXmvIvarDeclaration || parent is NuXmvModule) {
            return
        }

        val reference = NuXmvReference(element, TextRange(0, element.text.length))

        if (reference.resolve() == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference: ${element.text}")
                .range(element)
                .create()
        }
    }
}
