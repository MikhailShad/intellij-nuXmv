package dev.mikhailshad.nuxmvplugin.ide.run.visualization.model

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType

data class StateVariable(
    val name: String,
    val type: NuXmvBuiltInType,
    val transitions: MutableList<Transition> = mutableListOf(),
    var initialValue: String? = null
)
