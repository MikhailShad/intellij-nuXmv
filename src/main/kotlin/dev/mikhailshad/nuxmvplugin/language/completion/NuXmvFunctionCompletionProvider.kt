package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

object NuXmvFunctionCompletionProvider : CompletionProvider<CompletionParameters>() {
    // Map of function names to their parameter documentation
    private val functions = mapOf(
        "next" to "expr",
        "init" to "expr",
        "abs" to "expr",
        "max" to "expr1, expr2",
        "min" to "expr1, expr2",
        "sin" to "expr",
        "cos" to "expr",
        "exp" to "expr",
        "tan" to "expr",
        "ln" to "expr",
        "pow" to "base, exponent",
        "asin" to "expr",
        "acos" to "expr",
        "atan" to "expr",
        "sqrt" to "expr",
        "word1" to "boolean_expr",
        "bool" to "expr",
        "toint" to "expr",
        "count" to "bool_expr1, bool_expr2, ...",
        "swconst" to "integer, bits",
        "uwconst" to "integer, bits",
        "signed" to "word_expr",
        "unsigned" to "word_expr",
        "sizeof" to "word_expr",
        "floor" to "real_expr",
        "extend" to "word_expr, bits",
        "resize" to "word_expr, size",
        "READ" to "array, index",
        "WRITE" to "array, index, value",
        "CONSTARRAY" to "type, value"
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        functions.forEach { (name, params) ->
            val element = LookupElementBuilder.create(name)
                .withTailText("($params)")
                .withTypeText("function")
                .withInsertHandler(ParenthesisInsertHandler.WITH_PARAMETERS)

            resultSet.addElement(element)
        }
    }
}
