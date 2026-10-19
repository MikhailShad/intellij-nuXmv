package dev.mikhailshad.nuxmvplugin.ide.structure

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration

class NuXmvNavBarModelExtensionTest : NuXmvCodeInsightFixtureTestCase() {
    private val extension = NuXmvNavBarModelExtension()

    fun testCaretInsideVariableLeadsToVariableThenModuleThenFile() {
        val file = myFixture.configureByText(NuXmvFileType, "MODULE main\nVAR\n    counter : 0..3;\n")
        val leaf = checkNotNull(file.findElementAt(file.text.indexOf("0..3")))

        val variable = extension.adjustElement(leaf)
        assertInstanceOf(variable, NuXmvSingleVarDeclaration::class.java)
        assertEquals("counter: 0..3", extension.getPresentableText(variable))

        val module = extension.getParent(variable)
        assertInstanceOf(module, NuXmvModule::class.java)
        assertEquals("main", extension.getPresentableText(module))

        assertSame(file, extension.getParent(checkNotNull(module)))
        assertNull(extension.getParent(file))
    }
}
