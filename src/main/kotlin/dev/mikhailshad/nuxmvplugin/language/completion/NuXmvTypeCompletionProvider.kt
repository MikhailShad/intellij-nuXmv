package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.isInTimedModel

object NuXmvTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
    private class TypeTemplate(val label: String, val text: String, val caretShift: Int = text.length)

    private val basicTypes = listOf(
        TypeTemplate("boolean", "boolean"),
        TypeTemplate("integer", "integer"),
        TypeTemplate("real", "real"),
        TypeTemplate("word[N]", "word[]", 5),
        TypeTemplate("unsigned word[N]", "unsigned word[]", 14),
        TypeTemplate("signed word[N]", "signed word[]", 12),
        TypeTemplate("array N..M of type", "array 0..1 of ", 6),
        TypeTemplate("array word[N] of type", "array word[] of ", 11),
        TypeTemplate("array integer of type", "array integer of "),
    )
    private val timedTypes = listOf(TypeTemplate("clock", "clock"))

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val types = if (parameters.originalFile.isInTimedModel) basicTypes + timedTypes else basicTypes
        types.forEach { type ->
            val element = LookupElementBuilder.create(type.label)
                .withTypeText("type")
                .withInsertHandler { insertContext, _ ->
                    val start = insertContext.startOffset
                    insertContext.document.replaceString(start, insertContext.tailOffset, type.text)
                    insertContext.editor.caretModel.moveToOffset(start + type.caretShift)
                }
            resultSet.addElement(element)
        }
    }
}
