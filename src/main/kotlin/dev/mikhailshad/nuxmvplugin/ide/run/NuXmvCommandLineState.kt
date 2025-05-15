package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.LocalFileSystem
import dev.mikhailshad.nuxmvplugin.ide.configuration.NuXmvSettingsState
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvBddCommands
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvMsatCommands
import dev.mikhailshad.nuxmvplugin.ide.run.configuration.NuXmvRunConfiguration
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import java.io.File

class NuXmvCommandLineState(
    environment: ExecutionEnvironment,
    private val runConfiguration: NuXmvRunConfiguration
) : CommandLineState(environment) {

    companion object {
        private val logger = Logger.getInstance(NuXmvCommandLineState::class.java)
    }

    private val project = environment.project
    private val modelFile = File(runConfiguration.modelFilePath)
    private val modelVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(modelFile)
        ?: throw ExecutionException("Model file does not exist: ${modelFile.absolutePath}")
    private val consoleBuilder = NuXmvSplitConsoleBuilder(project, modelVirtualFile)
    private val console: NuXmvSplitConsole = consoleBuilder.console as NuXmvSplitConsole

    override fun createConsole(executor: Executor): ConsoleView {
        return console
    }

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val nuXmvPath = NuXmvSettingsState.getInstance().state.nuXmvExecutablePath
        if (nuXmvPath.isBlank()) {
            throw ExecutionException("NuXmv executable path is not set. Please configure it in Settings | Tools | nuXmv")
        }

        val nuXmvExecutable = File(nuXmvPath)
        if (!nuXmvExecutable.exists() || !nuXmvExecutable.canExecute()) {
            throw ExecutionException("Invalid nuXmv executable at: $nuXmvPath. Please check the path in Settings | Tools | nuXmv")
        }

        // Expand macros before execution
        val expandedFile = expandMacrosIfNeeded(modelVirtualFile)

        // Visualize the original model (for better user experience)
        console.visualizeModel(modelVirtualFile)

        // Use the expanded file for execution
        val fileToExecute = File(expandedFile.path)

        val cmd = buildCommandLine(nuXmvPath, fileToExecute)
        logger.info("Starting interactive NuXmv: ${cmd.commandLineString}")

        val handler = OSProcessHandler(cmd)
        console.attachToProcess(handler)

        val commands = buildCommandList()
        handler.addProcessListener(NuXmvProcessListener(console, commands))

        handler.startNotify()
        return handler
    }

    private fun expandMacrosIfNeeded(modelFile: com.intellij.openapi.vfs.VirtualFile): com.intellij.openapi.vfs.VirtualFile {
        try {
            // Import dynamically to avoid circular dependencies
            val macroExpansionService =
                dev.mikhailshad.nuxmvplugin.language.utils.MacroExpansionService.getInstance(project)
            return macroExpansionService.expandMacrosInFile(modelFile)
        } catch (e: Exception) {
            logger.error("Failed to expand macros in file: ${modelFile.path}", e)
            return modelFile
        }
    }

    @Throws(ExecutionException::class)
    private fun buildCommandLine(nuXmvPath: String, modelFile: File): GeneralCommandLine {
        val commandLine = GeneralCommandLine(nuXmvPath)
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withParameters("-int")
            .withWorkDirectory(modelFile.parentFile)

        if (runConfiguration.commandLineOptions.isNotBlank()) {
            runConfiguration.commandLineOptions
                .split(" ")
                .filter { it.isNotBlank() }
                .forEach { commandLine.addParameter(it) }
        }

        commandLine.addParameter(modelFile.absolutePath)
        return commandLine
    }

    private fun buildCommandList(): List<String> {
        val runCommands = when (runConfiguration.domainType) {
            NuXmvDomainType.FINITE_DOMAIN -> NuXmvBddCommands
            else -> NuXmvMsatCommands
        }
        val list = mutableListOf<String>()
        list += runCommands.buildCmd
        if (runConfiguration.checkCtlSpecifications) list += runCommands.checkCtlCmd
        if (runConfiguration.checkLtlSpecifications) list += runCommands.checkLtlCmd
        if (runConfiguration.checkInvarSpecifications) list += runCommands.checkInvarCmd
        list += runCommands.showTracesCmd
        list += runCommands.quitCmd
        return list
    }
}
