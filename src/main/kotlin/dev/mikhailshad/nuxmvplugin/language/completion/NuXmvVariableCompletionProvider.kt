package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvIdentifierReference
import javax.swing.Icon

object NuXmvVariableCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val position = parameters.position
        if (position.containingFile !is NuXmvFile) return

        val reference = spanReferenceAt(position)
        val module = if (reference != null) reference.module else position.parentOfType<NuXmvModule>()
        module ?: return
        val qualified = reference?.isQualified == true

        for ((name, element) in module.scope().declarations) {
            if (qualified && element is NuXmvModuleParameter) continue
            val (kind, icon) = describe(element) ?: continue
            resultSet.addElement(
                LookupElementBuilder.create(element, name)
                    .withIcon(icon)
                    .withTypeText(kind)
            )
        }
    }

    private fun spanReferenceAt(position: PsiElement): NuXmvIdentifierReference? {
        val usage = position.parent as? NuXmvIdentifierUsage ?: return null
        val offset = position.startOffsetInParent
        return usage.references.filterIsInstance<NuXmvIdentifierReference>()
            .firstOrNull { offset in it.rangeInElement.startOffset..it.rangeInElement.endOffset }
    }

    private fun describe(element: NuXmvNamedElement): Pair<String, Icon?>? = when (element) {
        is NuXmvVarName -> when (val declaration = element.parent) {
            is NuXmvSingleVarDeclaration -> "VAR: ${declaration.typeSpecifier?.text}" to NuXmvIcons.VAR
            is NuXmvSingleIvarDeclaration -> if (declaration.parent is NuXmvFrozenVarDeclaration) {
                "FROZENVAR: ${declaration.simpleTypeSpecifier?.text}" to NuXmvIcons.FROZEN_VAR
            } else {
                "IVAR: ${declaration.simpleTypeSpecifier?.text}" to NuXmvIcons.IVAR
            }

            else -> null
        }

        is NuXmvDefineName -> "DEFINE" to NuXmvIcons.DEFINE
        is NuXmvConstant -> "CONSTANT" to NuXmvIcons.CONSTANT
        is NuXmvEnumerationTypeValue -> "enum value" to NuXmvIcons.CONSTANT
        is NuXmvModuleParameter -> "parameter" to null
        is NuXmvFunctionName -> "FUN" to null
        else -> null
    }
}
