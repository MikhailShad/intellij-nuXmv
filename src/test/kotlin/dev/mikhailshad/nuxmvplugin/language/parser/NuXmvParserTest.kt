package dev.mikhailshad.nuxmvplugin.language.parser

import com.intellij.testFramework.ParsingTestCase

class NuXmvParserTest : ParsingTestCase("", "smv", NuXmvParserDefinition()) {

    fun testExample() {
        doTest(true, true)
    }

    fun testMultiModule() {
        doTest(true, true)
    }

    fun testWordConstants() {
        doTest(true, true)
    }

    fun testRealConstants() {
        doTest(true, true)
    }

    fun testNegativeNumbers() {
        doTest(true, true)
    }

    fun testLtlOperators() {
        doTest(true, true)
    }

    fun testTimeAsIdentifier() {
        doTest(true, true)
    }

    fun testSetExpr() {
        doTest(true, true)
    }

    fun testWordOps() {
        doTest(true, true)
    }

    fun testBuiltins() {
        doTest(true, true)
    }

    fun testArrayDefine() {
        doTest(true, true)
    }

    fun testFunctionCall() {
        doTest(true, true)
    }

    fun testTypes() {
        doTest(true, true)
    }

    fun testPrecedence() {
        doTest(true, true)
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/parser"
    }
}
