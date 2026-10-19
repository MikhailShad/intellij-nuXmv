package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.isInTimedModel
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

object NuXmvModuleElementSectionKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val moduleBodyKeywords = listOf(
        "VAR", "IVAR", "FROZENVAR", "FUN", "DEFINE", "CONSTANTS",
        "ASSIGN", "INIT", "TRANS", "INVAR",
        "FAIRNESS", "JUSTICE", "COMPASSION",
        "CTLSPEC", "LTLSPEC", "INVARSPEC", "PSLSPEC", "COMPUTE", "PARSYNTH", "ISA", "PRED", "MIRROR"
    )
    private val timedModuleBodyKeywords = listOf("URGENT")

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val keywords = if (parameters.originalFile.isInTimedModel) {
            moduleBodyKeywords + timedModuleBodyKeywords
        } else {
            moduleBodyKeywords
        }
        keywords.forEach { keyword ->
            resultSet.addElement(
                LookupElementBuilder.create(keyword)
                    .bold()
                    .withTypeText("module section")
            )
        }
    }
}
