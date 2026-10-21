package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import dev.mikhailshad.nuxmvplugin.ide.configuration.NuXmvSettingsState
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvCommandSequence
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvLaunchPlan
import dev.mikhailshad.nuxmvplugin.ide.run.configuration.NuXmvRunConfiguration
import dev.mikhailshad.nuxmvplugin.language.utils.MacroExpansionService
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
    private val console: NuXmvSplitConsole by lazy { consoleBuilder.console as NuXmvSplitConsole }

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

        try {
            return startNuXmv(nuXmvPath)
        } catch (e: Throwable) {
            Disposer.dispose(console)
            throw e
        }
    }

    private fun startNuXmv(nuXmvPath: String): ProcessHandler {
        val plan = runConfiguration.launchPlan()
            ?: throw ExecutionException("Model file is not a nuXmv model: ${modelFile.absolutePath}")
        plan.problems.firstOrNull { it.isError }?.let { throw ExecutionException(it.message) }

        val expandedFile = expandMacrosIfNeeded(modelVirtualFile)
        console.visualizeModel(modelVirtualFile)

        val cmd = buildCommandLine(nuXmvPath, File(expandedFile.path), plan)
        logger.info("Starting interactive NuXmv: ${cmd.commandLineString}")

        val handler = OSProcessHandler(cmd)
        console.attachToProcess(handler)
        plan.problems.forEach { console.print("${it.message}\n", ConsoleViewContentType.LOG_WARNING_OUTPUT) }

        val commands = NuXmvCommandSequence(plan) { console.print("$it\n", ConsoleViewContentType.ERROR_OUTPUT) }
        handler.addProcessListener(NuXmvProcessListener(commands, console::print) { traces ->
            console.visualizeModuleWithTraces(modelVirtualFile, traces)
        })

        handler.startNotify()
        return handler
    }

    private fun expandMacrosIfNeeded(modelFile: VirtualFile): VirtualFile {
        try {
            return MacroExpansionService.getInstance(project).expandMacrosInFile(modelFile)
        } catch (e: Exception) {
            logger.error("Failed to expand macros in file: ${modelFile.path}", e)
            return modelFile
        }
    }

    @Throws(ExecutionException::class)
    private fun buildCommandLine(nuXmvPath: String, modelFile: File, plan: NuXmvLaunchPlan): GeneralCommandLine {
        val commandLine = GeneralCommandLine(nuXmvPath)
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withParameters("-int")
            .withParameters(plan.cliParams)
            .withWorkDirectory(modelFile.parentFile)

        if (runConfiguration.commandLineOptions.isNotBlank()) {
            runConfiguration.commandLineOptions
                .split(" ")
                .filter { it.isNotBlank() && it !in plan.cliParams }
                .forEach { commandLine.addParameter(it) }
        }

        commandLine.addParameter(modelFile.absolutePath)
        return commandLine
    }
}
