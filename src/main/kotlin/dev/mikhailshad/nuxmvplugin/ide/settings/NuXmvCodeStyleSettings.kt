package dev.mikhailshad.nuxmvplugin.ide.settings

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class NuXmvCodeStyleSettings(settings: CodeStyleSettings) :
    CustomCodeStyleSettings("NuXmvCodeStyleSettings", settings) {

    @JvmField
    var BLANK_LINES_BETWEEN_MODULES = 2

    @JvmField
    var BLANK_LINES_BETWEEN_SECTIONS = 1
}
