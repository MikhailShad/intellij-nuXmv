package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

object NuXmvModuleElementSectionKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val moduleBodyKeywords = listOf(
        "VAR", "IVAR", "FROZENVAR", "DEFINE", "CONSTANTS",
        "ASSIGN", "INIT", "TRANS", "INVAR",
        "FAIRNESS", "JUSTICE", "COMPASSION",
        "CTLSPEC", "LTLSPEC", "INVARSPEC", "COMPUTE", "ISA", "PRED", "MIRROR"
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        moduleBodyKeywords.forEach { keyword ->
            resultSet.addElement(
                LookupElementBuilder.create(keyword)
                    .bold()
                    .withTypeText("module section")
            )
        }
    }
}
