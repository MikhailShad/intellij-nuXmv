package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons

/**
 * Провайдер автодополнения для макросов, срабатывает при вводе символа "%"
 */
object NuXmvMacroCompletionProvider : CompletionProvider<CompletionParameters>() {
    const val MACRO_PREFIX = '#'

    private val macroKeywordsInsertHandlers = mapOf(
        "${MACRO_PREFIX}FOR" to InsertHandler<LookupElement> { context, item ->
            val editor = context.editor
            val document = editor.document

            val currentStart = context.startOffset
            val hasPercentPrefix = currentStart > 0 && document.charsSequence[currentStart - 1] == MACRO_PREFIX

            document.deleteString(currentStart, context.tailOffset)

            val templateManager = TemplateManager.getInstance(context.project)
            val template: Template = TemplateImpl(
                "forMacro",
                "${if (hasPercentPrefix) "" else MACRO_PREFIX}FOR \$VAR$ IN \$FROM$..\$TO$\n\$BODY$\n${MACRO_PREFIX}END",
                "nuXmv"
            )
            template.addVariable("VAR", TextExpression("i"), TextExpression("i"), true)
            template.addVariable("FROM", TextExpression("1"), TextExpression("1"), true)
            template.addVariable("TO", TextExpression("10"), TextExpression("10"), true)
            template.addVariable("BODY", TextExpression(""), TextExpression(""), true)
            templateManager.startTemplate(editor, template)
        }
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        macroKeywordsInsertHandlers.forEach { keyword, insertHandler ->
            resultSet.addElement(
                LookupElementBuilder.create(keyword)
                    .bold()
                    .withCaseSensitivity(false)
                    .withLookupStrings(setOf(keyword.uppercase(), keyword.lowercase()))
                    .withTypeText("macro")
                    .withIcon(NuXmvIcons.OTHER_SPEC)
                    .withInsertHandler(insertHandler)
            )
        }
    }
}
