package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.testFramework.UsefulTestCase
import java.io.File

class TraceBlockOutputParserTest : UsefulTestCase() {
    private fun output(fileName: String): String = File("src/test/testData/run/output/$fileName").readText()

    fun testBddInvariantCounterexample() {
        val trace = TraceBlockOutputParser.parseOutput(output("bdd_checks.txt")).single()
        assertEquals(1, trace.traceNumber)
        assertEquals("s != 3", trace.specification)
        assertEquals("Counterexample", trace.type)
        assertEquals(4, trace.states.size)
        assertEquals("3", trace.states["1.4"]?.variables?.get("m.c"))
    }

    fun testLtlCounterexamplesWithLoops() {
        val traces = TraceBlockOutputParser.parseOutput(output("ltl_loop.txt"))
        assertEquals(listOf(1, 2), traces.map { it.traceNumber })
        assertEquals(listOf("G s < 3", "F ( G s = 0)"), traces.map { it.specification })
        assertTrue(traces[0].states.getValue("1.1").isLoopStart)
        assertEquals("FALSE", traces[0].states.getValue("1.5").variables["m.c"])
    }

    fun testIc3Counterexample() {
        val trace = TraceBlockOutputParser.parseOutput(output("ic3_invar.txt")).single()
        assertEquals("s != 3", trace.specification)
        assertEquals("IC3 counterexample", trace.description)
    }

    fun testMsatInductionFailureAndCounterexample() {
        val traces = TraceBlockOutputParser.parseOutput(output("msat_checks.txt"))
        assertEquals(listOf("x < 3", "x < 3"), traces.map { it.specification })
        assertEquals(listOf("Induction Sequence", "Counterexample"), traces.map { it.type })
        assertEquals("f'3/2", traces[1].states["1.4"]?.variables?.get("r"))
    }

    fun testSimulationTraceHasNoSpecification() {
        val traces = TraceBlockOutputParser.parseOutput(output("bdd_simulation_show_traces.txt"))
        val simulation = traces.last { it.type == "Simulation" }
        assertEquals(2, simulation.traceNumber)
        assertEquals("", simulation.specification)
        assertEquals(3, simulation.states.size)
    }

    fun testTimedSimulationSkipsTimeElapseLines() {
        val trace = ShowTracesOutputParser.parseOutput(output("timed_simulation.txt")).single()
        assertEquals("Timed Simulation", trace.description)
        assertEquals(listOf("1.1", "1.2", "1.3", "1.4"), trace.states.keys.toList())
        assertEquals("5.0", trace.states["1.2"]?.variables?.get("c"))
        assertEquals("FALSE", trace.states["1.3"]?.variables?.get("s"))
    }

    fun testShowTracesAddsSimulationTraces() {
        val traces = ShowTracesOutputParser.parseOutput(output("msat_simulation.txt"))
        assertEquals("Simulation", traces.single().type)
        assertEquals(4, traces.single().states.size)
    }
}
