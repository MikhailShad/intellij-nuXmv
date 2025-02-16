package dev.mikhailshad.nuxmvplugin.language.parser

import com.intellij.testFramework.ParsingTestCase

class NuXmvParserTest : ParsingTestCase("", "nuxmv", NuXmvParserDefinition()) {

    fun testExample() {
        doTest(true)
    }

    fun testMultiModule() {
        doTest(true)
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/parser"
    }
}
