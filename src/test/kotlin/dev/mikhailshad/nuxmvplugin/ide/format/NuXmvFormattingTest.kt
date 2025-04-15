package dev.mikhailshad.nuxmvplugin.ide.format

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.name


class NuXmvFormattingTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String {
        return "format"
    }

    fun testSimpleFormatting() {
        val before =
            "   MODULE    main   VAR   \tx\t:  boolean   ;\n z : real;     ASSIGN       x\t:=TRUE;INIT x \t\t=\tFALSE"
        val after = """
            MODULE main
              VAR
                x : boolean;
                z : real;
              ASSIGN
                x := TRUE;
              INIT x = FALSE
        """.trimIndent()
        myFixture.configureByText(NuXmvFileType, before)
        myFixture.performEditorAction("ReformatCode")
        assertEquals(after, myFixture.file.text)
    }

    fun testComplexFormatting() {
        val files = Files.walk(Paths.get(testDataPath, "input")).filter { it.isRegularFile() }.map { it.name }.toList()

        files.forEach { filename ->
            myFixture.configureByFiles("input/${filename}")
            myFixture.performEditorAction("ReformatCode")
            myFixture.checkResult(Files.readString(Paths.get(testDataPath, "expected", filename)))
        }
    }
}
