package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import com.intellij.execution.configurations.RunConfigurationOptions
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvLaunchSettings
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvRunMode

/**
 * Run configuration options for NuXmv
 */
class NuXmvRunConfigurationOptions : RunConfigurationOptions() {
    var modelFilePath by string("")
    var commandLineOptions by string("")
    var runMode by enum(NuXmvRunMode.VERIFICATION)
    var checkSelection by string("")
    var domainOverride by string("")
    var bmcBound by property(NuXmvLaunchSettings.DEFAULT_BMC_BOUND)
    var simulationSteps by property(NuXmvLaunchSettings.DEFAULT_SIMULATION_STEPS)
    var randomSimulation by property(false)
    var interactiveSession by property(false)
}
