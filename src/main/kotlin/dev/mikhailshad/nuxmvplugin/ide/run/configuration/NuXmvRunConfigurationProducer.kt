package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvApplicability
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvCheckSelection
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvKindMode
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvKindSelection
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvRunMode
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile

/**
 * Creates a verification configuration from the editor context: one specification when the caret is inside it,
 * every specification of the model otherwise.
 */
class NuXmvRunConfigurationProducer : NuXmvConfigurationProducerBase(NuXmvRunMode.VERIFICATION)

/** Creates a simulation configuration for the model file under the caret. */
class NuXmvSimulationConfigurationProducer : NuXmvConfigurationProducerBase(NuXmvRunMode.SIMULATION)

abstract class NuXmvConfigurationProducerBase(private val runMode: NuXmvRunMode) :
    LazyRunConfigurationProducer<NuXmvRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory = NuXmvConfigurationType().configurationFactories[0]

    override fun setupConfigurationFromContext(
        configuration: NuXmvRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val target = targetOf(sourceElement.get()) ?: return false
        configuration.modelFilePath = target.file.virtualFile.path
        configuration.runMode = runMode
        configuration.checkSelection = target.selection
        configuration.setGeneratedName()
        return true
    }

    override fun isConfigurationFromContext(
        configuration: NuXmvRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val target = targetOf(context.psiLocation) ?: return false
        if (configuration.modelFilePath != target.file.virtualFile.path) return false
        val targetSettings = configuration.launchSettings.copy(runMode = runMode, selection = target.selection)
        return NuXmvConfigurationNames.sameChecks(target.profile, configuration.launchSettings, targetSettings)
    }

    private fun targetOf(element: PsiElement?): Target? {
        val file = element?.containingFile as? NuXmvFile ?: return null
        if (file.virtualFile == null) return null
        val profile = NuXmvModelProfile.of(file)
        val spec = profile.specAt(element)
        if (runMode == NuXmvRunMode.SIMULATION) {
            return if (spec == null) Target(file, profile, NuXmvCheckSelection.ALL) else null
        }
        if (spec == null) return Target(file, profile, NuXmvCheckSelection.ALL)
        if (NuXmvApplicability.individualSelectionUnavailableReason(spec) != null) {
            return Target(file, profile, NuXmvCheckSelection(mapOf(spec.kind to NuXmvKindSelection(NuXmvKindMode.ALL))))
        }
        return Target(file, profile, NuXmvCheckSelection.single(spec.kind, spec.key))
    }

    private data class Target(val file: NuXmvFile, val profile: NuXmvModelProfile, val selection: NuXmvCheckSelection)
}
