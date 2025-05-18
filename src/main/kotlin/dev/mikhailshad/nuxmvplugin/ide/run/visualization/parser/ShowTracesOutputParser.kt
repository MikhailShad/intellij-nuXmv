package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.openapi.diagnostic.Logger
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace

/**
 * Парсер для вывода команды show_traces в nuXmv.
 * Поддерживает различные форматы вывода, включая HTML-комментарии.
 */
object ShowTracesOutputParser : BaseCommandOutputParser() {
    private val logger = Logger.getInstance(ShowTracesOutputParser::class.java)
    val TRACE_START_REGEX = Regex("""<!--\s*#+\s*Trace\snumber:\s(\d)+\s#+\s*-->""")

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
            val traceStates = parseTraceStates(traceText)
            trace.states.putAll(traceStates.associateBy { it.stateNumber }.toMutableMap())
            traces.add(trace)
        }

        logger.info("Parsed ${traces.size} traces from show_traces output")
        return traces
    }
}
