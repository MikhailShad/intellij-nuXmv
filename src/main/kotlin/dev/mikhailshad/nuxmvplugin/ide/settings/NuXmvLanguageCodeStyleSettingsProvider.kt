package dev.mikhailshad.nuxmvplugin.ide.settings

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizableOptions
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage

class NuXmvLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = NuXmvLanguage

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        indentOptions.TAB_SIZE = 2
        indentOptions.INDENT_SIZE = 2
        indentOptions.CONTINUATION_INDENT_SIZE = 4
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.BLANK_LINES_SETTINGS -> {
                consumer.showCustomOption(
                    NuXmvCodeStyleSettings::class.java,
                    "BLANK_LINES_BETWEEN_MODULES",
                    "Between modules",
                    CodeStyleSettingsCustomizableOptions.getInstance().BLANK_LINES_KEEP
                )
                consumer.showCustomOption(
                    NuXmvCodeStyleSettings::class.java,
                    "BLANK_LINES_BETWEEN_SECTIONS",
                    "Between sections (VAR, ASSIGN, etc.)",
                    CodeStyleSettingsCustomizableOptions.getInstance().BLANK_LINES_KEEP
                )
            }

            else -> {
                // TODO Другие настройки при необходимости
            }
        }
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return when (settingsType) {
            SettingsType.BLANK_LINES_SETTINGS -> """
                MODULE main
                
                    VAR
                        bit0: counter_cell(TRUE);
                        bit1: counter_cell(bit0.carry_out);
                    
                    DEFINE
                        start := TRUE;
                    
                    ASSIGN
                        init(value) := FALSE;
                    
                    SPEC
                        AG AF bit2.carry_out;
                
                
                MODULE counter_cell(carry_in)
                
                    VAR
                        value: boolean;
                    
                    ASSIGN
                        next(value) := value xor carry_in;
                    
                    DEFINE
                        carry_out := value & carry_in;
                """.trimIndent()

            else -> getDefaultCodeSample()
        }
    }

    private fun getDefaultCodeSample(): String {
        return """
            MODULE main
                VAR
                    bit0: counter_cell(TRUE);
                    bit1: counter_cell(bit0.carry_out);
                    bit2: counter_cell(bit1.carry_out);
                
                SPEC
                    AG AF bit2.carry_out;
                
            MODULE counter_cell(carry_in)
                VAR
                    value:boolean;
                ASSIGN
                    init(value) := FALSE;
                    next(value) := value xor carry_in;
                
                DEFINE
                    carry_out:=value & carry_in;
                
                SPEC
                    AF AG value;
                
                CONSTANTS
                    foo, bar;
        """.trimIndent()
    }
}
