package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvAssignConstraint
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

object NuXmvFunctionCompletionProvider : CompletionProvider<CompletionParameters>() {
    // Map of function names to their parameter documentation
    private val assignFunctionTemplates = mapOf(
        "init" to listOf("VAR"),
        "next" to listOf("VAR"),
    )
    private val functionTemplates = mapOf(
        "abs" to listOf("EXPR"),
        "max" to listOf("EXPR1", "EXPR2"),
        "min" to listOf("EXPR1", "EXPR2"),
        "sin" to listOf("EXPR"),
        "cos" to listOf("EXPR"),
        "exp" to listOf("EXPR"),
        "tan" to listOf("EXPR"),
        "ln" to listOf("EXPR"),
        "pow" to listOf("BASE", "EXPONENT"),
        "asin" to listOf("EXPR"),
        "acos" to listOf("EXPR"),
        "atan" to listOf("EXPR"),
        "sqrt" to listOf("EXPR"),
        "word1" to listOf("BOOLEAN_EXPR"),
        "bool" to listOf("EXPR"),
        "toint" to listOf("EXPR"),
        "count" to listOf("EXPR"),
        "swconst" to listOf("INTEGER", "BITS"),
        "uwconst" to listOf("INTEGER", "BITS"),
        "signed" to listOf("WORD_EXPR"),
        "unsigned" to listOf("WORD_EXPR"),
        "sizeof" to listOf("WORD_EXPR"),
        "floor" to listOf("REAL_EXPR"),
        "extend" to listOf("WORD_EXPR", "BITS"),
        "resize" to listOf("WORD_EXPR", "SIZE"),
        "READ" to listOf("ARRAY", "INDEX"),
        "WRITE" to listOf("ARRAY", "INDEX", "VALUE"),
        "CONSTARRAY" to listOf("TYPE", "VALUE")
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val positionHolder = parameters.position.parent.parent // identifier -> IdentifierUsage -> holder
        val isAssignLhs = PsiTreeUtil.findFirstParent(positionHolder, true) { it is NuXmvAssignConstraint } != null
                && PsiTreeUtil.findSiblingBackward(positionHolder, NuXmvTypes.ASSIGN) {} == null

        addFunctionCompletions(assignFunctionTemplates, resultSet)

        if (!isAssignLhs) {
            addFunctionCompletions(functionTemplates, resultSet)
        }
    }

    private fun addFunctionCompletions(
        functionsMap: Map<String, List<String>>,
        resultSet: CompletionResultSet
    ) {
        functionsMap.forEach { (name, params) ->
            val insertHandler = FunctionInsertHandler(name, params)
            val element = LookupElementBuilder.create(name)
                .withTailText("($params)")
                .withTypeText("function")
                .withInsertHandler(insertHandler)

            resultSet.addElement(element)
        }
    }

    private class FunctionInsertHandler(
        private val functionName: String,
        private val params: List<String>
    ) : InsertHandler<LookupElement> {

        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            document.deleteString(context.startOffset, context.tailOffset)

            val templateManager = TemplateManager.getInstance(context.project)
            val templateText = "$functionName(${params.joinToString(", ") { "$$it$" }})"
            val template = templateManager.createTemplate("${functionName}Template", "nuXmv", templateText)
            for (param in params) {
                template.addVariable(param, TextExpression(param), TextExpression(param), true)
            }
            templateManager.startTemplate(context.editor, template)
        }
    }
}
