// NuXmvOperatorCompletionProvider.kt
package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

object NuXmvOperatorCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val operators = mapOf(
        "!" to "Logical NOT",
        "&" to "Logical AND",
        "|" to "Logical OR",
        "xor" to "Exclusive OR",
        "xnor" to "Exclusive NOR",
        "->" to "Implication",
        "<->" to "Equivalence",
        "=" to "Equality",
        "!=" to "Inequality",
        "<" to "Less than",
        ">" to "Greater than",
        "<=" to "Less than or equal",
        ">=" to "Greater than or equal",
        "+" to "Addition",
        "-" to "Subtraction",
        "*" to "Multiplication",
        "/" to "Division",
        "mod" to "Modulo",
        "<<" to "Shift left",
        ">>" to "Shift right",
        "::" to "Concatenation",
        "in" to "Inclusion",
        "union" to "Union",
        "case" to "Case expression"
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        // Only suggest operators in expression contexts
        operators.forEach { (op, desc) ->
            val element = if (op == "case") {
                LookupElementBuilder.create(op)
                    .withTypeText(desc)
                    .bold()
                    .withInsertHandler { insertContext, _ ->
                        val document = insertContext.document
                        val offset = insertContext.selectionEndOffset
                        document.insertString(offset, "\n  condition : result;\n  TRUE : defaultValue;\nesac")
                        insertContext.editor.caretModel.moveToOffset(offset + 3)
                    }
            } else {
                LookupElementBuilder.create(op)
                    .withTypeText(desc)
            }

            resultSet.addElement(element)
        }
    }
}
