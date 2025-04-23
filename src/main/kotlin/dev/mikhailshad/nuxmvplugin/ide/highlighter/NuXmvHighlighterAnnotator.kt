package dev.mikhailshad.nuxmvplugin.ide.highlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleParameter
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName

// FIXME
class NuXmvHighlighterAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is NuXmvModuleName -> {
                holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                    .textAttributes(NuXmvSyntaxHighlighter.MODULE_PARAMETER_ATTRIBUTE)
                    .create()
            }

            is NuXmvModuleParameter -> {
                holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                    .textAttributes(NuXmvSyntaxHighlighter.MODULE_PARAMETER_ATTRIBUTE)
                    .create()
            }

            is NuXmvVarName -> {
                holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                    .textAttributes(NuXmvSyntaxHighlighter.VARIABLE_NAME_ATTRIBUTE)
                    .create()
            }

            is NuXmvIdentifierUsage -> {
                val resolved = element.reference?.resolve()
                when (resolved) {
                    is NuXmvModuleParameter -> {
                        holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                            .textAttributes(NuXmvSyntaxHighlighter.MODULE_PARAMETER_ATTRIBUTE)
                            .create()
                    }

                    is NuXmvVarName -> {
                        holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                            .textAttributes(NuXmvSyntaxHighlighter.VARIABLE_NAME_ATTRIBUTE)
                            .create()
                    }
                }
            }
        }
    }
}
