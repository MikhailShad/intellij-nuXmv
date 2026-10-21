package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecEntry
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecFeature
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind

/** An algorithm offered for a specification or a specification kind; [disabledReason] is set when it cannot be used. */
data class NuXmvAlgorithmOption(val algorithm: NuXmvCheckAlgorithm, val disabledReason: String?) {
    val isAvailable: Boolean
        get() = disabledReason == null
}

/**
 * Decides which checking algorithms apply to a model, see `context/nuxmv-run-modes.md`.
 * The run configuration editor, the configuration check and the launch planner all ask here.
 */
object NuXmvApplicability {
    /** Why nothing of [kind] can be checked in [domain], or `null` when some algorithm exists. */
    fun kindUnavailableReason(domain: NuXmvDomainType, kind: NuXmvSpecKind): String? =
        if (NuXmvCheckAlgorithm.of(domain, kind)
                .isEmpty()
        ) "$kind is not supported for the ${domain.stringRepresentation.lowercase()}" else null

    /** Algorithms for [specs] of [kind]; an algorithm is disabled if it rejects any of [specs]. */
    fun algorithmsFor(
        domain: NuXmvDomainType,
        kind: NuXmvSpecKind,
        specs: Collection<NuXmvSpecEntry>
    ): List<NuXmvAlgorithmOption> = NuXmvCheckAlgorithm.of(domain, kind).map { algorithm ->
        NuXmvAlgorithmOption(algorithm, rejectionOf(algorithm, specs))
    }

    /** The first available algorithm, which also keeps the commands the plugin used before algorithms were selectable. */
    fun defaultFor(
        domain: NuXmvDomainType,
        kind: NuXmvSpecKind,
        specs: Collection<NuXmvSpecEntry>
    ): NuXmvCheckAlgorithm? =
        algorithmsFor(domain, kind, specs).firstOrNull { it.isAvailable }?.algorithm

    /** Why [algorithm] cannot check [specs], or `null` when it can. */
    fun rejectionOf(algorithm: NuXmvCheckAlgorithm, specs: Collection<NuXmvSpecEntry>): String? {
        for (spec in specs) {
            val feature = spec.features.firstOrNull { it in algorithm.unsupported } ?: continue
            return "${algorithm.displayName} does not support ${feature.description} (${spec.label})"
        }
        return null
    }

    /** Why [spec] cannot be selected on its own; such specifications are checked only together with their whole kind. */
    fun individualSelectionUnavailableReason(spec: NuXmvSpecEntry): String? =
        if (NuXmvSpecFeature.MACRO in spec.features) NuXmvSpecFeature.MACRO.description.replaceFirstChar { it.uppercase() } +
                "; select the whole ${spec.kind} group instead" else null
}
