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

        val modules = PsiTreeUtil.findChildrenOfType(file, NuXmvModuleDeclaration::class.java)

        modules.forEach { module ->
            val moduleName = module.moduleName?.text ?: return

            val moduleParams = module.moduleParameters
            val paramList = moduleParams?.text?.let { " $it" } ?: ""

            resultSet.addElement(
                LookupElementBuilder.create(moduleName)
                    .withTailText(paramList)
                    .withTypeText("module")
                    .withInsertHandler { insertContext, _ ->
                        val document = insertContext.document
                        val offset = insertContext.selectionEndOffset

                        if (moduleParams != null) {
                            document.insertString(offset, "()")
                            insertContext.editor.caretModel.moveToOffset(offset + 1)
                        }
                    }
            )
        }
    }
}
