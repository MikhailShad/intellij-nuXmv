package dev.mikhailshad.nuxmvplugin.language.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.lexer.NuXmvLexerAdapter
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

class NuXmvParserDefinition : ParserDefinition {
    object Util {
        @JvmStatic
        val FILE: IFileElementType = IFileElementType(NuXmvLanguage)

        @JvmStatic
        val SEMICOLON = TokenSet.create(NuXmvTypes.SEMICOLON)

        @JvmStatic
        val COLON = TokenSet.create(NuXmvTypes.COLON)

        @JvmStatic
        val BRACKETS = TokenSet.create(NuXmvTypes.RBRACKET, NuXmvTypes.LBRACKET)

        @JvmStatic
        val BRACES = TokenSet.create(NuXmvTypes.RBRACE, NuXmvTypes.LBRACE)

        @JvmStatic
        val PARENTHESES = TokenSet.create(NuXmvTypes.LPAREN, NuXmvTypes.RPAREN)

        @JvmStatic
        val COMMA = TokenSet.create(NuXmvTypes.COMMA)

        @JvmStatic
        val IDENTIFIER = TokenSet.create(NuXmvTypes.IDENTIFIER)

        @JvmStatic
        val OPERATORS = TokenSet.create(
            NuXmvTypes.MINUS,
            NuXmvTypes.PLUS,
            NuXmvTypes.MULT,
            NuXmvTypes.DIV,
            NuXmvTypes.MOD,
            NuXmvTypes.LESS,
            NuXmvTypes.LESS_EQ,
            NuXmvTypes.GREATER,
            NuXmvTypes.GREATER_EQ,
            NuXmvTypes.EQUALITY,
            NuXmvTypes.NOT_EQUALITY
        )

        @JvmStatic
        val NUMBERS = TokenSet.create(
            NuXmvTypes.INTEGER_NUMBER,
            NuXmvTypes.FLOAT_NUMBER,
            NuXmvTypes.FRACTIONAL_NUMBER,
            NuXmvTypes.EXPONENTIAL_NUMBER
        )

        @JvmStatic
        val KEYWORDS =
            TokenSet.create(
                NuXmvTypes.CASE,
                NuXmvTypes.ESAC,
                NuXmvTypes.MODULE,
                NuXmvTypes.ASSIGN,
                NuXmvTypes.VAR,
                NuXmvTypes.IVAR,
                NuXmvTypes.FROZENVAR,
                NuXmvTypes.DEFINE_KW,
                NuXmvTypes.TRUE,
                NuXmvTypes.FALSE
            )

        @JvmStatic
        val COMMENTS = TokenSet.create(NuXmvTypes.LINE_COMMENT, NuXmvTypes.BLOCK_COMMENT)
    }


    override fun createLexer(project: Project?): Lexer = NuXmvLexerAdapter()

    override fun createParser(project: Project?): PsiParser = NuXmvParser()

    override fun getFileNodeType(): IFileElementType = Util.FILE

    override fun getCommentTokens(): TokenSet = Util.COMMENTS

    override fun getStringLiteralElements(): TokenSet = Util.IDENTIFIER

    override fun createElement(node: ASTNode?): PsiElement = NuXmvTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = NuXmvFile(viewProvider)
}
