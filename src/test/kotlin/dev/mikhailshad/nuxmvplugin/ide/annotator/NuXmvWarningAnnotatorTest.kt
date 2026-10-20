package dev.mikhailshad.nuxmvplugin.ide.annotator

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleTypeSpecifier
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

class NuXmvWarningAnnotatorTest : NuXmvCodeInsightFixtureTestCase() {

    fun testTimedConstructsInTimedModel() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            @TIME_DOMAIN continuous
            MODULE main
            VAR
                c : clock;
            URGENT c = 0;
            LTLSPEC G X~ (c > 0);
            INVARSPEC time >= 0;
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun testTimedConstructsOutsideTimedModel() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            MODULE main
            VAR
                c : <warning descr="'clock' is available only in timed models (@TIME_DOMAIN continuous)">clock</warning>;
            <warning descr="'URGENT' is available only in timed models (@TIME_DOMAIN continuous)">URGENT</warning> TRUE;
            LTLSPEC G <warning descr="'X~' is available only in timed models (@TIME_DOMAIN continuous)">X~</warning> TRUE;
            INVARSPEC <error descr="Unresolved reference: time">time</error> >= 0;
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun testUnknownTimeDomain() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            @TIME_DOMAIN <error descr="Unknown time domain 'discrete', expected 'none' or 'continuous'">discrete</error>
            MODULE main
            VAR x : boolean;
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun testPslBodyIsNotCheckedForReferences() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            MODULE main
            VAR
                a : boolean;
                b : boolean;
            PSLSPEC forall i in {0, 1} : always (a -> next! b);
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun testCapitalClockIsTypeInTimedModel() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            @TIME_DOMAIN continuous
            MODULE main
            VAR
                c : Clock;
            INVARSPEC TRUE;
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
        val type = PsiTreeUtil.findChildOfType(myFixture.file, NuXmvSimpleTypeSpecifier::class.java)
        assertEquals(NuXmvBuiltInType.CLOCK, type?.resolveType())
    }

    fun testNextOutsideNextExpressions() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            MODULE main
            VAR
                x : boolean;
                y : boolean;
            DEFINE
                d := next(x);
                e := d & y;
            ASSIGN
                next(y) := next(x);
                init(x) := <error descr="'next' is not allowed in ASSIGN init(…)">next(y)</error>;
            INIT <error descr="'next' is not allowed in INIT">next(x)</error> = x;
            INVAR <error descr="'e' uses 'next' (via DEFINE), which is not allowed in INVAR">e</error>;
            TRANS next(x) = !x & e;
            TRANS next(<error descr="Nested 'next'">next(x)</error>) = x;
            TRANS next(<error descr="'d' uses 'next' (via DEFINE) inside 'next'">d</error>) = x;
            FAIRNESS <error descr="'next' is not allowed in FAIRNESS">next(x)</error>;
            CTLSPEC AG (<error descr="'next' is not allowed in CTLSPEC">next(x)</error> = x);
            INVARSPEC next(x) = x;
            LTLSPEC G (next(x) = x);
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun testWordConstantRestrictions() {
        myFixture.configureByText(
            NuXmvFileType,
            """
            MODULE main
            DEFINE
                ok := 0b_1011 & 0ud8_255 & 0h8_ff & 0sd4_8 & 0b_1_ & 0uH8_Ff & 0o_37;
                w0 := <error descr="Width of a word constant must be in 1..2147483647">0b0_1</error>;
                wide := <error descr="Width of a word constant must be in 1..2147483647">0b2147483648_1</error>;
                dec := <error descr="Decimal word constant requires a width">0d_19</error>;
                digit := <error descr="Digit '2' is not allowed in a binary word constant">0b_2</error>;
                oct := <error descr="Digit '8' is not allowed in an octal word constant">0o_8</error>;
                fit := <error descr="Value of word constant 0d8_256 does not fit in 8 bits">0d8_256</error>;
                fit2 := <error descr="Value of word constant 0b2_111 does not fit in 2 bits">0b2_111</error>;
            """.trimIndent()
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun testPslSpecificationRequiresSemicolon() {
        val file = myFixture.configureByText(
            NuXmvFileType,
            "MODULE main\nVAR p : boolean;\nPSLSPEC AG p\nINVARSPEC p;\n"
        )
        assertNotNull(PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java))
    }
}
