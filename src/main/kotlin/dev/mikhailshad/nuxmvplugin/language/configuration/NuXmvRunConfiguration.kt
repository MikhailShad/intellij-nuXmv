package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import dev.mikhailshad.nuxmvplugin.language.utils.EditorUtils
import org.jdom.Element

class NuXmvRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<NuXmvRunConfigurationOptions>(project, factory, name) {
    companion object {
        const val MODEL_FILE_PATH_FIELD = "MODEL_FILE_PATH"
        const val COMMAND_LINE_OPTIONS_FIELD = "COMMAND_LINE_OPTIONS"
    }

    var modelFilePath: String
        get() = options.modelFilePath ?: project.projectFile?.path ?: ""
        set(value) {
            options.modelFilePath = value
        }

    var commandLineOptions: String
        get() = options.commandLineOptions ?: ""
        set(value) {
            options.commandLineOptions = value
        }

    // Set default model file path for new configurations
    init {
        if (options.modelFilePath?.isEmpty() == true) {
            val openedFile = EditorUtils.getActiveNuXmvFilePath(project)
            if (openedFile != null) {
                modelFilePath = openedFile.path
            }
        }
    }

    override fun getOptions(): NuXmvRunConfigurationOptions {
        return super.getOptions() as NuXmvRunConfigurationOptions
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return NuXmvSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return NuXmvCommandLineState(environment, this)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, MODEL_FILE_PATH_FIELD, modelFilePath)
        JDOMExternalizerUtil.writeField(element, COMMAND_LINE_OPTIONS_FIELD, commandLineOptions)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        modelFilePath = JDOMExternalizerUtil.readField(element, MODEL_FILE_PATH_FIELD) ?: ""
        commandLineOptions = JDOMExternalizerUtil.readField(element, COMMAND_LINE_OPTIONS_FIELD) ?: ""
    }

    override fun checkConfiguration() {
        if (modelFilePath.isBlank()) {
            throw RuntimeConfigurationError("Model file path is not specified")
        }
    }
}

