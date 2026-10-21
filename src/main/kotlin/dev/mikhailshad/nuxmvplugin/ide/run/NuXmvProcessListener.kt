package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Key
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvCommandSequence
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser.ShowTracesOutputParser
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser.TraceBlockOutputParser
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

/**
 * Drives the interactive nuXmv shell: sends the next command of [commands] on every prompt and collects traces
 * from the output of every command, including the ones the user types in the console.
 *
 * @param print prints a line to the run console
 * @param showTraces receives all traces collected so far, once the planned commands are done and whenever traces change after that
 */
class NuXmvProcessListener(
    private val commands: NuXmvCommandSequence,
    private val print: (String, ConsoleViewContentType) -> Unit,
    private val showTraces: (List<CounterexampleTrace>) -> Unit
) : ProcessListener {
    companion object {
        private val logger = Logger.getInstance(NuXmvProcessListener::class.java)
        private const val PROMPT = "nuXmv >"
    }

    private var currentCommand: String? = null
    private val currentOutputBuffer = StringBuilder()
    private val tracesByNumber = linkedMapOf<Int, CounterexampleTrace>()
    private var wasCommandPrinted = true
    private var sequenceFinished = false
    private var tracesChanged = false

    override fun startNotified(event: ProcessEvent) {}

    override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {}

    override fun processTerminated(event: ProcessEvent) {
        printCommandIfNeeded()
        collectTraces(takeOutput())
        showTracesIfChanged()
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        currentOutputBuffer.append(event.text)
        printCommandIfNeeded()
        if (!event.text.trim().endsWith(PROMPT)) return

        val output = takeOutput()
        collectTraces(output)
        val nextCommand = commands.next(currentCommand, output)
        currentCommand = nextCommand
        if (nextCommand != null) {
            wasCommandPrinted = false
            send(event, nextCommand)
        } else {
            sequenceFinished = true
        }
        if (sequenceFinished) showTracesIfChanged()
    }

    private fun takeOutput(): String = currentOutputBuffer.toString().also { currentOutputBuffer.clear() }

    private fun send(event: ProcessEvent, command: String) {
        try {
            val writer = BufferedWriter(OutputStreamWriter(event.processHandler.processInput!!))
            writer.write(command)
            writer.newLine()
            writer.flush()
        } catch (e: IOException) {
            logger.error("Failed to send command to nuXmv process", e)
            print("Error sending command: ${e.message}\n", ConsoleViewContentType.ERROR_OUTPUT)
        }
    }

    private fun printCommandIfNeeded() {
        val command = currentCommand
        if (!wasCommandPrinted && command != null) {
            print("$command\n", ConsoleViewContentType.USER_INPUT)
            wasCommandPrinted = true
        }
    }

    private fun collectTraces(output: String) {
        if (output.isBlank()) return
        try {
            val traces = if (ShowTracesOutputParser.TRACE_START_REGEX.containsMatchIn(output)) {
                ShowTracesOutputParser.parseOutput(output).onEach { trace ->
                    val stored = tracesByNumber[trace.traceNumber]
                    if (stored == null) tracesByNumber[trace.traceNumber] =
                        trace else stored.states.putAll(trace.states)
                }
            } else {
                TraceBlockOutputParser.parseOutput(output).onEach { tracesByNumber[it.traceNumber] = it }
            }
            if (traces.isNotEmpty()) tracesChanged = true
        } catch (e: Exception) {
            logger.error("Error parsing output of '${currentCommand.orEmpty()}': ${e.message}", e)
        }
    }

    private fun showTracesIfChanged() {
        if (!tracesChanged) return
        tracesChanged = false
        logger.info("Visualizing ${tracesByNumber.size} traces")
        showTraces(tracesByNumber.values.toList())
    }
}
