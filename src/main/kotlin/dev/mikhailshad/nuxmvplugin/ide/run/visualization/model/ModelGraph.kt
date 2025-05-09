package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import org.json.JSONArray
import org.json.JSONObject

data class ModelGraph(
    val stateVariables: Map<String, StateVariable>,
    val traces: List<CounterexampleTrace> = mutableListOf()
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        val nodes = JSONArray()

        for (variable in stateVariables.values) {
            val name = variable.name
            val nodeJson = JSONObject()
            nodeJson.put("id", name)
            nodeJson.put("label", name)
            nodeJson.put("type", variable.type.name)
            nodeJson.put("isVariable", true)
            nodes.put(nodeJson)

            val values = mutableSetOf<String>()
            for (transition in variable.transitions) {
                values.add(transition.from)
                values.add(transition.to)
            }

            for (value in values) {
                val valueNode = JSONObject()
                valueNode.put("id", "${name}=${value}")
                valueNode.put("label", "$name = $value")
                valueNode.put("isValue", true)
                valueNode.put("variableName", name)
                valueNode.put("variableValue", value)
                nodes.put(valueNode)
            }
        }
        json.put("nodes", nodes)

        val edges = JSONArray()
        for ((name, variable) in stateVariables) {
            for (transition in variable.transitions) {
                val sourceId = "${name}=${transition.from}"
                val targetId = "${name}=${transition.to}"

                val edgeJson = JSONObject()
                edgeJson.put("id", "${sourceId}_to_${targetId}")
                edgeJson.put("source", sourceId)
                edgeJson.put("target", targetId)
                edgeJson.put("condition", transition.condition)
                edges.put(edgeJson)
            }
        }
        json.put("edges", edges)

        val tracesJson = JSONArray()
        for (trace in traces) {
            tracesJson.put(trace.toJson())
        }
        json.put("traces", tracesJson)

        return json

    }
}
