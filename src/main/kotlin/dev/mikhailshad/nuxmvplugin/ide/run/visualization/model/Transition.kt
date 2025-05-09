package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

data class Transition(
    val from: String,
    val to: String,
) {
    lateinit var condition: String
}
