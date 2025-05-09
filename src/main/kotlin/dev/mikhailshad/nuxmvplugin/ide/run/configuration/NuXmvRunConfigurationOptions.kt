package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import com.intellij.execution.configurations.RunConfigurationOptions
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType

/**
 * Run configuration options for NuXmv
 */
class NuXmvRunConfigurationOptions : RunConfigurationOptions() {
    var modelFilePath by string("")
    var commandLineOptions by string("")
    var checkCtlSpecifications by property(false)
    var checkLtlSpecifications by property(false)
    var checkInvarSpecifications by property(false)
    var domainType by enum(NuXmvDomainType.FINITE_DOMAIN)
}
