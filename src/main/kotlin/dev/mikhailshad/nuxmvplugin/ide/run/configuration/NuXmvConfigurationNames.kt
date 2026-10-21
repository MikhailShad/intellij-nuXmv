package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import dev.mikhailshad.nuxmvplugin.ide.run.command.*
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind

/** Human-readable descriptions of what a run configuration checks, used for its generated name and gutter actions. */
object NuXmvConfigurationNames {
    const val ALL_SPECIFICATIONS = "all specifications"
    const val SIMULATION = "simulation"

    /**
     * `LTL s2` or `LTL at line 17` for one specification, `all LTL` for one whole kind,
     * `all specifications` when everything the model has is selected.
     */
    fun describe(profile: NuXmvModelProfile?, settings: NuXmvLaunchSettings): String {
        if (settings.runMode == NuXmvRunMode.SIMULATION) return SIMULATION
        if (profile == null) return ALL_SPECIFICATIONS

        val selection = settings.selection
        val presentKinds = NuXmvSpecKind.entries.filter { profile.specsOf(it).isNotEmpty() }
        val selectedKinds = presentKinds.filter { selection.kind(it).mode != NuXmvKindMode.NONE }
        if (selectedKinds.isEmpty()) return "nothing"
        if (selectedKinds.size == presentKinds.size && selectedKinds.all { selection.kind(it).mode == NuXmvKindMode.ALL }) {
            return ALL_SPECIFICATIONS
        }

        val kind = selectedKinds.singleOrNull() ?: return "selected " + selectedKinds.joinToString()
        if (selection.kind(kind).mode == NuXmvKindMode.ALL) return "all $kind"

        val keys = selection.specs.keys.filter { it.startsWith("${kind.name}:") }
        val spec = keys.singleOrNull()?.let { profile.findSpec(it) } ?: return "selected $kind"
        return if (spec.name != null) "$kind ${spec.name}" else "$kind at line ${spec.line}"
    }

    /** Whether two settings run the same checks on [profile], however their selections are written down. */
    fun sameChecks(profile: NuXmvModelProfile, first: NuXmvLaunchSettings, second: NuXmvLaunchSettings): Boolean {
        if (first.runMode != second.runMode) return false
        if (first.runMode == NuXmvRunMode.SIMULATION) return true
        return plannedChecks(profile, first) == plannedChecks(profile, second)
    }

    private fun plannedChecks(
        profile: NuXmvModelProfile,
        settings: NuXmvLaunchSettings
    ): Set<Pair<NuXmvCheckAlgorithm, String>> =
        NuXmvLaunchPlanner.plan(profile, settings).checks.flatMap { check ->
            when (check) {
                is NuXmvPlannedCheck.AllOfKind -> profile.specsOf(check.algorithm.kind)
                    .map { check.algorithm to it.key }

                is NuXmvPlannedCheck.Single -> listOf(check.algorithm to check.spec.key)
            }
        }.toSet()
}
