package dev.mikhailshad.nuxmvplugin.language.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import dev.mikhailshad.nuxmvplugin.language.lexer.NuXmvLexerAdapter
import dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserDefinition
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

class NuXmvSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        @JvmStatic
        val IDENTIFIER_ATTRIBUTE =
            createTextAttributesKey("NUXMV_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)

        @JvmStatic
        val KEYWORD_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        @JvmStatic
        val NUMBER_ATTRIBUTE = createTextAttributesKey("NUXMV_NUMBER", DefaultLanguageHighlighterColors.NUMBER)

        @JvmStatic
        val OPERATOR_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_OPERATORS",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        @JvmStatic
        val BAD_CHARACTER = createTextAttributesKey(
            "NUXMV_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        )

        @JvmStatic
        val COMMA_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_COMMA",
            DefaultLanguageHighlighterColors.COMMA
        )

        @JvmStatic
        val SEMICOLON_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_SEMICOLON",
            DefaultLanguageHighlighterColors.SEMICOLON
        )

        @JvmStatic
        val COLON_ATTRIBUTE = createTextAttributesKey("NUXMV_COLON", DefaultLanguageHighlighterColors.COMMA)

        @JvmStatic
        val BRACKETS_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_BRACKETS",
            DefaultLanguageHighlighterColors.BRACKETS
        )

        @JvmStatic
        val PARENTHESES_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_PARENTHESES",
            DefaultLanguageHighlighterColors.PARENTHESES
        )

        @JvmStatic
        val BRACES_ATTRIBUTE = createTextAttributesKey("NUXMV_BRACES", DefaultLanguageHighlighterColors.BRACES)

        @JvmStatic
        val MODULE_NAME_ATTRIBUTE =
            createTextAttributesKey("NUXMV_MODULE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME)

        @JvmStatic
        val FUNCTION_DECLARATION_ATTRIBUTE =
            createTextAttributesKey("NUXMV_FUNCTION_DECLARATION", DefaultLanguageHighlighterColors.CLASS_NAME)

        @JvmStatic
        val VARIABLE_NAME_ATTRIBUTE =
            createTextAttributesKey("NUXMV_VARIABLE_NAME", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)

        @JvmStatic
        val GLOBAL_NAME_ATTRIBUTE =
            createTextAttributesKey("NUXMV_GLOBAL_NAME", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)

        @JvmStatic
        val LINE_COMMENT_ATTRIBUTE =
            createTextAttributesKey("NUXMV_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)

        @JvmStatic
        val BLOCK_COMMENT_ATTRIBUTE =
            createTextAttributesKey("NUXMV_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)


        val EMPTY_ATTRIBUTE = arrayOf<TextAttributesKey>()
    }

    private val attributes: Map<IElementType, TextAttributesKey>

    init {
        val tokenTypeToAttribute = HashMap<IElementType, TextAttributesKey>()
//        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.SEPARATOR, SEPARATOR)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.IDENTIFIER, IDENTIFIER_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.KEYWORDS, KEYWORD_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.NUMBERS, NUMBER_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.OPERATORS, OPERATOR_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.COMMA, COMMA_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.COLON, COLON_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.SEMICOLON, SEMICOLON_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.BRACES, BRACES_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.BRACKETS, BRACKETS_ATTRIBUTE)
        fillMap(tokenTypeToAttribute, NuXmvParserDefinition.PARENTHESES, PARENTHESES_ATTRIBUTE)
        attributes = tokenTypeToAttribute
    }

    override fun getHighlightingLexer(): Lexer = NuXmvLexerAdapter()


    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when {
            NuXmvTypes.MODULE_NAME.equals(tokenType) -> pack(MODULE_NAME_ATTRIBUTE)
            NuXmvTypes.FUNCTION_DECLARATION.equals(tokenType) -> pack(FUNCTION_DECLARATION_ATTRIBUTE)
            NuXmvTypes.VAR_NAME.equals(tokenType) -> pack(VARIABLE_NAME_ATTRIBUTE)
            NuXmvTypes.LINE_COMMENT.equals(tokenType) -> pack(LINE_COMMENT_ATTRIBUTE)
            NuXmvTypes.BLOCK_COMMENT.equals(tokenType) -> pack(BLOCK_COMMENT_ATTRIBUTE)
            else -> {
                val attribute = attributes[tokenType]
                if (attribute == null) {
                    EMPTY_ATTRIBUTE
                } else {
                    pack(attribute)
                }
            }
        }
    }
}
