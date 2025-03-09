// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import dev.mikhailshad.nuxmvplugin.language.psi.impl.*;

public interface NuXmvTypes {

    IElementType AND_BASIC_EXPR = new NuXmvElementType("AND_BASIC_EXPR");
    IElementType ASSIGN_CONSTRAINT = new NuXmvElementType("ASSIGN_CONSTRAINT");
    IElementType BASIC_EXPR_LIST = new NuXmvElementType("BASIC_EXPR_LIST");
    IElementType BINARY_LTL_EXPR = new NuXmvElementType("BINARY_LTL_EXPR");
    IElementType BINARY_LTL_OP = new NuXmvElementType("BINARY_LTL_OP");
    IElementType BOOLEAN_CONSTANT = new NuXmvElementType("BOOLEAN_CONSTANT");
    IElementType BUILT_IN_CONSTANT = new NuXmvElementType("BUILT_IN_CONSTANT");
    IElementType CASE_BASIC_EXPR = new NuXmvElementType("CASE_BASIC_EXPR");
    IElementType COMPASSION_CONSTRAINT = new NuXmvElementType("COMPASSION_CONSTRAINT");
    IElementType COMPLEX_IDENTIFIER = new NuXmvElementType("COMPLEX_IDENTIFIER");
    IElementType COMPUTE_EXPR = new NuXmvElementType("COMPUTE_EXPR");
    IElementType COMPUTE_SPECIFICATION = new NuXmvElementType("COMPUTE_SPECIFICATION");
    IElementType CONCAT_BASIC_EXPR = new NuXmvElementType("CONCAT_BASIC_EXPR");
    IElementType CONSTANTS_BODY = new NuXmvElementType("CONSTANTS_BODY");
    IElementType CONSTANTS_DECLARATION = new NuXmvElementType("CONSTANTS_DECLARATION");
    IElementType CTL_SPECIFICATION = new NuXmvElementType("CTL_SPECIFICATION");
    IElementType DEFINE_BODY = new NuXmvElementType("DEFINE_BODY");
    IElementType DEFINE_DECLARATION = new NuXmvElementType("DEFINE_DECLARATION");
    IElementType DIV_BASIC_EXPR = new NuXmvElementType("DIV_BASIC_EXPR");
    IElementType ENUMERATION_TYPE_BODY = new NuXmvElementType("ENUMERATION_TYPE_BODY");
    IElementType ENUMERATION_TYPE_VALUE = new NuXmvElementType("ENUMERATION_TYPE_VALUE");
    IElementType EQUALITY_BASIC_EXPR = new NuXmvElementType("EQUALITY_BASIC_EXPR");
    IElementType EQUIVALENCE_BASIC_EXPR = new NuXmvElementType("EQUIVALENCE_BASIC_EXPR");
    IElementType EXPR = new NuXmvElementType("EXPR");
    IElementType FAIRNESS_CONSTRAINT = new NuXmvElementType("FAIRNESS_CONSTRAINT");
    IElementType FROZEN_VAR_DECLARATION = new NuXmvElementType("FROZEN_VAR_DECLARATION");
    IElementType FUNCTION_ARG_TYPES_SPECIFIER = new NuXmvElementType("FUNCTION_ARG_TYPES_SPECIFIER");
    IElementType FUNCTION_CALL_BASIC_EXPR = new NuXmvElementType("FUNCTION_CALL_BASIC_EXPR");
    IElementType FUNCTION_DECLARATION = new NuXmvElementType("FUNCTION_DECLARATION");
    IElementType FUNCTION_IDENTIFIER = new NuXmvElementType("FUNCTION_IDENTIFIER");
    IElementType FUNCTION_NAME = new NuXmvElementType("FUNCTION_NAME");
    IElementType FUNCTION_SPECIFICATION = new NuXmvElementType("FUNCTION_SPECIFICATION");
    IElementType FUNCTION_TYPE_SPECIFIER = new NuXmvElementType("FUNCTION_TYPE_SPECIFIER");
    IElementType GREATER_BASIC_EXPR = new NuXmvElementType("GREATER_BASIC_EXPR");
    IElementType GREATER_EQ_BASIC_EXPR = new NuXmvElementType("GREATER_EQ_BASIC_EXPR");
    IElementType ID_LIST = new NuXmvElementType("ID_LIST");
    IElementType IMPLICATION_BASIC_EXPR = new NuXmvElementType("IMPLICATION_BASIC_EXPR");
    IElementType INDEX_BASIC_EXPR = new NuXmvElementType("INDEX_BASIC_EXPR");
    IElementType INIT_ASSIGN_EXPR = new NuXmvElementType("INIT_ASSIGN_EXPR");
    IElementType INIT_CONSTRAINT = new NuXmvElementType("INIT_CONSTRAINT");
    IElementType INVAR_CONSTRAINT = new NuXmvElementType("INVAR_CONSTRAINT");
    IElementType INVAR_SPECIFICATION = new NuXmvElementType("INVAR_SPECIFICATION");
    IElementType IN_BASIC_EXPR = new NuXmvElementType("IN_BASIC_EXPR");
    IElementType ISA_DECLARATION = new NuXmvElementType("ISA_DECLARATION");
    IElementType IVAR_DECLARATION = new NuXmvElementType("IVAR_DECLARATION");
    IElementType JUSTICE_CONSTRAINT = new NuXmvElementType("JUSTICE_CONSTRAINT");
    IElementType LESS_BASIC_EXPR = new NuXmvElementType("LESS_BASIC_EXPR");
    IElementType LESS_EQ_BASIC_EXPR = new NuXmvElementType("LESS_EQ_BASIC_EXPR");
    IElementType LITERAL_BASIC_EXPR = new NuXmvElementType("LITERAL_BASIC_EXPR");
    IElementType LOGICAL_NOT_BASIC_EXPR = new NuXmvElementType("LOGICAL_NOT_BASIC_EXPR");
    IElementType LTL_SPECIFICATION = new NuXmvElementType("LTL_SPECIFICATION");
    IElementType MINUS_BASIC_EXPR = new NuXmvElementType("MINUS_BASIC_EXPR");
    IElementType MIRROR_DECLARATION = new NuXmvElementType("MIRROR_DECLARATION");
    IElementType MODULE_BODY = new NuXmvElementType("MODULE_BODY");
    IElementType MODULE_DECLARATION = new NuXmvElementType("MODULE_DECLARATION");
    IElementType MODULE_NAME = new NuXmvElementType("MODULE_NAME");
    IElementType MODULE_PARAMETERS = new NuXmvElementType("MODULE_PARAMETERS");
    IElementType MODULE_TYPE_SPECIFIER = new NuXmvElementType("MODULE_TYPE_SPECIFIER");
    IElementType MOD_BASIC_EXPR = new NuXmvElementType("MOD_BASIC_EXPR");
    IElementType MUL_BASIC_EXPR = new NuXmvElementType("MUL_BASIC_EXPR");
    IElementType NEXT_ASSIGN_EXPR = new NuXmvElementType("NEXT_ASSIGN_EXPR");
    IElementType NOT_EQUALITY_BASIC_EXPR = new NuXmvElementType("NOT_EQUALITY_BASIC_EXPR");
    IElementType NOT_XOR_BASIC_EXPR = new NuXmvElementType("NOT_XOR_BASIC_EXPR");
    IElementType NU_XMV_MODULE = new NuXmvElementType("NU_XMV_MODULE");
    IElementType OR_BASIC_EXPR = new NuXmvElementType("OR_BASIC_EXPR");
    IElementType PARAMETER_LIST = new NuXmvElementType("PARAMETER_LIST");
    IElementType PARAMETER_SYNTH_PROBLEM = new NuXmvElementType("PARAMETER_SYNTH_PROBLEM");
    IElementType PARAMETER_SYNTH_PROBLEM_DECLARATION = new NuXmvElementType("PARAMETER_SYNTH_PROBLEM_DECLARATION");
    IElementType PARENTHESIS_BASIC_EXPR = new NuXmvElementType("PARENTHESIS_BASIC_EXPR");
    IElementType PLUS_BASIC_EXPR = new NuXmvElementType("PLUS_BASIC_EXPR");
    IElementType PRED_DECLARATION = new NuXmvElementType("PRED_DECLARATION");
    IElementType RANGE_CONSTANT = new NuXmvElementType("RANGE_CONSTANT");
    IElementType REAL_NUMBER = new NuXmvElementType("REAL_NUMBER");
    IElementType REFERENCE_BASIC_EXPR = new NuXmvElementType("REFERENCE_BASIC_EXPR");
    IElementType REGULAR_CASE_BODY = new NuXmvElementType("REGULAR_CASE_BODY");
    IElementType RT_CTL_EXPR = new NuXmvElementType("RT_CTL_EXPR");
    IElementType SHIFT_LEFT_BASIC_EXPR = new NuXmvElementType("SHIFT_LEFT_BASIC_EXPR");
    IElementType SHIFT_RIGHT_BASIC_EXPR = new NuXmvElementType("SHIFT_RIGHT_BASIC_EXPR");
    IElementType SIMPLE_ASSIGN_EXPR = new NuXmvElementType("SIMPLE_ASSIGN_EXPR");
    IElementType SIMPLE_IDENTIFIER = new NuXmvElementType("SIMPLE_IDENTIFIER");
    IElementType SIMPLE_TYPE_SPECIFIER = new NuXmvElementType("SIMPLE_TYPE_SPECIFIER");
    IElementType SINGLE_ASSIGN_CONSTRAINT = new NuXmvElementType("SINGLE_ASSIGN_CONSTRAINT");
    IElementType SINGLE_IVAR_DECLARATION = new NuXmvElementType("SINGLE_IVAR_DECLARATION");
    IElementType SINGLE_VAR_DECLARATION = new NuXmvElementType("SINGLE_VAR_DECLARATION");
    IElementType SYNTH_OPT = new NuXmvElementType("SYNTH_OPT");
    IElementType SYNTH_OPTS = new NuXmvElementType("SYNTH_OPTS");
    IElementType TERNARY_BASIC_EXPR = new NuXmvElementType("TERNARY_BASIC_EXPR");
    IElementType TIMED_LTL_EXPR = new NuXmvElementType("TIMED_LTL_EXPR");
    IElementType TIME_LTL_OP = new NuXmvElementType("TIME_LTL_OP");
    IElementType TRANS_CONSTRAINT = new NuXmvElementType("TRANS_CONSTRAINT");
    IElementType TYPE_SPECIFIER = new NuXmvElementType("TYPE_SPECIFIER");
    IElementType UNARY_CTL_EXPR = new NuXmvElementType("UNARY_CTL_EXPR");
    IElementType UNARY_CTL_OP = new NuXmvElementType("UNARY_CTL_OP");
    IElementType UNARY_LTL_EXPR = new NuXmvElementType("UNARY_LTL_EXPR");
    IElementType UNARY_LTL_OP = new NuXmvElementType("UNARY_LTL_OP");
    IElementType UNARY_MINUS_BASIC_EXPR = new NuXmvElementType("UNARY_MINUS_BASIC_EXPR");
    IElementType UNION_BASIC_EXPR = new NuXmvElementType("UNION_BASIC_EXPR");
    IElementType UNTIL_CTL_EXPR = new NuXmvElementType("UNTIL_CTL_EXPR");
    IElementType UNTIL_CTL_OP = new NuXmvElementType("UNTIL_CTL_OP");
    IElementType VARIABLE_IDENTIFIER = new NuXmvElementType("VARIABLE_IDENTIFIER");
    IElementType VAR_DECLARATION = new NuXmvElementType("VAR_DECLARATION");
    IElementType VAR_NAME = new NuXmvElementType("VAR_NAME");
    IElementType WHOLE_NUMBER = new NuXmvElementType("WHOLE_NUMBER");
    IElementType WORD_CONSTANT = new NuXmvElementType("WORD_CONSTANT");
    IElementType XOR_BASIC_EXPR = new NuXmvElementType("XOR_BASIC_EXPR");

    IElementType AND = new NuXmvTokenType("&");
    IElementType ARRAY_OF = new NuXmvTokenType("of");
    IElementType ARRAY_TYPE = new NuXmvTokenType("array");
    IElementType ASSIGN = new NuXmvTokenType(":=");
    IElementType ASSIGN_KW = new NuXmvTokenType("ASSIGN");
    IElementType BLOCK_COMMENT = new NuXmvTokenType("BLOCK_COMMENT");
    IElementType BOOLEAN_TYPE = new NuXmvTokenType("boolean");
    IElementType CASE = new NuXmvTokenType("case");
    IElementType CLOCK_TYPE = new NuXmvTokenType("clock");
    IElementType COLON = new NuXmvTokenType(":");
    IElementType COMMA = new NuXmvTokenType(",");
    IElementType COMPASSION_KW = new NuXmvTokenType("COMPASSION");
    IElementType COMPUTE_KW = new NuXmvTokenType("COMPUTE");
    IElementType CONCAT = new NuXmvTokenType("::");
    IElementType CONSTANTS_KW = new NuXmvTokenType("CONSTANTS");
    IElementType COUNT = new NuXmvTokenType("count");
    IElementType CTLSPEC = new NuXmvTokenType("CTLSPEC");
    IElementType DEFINE_KW = new NuXmvTokenType("DEFINE");
    IElementType DIV = new NuXmvTokenType("/");
    IElementType DOT = new NuXmvTokenType(".");
    IElementType EQUALITY = new NuXmvTokenType("=");
    IElementType EQUIVALENCE = new NuXmvTokenType("<->");
    IElementType ESAC = new NuXmvTokenType("esac");
    IElementType EXPONENTIAL_NUMBER = new NuXmvTokenType("EXPONENTIAL_NUMBER");
    IElementType EXTEND = new NuXmvTokenType("extend");
    IElementType FAIRNESS_KW = new NuXmvTokenType("FAIRNESS");
    IElementType FALSE = new NuXmvTokenType("FALSE");
    IElementType FLOAT_NUMBER = new NuXmvTokenType("FLOAT_NUMBER");
    IElementType FLOOR = new NuXmvTokenType("floor");
    IElementType FRACTIONAL_NUMBER = new NuXmvTokenType("FRACTIONAL_NUMBER");
    IElementType FROZENVAR = new NuXmvTokenType("FROZENVAR");
    IElementType FUN_KW = new NuXmvTokenType("FUN");
    IElementType GREATER = new NuXmvTokenType(">");
    IElementType GREATER_EQ = new NuXmvTokenType(">=");
    IElementType HEX_NUMBER = new NuXmvTokenType("HEX_NUMBER");
    IElementType IDENTIFIER = new NuXmvTokenType("identifier");
    IElementType IMPLICATION = new NuXmvTokenType("->");
    IElementType IN = new NuXmvTokenType("in");
    IElementType INIT_FUN = new NuXmvTokenType("init");
    IElementType INIT_KW = new NuXmvTokenType("INIT");
    IElementType INTEGER_NUMBER = new NuXmvTokenType("INTEGER_NUMBER");
    IElementType INTEGER_TYPE = new NuXmvTokenType("integer");
    IElementType INVARSPEC = new NuXmvTokenType("INVARSPEC");
    IElementType INVAR_KW = new NuXmvTokenType("INVAR");
    IElementType ISA_KW = new NuXmvTokenType("ISA");
    IElementType IVAR = new NuXmvTokenType("IVAR");
    IElementType JUSTICE_KW = new NuXmvTokenType("JUSTICE");
    IElementType LBRACE = new NuXmvTokenType("{");
    IElementType LBRACKET = new NuXmvTokenType("[");
    IElementType LESS = new NuXmvTokenType("<");
    IElementType LESS_EQ = new NuXmvTokenType("<=");
    IElementType LINE_COMMENT = new NuXmvTokenType("LINE_COMMENT");
    IElementType LPAREN = new NuXmvTokenType("(");
    IElementType LTLSPEC = new NuXmvTokenType("LTLSPEC");
    IElementType MAX_KW = new NuXmvTokenType("MAX");
    IElementType MINUS = new NuXmvTokenType("-");
    IElementType MIN_KW = new NuXmvTokenType("MIN");
    IElementType MIRROR_KW = new NuXmvTokenType("MIRROR");
    IElementType MOD = new NuXmvTokenType("mod");
    IElementType MODULE = new NuXmvTokenType("MODULE");
    IElementType MONONEG_KW = new NuXmvTokenType("MONONEG");
    IElementType MONOPOS_KW = new NuXmvTokenType("MONOPOS");
    IElementType MULT = new NuXmvTokenType("*");
    IElementType NAME_KW = new NuXmvTokenType("NAME");
    IElementType NEXT = new NuXmvTokenType("next");
    IElementType NOT = new NuXmvTokenType("!");
    IElementType NOT_EQUALITY = new NuXmvTokenType("!=");
    IElementType NOT_XOR = new NuXmvTokenType("xnor");
    IElementType OR = new NuXmvTokenType("|");
    IElementType PARSYNTH_KW = new NuXmvTokenType("PARSYNTH");
    IElementType PI = new NuXmvTokenType("PI");
    IElementType PLUS = new NuXmvTokenType("+");
    IElementType POSITIVE_INTEGER_NUMBER = new NuXmvTokenType("POSITIVE_INTEGER_NUMBER");
    IElementType PRED_KW = new NuXmvTokenType("PRED");
    IElementType QUESTION_MARK = new NuXmvTokenType("?");
    IElementType RANGE = new NuXmvTokenType("..");
    IElementType RBRACE = new NuXmvTokenType("}");
    IElementType RBRACKET = new NuXmvTokenType("]");
    IElementType REAL_TYPE = new NuXmvTokenType("real");
    IElementType RESIZE = new NuXmvTokenType("resize");
    IElementType RPAREN = new NuXmvTokenType(")");
    IElementType SAT_KW = new NuXmvTokenType("SAT");
    IElementType SELF = new NuXmvTokenType("self");
    IElementType SEMICOLON = new NuXmvTokenType(";");
    IElementType SHIFT_LEFT = new NuXmvTokenType("<<");
    IElementType SHIFT_RIGHT = new NuXmvTokenType(">>");
    IElementType SIGNED_WORD_TYPE = new NuXmvTokenType("signed word");
    IElementType SPEC = new NuXmvTokenType("SPEC");
    IElementType TIME = new NuXmvTokenType("TIME");
    IElementType TIME_DOMAIN_KW = new NuXmvTokenType("@TIME DOMAIN");
    IElementType TO_BOOL = new NuXmvTokenType("bool");
    IElementType TO_INT = new NuXmvTokenType("toint");
    IElementType TO_SIGNED = new NuXmvTokenType("signed");
    IElementType TO_UNSIGNED = new NuXmvTokenType("unsigned");
    IElementType TO_WORD1 = new NuXmvTokenType("word1");
    IElementType TRANS_KW = new NuXmvTokenType("TRANS");
    IElementType TRUE = new NuXmvTokenType("TRUE");
    IElementType UNION = new NuXmvTokenType("union");
    IElementType UNSIGNED_WORD_TYPE = new NuXmvTokenType("unsigned word");
    IElementType VALID_KW = new NuXmvTokenType("VALID");
    IElementType VAR = new NuXmvTokenType("VAR");
    IElementType WORD_TYPE = new NuXmvTokenType("word");
    IElementType XOR = new NuXmvTokenType("xor");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == AND_BASIC_EXPR) {
                return new NuXmvAndBasicExprImpl(node);
            } else if (type == ASSIGN_CONSTRAINT) {
                return new NuXmvAssignConstraintImpl(node);
            } else if (type == BASIC_EXPR_LIST) {
                return new NuXmvBasicExprListImpl(node);
            } else if (type == BINARY_LTL_EXPR) {
                return new NuXmvBinaryLtlExprImpl(node);
            } else if (type == BINARY_LTL_OP) {
                return new NuXmvBinaryLtlOpImpl(node);
            } else if (type == BOOLEAN_CONSTANT) {
                return new NuXmvBooleanConstantImpl(node);
            } else if (type == BUILT_IN_CONSTANT) {
                return new NuXmvBuiltInConstantImpl(node);
            } else if (type == CASE_BASIC_EXPR) {
                return new NuXmvCaseBasicExprImpl(node);
            } else if (type == COMPASSION_CONSTRAINT) {
                return new NuXmvCompassionConstraintImpl(node);
            } else if (type == COMPLEX_IDENTIFIER) {
                return new NuXmvComplexIdentifierImpl(node);
            } else if (type == COMPUTE_EXPR) {
                return new NuXmvComputeExprImpl(node);
            } else if (type == COMPUTE_SPECIFICATION) {
                return new NuXmvComputeSpecificationImpl(node);
            } else if (type == CONCAT_BASIC_EXPR) {
                return new NuXmvConcatBasicExprImpl(node);
            } else if (type == CONSTANTS_BODY) {
                return new NuXmvConstantsBodyImpl(node);
            } else if (type == CONSTANTS_DECLARATION) {
                return new NuXmvConstantsDeclarationImpl(node);
            } else if (type == CTL_SPECIFICATION) {
                return new NuXmvCtlSpecificationImpl(node);
            } else if (type == DEFINE_BODY) {
                return new NuXmvDefineBodyImpl(node);
            } else if (type == DEFINE_DECLARATION) {
                return new NuXmvDefineDeclarationImpl(node);
            } else if (type == DIV_BASIC_EXPR) {
                return new NuXmvDivBasicExprImpl(node);
            } else if (type == ENUMERATION_TYPE_BODY) {
                return new NuXmvEnumerationTypeBodyImpl(node);
            } else if (type == ENUMERATION_TYPE_VALUE) {
                return new NuXmvEnumerationTypeValueImpl(node);
            } else if (type == EQUALITY_BASIC_EXPR) {
                return new NuXmvEqualityBasicExprImpl(node);
            } else if (type == EQUIVALENCE_BASIC_EXPR) {
                return new NuXmvEquivalenceBasicExprImpl(node);
            } else if (type == FAIRNESS_CONSTRAINT) {
                return new NuXmvFairnessConstraintImpl(node);
            } else if (type == FROZEN_VAR_DECLARATION) {
                return new NuXmvFrozenVarDeclarationImpl(node);
            } else if (type == FUNCTION_ARG_TYPES_SPECIFIER) {
                return new NuXmvFunctionArgTypesSpecifierImpl(node);
            } else if (type == FUNCTION_CALL_BASIC_EXPR) {
                return new NuXmvFunctionCallBasicExprImpl(node);
            } else if (type == FUNCTION_DECLARATION) {
                return new NuXmvFunctionDeclarationImpl(node);
            } else if (type == FUNCTION_IDENTIFIER) {
                return new NuXmvFunctionIdentifierImpl(node);
            } else if (type == FUNCTION_NAME) {
                return new NuXmvFunctionNameImpl(node);
            } else if (type == FUNCTION_SPECIFICATION) {
                return new NuXmvFunctionSpecificationImpl(node);
            } else if (type == FUNCTION_TYPE_SPECIFIER) {
                return new NuXmvFunctionTypeSpecifierImpl(node);
            } else if (type == GREATER_BASIC_EXPR) {
                return new NuXmvGreaterBasicExprImpl(node);
            } else if (type == GREATER_EQ_BASIC_EXPR) {
                return new NuXmvGreaterEqBasicExprImpl(node);
            } else if (type == ID_LIST) {
                return new NuXmvIdListImpl(node);
            } else if (type == IMPLICATION_BASIC_EXPR) {
                return new NuXmvImplicationBasicExprImpl(node);
            } else if (type == INDEX_BASIC_EXPR) {
                return new NuXmvIndexBasicExprImpl(node);
            } else if (type == INIT_ASSIGN_EXPR) {
                return new NuXmvInitAssignExprImpl(node);
            } else if (type == INIT_CONSTRAINT) {
                return new NuXmvInitConstraintImpl(node);
            } else if (type == INVAR_CONSTRAINT) {
                return new NuXmvInvarConstraintImpl(node);
            } else if (type == INVAR_SPECIFICATION) {
                return new NuXmvInvarSpecificationImpl(node);
            } else if (type == IN_BASIC_EXPR) {
                return new NuXmvInBasicExprImpl(node);
            } else if (type == ISA_DECLARATION) {
                return new NuXmvIsaDeclarationImpl(node);
            } else if (type == IVAR_DECLARATION) {
                return new NuXmvIvarDeclarationImpl(node);
            } else if (type == JUSTICE_CONSTRAINT) {
                return new NuXmvJusticeConstraintImpl(node);
            } else if (type == LESS_BASIC_EXPR) {
                return new NuXmvLessBasicExprImpl(node);
            } else if (type == LESS_EQ_BASIC_EXPR) {
                return new NuXmvLessEqBasicExprImpl(node);
            } else if (type == LITERAL_BASIC_EXPR) {
                return new NuXmvLiteralBasicExprImpl(node);
            } else if (type == LOGICAL_NOT_BASIC_EXPR) {
                return new NuXmvLogicalNotBasicExprImpl(node);
            } else if (type == LTL_SPECIFICATION) {
                return new NuXmvLtlSpecificationImpl(node);
            } else if (type == MINUS_BASIC_EXPR) {
                return new NuXmvMinusBasicExprImpl(node);
            } else if (type == MIRROR_DECLARATION) {
                return new NuXmvMirrorDeclarationImpl(node);
            } else if (type == MODULE_BODY) {
                return new NuXmvModuleBodyImpl(node);
            } else if (type == MODULE_DECLARATION) {
                return new NuXmvModuleDeclarationImpl(node);
            } else if (type == MODULE_NAME) {
                return new NuXmvModuleNameImpl(node);
            } else if (type == MODULE_PARAMETERS) {
                return new NuXmvModuleParametersImpl(node);
            } else if (type == MODULE_TYPE_SPECIFIER) {
                return new NuXmvModuleTypeSpecifierImpl(node);
            } else if (type == MOD_BASIC_EXPR) {
                return new NuXmvModBasicExprImpl(node);
            } else if (type == MUL_BASIC_EXPR) {
                return new NuXmvMulBasicExprImpl(node);
            } else if (type == NEXT_ASSIGN_EXPR) {
                return new NuXmvNextAssignExprImpl(node);
            } else if (type == NOT_EQUALITY_BASIC_EXPR) {
                return new NuXmvNotEqualityBasicExprImpl(node);
            } else if (type == NOT_XOR_BASIC_EXPR) {
                return new NuXmvNotXorBasicExprImpl(node);
            } else if (type == NU_XMV_MODULE) {
                return new NuXmvNuXmvModuleImpl(node);
            } else if (type == OR_BASIC_EXPR) {
                return new NuXmvOrBasicExprImpl(node);
            } else if (type == PARAMETER_LIST) {
                return new NuXmvParameterListImpl(node);
            } else if (type == PARAMETER_SYNTH_PROBLEM) {
                return new NuXmvParameterSynthProblemImpl(node);
            } else if (type == PARAMETER_SYNTH_PROBLEM_DECLARATION) {
                return new NuXmvParameterSynthProblemDeclarationImpl(node);
            } else if (type == PARENTHESIS_BASIC_EXPR) {
                return new NuXmvParenthesisBasicExprImpl(node);
            } else if (type == PLUS_BASIC_EXPR) {
                return new NuXmvPlusBasicExprImpl(node);
            } else if (type == PRED_DECLARATION) {
                return new NuXmvPredDeclarationImpl(node);
            } else if (type == RANGE_CONSTANT) {
                return new NuXmvRangeConstantImpl(node);
            } else if (type == REAL_NUMBER) {
                return new NuXmvRealNumberImpl(node);
            } else if (type == REFERENCE_BASIC_EXPR) {
                return new NuXmvReferenceBasicExprImpl(node);
            } else if (type == REGULAR_CASE_BODY) {
                return new NuXmvRegularCaseBodyImpl(node);
            } else if (type == RT_CTL_EXPR) {
                return new NuXmvRtCtlExprImpl(node);
            } else if (type == SHIFT_LEFT_BASIC_EXPR) {
                return new NuXmvShiftLeftBasicExprImpl(node);
            } else if (type == SHIFT_RIGHT_BASIC_EXPR) {
                return new NuXmvShiftRightBasicExprImpl(node);
            } else if (type == SIMPLE_ASSIGN_EXPR) {
                return new NuXmvSimpleAssignExprImpl(node);
            } else if (type == SIMPLE_IDENTIFIER) {
                return new NuXmvSimpleIdentifierImpl(node);
            } else if (type == SIMPLE_TYPE_SPECIFIER) {
                return new NuXmvSimpleTypeSpecifierImpl(node);
            } else if (type == SINGLE_ASSIGN_CONSTRAINT) {
                return new NuXmvSingleAssignConstraintImpl(node);
            } else if (type == SINGLE_IVAR_DECLARATION) {
                return new NuXmvSingleIvarDeclarationImpl(node);
            } else if (type == SINGLE_VAR_DECLARATION) {
                return new NuXmvSingleVarDeclarationImpl(node);
            } else if (type == SYNTH_OPT) {
                return new NuXmvSynthOptImpl(node);
            } else if (type == SYNTH_OPTS) {
                return new NuXmvSynthOptsImpl(node);
            } else if (type == TERNARY_BASIC_EXPR) {
                return new NuXmvTernaryBasicExprImpl(node);
            } else if (type == TIMED_LTL_EXPR) {
                return new NuXmvTimedLtlExprImpl(node);
            } else if (type == TIME_LTL_OP) {
                return new NuXmvTimeLtlOpImpl(node);
            } else if (type == TRANS_CONSTRAINT) {
                return new NuXmvTransConstraintImpl(node);
            } else if (type == TYPE_SPECIFIER) {
                return new NuXmvTypeSpecifierImpl(node);
            } else if (type == UNARY_CTL_EXPR) {
                return new NuXmvUnaryCtlExprImpl(node);
            } else if (type == UNARY_CTL_OP) {
                return new NuXmvUnaryCtlOpImpl(node);
            } else if (type == UNARY_LTL_EXPR) {
                return new NuXmvUnaryLtlExprImpl(node);
            } else if (type == UNARY_LTL_OP) {
                return new NuXmvUnaryLtlOpImpl(node);
            } else if (type == UNARY_MINUS_BASIC_EXPR) {
                return new NuXmvUnaryMinusBasicExprImpl(node);
            } else if (type == UNION_BASIC_EXPR) {
                return new NuXmvUnionBasicExprImpl(node);
            } else if (type == UNTIL_CTL_EXPR) {
                return new NuXmvUntilCtlExprImpl(node);
            } else if (type == UNTIL_CTL_OP) {
                return new NuXmvUntilCtlOpImpl(node);
            } else if (type == VARIABLE_IDENTIFIER) {
                return new NuXmvVariableIdentifierImpl(node);
            } else if (type == VAR_DECLARATION) {
                return new NuXmvVarDeclarationImpl(node);
            } else if (type == VAR_NAME) {
                return new NuXmvVarNameImpl(node);
            } else if (type == WHOLE_NUMBER) {
                return new NuXmvWholeNumberImpl(node);
            } else if (type == WORD_CONSTANT) {
                return new NuXmvWordConstantImpl(node);
            } else if (type == XOR_BASIC_EXPR) {
                return new NuXmvXorBasicExprImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
