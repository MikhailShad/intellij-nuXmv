package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.testFramework.UsefulTestCase
import java.io.File

class ShowTracesOutputParserTest : UsefulTestCase() {

    fun testParseShowTracesOutput() {
        val outputFile = File("src/test/testData/parser/output/show_traces_output.txt")
        assertTrue("Test output file does not exist", outputFile.exists())

        val output = outputFile.readText()
        val parser = ShowTracesOutputParser()

        val traces = parser.parseOutput(output)

        assertEquals("Should parse 2 traces", 2, traces.size)

        val trace1 = traces[0]
        assertEquals("Specification should match", "LTL Counterexample", trace1.specification)
        assertEquals("Spec type should match", "Counterexample", trace1.specType)
        assertEquals("Trace 1 should have 3 states", 3, trace1.states.size)

        val state1_1 = trace1.states[0]
        assertEquals("State 1 number should match", 1.1, state1_1.stateNumber)
        assertEquals("State 1 state variable should match", "welcome", state1_1.variables["state"])
        assertEquals("State 1 input variable should match", "cardIn", state1_1.variables["input"])
        assertTrue("State 1.1 should be marked as loop state", state1_1.isLoopState)

        val state1_2 = trace1.states[1]
        assertEquals("State 2 number should match", 1.2, state1_2.stateNumber)
        assertEquals("State 2 state variable should match", "enterPin", state1_2.variables["state"])

        val state1_3 = trace1.states[2]
        assertEquals("State 3 number should match", 1.3, state1_3.stateNumber)
        assertEquals("State 3 state variable should match", "enterPin", state1_3.variables["state"])
        assertEquals("State 3 input variable should match", "wrongPin", state1_3.variables["input"])

        // Проверка второго трейса
        val trace2 = traces[1]
        assertEquals("Trace 2 specification should match", "LTL Counterexample", trace2.specification)
        assertEquals("Trace 2 spec type should match", "Counterexample", trace2.specType)
        assertEquals("Trace 2 should have 7 states", 7, trace2.states.size)

        val state2_1 = trace2.states[0]
        assertEquals("Trace 2 State 1 number should match", 2.1, state2_1.stateNumber)
        assertEquals("Trace 2 State 1 state variable should match", "welcome", state2_1.variables["state"])
        assertEquals("Trace 2 State 1 input variable should match", "cardIn", state2_1.variables["input"])

        val state2_5 = trace2.states[4]
        assertEquals("Trace 2 State 5 number should match", 2.5, state2_5.stateNumber)
        assertEquals("Trace 2 State 5 input variable should match", "ack", state2_5.variables["input"])
        assertTrue("State 2.5 should be marked as loop state", state2_5.isLoopState)

        val state2_7 = trace2.states[6]
        assertEquals("Trace 2 State 7 number should match", 2.7, state2_7.stateNumber)
        assertEquals("Trace 2 State 7 state variable should match", "tryAgain", state2_7.variables["state"])
        assertEquals("Trace 2 State 7 input variable should match", "ack", state2_7.variables["input"])
    }
}
