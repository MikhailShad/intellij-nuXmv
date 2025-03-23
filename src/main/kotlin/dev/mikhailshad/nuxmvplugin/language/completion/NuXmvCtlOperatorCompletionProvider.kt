package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

object NuXmvCtlOperatorCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val operators = mapOf(
        "EX" to "CTL: Exists Next",
        "AX" to "CTL: For All Next",
        "EG" to "CTL: Exists Globally",
        "AG" to "CTL: For All Globally",
        "EF" to "CTL: Exists Finally",
        "AF" to "CTL: For All Finally",
        "E" to "CTL: Exists Until (E[p U q])",
        "A" to "CTL: For All Until (A[p U q])"
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
