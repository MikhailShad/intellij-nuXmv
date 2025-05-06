package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import dev.mikhailshad.nuxmvplugin.ide.EditorUtils

class NuXmvConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "NuXmv"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        val configuration = NuXmvRunConfiguration(project, this, "nuXmv")

        // Try to set a default model file based on current selection in project
        val openedFile = EditorUtils.getActiveNuXmvFilePath(project)
        if (openedFile != null) {
            configuration.modelFilePath = openedFile.path
        }

        return configuration
    }

    override fun getName(): String = "nuXmv Model"

    override fun getOptionsClass(): Class<NuXmvRunConfigurationOptions> = NuXmvRunConfigurationOptions::class.java
}
