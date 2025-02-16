package dev.mikhailshad.nuxmvplugin.language.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import javax.swing.Icon

class NuXmvColourSettingsPage : ColorSettingsPage {
    companion object {
        @JvmStatic
        val ATTRIBUTE_DESCRIPTORS = arrayOf(
            AttributesDescriptor("Identifier", NuXmvSyntaxHighlighter.IDENTIFIER_ATTRIBUTE),
            AttributesDescriptor("Keyword", NuXmvSyntaxHighlighter.KEYWORD_ATTRIBUTE)
        )
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = ATTRIBUTE_DESCRIPTORS

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
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null
}
