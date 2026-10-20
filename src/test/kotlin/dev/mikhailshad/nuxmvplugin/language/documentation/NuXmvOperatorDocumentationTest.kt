package dev.mikhailshad.nuxmvplugin.language.documentation

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

class NuXmvOperatorDocumentationTest : NuXmvCodeInsightFixtureTestCase() {
    private val provider = NuXmvDocumentationProvider()

    fun testLtlOperatorDocumentation() {
        assertTrue(docAt("LTLSPEC G (p -> F q);", "G").contains("globally"))
        assertTrue(docAt("LTLSPEC p U q;", "U").contains("until"))
        assertTrue(docAt("LTLSPEC G (x @F~ p) = 1;", "@F~").contains("next time"))
    }

    fun testCtlAndRtctlOperatorDocumentation() {
        assertTrue(docAt("CTLSPEC AG EF p;", "EF").contains("exists"))
        assertTrue(docAt("CTLSPEC EBF 1..3 p;", "EBF").contains("m ≤ i ≤ n"))
    }

    fun testNoDocumentationForPlainIdentifier() {
        val file = configure("LTLSPEC G p;")
        val leaf = file.findElementAt(file.text.indexOf("G p") + 2)
        assertNull(provider.getCustomDocumentationElement(myFixture.editor, file, leaf, 0))
    }

    private fun docAt(spec: String, operator: String): String {
        val file = configure(spec)
        val offset = file.text.indexOf(spec) + spec.indexOf(operator)
        val leaf = file.findElementAt(offset)
        val element = checkNotNull(provider.getCustomDocumentationElement(myFixture.editor, file, leaf, offset)) {
            "No documentation element for '$operator'"
        }
        return checkNotNull(provider.generateDoc(element, leaf))
    }

    private fun configure(spec: String) = myFixture.configureByText(
        NuXmvFileType,
        "MODULE main\nVAR p : boolean; q : boolean; x : 0..3;\n$spec\n"
    )
}
