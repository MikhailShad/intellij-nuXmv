package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecEntry
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind

/** Turns a model profile and run configuration settings into a [NuXmvLaunchPlan]. */
object NuXmvLaunchPlanner {
    const val SHOW_TRACES = "show_traces -a -v"
    const val QUIT = "quit"

    fun plan(profile: NuXmvModelProfile, settings: NuXmvLaunchSettings): NuXmvLaunchPlan {
        val domain = settings.domainOverride ?: profile.domain
        val engine = NuXmvEngine.of(domain)
        val problems = mutableListOf<NuXmvPlanProblem>()

        val checks = mutableListOf<NuXmvPlannedCheck>()
        val simulation = mutableListOf<String>()
        when (settings.runMode) {
            NuXmvRunMode.VERIFICATION -> NuXmvSpecKind.entries.forEach { kind ->
                checks += planKind(profile, domain, kind, settings.selection, problems)
            }

            NuXmvRunMode.SIMULATION -> simulation += engine.simulationCommands(
                settings.simulationSteps,
                settings.randomSimulation
            )
        }

        val setup = mutableListOf(engine.setupCommand)
        if (domain == NuXmvDomainType.FINITE_DOMAIN && checks.any { it.algorithm.needsBooleanModel }) {
            setup += NuXmvEngine.BOOLEAN_MODEL_SETUP_COMMAND
        }
        val tail = mutableListOf(SHOW_TRACES)
        if (!settings.interactiveSession) tail += QUIT

        if (checks.isEmpty() && simulation.isEmpty()) {
            problems += NuXmvPlanProblem("Nothing to check: no applicable specification is selected", isError = true)
        }
        return NuXmvLaunchPlan(engine.cliParams, setup, checks, simulation, tail, settings.bmcBound, problems)
    }

    private fun planKind(
        profile: NuXmvModelProfile,
        domain: NuXmvDomainType,
        kind: NuXmvSpecKind,
        selection: NuXmvCheckSelection,
        problems: MutableList<NuXmvPlanProblem>
    ): List<NuXmvPlannedCheck> {
        val kindSelection = selection.kind(kind)
        val specsOfKind = profile.specsOf(kind)
        if (kindSelection.mode == NuXmvKindMode.NONE || specsOfKind.isEmpty()) return emptyList()

        val selected = when (kindSelection.mode) {
            NuXmvKindMode.ALL -> specsOfKind.associateWith { selection.specs[it.key] }
            else -> selectedSpecs(profile, kind, selection, problems)
        }
        if (selected.isEmpty()) return emptyList()

        NuXmvApplicability.kindUnavailableReason(domain, kind)?.let {
            problems += NuXmvPlanProblem("$it, skipped")
            return emptyList()
        }

        val kindAlgorithm = kindSelection.algorithm?.takeIf { it.domain == domain && it.kind == kind }
            ?: NuXmvApplicability.defaultFor(domain, kind, selected.keys)
        if (selected.size == specsOfKind.size && selected.values.all { it == null || it == kindAlgorithm }) {
            return planWholeKind(kind, kindAlgorithm, specsOfKind, problems)
        }

        return selected.mapNotNull { (spec, override) ->
            val algorithm = override?.takeIf { it.domain == domain && it.kind == kind } ?: kindAlgorithm
            val reason = when {
                algorithm == null -> "no algorithm can check it in the ${domain.stringRepresentation.lowercase()}"
                else -> NuXmvApplicability.individualSelectionUnavailableReason(spec)
                    ?: NuXmvApplicability.rejectionOf(algorithm, listOf(spec))
                    ?: if (spec.instances.isEmpty()) "module ${spec.module} is not instantiated from main" else null
            }
            if (reason != null) {
                problems += NuXmvPlanProblem("$kind specification '${spec.label}' skipped: $reason")
                null
            } else {
                NuXmvPlannedCheck.Single(algorithm!!, spec)
            }
        }
    }

    private fun planWholeKind(
        kind: NuXmvSpecKind,
        algorithm: NuXmvCheckAlgorithm?,
        specs: List<NuXmvSpecEntry>,
        problems: MutableList<NuXmvPlanProblem>
    ): List<NuXmvPlannedCheck> {
        val reason = if (algorithm == null) "no algorithm supports all of them" else NuXmvApplicability.rejectionOf(
            algorithm,
            specs
        )
        if (reason != null) {
            problems += NuXmvPlanProblem("$kind specifications skipped: $reason")
            return emptyList()
        }
        return listOf(NuXmvPlannedCheck.AllOfKind(algorithm!!))
    }

    private fun selectedSpecs(
        profile: NuXmvModelProfile,
        kind: NuXmvSpecKind,
        selection: NuXmvCheckSelection,
        problems: MutableList<NuXmvPlanProblem>
    ): Map<NuXmvSpecEntry, NuXmvCheckAlgorithm?> {
        val result = LinkedHashMap<NuXmvSpecEntry, NuXmvCheckAlgorithm?>()
        for ((key, algorithm) in selection.specs) {
            if (!key.startsWith("${kind.name}:")) continue
            val spec = profile.findSpec(key)
            if (spec == null) {
                problems += NuXmvPlanProblem("$kind specification ${key.substringAfter(':')} is no longer in the model, skipped")
            } else {
                result[spec] = algorithm
            }
        }
        return result
    }
}
