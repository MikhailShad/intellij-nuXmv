// NuXmvModuleInstanceCompletionProvider.kt
package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes

object NuXmvModuleInstanceCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val file = parameters.position.containingFile as? NuXmvFile ?: return

        for ((moduleName, module) in NuXmvScopes.modulesIn(file)) {
            val moduleParams = module.moduleDeclaration.moduleParameterList
            val paramList = " $moduleParams"

            resultSet.addElement(
                LookupElementBuilder.create(moduleName)
                    .withTailText(paramList)
                    .withTypeText("module")
                    .withInsertHandler { insertContext, _ ->
                        val document = insertContext.document
                        val offset = insertContext.selectionEndOffset

                        document.insertString(offset, "()")
                        insertContext.editor.caretModel.moveToOffset(offset + 1)
                    }
            )
        }
    }
}
