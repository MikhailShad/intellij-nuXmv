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
        configuration.name = target.name
        return true
    }

    override fun isConfigurationFromContext(
        configuration: NuXmvRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val target = targetOf(context.psiLocation) ?: return false
        return configuration.modelFilePath == target.file.virtualFile.path &&
                configuration.runMode == runMode &&
                configuration.checkSelection == target.selection
    }

    private fun targetOf(element: PsiElement?): Target? {
        val file = element?.containingFile as? NuXmvFile ?: return null
        val name = file.virtualFile?.nameWithoutExtension ?: return null
        val spec = NuXmvModelProfile.of(file).specAt(element)
        if (runMode == NuXmvRunMode.SIMULATION) {
            return if (spec == null) Target(file, NuXmvCheckSelection.ALL, "$name (simulation)") else null
        }
        if (spec == null) return Target(file, NuXmvCheckSelection.ALL, name)
        if (NuXmvApplicability.individualSelectionUnavailableReason(spec) != null) {
            val wholeKind = NuXmvCheckSelection(mapOf(spec.kind to NuXmvKindSelection(NuXmvKindMode.ALL)))
            return Target(file, wholeKind, "$name: ${spec.kind}")
        }
        return Target(
            file,
            NuXmvCheckSelection.single(spec.kind, spec.key),
            "$name: ${spec.name ?: "${spec.kind} at line ${spec.line}"}"
        )
    }

    private data class Target(val file: NuXmvFile, val selection: NuXmvCheckSelection, val name: String)
}
