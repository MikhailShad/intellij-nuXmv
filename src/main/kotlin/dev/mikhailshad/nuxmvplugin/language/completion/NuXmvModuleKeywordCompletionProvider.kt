package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

object NuXmvModuleKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val moduleKeywords = listOf(
        "MODULE"
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        moduleKeywords.forEach { keyword ->
            resultSet.addElement(
                LookupElementBuilder.create(keyword)
                    .bold()
                    .withTypeText("keyword")
            )
        }
    }
}
