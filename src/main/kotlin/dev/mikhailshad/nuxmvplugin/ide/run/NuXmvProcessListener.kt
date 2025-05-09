package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Key
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser.ShowTracesOutputParser
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

class NuXmvProcessListener(
    private val console: NuXmvSplitConsole,
    commands: List<String>
) : ProcessListener {
    private val logger = Logger.getInstance(NuXmvProcessListener::class.java)
    private val commandsIt = commands.iterator()
    private var currentCommand = ""
    private var currentOutputBuffer = StringBuilder()
    private var allTraces = mutableListOf<CounterexampleTrace>()
    private var wasCommandPrinted = false
    private var failedSpecifications = false

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
                    if (output.contains(" is false") ||
                        output.contains("is violated") ||
                        output.contains("-- no proof or counterexample found with bound") ||
                        output.contains("-- invariant violated at depth")
                    ) {

                        failedSpecifications = true
                        logger.info("Found failed specifications in command: $currentCommand")
                    }
                }

                val baseCommand = currentCommand.split(" ").firstOrNull() ?: ""

                val traces = if (baseCommand == "show_traces") {
                    val showTracesParser = ShowTracesOutputParser()
                    showTracesParser.parseOutput(output)
                } else {
                    // TODO: Add other parsers for different commands in the future
                    emptyList()
                }

                if (traces.isNotEmpty()) {
                    allTraces.addAll(traces)
                    logger.info("Found ${traces.size} traces in command output for '$currentCommand', total traces: ${allTraces.size}")

                    console.print(
                        "\nParsed ${traces.size} counterexample traces\n",
                        ConsoleViewContentType.SYSTEM_OUTPUT
                    )
                }
            } catch (e: Exception) {
                logger.error("Error parsing command output for '$currentCommand': ${e.message}", e)
            }
        }
    }

    private fun visualizeAllTraces() {
        if (allTraces.isNotEmpty() && console.modelFile != null) {
            logger.info("Visualizing ${allTraces.size} traces for model ${console.modelFile.path}")
            console.visualizeModuleWithTraces(console.modelFile, allTraces)
        } else {
            logger.info("No traces to visualize or model file is null")
        }
    }
}
