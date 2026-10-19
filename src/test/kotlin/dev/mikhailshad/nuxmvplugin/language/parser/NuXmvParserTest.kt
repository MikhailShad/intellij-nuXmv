package dev.mikhailshad.nuxmvplugin.language.parser

import com.intellij.testFramework.ParsingTestCase

class NuXmvParserTest : ParsingTestCase("", "smv", NuXmvParserDefinition()) {

    fun testExample() {
        doTest(true, true)
    }

    fun testMultiModule() {
        doTest(true, true)
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/parser"
    }
}
