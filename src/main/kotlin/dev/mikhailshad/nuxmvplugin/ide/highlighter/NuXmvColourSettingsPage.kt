package dev.mikhailshad.nuxmvplugin.ide.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import javax.swing.Icon

class NuXmvColourSettingsPage : ColorSettingsPage {
    object Util {
        @JvmStatic
        val ATTRIBUTE_DESCRIPTORS = arrayOf(
            AttributesDescriptor("Identifier", NuXmvSyntaxHighlighter.IDENTIFIER_ATTRIBUTE),
            AttributesDescriptor("Keyword", NuXmvSyntaxHighlighter.KEYWORD_ATTRIBUTE),
            AttributesDescriptor("Constants", NuXmvSyntaxHighlighter.CONSTANT_NAME_ATTRIBUTE),
            AttributesDescriptor("Number", NuXmvSyntaxHighlighter.NUMBER_ATTRIBUTE),
            AttributesDescriptor("Word", NuXmvSyntaxHighlighter.WORD_ATTRIBUTE),
            AttributesDescriptor("Math operator", NuXmvSyntaxHighlighter.MATH_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Bool operator", NuXmvSyntaxHighlighter.BOOL_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Timed logic operator", NuXmvSyntaxHighlighter.TIMED_LOGIC_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Built-in operator", NuXmvSyntaxHighlighter.BUILTIN_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Module name", NuXmvSyntaxHighlighter.MODULE_NAME_ATTRIBUTE),
            AttributesDescriptor("Function declaration", NuXmvSyntaxHighlighter.FUNCTION_DECLARATION_ATTRIBUTE),
            AttributesDescriptor("Function call", NuXmvSyntaxHighlighter.FUNCTION_CALL_ATTRIBUTE),
            AttributesDescriptor("Variable name", NuXmvSyntaxHighlighter.VARIABLE_NAME_ATTRIBUTE),
            AttributesDescriptor("Bad character", NuXmvSyntaxHighlighter.BAD_CHARACTER),
        )
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = Util.ATTRIBUTE_DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "NuXmv"

    override fun getIcon(): Icon = NuXmvIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = NuXmvSyntaxHighlighter()

    override fun getDemoText(): String = """
        -- Demo text below
        MODULE foo
        VAR
            bar : boolean;
            baz : float;
        ASSIGN
            init(bar) = TRUE;
            init(baz) = 0.123;
        SPEC
            AG AF bar;
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null
}
