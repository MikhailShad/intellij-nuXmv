package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.diagnostic.Logger
import org.json.JSONArray
import org.json.JSONObject

/**
 * Generates graph representation of state variables and transitions for visualization.
 */
class ModelGraphGenerator {
    private val logger = Logger.getInstance(ModelGraphGenerator::class.java)

    /**
     * Generate a state graph for visualization based on the model's state variables
     * @param stateVariables List of state variables with transitions
     * @return JSON string representation of the graph for Cytoscape.js or empty array if no visualizable data
     */
    fun generateStateGraph(stateVariables: List<ModelStateAnalyzer.StateVariable>): String {
        if (stateVariables.isEmpty()) {
            logger.info("No state variables found")
            return "[]"
        }

        // Choose the state variable with the most transitions for the best visualization
        val stateVar = stateVariables.maxByOrNull { it.transitions.size } ?: stateVariables.first()
        logger.info("Using state variable ${stateVar.name} with ${stateVar.transitions.size} transitions")

        return generateSimpleGraph(stateVar)
    }

    /**
     * Generate a graph for a single state variable
     */
    private fun generateSimpleGraph(stateVar: ModelStateAnalyzer.StateVariable): String {
        val jsonArray = JSONArray()
        val statesMap = mutableMapOf<String, String>() // Maps state value to node ID

        try {
            if (stateVar.transitions.isEmpty()) {
                logger.info("No transitions found for ${stateVar.name}")
                return "[]"
            }

            logger.info("Generating graph for variable ${stateVar.name} with ${stateVar.transitions.size} transitions")

            stateVar.transitions.forEach { transition ->
                // Add source state node if it's not a wildcard and not already added
                if (transition.from != "*" && !statesMap.containsKey(transition.from)) {
                    val id = "${stateVar.name}_${transition.from}"
                    statesMap[transition.from] = id

                    val nodeJson = JSONObject()
                    val dataJson = JSONObject()
                    dataJson.put("id", id)
                    dataJson.put("label", "${stateVar.name}=${transition.from}")

                    // Add some visual properties
                    nodeJson.put("data", dataJson)
                    nodeJson.put("position", JSONObject().also {
                        it.put("x", 0)
                        it.put("y", 0)
                    })
                    jsonArray.put(nodeJson)
                }

                // Add target state node if not already added
                if (!statesMap.containsKey(transition.to)) {
                    val id = "${stateVar.name}_${transition.to}"
                    statesMap[transition.to] = id

                    val nodeJson = JSONObject()
                    val dataJson = JSONObject()
                    dataJson.put("id", id)
                    dataJson.put("label", "${stateVar.name}=${transition.to}")

                    // Add some visual properties
                    nodeJson.put("data", dataJson)
                    nodeJson.put("position", JSONObject().also {
                        it.put("x", 0)
                        it.put("y", 0)
                    })
                    jsonArray.put(nodeJson)
                }
            }

            // Create edges for transitions with non-wildcard source states
            stateVar.transitions.forEachIndexed { index, transition ->
                if (transition.from != "*") {
                    val sourceId = statesMap[transition.from]
                    val targetId = statesMap[transition.to]

                    if (sourceId != null && targetId != null) {
                        val edgeJson = JSONObject()
                        val dataJson = JSONObject()
                        dataJson.put("id", "edge_${index}")
                        dataJson.put("source", sourceId)
                        dataJson.put("target", targetId)

                        // Use condition as label if it's not "true"
                        val label = if (transition.condition == "true") "" else transition.condition
                        dataJson.put("label", label)

                        edgeJson.put("data", dataJson)
                        jsonArray.put(edgeJson)
                    }
                }
            }

            logger.info("Generated graph with ${statesMap.size} nodes and ${stateVar.transitions.count { it.from != "*" }} edges")

        } catch (e: Exception) {
            logger.error("Error generating graph for ${stateVar.name}: ${e.message}")
        }

        val result = jsonArray.toString(2)
        logger.info("Generated JSON: ${if (result.length > 100) result.substring(0, 100) + "..." else result}")
        return result
    }
}
