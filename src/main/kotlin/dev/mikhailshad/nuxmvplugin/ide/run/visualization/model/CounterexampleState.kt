package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import org.json.JSONObject

data class CounterexampleState(
    val stateNumber: String,
    val isLoopStart: Boolean = false,
    val variables: MutableMap<String, String> = mutableMapOf()
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("stateNumber", stateNumber)
        json.put("isLoopState", isLoopStart)

        val variablesJson = JSONObject()
        for ((varName, value) in variables) {
            variablesJson.put(varName, value)
        }
        json.put("variables", variablesJson)

        return json
    }
}
