package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import org.json.JSONArray
import org.json.JSONObject

data class CounterexampleTrace(
    val specification: String,
    val specType: String,
    val states: List<CounterexampleState>,
    val traceNumber: Int
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("traceNumber", traceNumber)
        json.put("specification", specification)
        json.put("specType", specType)

        val statesJson = JSONArray()
        for (state in states) {
            statesJson.put(state.toJson())
        }
        json.put("states", statesJson)

        return json
    }
}
