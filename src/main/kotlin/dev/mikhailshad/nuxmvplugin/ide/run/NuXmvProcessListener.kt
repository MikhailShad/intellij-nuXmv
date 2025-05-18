package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Key
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser.InvarCheckCommandOutputParser
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser.ShowTracesOutputParser
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser.TimedLogicCheckCommandOutputParser
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

class NuXmvProcessListener(
    private val console: NuXmvSplitConsole,
    commands: List<String>
) : ProcessListener {
    companion object {
        private val logger = Logger.getInstance(NuXmvProcessListener::class.java)
    }

    private val commandsIt = commands.iterator()
    private var currentCommand = ""
    private var currentOutputBuffer = StringBuilder()
    private var tracesByNumber = mutableMapOf<Int, CounterexampleTrace>()
    private var wasCommandPrinted = false

    override fun startNotified(event: ProcessEvent) {}

    override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {}

    override fun processTerminated(event: ProcessEvent) {
        printCommandIfNeeded()
        parseCurrentCommandOutput()
        visualizeAllTraces()
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        val line = event.text
        currentOutputBuffer.append(line)
        printCommandIfNeeded()

        if (line.trim().endsWith("nuXmv >")) {
            parseCurrentCommandOutput()

            if (commandsIt.hasNext()) {
                val nextCmd = commandsIt.next()
                currentCommand = nextCmd
                wasCommandPrinted = false

                try {
                    val writer = BufferedWriter(OutputStreamWriter(event.processHandler.processInput!!))
                    writer.write(nextCmd)
                    writer.newLine()
                    writer.flush()
                } catch (e: IOException) {
                    logger.error("Failed to send command to nuXmv process", e)
                    console.print("Error sending command: ${e.message}\n", ConsoleViewContentType.ERROR_OUTPUT)
                }
            } else {
                visualizeAllTraces()
            }
        }
    }

    private fun printCommandIfNeeded() {
        if (!wasCommandPrinted && currentCommand.isNotEmpty()) {
            console.print("$currentCommand\n", ConsoleViewContentType.USER_INPUT)
            wasCommandPrinted = true
        }
    }

    private fun parseCurrentCommandOutput() {
        if (currentCommand.isNotBlank()) {
            val output = currentOutputBuffer.toString()
            currentOutputBuffer.clear()

            try {
                if (currentCommand.contains("check_")) {
                    if (output.contains("is false") ||
                        output.contains("is violated") ||
                        output.contains("-- no proof or counterexample found with bound") ||
                        output.contains("-- invariant violated at depth")
                    ) {
                        logger.info("Found failed specifications in command: $currentCommand")
                    }
                }

                when {
                    currentCommand.contains("invar") -> {
                        InvarCheckCommandOutputParser.parseOutput(output).forEach { trace ->
                            // Add trace data to the map
                            tracesByNumber[trace.traceNumber] = trace
                        }
                    }

                    currentCommand.contains("ctl") || currentCommand.contains("ltl") -> {
                        // Add trace data to the map
                        TimedLogicCheckCommandOutputParser.parseOutput(output).forEach { trace ->
                            tracesByNumber[trace.traceNumber] = trace
                        }
                    }

                    currentCommand.contains("show_traces") -> {
                        // Merge full traces with already stored ones
                        val allTraces = ShowTracesOutputParser.parseOutput(output)
                        for (trace in allTraces) {
                            val storedTrace = tracesByNumber[trace.traceNumber]
                            storedTrace?.states?.putAll(trace.states)
                        }
                    }

                    else -> {
                        // TODO: Add other parsers for different commands in the future
                    }
                }
            } catch (e: Exception) {
                logger.error("Error parsing command output for '$currentCommand': ${e.message}", e)
            }
        }
    }

    private fun visualizeAllTraces() {
        if (tracesByNumber.isNotEmpty() && console.modelFile != null) {
            logger.info("Visualizing ${tracesByNumber.size} traces for model ${console.modelFile.path}")
            console.visualizeModuleWithTraces(console.modelFile, tracesByNumber.values.toList())
        } else {
            logger.info("No traces to visualize or model file is null")
        }
    }
}
