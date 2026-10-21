package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecEntry

/** A check the launch will run: either every property of a kind, or the properties of one specification. */
sealed interface NuXmvPlannedCheck {
    val algorithm: NuXmvCheckAlgorithm

    data class AllOfKind(override val algorithm: NuXmvCheckAlgorithm) : NuXmvPlannedCheck

    data class Single(override val algorithm: NuXmvCheckAlgorithm, val spec: NuXmvSpecEntry) : NuXmvPlannedCheck
}

/** Something the planner had to leave out or could not decide; an error means there is nothing sensible to run. */
data class NuXmvPlanProblem(val message: String, val isError: Boolean = false)

/**
 * The nuXmv invocation for one run: command line parameters and the commands sent to the interactive shell.
 * Checks of unnamed specifications are turned into commands only after nuXmv lists its properties, see [NuXmvCommandSequence].
 */
data class NuXmvLaunchPlan(
    val cliParams: List<String>,
    val setup: List<String>,
    val checks: List<NuXmvPlannedCheck>,
    val simulation: List<String>,
    val tail: List<String>,
    val bmcBound: Int,
    val problems: List<NuXmvPlanProblem>
) {
    val needsPropertyList: Boolean
        get() = checks.any { it is NuXmvPlannedCheck.Single && it.spec.name == null }

    val hasWork: Boolean
        get() = checks.isNotEmpty() || simulation.isNotEmpty()

    /** The commands as they will be sent; indices of unnamed specifications are shown as placeholders. */
    fun preview(): List<String> {
        val checkCommands = checks.flatMap { check ->
            when (check) {
                is NuXmvPlannedCheck.AllOfKind -> listOf(check.algorithm.commandLine(bmcBound))
                is NuXmvPlannedCheck.Single -> NuXmvPropertyResolver.namedSelectors(check.spec)
                    ?.map { check.algorithm.commandLine(bmcBound, it) }
                    ?: listOf(check.algorithm.commandLine(bmcBound, "-n <index of \"${check.spec.text}\">"))
            }
        }
        val listing = if (needsPropertyList) listOf(NuXmvCommandSequence.SHOW_PROPERTY) else emptyList()
        return setup + listing + checkCommands + simulation + tail
    }
}
