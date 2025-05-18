package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import kotlinx.serialization.Serializable

@Serializable
data class CounterexampleTrace(
    val traceNumber: Int,
    val states: MutableMap<String, CounterexampleState> = mutableMapOf()
) {
    lateinit var specification: String
    lateinit var type: String
    lateinit var description: String
}
