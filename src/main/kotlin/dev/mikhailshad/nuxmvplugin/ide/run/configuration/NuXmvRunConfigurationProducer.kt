package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

class NuXmvRunConfigurationProducer : LazyRunConfigurationProducer<NuXmvRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return NuXmvConfigurationType().configurationFactories[0]
    }

    override fun setupConfigurationFromContext(
        configuration: NuXmvRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val psiFile = sourceElement.get()?.containingFile ?: return false

        // Check if the file is a nuXmv file
        if (psiFile.fileType.name != NuXmvFileType.name) {
            return false
        }

        val virtualFile = psiFile.virtualFile ?: return false

        // Set configuration name and model file path
        configuration.name = virtualFile.nameWithoutExtension
        configuration.modelFilePath = virtualFile.path

        return true
    }

    override fun isConfigurationFromContext(
        configuration: NuXmvRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val psiFile = context.psiLocation?.containingFile ?: return false

        if (psiFile.fileType.name != NuXmvFileType.name) {
            return false
        }

        val virtualFile = psiFile.virtualFile ?: return false

        return configuration.modelFilePath == virtualFile.path
    }
}
