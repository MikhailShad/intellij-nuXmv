// NuXmvModuleInstanceCompletionProvider.kt
package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration

object NuXmvModuleInstanceCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val position = parameters.position
        val file = position.containingFile

        if (file !is NuXmvFile) return

        val moduleDeclarations = PsiTreeUtil.findChildrenOfType(file, NuXmvModuleDeclaration::class.java)

        moduleDeclarations.forEach { module ->
            val moduleName = module.moduleName?.name ?: return

            val moduleParams = module.moduleParameterList
            val paramList = moduleParams.let { " $it" }

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
