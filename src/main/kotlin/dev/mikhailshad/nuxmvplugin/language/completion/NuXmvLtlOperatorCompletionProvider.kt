package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.isInTimedModel

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
    private val timedOperators = mapOf(
        "X~" to "LTL: Timed Next",
        "Y~" to "LTL: Timed Previous"
    )
    private val boundedOperators = listOf(
        "G" to "Globally", "F" to "Finally", "H" to "Historically", "O" to "Once"
    ).flatMap { (op, name) ->
        listOf(
            BoundedTemplate("$op [l, u]", "$op [\$L\$, \$U\$] ", "LTL: Bounded $name"),
            BoundedTemplate("$op [l, +oo)", "$op [\$L\$, +oo) ", "LTL: Bounded $name")
        )
    } + listOf("U" to "Until", "V" to "Releases", "S" to "Since", "T" to "Triggered").map { (op, name) ->
        BoundedTemplate("$op [l, u]", "$op [\$L\$, \$U\$] ", "LTL: Bounded $name")
    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val available = if (parameters.originalFile.isInTimedModel) operators + timedOperators else operators
        available.forEach { (op, desc) ->
            val element = LookupElementBuilder.create(op)
                .withTypeText(desc)
                .bold()

            resultSet.addElement(element)
        }
        boundedOperators.forEach { template ->
            resultSet.addElement(
                LookupElementBuilder.create(template.label)
                    .withTypeText(template.description)
                    .bold()
                    .withInsertHandler(template)
            )
        }
    }

    private class BoundedTemplate(
        val label: String,
        private val text: String,
        val description: String
    ) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            context.document.deleteString(context.startOffset, context.tailOffset)
            val templateManager = TemplateManager.getInstance(context.project)
            val template = templateManager.createTemplate("", "nuXmv", text)
            template.addVariable("L", TextExpression("l"), TextExpression("l"), true)
            if (text.contains("\$U\$")) {
                template.addVariable("U", TextExpression("u"), TextExpression("u"), true)
            }
            templateManager.startTemplate(context.editor, template)
        }
    }
}
