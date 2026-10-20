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

    fun testTimedAndPslFormatting() {
        val before = "@TIME_DOMAIN   continuous\nMODULE main\nVAR c:clock;\np:boolean;\nURGENT    c=0;\n" +
                "LTLSPEC G[0,5]p;\nLTLSPEC F  [ 2 , +oo )  p;\nLTLSPEC p U[1,3]p;\nPSLSPEC {p; p[*]}  |=> {p};"
        val after = """
            @TIME_DOMAIN continuous
            MODULE main
              VAR
                c : clock;
                p : boolean;
              URGENT c = 0;
              LTLSPEC G [0, 5] p;
              LTLSPEC F [2, +oo) p;
              LTLSPEC p U [1, 3] p;
              PSLSPEC {p; p[*]}  |=> {p};
        """.trimIndent()
        myFixture.configureByText(NuXmvFileType, before)
        myFixture.performEditorAction("ReformatCode")
        assertEquals(after, myFixture.file.text)
    }

    fun testOneDeclarationPerLine() {
        val before = "MODULE main\nVAR a:boolean;b:boolean;\nIVAR i:boolean;j:boolean;\nFROZENVAR p:real;q:real;\n" +
                "DEFINE d:=a;e:=b;\nASSIGN init(a):=TRUE;next(a):=b;\nFUN f:real -> real;g:real -> real;\nCONSTANTS c1, c2;"
        val after = """
            MODULE main
              VAR
                a : boolean;
                b : boolean;
              IVAR
                i : boolean;
                j : boolean;
              FROZENVAR
                p : real;
                q : real;
              DEFINE
                d := a;
                e := b;
              ASSIGN
                init(a) := TRUE;
                next(a) := b;
              FUN
                f : real -> real;
                g : real -> real;
              CONSTANTS c1, c2;
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
