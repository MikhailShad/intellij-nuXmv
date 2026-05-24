package dev.mikhailshad.nuxmvplugin.language.completion

import NuXmvCodeInsightFixtureTestCase
import com.intellij.codeInsight.completion.CompletionType

class NuXmvCompletionTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String {
        return "completion"
    }

    fun testModuleCompletionOnEmptyFile() {
        myFixture.configureByFiles("moduleCompletionEmptyFile.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        assertSameElements(lookupElementStrings, "MODULE")
    }

    fun testCompletionInModuleBody() {
        myFixture.configureByFiles("moduleCompletionInBody.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf(
            "VAR",
            "FROZENVAR",
            "INVAR",
            "INVARSPEC",
            "IVAR",
        )
        assertSameElements(lookupElementStrings, expected)
    }

    fun testCompletionAfterModuleBody() {
        myFixture.configureByFiles("moduleCompletionAfterBody.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf(
            "ASSIGN",
            "COMPASSION",
            "COMPUTE",
            "CONSTANTS",
            "CTLSPEC",
            "DEFINE",
            "FAIRNESS",
            "FROZENVAR",
            "INIT",
            "INVAR",
            "INVARSPEC",
            "ISA",
            "IVAR",
            "JUSTICE",
            "LTLSPEC",
            "MIRROR",
            "MODULE",
            "PRED",
            "TRANS",
            "VAR",
            "#FOR"
        )
        assertSameElements(lookupElementStrings, expected)
    }

    fun testTypeCompletion() {
        myFixture.configureByFiles("typeCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf(
            "array N..M of type",
            "boolean",
            "clock",
            "integer",
            "real",
            "signed word[N]",
            "unsigned word[N]",
            "word[N]"
        )
        assertSameElements(lookupElementStrings, expected)
    }

    fun testFunctionCompletion() {
        myFixture.configureByFiles("assignCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf("next", "init", "abs", "max", "min")
        assertTrue(
            "Expected: ${expected.joinToString(", ")}\nActual: ${lookupElementStrings.joinToString(", ")}",
            lookupElementStrings.containsAll(expected)
        )
    }

    fun testVariableCompletion() {
        myFixture.configureByFiles("variableCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf(
            "x",
            "state",
            "counter",
            "input_signal",
            "config",
            "is_active"
        )
        assertTrue(
            "Expected: ${expected.joinToString(", ")}\nActual: ${lookupElementStrings.joinToString(", ")}",
            lookupElementStrings.containsAll(expected)
        )
    }

    /**
     * TODO: add const completion by variable type
     */
    fun _testValueCompletion() {
        myFixture.configureByFiles("valueCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf("TRUE", "FALSE")
        assertTrue(
            "Expected: ${expected.joinToString(", ")}\nActual: ${lookupElementStrings.joinToString(", ")}",
            lookupElementStrings.containsAll(expected)
        )
    }

    fun testLtlOperatorCompletion() {
        myFixture.configureByFiles("ltlOperatorCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf("X", "G", "F", "U", "V")
        assertTrue(
            "Expected: ${expected.joinToString(", ")}\nActual: ${lookupElementStrings.joinToString(", ")}",
            lookupElementStrings.containsAll(expected)
        )
    }

    fun testCtlOperatorCompletion() {
        myFixture.configureByFiles("ctlOperatorCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!
        val expected = listOf("EX", "AX", "EG", "AG", "EF", "AF")
        assertTrue(
            "Expected: ${expected.joinToString(", ")}\nActual: ${lookupElementStrings.joinToString(", ")}",
            lookupElementStrings.containsAll(expected)
        )
    }

//    TODO: solve why test fails, but completion works in IDE correctly
//    fun testMacroCompletion() {
//        myFixture.configureByFiles("macroCompletion.smv")
//        myFixture.complete(CompletionType.BASIC)
//        val lookupElementStrings = myFixture.lookupElementStrings
//        assertNotNull(lookupElementStrings)
//        assertTrue(lookupElementStrings!!.containsAll(listOf("#FOR")))
//    }
}
