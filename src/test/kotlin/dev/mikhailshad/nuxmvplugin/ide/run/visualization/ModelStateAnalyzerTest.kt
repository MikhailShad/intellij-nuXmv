package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration

class ModelStateAnalyzerTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "references"

    fun testFlatModelVariablesKeepFullNames() {
        val file = myFixture.configureByFile("flat_model.smv")
        val graph = ModelStateAnalyzer(project).analyzeModelFile(file.virtualFile)
        val conS = checkNotNull(graph.stateVariables["con.s"]) { "Known variables: ${graph.stateVariables.keys}" }
        assertFalse(conS.transitions.isEmpty())
        assertEquals("0", conS.initialValue)
    }

    fun testStructurePresentationShowsFullName() {
        val file = myFixture.configureByFile("flat_model.smv")
        val declaration = PsiTreeUtil.findChildrenOfType(file, NuXmvSingleVarDeclaration::class.java)
            .first { it.varName.name == "con.s" }
        assertEquals("con.s: {0, 1, 2}", declaration.presentation?.presentableText)
    }
}
