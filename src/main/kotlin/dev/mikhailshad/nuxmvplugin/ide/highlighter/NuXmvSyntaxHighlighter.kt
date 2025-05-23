package dev.mikhailshad.nuxmvplugin.ide.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import dev.mikhailshad.nuxmvplugin.language.lexer.NuXmvLexerAdapter
import dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserDefinition
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

class NuXmvSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        @JvmStatic
        val LINE_COMMENT_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        @JvmStatic
        val BLOCK_COMMENT_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_BLOCK_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        @JvmStatic
        val KEYWORD_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        @JvmStatic
        val MACRO_KEYWORD_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_MACRO_KEYWORD",
            DefaultLanguageHighlighterColors.METADATA
        )

        @JvmStatic
        val NUMBER_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )

        @JvmStatic
        val WORD_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_WORD",
            DefaultLanguageHighlighterColors.STRING
        )

        @JvmStatic
        val MATH_OPERATOR_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_MATH_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        @JvmStatic
        val BOOL_OPERATOR_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_BOOL_OPERATOR",
            DefaultLanguageHighlighterColors.METADATA
        )

        @JvmStatic
        val TIMED_LOGIC_OPERATOR_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_TIMED_LOGIC_OPERATOR",
            DefaultLanguageHighlighterColors.STATIC_METHOD
        )

        @JvmStatic
        val BUILTIN_OPERATOR_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_BUILTIN_OPERATOR",
            DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )

        @JvmStatic
        val INIT_FUNCTION_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_INIT_FUNCTION",
            DefaultLanguageHighlighterColors.STATIC_FIELD
        )

        @JvmStatic
        val NEXT_FUNCTION_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_NEXT_FUNCTION",
            DefaultLanguageHighlighterColors.STATIC_FIELD
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
        val COLON_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_COLON", DefaultLanguageHighlighterColors.COMMA
        )

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
        val BRACES_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )

        @JvmStatic
        val MODULE_NAME_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_MODULE_NAME",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )

        @JvmStatic
        val MODULE_PARAMETER_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_PARAMETER_NAME",
            DefaultLanguageHighlighterColors.PARAMETER
        )

        @JvmStatic
        val FUNCTION_DECLARATION_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_FUNCTION_DECLARATION",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )

        @JvmStatic
        val FUNCTION_CALL_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_FUNCTION_CALL",
            DefaultLanguageHighlighterColors.FUNCTION_CALL
        )

        @JvmStatic
        val VARIABLE_NAME_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_VARIABLE_NAME",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )

        @JvmStatic
        val CONSTANT_NAME_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_CONSTANT_NAME",
            DefaultLanguageHighlighterColors.CONSTANT
        )

        @JvmStatic
        val TYPE_SPECIFIER_ATTRIBUTE = createTextAttributesKey(
            "NUXMV_TYPE_SPECIFIER",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )

        val EMPTY_ATTRIBUTE = arrayOf<TextAttributesKey>()
    }

    private val attributes: Map<IElementType, TextAttributesKey>

    init {
        val tokenTypeToAttribute = HashMap<IElementType, TextAttributesKey>()
        mapOf(
            NuXmvParserDefinition.Util.KEYWORDS to KEYWORD_ATTRIBUTE,
            NuXmvParserDefinition.Util.MACRO_KEYWORDS to MACRO_KEYWORD_ATTRIBUTE,
            NuXmvParserDefinition.Util.CONSTANTS to CONSTANT_NAME_ATTRIBUTE,
            NuXmvParserDefinition.Util.NUMBERS to NUMBER_ATTRIBUTE,
            NuXmvParserDefinition.Util.MATH_OPERATORS to MATH_OPERATOR_ATTRIBUTE,
            NuXmvParserDefinition.Util.BOOL_OPERATORS to BOOL_OPERATOR_ATTRIBUTE,
            NuXmvParserDefinition.Util.BUILTIN_OPERATORS to BUILTIN_OPERATOR_ATTRIBUTE,
            NuXmvParserDefinition.Util.TL_OPERATORS to TIMED_LOGIC_OPERATOR_ATTRIBUTE,
            NuXmvParserDefinition.Util.COMMA to COMMA_ATTRIBUTE,
            NuXmvParserDefinition.Util.COLON to COLON_ATTRIBUTE,
            NuXmvParserDefinition.Util.SEMICOLON to SEMICOLON_ATTRIBUTE,
            NuXmvParserDefinition.Util.BRACES to BRACES_ATTRIBUTE,
            NuXmvParserDefinition.Util.BRACKETS to BRACKETS_ATTRIBUTE,
            NuXmvParserDefinition.Util.PARENTHESES to PARENTHESES_ATTRIBUTE,
            NuXmvParserDefinition.Util.FUNCTION_CALLS to FUNCTION_CALL_ATTRIBUTE,
            NuXmvParserDefinition.Util.TYPES to TYPE_SPECIFIER_ATTRIBUTE,
        ).forEach {
            fillMap(tokenTypeToAttribute, it.key, it.value)
        }

        attributes = tokenTypeToAttribute
    }

    override fun getHighlightingLexer(): Lexer = NuXmvLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when {
            tokenType == NuXmvTypes.INIT_FUN -> pack(INIT_FUNCTION_ATTRIBUTE)
            tokenType == NuXmvTypes.NEXT_FUN -> pack(NEXT_FUNCTION_ATTRIBUTE)
            NuXmvTypes.FUNCTION_DECLARATION.equals(tokenType) -> pack(FUNCTION_DECLARATION_ATTRIBUTE)
            NuXmvTypes.FUNCTION_CALL_BASIC_EXPR.equals(tokenType) -> pack(FUNCTION_CALL_ATTRIBUTE)
            NuXmvTypes.LINE_COMMENT.equals(tokenType) -> pack(LINE_COMMENT_ATTRIBUTE)
            NuXmvTypes.BLOCK_COMMENT.equals(tokenType) -> pack(BLOCK_COMMENT_ATTRIBUTE)
            NuXmvTypes.WORD.equals(tokenType) -> pack(WORD_ATTRIBUTE)
            attributes.containsKey(tokenType) -> pack(attributes[tokenType])
            tokenType?.equals(TokenType.BAD_CHARACTER) ?: false -> pack(BAD_CHARACTER)
            else -> EMPTY_ATTRIBUTE
        }
    }
}
