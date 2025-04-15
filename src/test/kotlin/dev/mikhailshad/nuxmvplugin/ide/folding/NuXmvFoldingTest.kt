package dev.mikhailshad.nuxmvplugin.ide.folding

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

class NuXmvFoldingTest : NuXmvCodeInsightFixtureTestCase() {

    fun testModuleFolding() {
        myFixture.configureByText(
            NuXmvFileType, """
            <caret>MODULE main
              VAR
                param_qfok : boolean;
                _OK_ : boolean;
                ccont : boolean;
                cca : boolean;
                param_sdok : boolean;
                env : boolean;
                flby : boolean;
                param_accok : boolean;
                ite : boolean;
                param_ccseti : boolean;
                param_igsw : boolean;
                param_ccsetd : boolean;
                param_ccd : boolean;
                param_ccr : boolean;
                param_cconoff : boolean;
                param_vs : integer;
                param_bpa : boolean;
                param_cccanc : boolean;
                param_battok : boolean;
                param_gearok : boolean;
                call__main_ : _main_ (param_igsw, param_ccd, param_cconoff, param_bpa, param_cccanc, param_battok, param_gearok, param_qfok, param_sdok, param_accok, param_ccseti, param_ccsetd, param_ccr, param_vs);
                call_PosEdge : PosEdge (cca);
                call_PosEdge2 : PosEdge (param_ccseti);
                call_PosEdge3 : PosEdge (param_ccsetd);
                call_PosEdge4 : PosEdge (param_ccr);
        
              ASSIGN
                _OK_ := ite;
                ccont := call__main_.ccont;
                cca := call__main_.cca;
                env := flby;
                init(flby) := ! (param_igsw);
                next(flby) := TRUE;
        """.trimIndent()
        )

        myFixture.doHighlighting()
        val foldRegions = myFixture.editor.foldingModel.allFoldRegions
        assertEquals(3, foldRegions.size)

        assertEquals("MODULE...", foldRegions[0].placeholderText)
        assertEquals("VAR...", foldRegions[1].placeholderText)
        assertEquals("ASSIGN...", foldRegions[2].placeholderText)
    }
}
