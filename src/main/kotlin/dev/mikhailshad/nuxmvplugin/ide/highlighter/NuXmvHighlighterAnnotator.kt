package dev.mikhailshad.nuxmvplugin.ide.highlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvHighlighterAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is NuXmvIdentifierUsage -> {
                val resolved = element.reference?.resolve()
                innerAnnotate(element, holder, resolved)
            }

            else -> innerAnnotate(element, holder)
        }
    }

    private fun innerAnnotate(element: PsiElement, holder: AnnotationHolder, reference: PsiElement? = null) {
        val target = reference ?: element
        when (target) {
            is NuXmvModuleName -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(NuXmvSyntaxHighlighter.MODULE_NAME_ATTRIBUTE)
                    .range(element)
                    .create()
            }

            is NuXmvModuleParameter -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(NuXmvSyntaxHighlighter.MODULE_PARAMETER_ATTRIBUTE)
                    .range(element)
                    .create()
            }

            is NuXmvVarName -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(NuXmvSyntaxHighlighter.VARIABLE_NAME_ATTRIBUTE)
                    .range(element)
                    .create()
            }

            is NuXmvDefineName, is NuXmvEnumerationTypeValue, is NuXmvConstant -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(NuXmvSyntaxHighlighter.CONSTANT_NAME_ATTRIBUTE)
                    .range(element)
                    .create()
            }

            is NuXmvModuleTypeSpecifier -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(NuXmvSyntaxHighlighter.TYPE_SPECIFIER_ATTRIBUTE)
                    .range((element as NuXmvModuleTypeSpecifier).identifier)
                    .create()
            }
        }
    }
}
