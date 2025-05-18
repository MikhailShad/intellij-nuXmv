package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.openapi.diagnostic.Logger
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace

object InvarCheckCommandOutputParser : BaseCommandOutputParser() {
    private val logger = Logger.getInstance(ShowTracesOutputParser::class.java)

    private val SPECIFICATION_RESULT_REGEX =
        Regex("""--\s*cannot prove the invariant\s+(.+)\s+is true or false\s*:\s*the induction failed""")
    private val TRACE_DESCRIPTION_REGEX = Regex("Trace\\s*[Dd]escription:\\s*(.+)")
    private val TRACE_TYPE_REGEX = Regex("Trace\\s*[Tt]ype:\\s*(.+)")

    override fun parseOutput(output: String): List<CounterexampleTrace> {
        logger.debug("Parsing invar check output, length=${output.length}")
        if (output.isBlank()) {
            return emptyList()
        }

        val traces = mutableListOf<CounterexampleTrace>()

        val specificationMatches = SPECIFICATION_RESULT_REGEX.findAll(output).toList()
        specificationMatches.forEachIndexed { index, matchResult ->
            val start = matchResult.range.first
            val end = if (index < specificationMatches.lastIndex) {
                specificationMatches[index + 1].range.first
            } else {
                output.length
            }
            val violatedSpecification = matchResult.groupValues[1].trim().replace(Regex("""\s+"""), " ")

            val specificationCheckOutput = output.substring(start, end).trim()
            val description = TRACE_DESCRIPTION_REGEX.find(specificationCheckOutput)?.groupValues?.get(1)
                ?: "unknown"
            val type = TRACE_TYPE_REGEX.find(specificationCheckOutput)?.groupValues?.get(1)
                ?: "unknown"

            val trace = CounterexampleTrace(index + 1)
            trace.specification = violatedSpecification
            trace.type = type
            trace.description = description
            val traceStates = parseTraceStates(specificationCheckOutput)
            trace.states.putAll(traceStates.associateBy { it.stateNumber }.toMutableMap())

            traces.add(trace)
        }

        logger.info("Parsed ${traces.size} traces from invar check output")
        return traces
    }
}
