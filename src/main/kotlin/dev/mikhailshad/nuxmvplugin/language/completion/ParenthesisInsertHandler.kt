package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement

class ParenthesisInsertHandler private constructor(private val withParameters: Boolean) : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val document = editor.document
        val caretModel = editor.caretModel
        val offset = context.tailOffset

        document.insertString(offset, "()")

        if (withParameters) {
            caretModel.moveToOffset(offset + 1)
        } else {
            caretModel.moveToOffset(offset + 2)
        }
    }

    companion object {
        val WITH_PARAMETERS = ParenthesisInsertHandler(withParameters = true)
        val WITHOUT_PARAMETERS = ParenthesisInsertHandler(withParameters = false)
    }
}
