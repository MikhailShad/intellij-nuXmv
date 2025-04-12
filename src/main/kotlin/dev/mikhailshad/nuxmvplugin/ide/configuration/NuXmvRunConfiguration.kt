package dev.mikhailshad.nuxmvplugin.ide.configuration

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
        const val CHECK_CTL_SPECIFICATIONS_FIELD = "CHECK_CTL_SPECIFICATIONS"
        const val CHECK_LTL_SPECIFICATIONS_FIELD = "CHECK_LTL_SPECIFICATIONS"
        const val CHECK_INVAR_SPECIFICATIONS_FIELD = "CHECK_INVAR_SPECIFICATIONS"
    }

    var modelFilePath: String
        get() = options.modelFilePath ?: ""
        set(value) {
            options.modelFilePath = value
        }

    var commandLineOptions: String
        get() = options.commandLineOptions ?: ""
        set(value) {
            options.commandLineOptions = value
        }

    var checkCtlSpecifications: Boolean
        get() = options.checkCtlSpecifications
        set(value) {
            options.checkCtlSpecifications = value
        }

    var checkLtlSpecifications: Boolean
        get() = options.checkLtlSpecifications
        set(value) {
            options.checkLtlSpecifications = value
        }

    var checkInvarSpecifications: Boolean
        get() = options.checkInvarSpecifications
        set(value) {
            options.checkInvarSpecifications = value
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
        JDOMExternalizerUtil.writeField(element, CHECK_CTL_SPECIFICATIONS_FIELD, checkCtlSpecifications.toString())
        JDOMExternalizerUtil.writeField(element, CHECK_LTL_SPECIFICATIONS_FIELD, checkLtlSpecifications.toString())
        JDOMExternalizerUtil.writeField(element, CHECK_INVAR_SPECIFICATIONS_FIELD, checkInvarSpecifications.toString())
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        modelFilePath = JDOMExternalizerUtil.readField(element, MODEL_FILE_PATH_FIELD) ?: ""
        commandLineOptions = JDOMExternalizerUtil.readField(element, COMMAND_LINE_OPTIONS_FIELD) ?: ""
        checkCtlSpecifications =
            JDOMExternalizerUtil.readField(element, CHECK_CTL_SPECIFICATIONS_FIELD)?.toBoolean() ?: false
        checkLtlSpecifications =
            JDOMExternalizerUtil.readField(element, CHECK_LTL_SPECIFICATIONS_FIELD)?.toBoolean() ?: false
        checkInvarSpecifications =
            JDOMExternalizerUtil.readField(element, CHECK_INVAR_SPECIFICATIONS_FIELD)?.toBoolean() ?: false
    }

    override fun checkConfiguration() {
        if (modelFilePath.isBlank()) {
            throw RuntimeConfigurationError("Model file path is not specified")
        }
    }
}

