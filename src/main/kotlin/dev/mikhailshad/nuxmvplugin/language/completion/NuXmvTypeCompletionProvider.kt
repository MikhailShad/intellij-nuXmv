package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

object NuXmvTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val basicTypes = listOf(
        "boolean",
        "integer",
        "real",
        "clock",
        "word[N]",
        "unsigned word[N]",
        "signed word[N]",
        "array N..M of type"
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        basicTypes.forEach { type ->
            val element = LookupElementBuilder.create(type)
                .withTypeText("type")

            val insertHandler = when {
                type.contains("word[N]") -> InsertHandler<LookupElement> { insertContext, item ->
                    val document = insertContext.document
                    val offset = insertContext.selectionEndOffset
                    val textToInsert = type.replace("N", "")
                    document.replaceString(insertContext.startOffset, offset, textToInsert)
                    insertContext.editor.caretModel.moveToOffset(offset - 1)
                }

                type.contains("array") -> InsertHandler<LookupElement> { insertContext, item ->
                    val document = insertContext.document
                    val offset = insertContext.selectionEndOffset
                    document.replaceString(insertContext.startOffset, offset, "array 0..1 of ")
                    insertContext.editor.caretModel.moveToOffset(offset + 7)
                }

                else -> null
            }

            val finalElement = if (insertHandler != null) element.withInsertHandler(insertHandler) else element
            resultSet.addElement(finalElement)
        }
    }
}
