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

    fun testTimedModel() {
        doTest(true, true)
    }

    fun testPslSpec() {
        doTest(true, true)
    }

    fun testCompute() {
        doTest(true, true)
    }

    fun testParSynth() {
        doTest(true, true)
    }

    fun testPredMirror() {
        doTest(true, true)
    }

    fun testBoundedLtl() {
        doTest(true, true)
    }

    fun testRtctl() {
        doTest(true, true)
    }

    fun testIndexBeforeDot() {
        doTest(true, true)
    }

    fun testWordTypeSpacing() {
        doTest(true, true)
    }

    fun testEmptyModuleParameters() {
        doTest(true, true)
    }

    fun testNumericNameSegments() {
        doTest(true, true)
    }

    fun testQuotedIdentifiers() {
        doTest(true, true)
    }

    fun testSpecContext() {
        doTest(true, true)
    }

    fun testTemporalPrecedence() {
        doTest(true, true)
    }

    fun testRecovery() {
        doTest(true, false)
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/parser"
    }
}
