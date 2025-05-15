package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForLoopMacro
import java.io.File

class NuXmvForMacroMixinTest : NuXmvCodeInsightFixtureTestCase() {

    override fun getBasePath(): String {
        return "macro"
    }

    fun testMacroExpansion() {
        val testFile = myFixture.configureByFile("ForMacro.smv")
        val forMacro = PsiTreeUtil.findChildrenOfType(testFile, NuXmvForLoopMacro::class.java).first()
        val expandedText = forMacro.expand()
        val expectedText = File("src/test/testData/macro/ForMacroExpanded.smv").readText()
        assertEquals(expectedText.trimIndent(), expandedText.trimIndent())
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
        assertEquals("-- Empty FOR macro expansion", expandedText)
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
        assertEquals("-- Empty FOR macro expansion", expandedText)
    }

}
