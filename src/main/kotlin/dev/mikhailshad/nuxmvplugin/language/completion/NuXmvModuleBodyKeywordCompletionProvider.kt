package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

object NuXmvModuleBodyKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
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
        val position = parameters.position
        logger.info {
            """
            Current element: ${position.javaClass.name}
            Text: ${position.text}
            Parents: ${
                sequence {
                    var el = position
                    while (el.parent != null) {
                        el = el.parent
                        yield(el)
                    }
                }.toList()
            }
        """.trimIndent()
        }

        moduleBodyKeywords.forEach { keyword ->
            resultSet.addElement(
                LookupElementBuilder.create(keyword)
                    .bold()
                    .withTypeText("section")
            )
        }
    }
}
