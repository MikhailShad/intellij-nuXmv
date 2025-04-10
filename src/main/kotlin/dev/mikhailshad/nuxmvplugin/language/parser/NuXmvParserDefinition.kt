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
        val MATH_OPERATORS = TokenSet.create(
            NuXmvTypes.MINUS,
            NuXmvTypes.PLUS,
            NuXmvTypes.MULT,
            NuXmvTypes.DIV,
            NuXmvTypes.MOD,
            NuXmvTypes.SHIFT_RIGHT,
            NuXmvTypes.SHIFT_LEFT,
        )

        @JvmStatic
        val BOOL_OPERATORS = TokenSet.create(
            NuXmvTypes.NOT,
            NuXmvTypes.AND,
            NuXmvTypes.OR,
            NuXmvTypes.XOR,
            NuXmvTypes.NOT_XOR,
            NuXmvTypes.IMPLICATION,
            NuXmvTypes.EQUIVALENCE,
            NuXmvTypes.LESS,
            NuXmvTypes.LESS_EQ,
            NuXmvTypes.GREATER,
            NuXmvTypes.GREATER_EQ,
            NuXmvTypes.EQUALITY,
            NuXmvTypes.NOT_EQUALITY,
        )

        @JvmStatic
        val TL_OPERATORS = TokenSet.create(
            NuXmvTypes.TL_UNTIL,
            NuXmvTypes.LTL_AT_NEXT,
            NuXmvTypes.LTL_AT_LAST,
            NuXmvTypes.LTL_NEXT,
            NuXmvTypes.LTL_GLOBALLY,
            NuXmvTypes.LTL_FINALLY,
            NuXmvTypes.LTL_RELEASES,
            NuXmvTypes.LTL_PREVIOUS,
            NuXmvTypes.LTL_NOT_PREVIOUS,
            NuXmvTypes.LTL_HISTORICALLY,
            NuXmvTypes.LTL_HISTORICALLY,
            NuXmvTypes.LTL_SINCE,
            NuXmvTypes.LTL_TRIGGERED,
            NuXmvTypes.LTL_TIME_SINCE,
            NuXmvTypes.LTL_TIME_UNTIL,
            NuXmvTypes.CTL_FORALL,
            NuXmvTypes.CTL_EXISTS,
            NuXmvTypes.CTL_EXISTS_GLOBALLY,
            NuXmvTypes.CTL_EXISTS_NEXT,
            NuXmvTypes.CTL_EXISTS_FINALLY,
            NuXmvTypes.CTL_FORALL_GLOBALLY,
            NuXmvTypes.CTL_FORALL_NEXT,
            NuXmvTypes.CTL_FORALL_FINALLY,
            NuXmvTypes.RTCTL_EBF,
            NuXmvTypes.RTCTL_ABF,
            NuXmvTypes.RTCTL_EBG,
            NuXmvTypes.RTCTL_ABG,
            NuXmvTypes.RTCTL_BU,
        )

        @JvmStatic
        val BUILTIN_OPERATORS = TokenSet.create(
            NuXmvTypes.CONCAT,
            NuXmvTypes.RANGE,
            NuXmvTypes.UNION,
            NuXmvTypes.IN,
            NuXmvTypes.QUESTION_MARK
        )

        @JvmStatic
        val NUMBERS = TokenSet.create(
            NuXmvTypes.POSITIVE_INTEGER_NUMBER,
            NuXmvTypes.INTEGER_NUMBER,
            NuXmvTypes.FLOAT_NUMBER,
            NuXmvTypes.FRACTIONAL_NUMBER,
            NuXmvTypes.EXPONENTIAL_NUMBER
        )

        @JvmStatic
        val CONSTANTS = TokenSet.create(
            NuXmvTypes.TIME,
            NuXmvTypes.PI
        )

        @JvmStatic
        val KEYWORDS =
            TokenSet.create(
                NuXmvTypes.LTLSPEC_KW,
                NuXmvTypes.CTLSPEC_KW,
                NuXmvTypes.SPEC_KW,
                NuXmvTypes.INVARSPEC_KW,

                NuXmvTypes.INVARSPEC_KW,
                NuXmvTypes.PSLSPEC_KW,
                NuXmvTypes.MODULE_KW,
                NuXmvTypes.SELF_KW,
                NuXmvTypes.TRUE_KW,
                NuXmvTypes.FALSE_KW,
                NuXmvTypes.FUN_KW,
                NuXmvTypes.DEFINE_KW,
                NuXmvTypes.CONSTANTS_KW,
                NuXmvTypes.ASSIGN_KW,
                NuXmvTypes.INIT_KW,
                NuXmvTypes.TRANS_KW,
                NuXmvTypes.VAR_KW,
                NuXmvTypes.IVAR_KW,
                NuXmvTypes.FROZENVAR_KW,
                NuXmvTypes.INVAR_KW,
                NuXmvTypes.FAIRNESS_KW,
                NuXmvTypes.JUSTICE_KW,
                NuXmvTypes.COMPASSION_KW,
                NuXmvTypes.TIME_DOMAIN_KW,
                NuXmvTypes.MIRROR_KW,
                NuXmvTypes.ISA_KW,
                NuXmvTypes.NAME_KW,
                NuXmvTypes.COMPUTE_KW,
                NuXmvTypes.MIN_KW,
                NuXmvTypes.MAX_KW,
                NuXmvTypes.PARSYNTH_KW,
                NuXmvTypes.VALID_KW,
                NuXmvTypes.SAT_KW,
                NuXmvTypes.MONOPOS_KW,
                NuXmvTypes.MONONEG_KW,
                NuXmvTypes.CASE_KW,
                NuXmvTypes.ESAC_KW,
                NuXmvTypes.CLOCK_KW,
                NuXmvTypes.COMPID_KW,
                NuXmvTypes.COMPWFF_KW,
                NuXmvTypes.CONSTRAINT_KW,
                NuXmvTypes.CTLWFF_KW,
                NuXmvTypes.IN_KW,
                NuXmvTypes.INTEGER_KW,
                NuXmvTypes.ITYPE_KW,
                NuXmvTypes.LTLWFF_KW,
                NuXmvTypes.MDEFINE_KW,
                NuXmvTypes.NEXTWFF_KW,
                NuXmvTypes.NONCONTINUOUS_KW,
                NuXmvTypes.PREDICATES_KW,
                NuXmvTypes.REAL_KW,
                NuXmvTypes.SIMPWFF_KW,
                NuXmvTypes.URGENT_KW,
                NuXmvTypes.WORD_KW,
            )

        @JvmStatic
        val FUNCTION_CALLS = TokenSet.create(
            NuXmvTypes.FUNCTION_NAME_USAGE,
            NuXmvTypes.INIT_FUN,
            NuXmvTypes.NEXT_FUN,
            NuXmvTypes.ABS_FUN,
            NuXmvTypes.MAX_FUN,
            NuXmvTypes.MIN_FUN,
            NuXmvTypes.SIN_FUN,
            NuXmvTypes.COS_FUN,
            NuXmvTypes.EXP_FUN,
            NuXmvTypes.TAN_FUN,
            NuXmvTypes.LN_FUN,
            NuXmvTypes.POW_FUN,
            NuXmvTypes.ASIN_FUN,
            NuXmvTypes.ACOS_FUN,
            NuXmvTypes.ATAN_FUN,
            NuXmvTypes.SQRT_FUN,
            NuXmvTypes.TO_WORD1_FUN,
            NuXmvTypes.TO_BOOL_FUN,
            NuXmvTypes.TO_INT_FUN,
            NuXmvTypes.COUNT_FUN,
            NuXmvTypes.SWCONST_FUN,
            NuXmvTypes.UWCONST_FUN,
            NuXmvTypes.TO_SIGNED_FUN,
            NuXmvTypes.TO_UNSIGNED_FUN,
            NuXmvTypes.SIZEOF_FUN,
            NuXmvTypes.FLOOR_FUN,
            NuXmvTypes.EXTEND_FUN,
            NuXmvTypes.RESIZE_FUN,
        )

        @JvmStatic
        val COMMENTS = TokenSet.create(
            NuXmvTypes.LINE_COMMENT,
            NuXmvTypes.BLOCK_COMMENT
        )
    }


    override fun createLexer(project: Project?): Lexer = NuXmvLexerAdapter()

    override fun createParser(project: Project?): PsiParser = NuXmvParser()

    override fun getFileNodeType(): IFileElementType = Util.FILE

    override fun getCommentTokens(): TokenSet = Util.COMMENTS

    override fun getStringLiteralElements(): TokenSet = Util.IDENTIFIER

    override fun createElement(node: ASTNode?): PsiElement = NuXmvTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = NuXmvFile(viewProvider)
}
