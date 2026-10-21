package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecEntry

/**
 * A row of the nuXmv property database as printed by `show_property`.
 *
 * @property context the module instance after `IN`, `null` for `main`
 */
data class NuXmvProperty(val index: Int, val type: String, val formula: String, val context: String?, val name: String?)

/** Maps specifications written in the model to properties in the nuXmv database. */
object NuXmvPropertyResolver {
    private val HEADER_REGEX = Regex("""^\s*(\d+)\s*:(.*)$""")
    private val STATUS_REGEX = Regex("""^\s*\[\s*(\S+)\s+\S+\s+\S+\s+(\S+)\s*]\s*$""")
    private const val CONTEXT_SEPARATOR = " IN "
    private const val NO_NAME = "N/A"

    fun parsePropertyList(output: String): List<NuXmvProperty> {
        val properties = mutableListOf<NuXmvProperty>()
        val lines = output.lines()
        for ((i, line) in lines.withIndex()) {
            val header = HEADER_REGEX.find(line) ?: continue
            val status = lines.getOrNull(i + 1)?.let { STATUS_REGEX.find(it) } ?: continue
            val text = header.groupValues[2].trim()
            val contextAt = text.lastIndexOf(CONTEXT_SEPARATOR)
            val formula = if (contextAt >= 0) text.substring(0, contextAt).trim() else text
            val context = if (contextAt >= 0) text.substring(contextAt + CONTEXT_SEPARATOR.length).trim() else null
            val name = status.groupValues[2].takeIf { it != NO_NAME }
            properties += NuXmvProperty(header.groupValues[1].toInt(), status.groupValues[1], formula, context, name)
        }
        return properties
    }

    /** `-P` selectors for every instance of a named specification, `null` for an unnamed one. */
    fun namedSelectors(spec: NuXmvSpecEntry): List<String>? {
        val name = spec.name ?: return null
        return spec.instances.map { "-P ${qualify(it, name)}" }
    }

    /** `-n` selectors of the properties [spec] became in [properties]; empty when nuXmv has no such property. */
    fun indexSelectors(spec: NuXmvSpecEntry, properties: List<NuXmvProperty>): List<String> {
        val formula = NuXmvSpecEntry.normalizeFormula(spec.text)
        return spec.instances.flatMap { instance ->
            val context = contextOf(instance, spec.context)
            properties.filter {
                it.type == spec.kind.propertyType &&
                        it.context == context &&
                        NuXmvSpecEntry.normalizeFormula(it.formula) == formula
            }.map { "-n ${it.index}" }
        }
    }

    private fun qualify(instance: String, name: String): String = if (instance.isEmpty()) name else "$instance.$name"

    private fun contextOf(instance: String, explicit: String?): String? = when {
        instance.isEmpty() -> explicit
        explicit == null -> instance
        else -> "$instance.$explicit"
    }
}
