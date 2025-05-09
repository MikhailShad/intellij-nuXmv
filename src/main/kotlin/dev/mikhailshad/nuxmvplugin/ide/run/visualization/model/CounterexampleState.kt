package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import org.json.JSONObject

data class CounterexampleState(
    val stateNumber: Double,
    val isLoopState: Boolean = false,
    val variables: Map<String, String> = emptyMap()
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("stateNumber", stateNumber)
        json.put("isLoopState", isLoopState)

        val variablesJson = JSONObject()
        for ((varName, value) in variables) {
            variablesJson.put(varName, value)
        }
        json.put("variables", variablesJson)

        return json
    }
}
