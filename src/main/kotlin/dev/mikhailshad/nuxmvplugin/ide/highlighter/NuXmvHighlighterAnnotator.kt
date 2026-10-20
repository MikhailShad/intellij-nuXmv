package dev.mikhailshad.nuxmvplugin.ide.highlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

class NuXmvHighlighterAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is NuXmvIdentifierUsage -> {
                val resolved = element.reference?.resolve()
                if (resolved == null && element.text == NuXmvFile.TIME_VARIABLE && element.isInTimedModel) {
                    highlight(element, holder, NuXmvSyntaxHighlighter.CONSTANT_NAME_ATTRIBUTE)
                } else {
                    innerAnnotate(element, holder, resolved)
                }
            }

            is NuXmvIsaModuleName -> highlight(element, holder, NuXmvSyntaxHighlighter.MODULE_NAME_ATTRIBUTE)

            is NuXmvSimpleTypeSpecifier -> if (element.resolveType() == NuXmvBuiltInType.CLOCK) {
                highlight(element, holder, NuXmvSyntaxHighlighter.KEYWORD_ATTRIBUTE)
            }

            else -> if (isPslKeyword(element)) {
                highlight(element, holder, NuXmvSyntaxHighlighter.KEYWORD_ATTRIBUTE)
            } else {
                innerAnnotate(element, holder)
            }
        }
    }

    private fun isPslKeyword(element: PsiElement): Boolean =
        element.elementType == NuXmvTypes.IDENTIFIER && element.parent is NuXmvPslBody

    private fun highlight(element: PsiElement, holder: AnnotationHolder, attributes: TextAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(attributes)
            .range(element)
            .create()
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
                val moduleName = (element as NuXmvModuleTypeSpecifier).identifier ?: return
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(NuXmvSyntaxHighlighter.TYPE_SPECIFIER_ATTRIBUTE)
                    .range(moduleName)
                    .create()
            }
        }
    }
}
