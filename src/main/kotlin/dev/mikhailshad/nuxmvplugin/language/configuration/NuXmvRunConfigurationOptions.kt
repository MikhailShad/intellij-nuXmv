package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.execution.configurations.RunConfigurationOptions

class NuXmvRunConfigurationOptions : RunConfigurationOptions() {
    var modelFilePath by string("")
    var commandLineOptions by string("-int")
}

