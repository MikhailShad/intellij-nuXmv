package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import javax.swing.Icon

class NuXmvConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = NuXmvLanguage.id

    override fun getConfigurationTypeDescription(): String = "Run nuXmv model checking"

    override fun getIcon(): Icon = NuXmvIcons.FILE

    override fun getId(): String = "NUXMV_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(NuXmvConfigurationFactory(this))
}
