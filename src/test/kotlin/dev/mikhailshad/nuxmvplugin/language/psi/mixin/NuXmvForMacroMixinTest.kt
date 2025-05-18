package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForLoopMacro

class NuXmvForMacroMixinTest : NuXmvCodeInsightFixtureTestCase() {

    override fun getBasePath(): String {
        return "macro"
    }

    fun testMacroExpansion() {
        val modelText = """
            MODULE main
            VAR
              x : array 1..5 of boolean;

            #FOR i IN 1..5
              ASSIGN
                x[i] := i mod 2 = 0;
            #END
        """.trimIndent()
        val testFile = myFixture.configureByText(NuXmvFileType, modelText)
        val forMacro = PsiTreeUtil.findChildrenOfType(testFile, NuXmvForLoopMacro::class.java).first()
        val expandedText = forMacro.expand()
        assertEquals(
            """
            ASSIGN
                x[1] := 1 mod 2 = 0;
            ASSIGN
                x[2] := 2 mod 2 = 0;
            ASSIGN
                x[3] := 3 mod 2 = 0;
            ASSIGN
                x[4] := 4 mod 2 = 0;
            ASSIGN
                x[5] := 5 mod 2 = 0;
        """.trimIndent(),
            expandedText.trimIndent()
        )
    }

    fun testEmptyBody() {
        val emptyBodyText = """
            MODULE main
            #FOR i IN 1..3
            #END
        """.trimIndent()

        val file = myFixture.configureByText(NuXmvFileType, emptyBodyText)
        val forMacro = PsiTreeUtil.findChildrenOfType(file, NuXmvForLoopMacro::class.java).first()
        val expandedText = forMacro.expand()
        assertTrue(expandedText.isEmpty())
    }

    fun testEmptyRange() {
        val emptyRangeText = """
            MODULE main
            #FOR i IN 5..1
              DEFINE
                value_i := i;
            #END
        """.trimIndent()

        val file = myFixture.configureByText(NuXmvFileType, emptyRangeText)
        val forMacro = PsiTreeUtil.findChildrenOfType(file, NuXmvForLoopMacro::class.java).first()
        val expandedText = forMacro.expand()
        assertTrue(expandedText.isEmpty())
    }

    fun testNestedMacroExpansion() {
        val modelText = """
        MODULE main
        VAR
          x : array 1..5 of boolean;

        #FOR i IN 1..2
          #FOR j IN 1..2
            ASSIGN
              x[i][j] := 0;
            #FOR k IN 1..2
            #END
          #END
        #END
    """.trimIndent()
        val testFile = myFixture.configureByText(NuXmvFileType, modelText)
        val forMacro = PsiTreeUtil.findChildrenOfType(testFile, NuXmvForLoopMacro::class.java).first()
        val expandedText = forMacro.expand()
        assertEquals(
            """
                ASSIGN
                      x[1][1] := 0;
                ASSIGN
                      x[1][2] := 0;

                ASSIGN
                      x[2][1] := 0;
                ASSIGN
                      x[2][2] := 0;
                
            """.trimIndent(),
            expandedText.trimIndent()
        )
    }
}
