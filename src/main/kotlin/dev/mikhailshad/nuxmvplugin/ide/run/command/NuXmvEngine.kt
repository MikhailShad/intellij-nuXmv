package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType

/**
 * How nuXmv builds and simulates a model of a domain.
 *
 * @property cliParams command line parameters the domain requires
 * @property supportsRandomSimulation `pick_state`/`simulate` accept `-r`
 */
enum class NuXmvEngine(
    val domain: NuXmvDomainType,
    val setupCommand: String,
    val cliParams: List<String>,
    private val pickStateCommand: String,
    private val simulateCommand: String,
    val supportsRandomSimulation: Boolean
) {
    FINITE(NuXmvDomainType.FINITE_DOMAIN, "go", emptyList(), "pick_state", "simulate", true),
    INFINITE(NuXmvDomainType.INFINITE_DOMAIN, "go_msat", emptyList(), "msat_pick_state", "msat_simulate", false),
    TIMED(NuXmvDomainType.TIMED_DOMAIN, "go_time", listOf("-time"), "timed_pick_state", "timed_simulate", false);

    fun simulationCommands(steps: Int, random: Boolean): List<String> {
        val randomFlag = if (random && supportsRandomSimulation) " -r" else ""
        return listOf("$pickStateCommand$randomFlag", "$simulateCommand$randomFlag -k $steps")
    }

    companion object {
        const val BOOLEAN_MODEL_SETUP_COMMAND = "go_bmc"

        fun of(domain: NuXmvDomainType): NuXmvEngine = entries.first { it.domain == domain }
    }
}
