package dev.mikhailshad.nuxmvplugin.ide.settings

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.*
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage


class NuXmvCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun getLanguage(): Language = NuXmvLanguage

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return NuXmvCodeStyleSettings(settings)
    }

    override fun createConfigurable(
        baseSettings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return CodeStyleConfigurable(baseSettings, modelSettings)
    }

    class CodeStyleConfigurable(codeStyleSettings: CodeStyleSettings, cloneCodeStyleSettings: CodeStyleSettings) :
        CodeStyleAbstractConfigurable(codeStyleSettings, cloneCodeStyleSettings, "NuXmv") {
        override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
            return CodeStylePanel(currentSettings, settings)
        }

        class CodeStylePanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
            TabbedLanguageCodeStylePanel(NuXmvLanguage, currentSettings, settings)
    }
}
