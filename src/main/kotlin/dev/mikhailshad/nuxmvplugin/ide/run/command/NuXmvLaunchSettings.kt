package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

enum class NuXmvRunMode(val displayName: String) {
    VERIFICATION("Verification"),
    SIMULATION("Simulation");

    override fun toString(): String = displayName
}

/** How the specifications of one kind are selected: all of them, the ones listed in [NuXmvCheckSelection.specs], or none. */
enum class NuXmvKindMode { ALL, SOME, NONE }

/** Selection of one kind; [algorithm] is `null` when the default for the domain applies. */
@Serializable
data class NuXmvKindSelection(val mode: NuXmvKindMode, val algorithm: NuXmvCheckAlgorithm? = null)

/**
 * Which specifications to check and how.
 *
 * @property specs specification keys selected individually, mapped to an algorithm that overrides the kind algorithm
 */
@Serializable
data class NuXmvCheckSelection(
    val kinds: Map<NuXmvSpecKind, NuXmvKindSelection> = emptyMap(),
    val specs: Map<String, NuXmvCheckAlgorithm?> = emptyMap()
) {
    fun kind(kind: NuXmvSpecKind): NuXmvKindSelection = kinds[kind] ?: NuXmvKindSelection(NuXmvKindMode.NONE)

    fun withKind(kind: NuXmvSpecKind, selection: NuXmvKindSelection): NuXmvCheckSelection =
        copy(kinds = kinds + (kind to selection))

    fun serialize(): String = JSON.encodeToString(serializer(), this)

    companion object {
        private val JSON = Json { ignoreUnknownKeys = true }

        /** Everything the model has, with default algorithms. */
        val ALL = NuXmvCheckSelection(NuXmvSpecKind.entries.associateWith { NuXmvKindSelection(NuXmvKindMode.ALL) })

        /** Only the specification with [key], checked with the default algorithm. */
        fun single(kind: NuXmvSpecKind, key: String) =
            NuXmvCheckSelection(mapOf(kind to NuXmvKindSelection(NuXmvKindMode.SOME)), mapOf(key to null))

        /** Reads a selection written by [serialize]; `null` for blank or unreadable text. */
        fun deserialize(text: String?): NuXmvCheckSelection? {
            if (text.isNullOrBlank()) return null
            return try {
                JSON.decodeFromString(serializer(), text)
            } catch (_: SerializationException) {
                null
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}

/** Everything the launch planner needs from a run configuration. [domainOverride] replaces the detected domain. */
data class NuXmvLaunchSettings(
    val runMode: NuXmvRunMode = NuXmvRunMode.VERIFICATION,
    val selection: NuXmvCheckSelection = NuXmvCheckSelection.ALL,
    val domainOverride: NuXmvDomainType? = null,
    val bmcBound: Int = DEFAULT_BMC_BOUND,
    val simulationSteps: Int = DEFAULT_SIMULATION_STEPS,
    val randomSimulation: Boolean = false,
    val interactiveSession: Boolean = false
) {
    companion object {
        const val DEFAULT_BMC_BOUND = 10
        const val DEFAULT_SIMULATION_STEPS = 10
    }
}
