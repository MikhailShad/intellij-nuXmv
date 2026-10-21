package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.openapi.diagnostic.Logger
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace

/**
 * Парсер для вывода команды show_traces в nuXmv.
 * Поддерживает различные форматы вывода, включая HTML-комментарии.
 */
object ShowTracesOutputParser : BaseCommandOutputParser() {
    private val logger = Logger.getInstance(ShowTracesOutputParser::class.java)
    private val TRACE_DESCRIPTION_REGEX = Regex("""Trace\s*[Dd]escription:\s*(.+)""")
    private val TRACE_TYPE_REGEX = Regex("""Trace\s*[Tt]ype:\s*(.+)""")
    val TRACE_START_REGEX = Regex("""<!--\s*#+\s*Trace\snumber:\s(\d+)\s#+\s*-->""")

    override fun parseOutput(output: String): List<CounterexampleTrace> {
        logger.debug("Parsing show_traces output, length=${output.length}")
        if (output.isBlank()) {
            return emptyList()
        }

        val traces = mutableListOf<CounterexampleTrace>()

        val traceMatches = TRACE_START_REGEX.findAll(output).toList()
        traceMatches.forEachIndexed { index, matchResult ->
            val traceNumber = matchResult.groupValues[1].toInt()
            val trace = CounterexampleTrace(traceNumber)

            val start = matchResult.range.first
            val end = if (index < traceMatches.lastIndex) {
                traceMatches[index + 1].range.first
            } else {
                output.length
            }
            val traceText = output.substring(start, end).trim()
            trace.specification = ""
            trace.description = TRACE_DESCRIPTION_REGEX.find(traceText)?.groupValues?.get(1)?.trim() ?: "unknown"
            trace.type = TRACE_TYPE_REGEX.find(traceText)?.groupValues?.get(1)?.trim() ?: "unknown"
            val traceStates = parseTraceStates(traceText)
            trace.states.putAll(traceStates.associateBy { it.stateNumber }.toMutableMap())
            traces.add(trace)
        }

        logger.info("Parsed ${traces.size} traces from show_traces output")
        return traces
    }
}
