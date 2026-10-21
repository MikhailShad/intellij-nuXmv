package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import dev.mikhailshad.nuxmvplugin.ide.EditorUtils
import dev.mikhailshad.nuxmvplugin.ide.run.NuXmvCommandLineState
import dev.mikhailshad.nuxmvplugin.ide.run.NuXmvSettingsEditor
import dev.mikhailshad.nuxmvplugin.ide.run.command.*
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind
import org.jdom.Element
import java.io.File

class NuXmvRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<NuXmvRunConfigurationOptions>(project, factory, name) {
    companion object {
        const val MODEL_FILE_PATH_FIELD = "MODEL_FILE_PATH"
        const val COMMAND_LINE_OPTIONS_FIELD = "COMMAND_LINE_OPTIONS"
        const val RUN_MODE_FIELD = "RUN_MODE"
        const val CHECK_SELECTION_FIELD = "CHECK_SELECTION"
        const val DOMAIN_OVERRIDE_FIELD = "DOMAIN_OVERRIDE"
        const val BMC_BOUND_FIELD = "BMC_BOUND"
        const val SIMULATION_STEPS_FIELD = "SIMULATION_STEPS"
        const val RANDOM_SIMULATION_FIELD = "RANDOM_SIMULATION"
        const val INTERACTIVE_SESSION_FIELD = "INTERACTIVE_SESSION"

        private val LEGACY_CHECK_FIELDS = mapOf(
            NuXmvSpecKind.CTL to "CHECK_CTL_SPECIFICATIONS",
            NuXmvSpecKind.LTL to "CHECK_LTL_SPECIFICATIONS",
            NuXmvSpecKind.INVAR to "CHECK_INVAR_SPECIFICATIONS"
        )
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

    var runMode: NuXmvRunMode
        get() = options.runMode
        set(value) {
            options.runMode = value
        }

    var checkSelection: NuXmvCheckSelection
        get() = NuXmvCheckSelection.deserialize(options.checkSelection) ?: NuXmvCheckSelection.ALL
        set(value) {
            options.checkSelection = value.serialize()
        }

    /** The domain chosen by the user instead of the detected one, `null` for automatic detection. */
    var domainOverride: NuXmvDomainType?
        get() = NuXmvDomainType.entries.firstOrNull { it.name == options.domainOverride }
        set(value) {
            options.domainOverride = value?.name.orEmpty()
        }

    var bmcBound: Int
        get() = options.bmcBound
        set(value) {
            options.bmcBound = value
        }

    var simulationSteps: Int
        get() = options.simulationSteps
        set(value) {
            options.simulationSteps = value
        }

    var randomSimulation: Boolean
        get() = options.randomSimulation
        set(value) {
            options.randomSimulation = value
        }

    var interactiveSession: Boolean
        get() = options.interactiveSession
        set(value) {
            options.interactiveSession = value
        }

    val launchSettings: NuXmvLaunchSettings
        get() = NuXmvLaunchSettings(
            runMode = runMode,
            selection = checkSelection,
            domainOverride = domainOverride,
            bmcBound = bmcBound,
            simulationSteps = simulationSteps,
            randomSimulation = randomSimulation,
            interactiveSession = interactiveSession
        )

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

    /** The profile of the model file as it is now, or `null` if the file is missing or is not a nuXmv model. */
    fun modelProfile(): NuXmvModelProfile? = ApplicationManager.getApplication().runReadAction(Computable {
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(File(modelFilePath))
        val psiFile = virtualFile?.let { PsiManager.getInstance(project).findFile(it) } as? NuXmvFile
        psiFile?.let { NuXmvModelProfile.of(it) }
    })

    fun launchPlan(): NuXmvLaunchPlan? = modelProfile()?.let { NuXmvLaunchPlanner.plan(it, launchSettings) }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, MODEL_FILE_PATH_FIELD, modelFilePath)
        JDOMExternalizerUtil.writeField(element, COMMAND_LINE_OPTIONS_FIELD, commandLineOptions)
        JDOMExternalizerUtil.writeField(element, RUN_MODE_FIELD, runMode.name)
        JDOMExternalizerUtil.writeField(element, CHECK_SELECTION_FIELD, checkSelection.serialize())
        JDOMExternalizerUtil.writeField(element, DOMAIN_OVERRIDE_FIELD, domainOverride?.name.orEmpty())
        JDOMExternalizerUtil.writeField(element, BMC_BOUND_FIELD, bmcBound.toString())
        JDOMExternalizerUtil.writeField(element, SIMULATION_STEPS_FIELD, simulationSteps.toString())
        JDOMExternalizerUtil.writeField(element, RANDOM_SIMULATION_FIELD, randomSimulation.toString())
        JDOMExternalizerUtil.writeField(element, INTERACTIVE_SESSION_FIELD, interactiveSession.toString())
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        modelFilePath = JDOMExternalizerUtil.readField(element, MODEL_FILE_PATH_FIELD) ?: ""
        commandLineOptions = JDOMExternalizerUtil.readField(element, COMMAND_LINE_OPTIONS_FIELD) ?: ""
        runMode =
            NuXmvRunMode.entries.firstOrNull { it.name == JDOMExternalizerUtil.readField(element, RUN_MODE_FIELD) }
                ?: NuXmvRunMode.VERIFICATION
        checkSelection = NuXmvCheckSelection.deserialize(JDOMExternalizerUtil.readField(element, CHECK_SELECTION_FIELD))
            ?: legacySelection(element)
        options.domainOverride = JDOMExternalizerUtil.readField(element, DOMAIN_OVERRIDE_FIELD) ?: ""
        bmcBound = JDOMExternalizerUtil.readField(element, BMC_BOUND_FIELD)?.toIntOrNull()
            ?: NuXmvLaunchSettings.DEFAULT_BMC_BOUND
        simulationSteps = JDOMExternalizerUtil.readField(element, SIMULATION_STEPS_FIELD)?.toIntOrNull()
            ?: NuXmvLaunchSettings.DEFAULT_SIMULATION_STEPS
        randomSimulation = JDOMExternalizerUtil.readField(element, RANDOM_SIMULATION_FIELD)?.toBoolean() ?: false
        interactiveSession = JDOMExternalizerUtil.readField(element, INTERACTIVE_SESSION_FIELD)?.toBoolean() ?: false
    }

    private fun legacySelection(element: Element): NuXmvCheckSelection {
        val flags =
            LEGACY_CHECK_FIELDS.mapValues { (_, field) -> JDOMExternalizerUtil.readField(element, field)?.toBoolean() }
        if (flags.values.all { it == null }) return NuXmvCheckSelection.ALL
        return NuXmvCheckSelection(NuXmvSpecKind.entries.associateWith { kind ->
            NuXmvKindSelection(if (flags[kind] == true) NuXmvKindMode.ALL else NuXmvKindMode.NONE)
        })
    }

    override fun checkConfiguration() {
        if (modelFilePath.isBlank()) {
            throw RuntimeConfigurationError("Model file path is not specified")
        }
        val plan = launchPlan() ?: throw RuntimeConfigurationError("Model file is not a nuXmv model: $modelFilePath")
        plan.problems.firstOrNull { it.isError }?.let { throw RuntimeConfigurationError(it.message) }
        if (plan.problems.isNotEmpty()) {
            throw RuntimeConfigurationWarning(plan.problems.joinToString("\n") { it.message })
        }
    }
}
