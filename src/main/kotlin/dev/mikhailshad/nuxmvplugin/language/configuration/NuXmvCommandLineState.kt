package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger
import java.io.File

class NuXmvCommandLineState(
    environment: ExecutionEnvironment,
    private val runConfiguration: NuXmvRunConfiguration
) : CommandLineState(environment) {

    companion object {
        private val LOG = Logger.getInstance(NuXmvCommandLineState::class.java)
    }

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val nuXmvPath = NuXmvSettingsState.getInstance().state.nuXmvExecutablePath
        if (nuXmvPath.isBlank()) {
            throw ExecutionException("NuXmv executable path is not set. Please configure it in Settings | Build, Execution, Deployment | nuXmv")
        }

        val nuXmvExecutable = File(nuXmvPath)
        if (!nuXmvExecutable.exists() || !nuXmvExecutable.canExecute()) {
            throw ExecutionException("Invalid nuXmv executable at: $nuXmvPath. Please check the path in Settings | Build, Execution, Deployment | nuXmv")
        }

        val modelFile = File(runConfiguration.modelFilePath)
        if (!modelFile.exists()) {
            throw ExecutionException("Model file does not exist: ${runConfiguration.modelFilePath}")
        }

        val commandLine = GeneralCommandLine()
        commandLine.exePath = nuXmvPath

        if (runConfiguration.commandLineOptions.isNotBlank()) {
            runConfiguration.commandLineOptions.split(" ").forEach { option ->
                if (option.isNotBlank()) {
                    commandLine.addParameter(option)
                }
            }
        }

        commandLine.addParameter(modelFile.absolutePath)

        commandLine.workDirectory = modelFile.parentFile

        LOG.info("Executing: ${commandLine.commandLineString}")

        val processHandler = ColoredProcessHandler(commandLine)
        ProcessTerminatedListener.attach(processHandler)
        return processHandler
    }
}
