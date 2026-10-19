package dev.mikhailshad.nuxmvplugin.ide.annotator

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

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
}
