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
            AttributesDescriptor("Type specifier", NuXmvSyntaxHighlighter.TYPE_SPECIFIER_ATTRIBUTE),
            AttributesDescriptor("Keyword", NuXmvSyntaxHighlighter.KEYWORD_ATTRIBUTE),
            AttributesDescriptor("Constants", NuXmvSyntaxHighlighter.CONSTANT_NAME_ATTRIBUTE),
            AttributesDescriptor("Number", NuXmvSyntaxHighlighter.NUMBER_ATTRIBUTE),
            AttributesDescriptor("Word", NuXmvSyntaxHighlighter.WORD_ATTRIBUTE),
            AttributesDescriptor("Math operator", NuXmvSyntaxHighlighter.MATH_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Boolean operator", NuXmvSyntaxHighlighter.BOOL_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Temporal Logic operator", NuXmvSyntaxHighlighter.TIMED_LOGIC_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Built-in operator", NuXmvSyntaxHighlighter.BUILTIN_OPERATOR_ATTRIBUTE),
            AttributesDescriptor("Init function", NuXmvSyntaxHighlighter.INIT_FUNCTION_ATTRIBUTE),
            AttributesDescriptor("Next() function", NuXmvSyntaxHighlighter.NEXT_FUNCTION_ATTRIBUTE),
            AttributesDescriptor("Module name", NuXmvSyntaxHighlighter.MODULE_NAME_ATTRIBUTE),
            AttributesDescriptor("Module parameter name", NuXmvSyntaxHighlighter.MODULE_PARAMETER_ATTRIBUTE),
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
                arr : array 0..3 of boolean;
                w : signed word[8];
            DEFINE
                always_true := TRUE;
            ASSIGN
                init(bar) = FALSE & TRUE;
                next(bar) = !bar | baz > 0;
                init(baz) = 0.123;
                next(arr[0]) = arr[1] xor arr[2];
                w := 0sd8_0;
            CTLSPEC
                AG(bar -> AF(!bar));
            LTLSPEC
                G(bar -> X(!bar)) & F(baz > 1.0);
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null
}
