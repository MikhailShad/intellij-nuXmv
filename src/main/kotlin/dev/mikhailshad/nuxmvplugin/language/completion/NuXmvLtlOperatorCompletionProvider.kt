package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

object NuXmvLtlOperatorCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val operators = mapOf(
        "X" to "LTL: Next",
        "G" to "LTL: Globally",
        "F" to "LTL: Finally",
        "U" to "LTL: Until",
        "V" to "LTL: Releases",
        "Y" to "LTL: Previous",
        "Z" to "LTL: Not Previous Not",
        "H" to "LTL: Historically",
        "O" to "LTL: Once",
        "S" to "LTL: Since",
        "T" to "LTL: Triggered",
        "@F~" to "LTL: At Next",
        "@O~" to "LTL: At Last"
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        operators.forEach { (op, desc) ->
            val element = LookupElementBuilder.create(op)
                .withTypeText(desc)
                .bold()

            resultSet.addElement(element)
        }
    }
}
