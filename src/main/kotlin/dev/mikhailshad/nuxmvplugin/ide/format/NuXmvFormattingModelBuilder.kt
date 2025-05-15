package dev.mikhailshad.nuxmvplugin.ide.format

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType
import dev.mikhailshad.nuxmvplugin.ide.settings.NuXmvCodeStyleSettings
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserDefinition
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.*

class NuXmvFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val customSettings = settings.getCustomSettings(NuXmvCodeStyleSettings::class.java)
        val indentOptions = settings.getIndentOptions(formattingContext.containingFile.fileType)
        val spacingBuilder = createSpaceBuilder(settings, customSettings)
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            NuXmvBlock(
                formattingContext.node,
                spacingBuilder,
                Indent.getNoneIndent(),
                indentOptions
            ),
            settings
        )
    }

    private fun createSpaceBuilder(
        settings: CodeStyleSettings,
        customSettings: NuXmvCodeStyleSettings
    ): SpacingBuilder {
        return SpacingBuilder(settings, NuXmvLanguage).apply {
            // Punctuation spacing
            before(SEMICOLON).none()
            after(SEMICOLON).lineBreakInCode()
            before(COMMA).none()
            after(COMMA).spaces(1)

            // Parentheses and brackets spacing
            around(LPAREN).none()
            before(RPAREN).none()
            around(LBRACE).none()
            after(LBRACE).none()
            before(RBRACE).none()
            around(LBRACKET).none()
            before(RBRACKET).none()

            // Expression operators spacing
            aroundInside(QUESTION_MARK, TERNARY_BASIC_EXPR).spaces(1)
            around(ASSIGN).spaces(1)
            around(COLON).spaces(1)
            before(NOT).spaces(1)
            after(NOT).spaces(0)
            around(EQUALITY).spaces(1)
            around(EQUIVALENCE).spaces(1)
            around(NOT_EQUALITY).spaces(1)
            around(IMPLICATION).spaces(1)
            around(OR).spaces(1)
            around(XOR).spaces(1)
            around(NOT_XOR).spaces(1)
            around(GREATER).spaces(1)
            around(GREATER_EQ).spaces(1)
            around(LESS).spaces(1)
            around(LESS_EQ).spaces(1)
            around(AND).spaces(1)
            around(PLUS).spaces(1)
            around(MINUS).spaces(1)
            around(DIV).spaces(1)
            around(MULT).spaces(1)
            around(MOD).spaces(1)
            around(SHIFT_LEFT).spaces(1)
            around(SHIFT_RIGHT).spaces(1)
            around(CONCAT).spaces(1)
            around(UNION).spaces(1)
            around(IN).spaces(1)

            // LTL/CTL operators spacing
            around(NuXmvParserDefinition.Util.TL_OPERATORS).spaces(1)

            // Module-level spacing
            after(MODULE_KW).spaces(1)
            after(MODULE_DECLARATION).lineBreakInCode()
            between(MODULE, MODULE).blankLines(customSettings.BLANK_LINES_BETWEEN_MODULES)

            // Section-level line breaks
            before(VAR_DECLARATION).lineBreakInCode()
            before(IVAR_DECLARATION).lineBreakInCode()
            before(FROZEN_VAR_DECLARATION).lineBreakInCode()
            before(FUNCTION_DECLARATION).lineBreakInCode()
            before(DEFINE_DECLARATION).lineBreakInCode()
            before(CONSTANTS_DECLARATION).lineBreakInCode()
            before(ASSIGN_CONSTRAINT).lineBreakInCode()
            before(TRANS_CONSTRAINT).lineBreakInCode()
            before(INIT_CONSTRAINT).lineBreakInCode()
            before(INVAR_CONSTRAINT).lineBreakInCode()
            before(FAIRNESS_CONSTRAINT).lineBreakInCode()
            before(JUSTICE_CONSTRAINT).lineBreakInCode()
            before(COMPASSION_CONSTRAINT).lineBreakInCode()
            before(CTL_SPECIFICATION).lineBreakInCode()
            before(LTL_SPECIFICATION).lineBreakInCode()
            before(INVAR_SPECIFICATION).lineBreakInCode()
            before(COMPUTE_SPECIFICATION).lineBreakInCode()
            before(PARAMETER_SYNTH_PROBLEM_DECLARATION).lineBreakInCode()
            before(ISA_DECLARATION).lineBreakInCode()
            before(PRED_DECLARATION).lineBreakInCode()
            before(MIRROR_DECLARATION).lineBreakInCode()
            before(FOR_LOOP_MACRO).lineBreakInCode()

            // Section members line breaks
            after(DEFINE_BODY).lineBreakInCode()
            after(REGULAR_CASE_BODY).lineBreakInCode()

            // Section-level blank lines
            val sectionBlankLines = customSettings.BLANK_LINES_BETWEEN_SECTIONS
            before(VAR_DECLARATION).blankLines(sectionBlankLines)
            before(IVAR_DECLARATION).blankLines(sectionBlankLines)
            before(FROZEN_VAR_DECLARATION).blankLines(sectionBlankLines)
            before(FUNCTION_DECLARATION).blankLines(sectionBlankLines)
            before(DEFINE_DECLARATION).blankLines(sectionBlankLines)
            before(CONSTANTS_DECLARATION).blankLines(sectionBlankLines)
            before(ASSIGN_CONSTRAINT).blankLines(sectionBlankLines)
            before(TRANS_CONSTRAINT).blankLines(sectionBlankLines)
            before(INIT_CONSTRAINT).blankLines(sectionBlankLines)
            before(INVAR_CONSTRAINT).blankLines(sectionBlankLines)
            before(FAIRNESS_CONSTRAINT).blankLines(sectionBlankLines)
            before(JUSTICE_CONSTRAINT).blankLines(sectionBlankLines)
            before(COMPASSION_CONSTRAINT).blankLines(sectionBlankLines)
            before(CTL_SPECIFICATION).blankLines(sectionBlankLines)
            before(LTL_SPECIFICATION).blankLines(sectionBlankLines)
            before(INVAR_SPECIFICATION).blankLines(sectionBlankLines)
            before(COMPUTE_SPECIFICATION).blankLines(sectionBlankLines)
            before(PARAMETER_SYNTH_PROBLEM_DECLARATION).blankLines(sectionBlankLines)
            before(ISA_DECLARATION).blankLines(sectionBlankLines)
            before(PRED_DECLARATION).blankLines(sectionBlankLines)
            before(MIRROR_DECLARATION).blankLines(sectionBlankLines)
            before(FOR_LOOP_MACRO).blankLines(sectionBlankLines)

            // Section keywords spacing
            betweenInside(VAR_KW, SINGLE_VAR_DECLARATION, VAR_DECLARATION).lineBreakInCode()
            betweenInside(IVAR_KW, SINGLE_IVAR_DECLARATION, IVAR_DECLARATION).lineBreakInCode()
            betweenInside(FROZENVAR_KW, SINGLE_IVAR_DECLARATION, FROZEN_VAR_DECLARATION).lineBreakInCode()
            betweenInside(DEFINE_KW, DEFINE_BODY, DEFINE_DECLARATION).lineBreakInCode()
            betweenInside(ASSIGN_KW, SINGLE_ASSIGN_CONSTRAINT, ASSIGN_CONSTRAINT).lineBreakInCode()

            // After declaration keywords spaces
            afterInside(VAR_KW, VAR_DECLARATION).lineBreakInCode()
            afterInside(IVAR_KW, IVAR_DECLARATION).lineBreakInCode()
            afterInside(FROZENVAR_KW, FROZEN_VAR_DECLARATION).lineBreakInCode()
            afterInside(DEFINE_KW, DEFINE_DECLARATION).lineBreakInCode()
            afterInside(CONSTANTS_KW, CONSTANTS_DECLARATION).spaces(1)
            afterInside(ASSIGN_KW, ASSIGN_CONSTRAINT).lineBreakInCode()
            afterInside(INIT_KW, INIT_CONSTRAINT).spaces(1)
            afterInside(TRANS_KW, TRANS_CONSTRAINT).spaces(1)
            afterInside(INVAR_KW, INVAR_CONSTRAINT).spaces(1)
            afterInside(FAIRNESS_KW, FAIRNESS_CONSTRAINT).spaces(1)
            afterInside(JUSTICE_KW, JUSTICE_CONSTRAINT).spaces(1)
            afterInside(COMPASSION_KW, COMPASSION_CONSTRAINT).spaces(1)
            afterInside(CTLSPEC_KW, CTL_SPECIFICATION).spaces(1)
            afterInside(SPEC_KW, CTL_SPECIFICATION).spaces(1)
            afterInside(LTLSPEC_KW, LTL_SPECIFICATION).spaces(1)
            afterInside(INVARSPEC_KW, INVAR_SPECIFICATION).spaces(1)
            afterInside(COMPUTE_KW, COMPUTE_SPECIFICATION).spaces(1)
            afterInside(FUN_KW, FUNCTION_DECLARATION).lineBreakInCode()
            afterInside(ISA_KW, ISA_DECLARATION).spaces(1)
            afterInside(PRED_KW, PRED_DECLARATION).spaces(1)
            afterInside(MIRROR_KW, MIRROR_DECLARATION).spaces(1)

            // Case expression spacing
            afterInside(CASE_KW, CASE_BASIC_EXPR).lineBreakInCode()
            beforeInside(ESAC_KW, CASE_BASIC_EXPR).lineBreakInCode()
            afterInside(COLON, REGULAR_CASE_BODY).spaces(1)

            // Proper formatting for function calls
            betweenInside(FUNCTION_NAME_USAGE, LPAREN, FUNCTION_CALL_BASIC_EXPR).none()
            betweenInside(LPAREN, BASIC_EXPR_LIST, FUNCTION_CALL_BASIC_EXPR).none()
            betweenInside(BASIC_EXPR_LIST, RPAREN, FUNCTION_CALL_BASIC_EXPR).none()

            // Arrays and type definitions
            between(ARRAY_TYPE, LBRACKET).spaces(1)
            between(ARRAY_OF, SIMPLE_TYPE_SPECIFIER).spaces(1)

            // FOR macro formatting
            after(FOR_MACRO_KW).spaces(1)
            around(IN_KW).spaces(1)
            afterInside(RANGE_CONSTANT, FOR_LOOP_MACRO).lineBreakInCode()
            before(END_MACRO_KW).lineBreakInCode()
        }
    }

    private class NuXmvBlock(
        private val node: ASTNode,
        private val spacingBuilder: SpacingBuilder,
        private val indent: Indent,
        private val indentOptions: CommonCodeStyleSettings.IndentOptions
    ) : AbstractBlock(node, null, null) {

        override fun buildChildren(): List<Block> {
            return node.getChildren(null)
                .filter { it.elementType != TokenType.WHITE_SPACE }
                .map {
                    NuXmvBlock(
                        it,
                        spacingBuilder,
                        getIndentFor(it),
                        indentOptions
                    )
                }
        }

        private fun getIndentFor(node: ASTNode): Indent {
            val parentType = node.treeParent?.elementType ?: return Indent.getNoneIndent()
            val nodeType = node.elementType

            // Handle sections with continuous indent
            if (shouldGetContinuationIndent(nodeType, parentType)) {
                return Indent.getNormalIndent(true)
            }

            // Handle expression indentation within parentheses
            if (parentType == PARENTHESIS_BASIC_EXPR && node.elementType !in listOf(LPAREN, RPAREN)) {
                return Indent.getNormalIndent(true)
            }

            // Handle case expressions
            if (parentType == CASE_BASIC_EXPR) {
                return when (node.elementType) {
                    CASE_KW -> Indent.getNormalIndent(true)
                    REGULAR_CASE_BODY -> Indent.getContinuationIndent(true)
                    ESAC_KW -> Indent.getNormalIndent(true)
                    else -> Indent.getNoneIndent()
                }
            }

            // Handle FOR macro indentation
            if (parentType == FOR_LOOP_MACRO) {
                return when (node.elementType) {
                    FOR_MACRO_KW, FOR_LOOP_VARIABLE, IN_KW, RANGE_CONSTANT, END_MACRO_KW -> Indent.getNoneIndent()
                    else -> Indent.getNormalIndent(true)
                }
            }

            // Handle other specific blocks that should be indented
            return when (node.elementType) {
                REGULAR_CASE_BODY -> Indent.getNormalIndent(true)
                SINGLE_VAR_DECLARATION, SINGLE_IVAR_DECLARATION, DEFINE_BODY,
                SINGLE_ASSIGN_CONSTRAINT, FUNCTION_SPECIFICATION -> Indent.getNormalIndent(true)

                MODULE_BODY -> Indent.getNormalIndent(false)
                else -> Indent.getNoneIndent()
            }
        }

        private fun shouldGetContinuationIndent(nodeType: IElementType, parentType: IElementType): Boolean {
            return (parentType == INIT_CONSTRAINT && nodeType != INIT_KW
                    || parentType == JUSTICE_CONSTRAINT && nodeType != JUSTICE_KW
                    || parentType == TRANS_CONSTRAINT && nodeType != TRANS_KW
                    || parentType == INVAR_CONSTRAINT && nodeType != INVAR_KW
                    || parentType == FAIRNESS_CONSTRAINT && nodeType != FAIRNESS_KW
                    || parentType == COMPASSION_CONSTRAINT && nodeType != COMPASSION_KW
                    || parentType == CONSTANTS_DECLARATION && nodeType != CONSTANTS_KW
                    || parentType == DEFINE_DECLARATION && nodeType != DEFINE_KW
                    || parentType == FROZEN_VAR_DECLARATION && nodeType != FROZENVAR_KW
                    || parentType == ISA_DECLARATION && nodeType != ISA_KW
                    || parentType == IVAR_DECLARATION && nodeType != IVAR_KW
                    || parentType == VAR_DECLARATION && nodeType != VAR_KW
                    || parentType == MIRROR_DECLARATION && nodeType != MIRROR_KW
                    || parentType == COMPUTE_SPECIFICATION && nodeType != COMPUTE_KW
                    || parentType == CTL_SPECIFICATION && nodeType !in listOf(CTLSPEC_KW, SPEC_KW)
                    || parentType == INVAR_SPECIFICATION && nodeType != INVARSPEC_KW
                    || parentType == LTL_SPECIFICATION && nodeType != LTLSPEC_KW
                    || parentType == FUNCTION_DECLARATION && nodeType != FUN_KW
                    || parentType == FOR_LOOP_MACRO && nodeType !in listOf(
                FOR_MACRO_KW,
                FOR_LOOP_VARIABLE,
                IN_KW,
                RANGE_CONSTANT,
                END_MACRO_KW
            )
                    )
        }

        override fun getIndent(): Indent = indent

        override fun getSpacing(child1: Block?, child2: Block): Spacing? {
            return spacingBuilder.getSpacing(this, child1, child2)
        }

        override fun isLeaf(): Boolean = node.firstChildNode == null
    }
}
