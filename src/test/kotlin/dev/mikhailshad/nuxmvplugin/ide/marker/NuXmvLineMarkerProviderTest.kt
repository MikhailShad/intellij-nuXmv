package dev.mikhailshad.nuxmvplugin.ide.marker

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

class NuXmvLineMarkerProviderTest : NuXmvCodeInsightFixtureTestCase() {
    fun testModuleDeclarationAndUsagesHaveGutters() {
        myFixture.configureByText(
            NuXmvFileType,
            "MODULE cell\nVAR v : boolean;\n\nMODULE derived\nISA cell\n\nMODULE main\nVAR c : cell;\n"
        )
        val tooltips = myFixture.findAllGutters().map { it.tooltipText }
        assertEquals(1, tooltips.count { it == "Navigate to module usages" })
        assertEquals(2, tooltips.count { it == "Navigate to module declaration" })
    }
}
