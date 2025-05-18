package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.testFramework.UsefulTestCase
import java.io.File

class ShowTracesOutputParserTest : UsefulTestCase() {

    fun testParseShowTracesOutput() {
        val outputFile = File("src/test/testData/parser/output/show_traces_output.txt")
        assertTrue("Test output file does not exist", outputFile.exists())

        val output = outputFile.readText()
        val parser = ShowTracesOutputParser

        val traces = parser.parseOutput(output)

        assertEquals("Should parse 2 traces", 2, traces.size)

        val trace1 = traces[0]
        assertEquals("Trace 1 should have 3 states", 3, trace1.states.size)

        val state11 = trace1.states["1.1"] ?: throw Exception()
        assertEquals("State 1.1 state variable should match", "welcome", state11.variables["state"])
        assertEquals("State 1.1 input variable should match", "cardIn", state11.variables["input"])


        val state12 = trace1.states["1.2"] ?: throw Exception()
        assertEquals("State 1.2 state variable should match", "enterPin", state12.variables["state"])
        assertTrue("State 1.2 should be marked as loop state", state12.isLoopStart)

        val state13 = trace1.states["1.3"] ?: throw Exception()
        assertEquals("State 1.3 state variable should match", "enterPin", state13.variables["state"])
        assertEquals("State 1.3 input variable should match", "wrongPin", state13.variables["input"])

        // Проверка второго трейса
        val trace2 = traces[1]
        assertEquals("Trace 2 should have 7 states", 7, trace2.states.size)

        val state21 = trace2.states["2.1"] ?: throw Exception()
        assertEquals("State 2.1 number should match", "2.1", state21.stateNumber)
        assertEquals("State 2.1 state variable should match", "welcome", state21.variables["state"])
        assertEquals("State 2.1 input variable should match", "cardIn", state21.variables["input"])

        val state25 = trace2.states["2.5"] ?: throw Exception()
        assertEquals("State 2.5 input variable should match", "ack", state25.variables["input"])
        assertTrue("State 2.5 should be marked as loop state", state25.isLoopStart)

        val state27 = trace2.states["2.7"] ?: throw Exception()
        assertEquals("State 2.7 state variable should match", "tryAgain", state27.variables["state"])
        assertEquals("State 2.7 input variable should match", "ack", state27.variables["input"])
    }
}
