package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import kotlinx.serialization.Serializable

@Serializable
data class CounterexampleState(
    val stateNumber: String,
    val isLoopStart: Boolean = false,
    val variables: MutableMap<String, String> = mutableMapOf()
)
