package dev.mikhailshad.nuxmvplugin.ide.configuration

import com.intellij.execution.configurations.RunConfigurationOptions

class NuXmvRunConfigurationOptions : RunConfigurationOptions() {
    var modelFilePath by string("")
    var commandLineOptions by string("-int")
    var checkCtlSpecifications by property(false)
    var checkLtlSpecifications by property(false)
    var checkInvarSpecifications by property(false)
}

