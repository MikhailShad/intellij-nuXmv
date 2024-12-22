package dev.mikhailshad.nuxmvplugin.language

import com.intellij.lang.Language

object NuXmvLanguage : Language("nuXmv") {
    private fun readResolve(): Any = NuXmvLanguage
}
