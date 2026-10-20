package dev.mikhailshad.nuxmvplugin.ide.navigation

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedSpecification

class NuXmvGotoDeclarationTest : NuXmvCodeInsightFixtureTestCase() {
    private val simpleModel = "MODULE main\nVAR\n  counter : 0..3;\nASSIGN\n  next(counter) := counter;\n"
    private val flatModel = "MODULE main\nVAR\n  con.s : 0..3;\nASSIGN\n  next(con.s) := con.s;\n"

    fun testUsageNavigatesToSimpleDeclaration() {
        assertEquals(
            simpleModel.indexOf("counter :"),
            gotoDeclarationFrom(simpleModel, simpleModel.indexOf("next(counter)") + 7)
        )
    }

    fun testEverySegmentOfFlatUsageNavigatesToDeclaration() {
        val usage = flatModel.indexOf("next(con.s)") + 5
        val declaration = flatModel.indexOf("con.s :")
        assertEquals(declaration, gotoDeclarationFrom(flatModel, usage + 1))
        assertEquals(declaration, gotoDeclarationFrom(flatModel, usage + 4))
    }

    fun testCaretStaysOnEverySegmentOfFlatDeclaration() {
        val declaration = flatModel.indexOf("con.s :")
        assertEquals(declaration + 1, gotoDeclarationFrom(flatModel, declaration + 1))
        assertEquals(declaration + 4, gotoDeclarationFrom(flatModel, declaration + 4))
    }

    fun testNamedSpecificationKeepsItsName() {
        val file = myFixture.configureByText(NuXmvFileType, "MODULE main\nVAR p : boolean;\nLTLSPEC NAME p1 := G p;\n")
        val spec = checkNotNull(PsiTreeUtil.findChildOfType(file, NuXmvNamedSpecification::class.java))
        assertEquals("p1", spec.name)
    }

    private fun gotoDeclarationFrom(text: String, offset: Int): Int {
        myFixture.configureByText(NuXmvFileType, text)
        myFixture.editor.caretModel.moveToOffset(offset)
        myFixture.performEditorAction("GotoDeclaration")
        return myFixture.editor.caretModel.offset
    }
}
