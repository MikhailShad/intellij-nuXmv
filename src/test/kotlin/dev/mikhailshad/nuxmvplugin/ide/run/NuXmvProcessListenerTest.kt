package dev.mikhailshad.nuxmvplugin.ide.run

import NuXmvCodeInsightFixtureTestCase
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import dev.mikhailshad.nuxmvplugin.ide.run.command.*
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class NuXmvProcessListenerTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "run"

    private class ScriptedProcessHandler : ProcessHandler() {
        private val input = ByteArrayOutputStream()

        fun takeSentCommands(): List<String> =
            input.toString().lines().filter { it.isNotBlank() }.also { input.reset() }

        fun prompt(output: String) = notifyTextAvailable("$output\nnuXmv > ", ProcessOutputTypes.STDOUT)

        fun terminate() = notifyProcessTerminated(0)

        override fun destroyProcessImpl() = notifyProcessTerminated(0)
        override fun detachProcessImpl() = notifyProcessDetached()
        override fun detachIsDefault(): Boolean = false
        override fun getProcessInput(): OutputStream = input
    }

    private val checkOutput = """
        -- specification  F ( G state = thanksGoodbye |  G state = sorry)  is false
        -- as demonstrated by the following execution sequence
        Trace Description: LTL Counterexample
        Trace Type: Counterexample
          -> State: 1.1 <-
            state = welcome
            input = cardIn
          -- Loop starts here
          -> State: 1.2 <-
            state = enterPin
          -> State: 1.3 <-
    """.trimIndent()

    private val propertyList = """
        **** PROPERTY LIST [ Type, Status, Counter-example Number, Name ] ****
        --------------------------  PROPERTY LIST  -------------------------
        000 : F ( G state = thanksGoodbye |  G state = sorry)
          [LTL            Unchecked      N/A    N/A]
        001 :( G (state = welcome ->  F input = cardIn) ->  F ( G state = thanksGoodbye |  G state = sorry))
          [LTL            Unchecked      N/A    s2]
    """.trimIndent()

    private val showTraces = """
            <!-- ################### Trace number: 1 ################### -->
        Trace Description: LTL Counterexample
        Trace Type: Counterexample
          -> State: 1.1 <-
            state = welcome
            input = cardIn
          -- Loop starts here
          -> State: 1.2 <-
            state = enterPin
            input = cardIn
          -> State: 1.3 <-
            state = enterPin
            input = cardIn
    """.trimIndent()

    private fun startSession(settings: (NuXmvModelProfile) -> NuXmvLaunchSettings): Pair<ScriptedProcessHandler, MutableList<List<CounterexampleTrace>>> {
        val profile = NuXmvModelProfile.of(myFixture.configureByFile("Atm.smv") as NuXmvFile)
        val shown = mutableListOf<List<CounterexampleTrace>>()
        val handler = ScriptedProcessHandler()
        val sequence = NuXmvCommandSequence(NuXmvLaunchPlanner.plan(profile, settings(profile)))
        handler.addProcessListener(NuXmvProcessListener(sequence, { _, _ -> }) { shown += it })
        handler.startNotify()
        return handler to shown
    }

    private fun unnamedLtl(profile: NuXmvModelProfile): NuXmvLaunchSettings {
        val spec = profile.specsOf(NuXmvSpecKind.LTL).single { it.name == null }
        return NuXmvLaunchSettings(selection = NuXmvCheckSelection.single(spec.kind, spec.key))
    }

    fun testCounterexampleIsShownWhenSessionEndsWithQuit() {
        val (handler, shown) = startSession(::unnamedLtl)

        handler.prompt("")
        assertEquals(listOf("go"), handler.takeSentCommands())
        handler.prompt("")
        assertEquals(listOf("show_property"), handler.takeSentCommands())
        handler.prompt(propertyList)
        assertEquals(listOf("check_ltlspec -n 0"), handler.takeSentCommands())
        handler.prompt(checkOutput)
        assertEquals(listOf("show_traces -a -v"), handler.takeSentCommands())
        handler.prompt(showTraces)
        assertEquals(listOf("quit"), handler.takeSentCommands())
        handler.terminate()

        val trace = shown.single().single()
        assertEquals("F ( G state = thanksGoodbye | G state = sorry)", trace.specification)
        assertEquals(listOf("1.1", "1.2", "1.3"), trace.states.keys.toList())
        assertEquals("cardIn", trace.states.getValue("1.3").variables["input"])
        assertTrue(trace.states.getValue("1.2").isLoopStart)
    }

    fun testInteractiveSessionShowsTracesAfterPlannedCommandsAndAfterTypedOnes() {
        val (handler, shown) = startSession { unnamedLtl(it).copy(interactiveSession = true) }

        handler.prompt("")
        handler.prompt("")
        handler.prompt(propertyList)
        handler.prompt(checkOutput)
        handler.prompt(showTraces)
        assertEquals(1, shown.size)

        handler.prompt("-- specification  G state = welcome  is true")
        assertEquals(1, shown.size)

        handler.prompt(checkOutput.replace("1.", "2."))
        assertEquals(listOf(1, 2), shown.last().map { it.traceNumber })

        handler.terminate()
        assertEquals(2, shown.size)
    }
}
