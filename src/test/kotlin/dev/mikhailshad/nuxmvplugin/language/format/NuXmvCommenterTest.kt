package dev.mikhailshad.nuxmvplugin.language.format

import NuXmvCodeInsightFixtureTestCase
import com.intellij.codeInsight.generation.actions.CommentByBlockCommentAction
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType


class NuXmvCommenterTest : NuXmvCodeInsightFixtureTestCase() {
    fun testLineCommenting() {
        val before = "MODULE main"
        val after = "--MODULE main"

        myFixture.configureByText(NuXmvFileType, "<caret>$before")
        val commentAction = CommentByLineCommentAction()
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult(after)
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult(before)
    }

    fun testBlockCommenting() {
        val before = "MODULE main"
        // There are new line breaks, however commenter does not place them in real editor
        val after = "/--\nMODULE main--/\n"

        myFixture.configureByText(NuXmvFileType, "<selection>$before</selection>")
        val commentAction = CommentByBlockCommentAction()
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult(after)
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult("$before\n") // Same here
    }
}
