package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger
import dev.mikhailshad.nuxmvplugin.ide.configuration.NuXmvSettingsState
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvBddCommands
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvMsatCommands
import dev.mikhailshad.nuxmvplugin.ide.run.configuration.NuXmvRunConfiguration
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import java.io.File
import java.util.*

class NuXmvCommandLineState(
    environment: ExecutionEnvironment,
    private val runConfiguration: NuXmvRunConfiguration
) : CommandLineState(environment) {

    companion object {
        private val logger = Logger.getInstance(NuXmvCommandLineState::class.java)
    }

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val nuXmvPath = NuXmvSettingsState.Companion.getInstance().state.nuXmvExecutablePath
        if (nuXmvPath.isBlank()) {
            throw ExecutionException("NuXmv executable path is not set. Please configure it in Settings | Tools | nuXmv")
        }

        val nuXmvExecutable = File(nuXmvPath)
        if (!nuXmvExecutable.exists() || !nuXmvExecutable.canExecute()) {
            throw ExecutionException("Invalid nuXmv executable at: $nuXmvPath. Please check the path in Settings | Tools | nuXmv")
        }

        val modelFile = File(runConfiguration.modelFilePath)
        if (!modelFile.exists()) {
            throw ExecutionException("Model file does not exist: ${runConfiguration.modelFilePath}")
        }

        val commandLine = buildCommandLine(nuXmvPath, modelFile)
        logger.info("Executing: ${commandLine.commandLineString}")

        val processHandler = ColoredProcessHandler(commandLine)
        ProcessTerminatedListener.attach(processHandler)
        return processHandler
    }

    private fun buildCommandLine(nuXmvPath: String, modelFile: File): GeneralCommandLine {
        val commandLine = GeneralCommandLine()
        commandLine.exePath = nuXmvPath

        //commandLine.addParameter("-int")

        if (runConfiguration.commandLineOptions.isNotBlank()) {
            runConfiguration.commandLineOptions.split(" ").forEach { option ->
                if (option.isNotBlank()) {
                    commandLine.addParameter(option)
                }
            }
        }

        val scriptFile = createCommandScript()
        if (scriptFile != null) {
            commandLine.addParameter("-source")
            commandLine.addParameter(scriptFile.absolutePath)
        }

        commandLine.addParameter(modelFile.absolutePath)
        commandLine.workDirectory = modelFile.parentFile
        return commandLine
    }

    private fun createCommandScript(): File? {
        try {
            val tempFile = File.createTempFile("nuxmv_script_${UUID.randomUUID()}", ".cmd")
            tempFile.deleteOnExit()

            tempFile.writer().use { writer ->
                val runCommands = when (runConfiguration.domainType) {
                    NuXmvDomainType.FINITE_DOMAIN -> NuXmvBddCommands
                    else -> NuXmvMsatCommands
                }
                writer.write("${runCommands.buildCmd}\n")

                if (runConfiguration.checkCtlSpecifications) {
                    writer.write("${runCommands.checkCtlCmd}\n")
                }

                if (runConfiguration.checkLtlSpecifications) {
                    writer.write("${runCommands.checkLtlCmd}\n")
                }

                if (runConfiguration.checkInvarSpecifications) {
                    writer.write("${runCommands.checkInvarCmd}\n")
                }

                writer.write("${runCommands.quitCmd}\n")
            }

            return tempFile
        } catch (e: Exception) {
            logger.error("Error creating command script", e)
            return null
        }
    }
}
