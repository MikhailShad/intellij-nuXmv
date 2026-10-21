package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.openapi.diagnostic.Logger
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace

/**
 * Extracts every trace printed by any nuXmv command: counterexamples of checks and simulation traces.
 * A counterexample is bound to the closest preceding verdict line, so the command that produced the output does not matter.
 */
object TraceBlockOutputParser : BaseCommandOutputParser() {
    private val logger = Logger.getInstance(TraceBlockOutputParser::class.java)

    private val TRACE_DESCRIPTION_REGEX = Regex("""Trace\s*[Dd]escription:\s*(.+)""")
    private val TRACE_TYPE_REGEX = Regex("""Trace\s*[Tt]ype:\s*(.+)""")
    private val TRACE_NUMBER_REGEX = Regex("""->\s*State:\s*(\d+)\.\d+\s*<-""")
    private val VERDICT_REGEX = Regex(
        """--\s*(?:(?:LTL\s+|PSL\s+)?specification|invariant)\s+(.+?)\s+is\s+(?:false|unknown)|""" +
                """--\s*cannot prove the invariant\s+(.+?)\s+is true or false"""
    )

    override fun parseOutput(output: String): List<CounterexampleTrace> {
        if (output.isBlank()) return emptyList()

        val descriptions = TRACE_DESCRIPTION_REGEX.findAll(output).toList()
        val traces = descriptions.mapIndexedNotNull { index, description ->
            val start = description.range.first
            val end = descriptions.getOrNull(index + 1)?.range?.first ?: output.length
            val block = output.substring(start, end)
            val traceNumber =
                TRACE_NUMBER_REGEX.find(block)?.groupValues?.get(1)?.toInt() ?: return@mapIndexedNotNull null

            val previousEnd = descriptions.getOrNull(index - 1)?.range?.last ?: 0
            val verdict = VERDICT_REGEX.findAll(output.substring(previousEnd, start)).lastOrNull()

            CounterexampleTrace(traceNumber).apply {
                specification = verdict?.let { (it.groupValues[1].ifEmpty { it.groupValues[2] }).normalizeSpaces() }
                    .orEmpty()
                this.description = description.groupValues[1].trim()
                type = TRACE_TYPE_REGEX.find(block)?.groupValues?.get(1)?.trim() ?: "unknown"
                states.putAll(parseTraceStates(block).associateBy { it.stateNumber })
            }
        }
        logger.debug("Parsed ${traces.size} traces")
        return traces
    }

    private fun String.normalizeSpaces(): String = trim().replace(Regex("""\s+"""), " ")
}
