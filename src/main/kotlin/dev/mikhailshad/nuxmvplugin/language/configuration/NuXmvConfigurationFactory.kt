package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class NuXmvConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "NuXmv"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return NuXmvRunConfiguration(project, this, "nuXmv")
    }

    override fun getName(): String = "nuXmv Model"

    override fun getOptionsClass(): Class<NuXmvRunConfigurationOptions> = NuXmvRunConfigurationOptions::class.java
}
