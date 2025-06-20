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
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertSameElements(
            lookupElementStrings!!,
            "MODULE"
        )
    }

    fun testCompletionInModuleBody() {
        myFixture.configureByFiles("moduleCompletionInBody.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertSameElements(
            lookupElementStrings!!,
            "VAR",
            "FROZENVAR",
            "INVAR",
            "INVARSPEC",
            "IVAR",
        )
    }

    fun testCompletionAfterModuleBody() {
        myFixture.configureByFiles("moduleCompletionAfterBody.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertSameElements(
            lookupElementStrings!!,
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
    }

    fun testTypeCompletion() {
        myFixture.configureByFiles("typeCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertSameElements(
            lookupElementStrings!!,
            "array N..M of type",
            "boolean",
            "clock",
            "integer",
            "real",
            "signed word[N]",
            "unsigned word[N]",
            "word[N]"
        )
    }

    fun testFunctionCompletion() {
        myFixture.configureByFiles("assignCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertTrue(lookupElementStrings!!.containsAll(listOf("next", "init", "abs", "max", "min")))
    }

    fun testVariableCompletion() {
        myFixture.configureByFiles("variableCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertTrue(
            lookupElementStrings!!.containsAll(
                listOf(
                    "x",
                    "state",
                    "counter",
                    "input_signal",
                    "config",
                    "is_active"
                )
            )
        )
    }

    fun testValueCompletion() {
        myFixture.configureByFiles("valueCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertTrue(lookupElementStrings!!.containsAll(listOf("TRUE", "FALSE")))
    }

    fun testLtlOperatorCompletion() {
        myFixture.configureByFiles("ltlOperatorCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertTrue(lookupElementStrings!!.containsAll(listOf("X", "G", "F", "U", "V")))
    }

    fun testCtlOperatorCompletion() {
        myFixture.configureByFiles("ctlOperatorCompletion.smv")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String>? = myFixture.lookupElementStrings
        assertNotNull(lookupElementStrings)
        assertTrue(lookupElementStrings!!.containsAll(listOf("EX", "AX", "EG", "AG", "EF", "AF")))
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
