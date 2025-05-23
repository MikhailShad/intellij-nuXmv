package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleState

abstract class BaseCommandOutputParser : CommandOutputParser {
    companion object {
        protected val STATE_REGEX =
            Regex("""(?m)(?:^\s*-- Loop starts here\s*\n)?\s*->\s*State:\s*(\d+\.\d+)\s*<-\s*\n((?:\s+\w+\s*=\s*.*\n?)*)""")
    }

    fun parseTraceStates(traceText: String): List<CounterexampleState> {
        val states = STATE_REGEX.findAll(traceText).map { match ->
            val isLoopStart = match.value.contains("Loop starts here")
            val stateNumber = match.groupValues[1]
            val variablesBlock = match.groupValues[2]

            val variables = variablesBlock.lines()
                .filter { it.contains("=") }
                .associate {
                    val (key, value) = it.trim().split("=").map(String::trim)
                    key to value
                }.toMutableMap()

            CounterexampleState(stateNumber, isLoopStart, variables)
        }.toList()

        states.forEachIndexed { index, state ->
            if (index != 0) {
                val previousState = states[index - 1]
                for ((previousVariable, previousVariableValue) in previousState.variables) {
                    if (state.variables[previousVariable] == null) {
                        state.variables[previousVariable] = previousVariableValue
                    }
                }
            }
        }

        return states
    }
}
