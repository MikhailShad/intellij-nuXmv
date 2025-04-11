package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import javax.swing.Icon

class NuXmvConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "nuXmv"

    override fun getConfigurationTypeDescription(): String = "Run nuXmv model checking"

    override fun getIcon(): Icon = AllIcons.RunConfigurations.Application  // Use a default icon

    override fun getId(): String = "NUXMV_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(NuXmvConfigurationFactory(this))
}
