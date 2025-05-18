package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import org.json.JSONArray
import org.json.JSONObject

data class CounterexampleTrace(
    val traceNumber: Int,
    val states: MutableMap<String, CounterexampleState> = mutableMapOf()
) {
    lateinit var specification: String
    lateinit var type: String
    lateinit var description: String

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("traceNumber", traceNumber)
        json.put("specification", specification)
        json.put("specType", type)

        val statesJson = JSONArray()
        for (state in states) {
//            statesJson.put(state.toJson())
        }
        json.put("states", statesJson)

        return json
    }
}
