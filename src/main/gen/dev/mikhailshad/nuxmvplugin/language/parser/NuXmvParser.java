// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserUtil.*;
import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class NuXmvParser implements PsiParser, LightPsiParser {

    public ASTNode parse(IElementType t, PsiBuilder b) {
        parseLight(t, b);
        return b.getTreeBuilt();
    }

    public void parseLight(IElementType t, PsiBuilder b) {
        boolean r;
        b = adapt_builder_(t, b, this, EXTENDS_SETS_);
        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        r = parse_root_(t, b);
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
    }

    protected boolean parse_root_(IElementType t, PsiBuilder b) {
        return parse_root_(t, b, 0);
    }

    static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
        return NuXmvFile(b, l + 1);
    }

    public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[]{
            create_token_set_(AND_BASIC_EXPR, BINARY_LTL_EXPR, CASE_BASIC_EXPR, COMPUTE_EXPR,
                    CONCAT_BASIC_EXPR, DIV_BASIC_EXPR, EQUALITY_BASIC_EXPR, EQUIVALENCE_BASIC_EXPR,
                    EXPR, FUNCTION_CALL_BASIC_EXPR, GREATER_BASIC_EXPR, GREATER_EQ_BASIC_EXPR,
                    IMPLICATION_BASIC_EXPR, INDEX_BASIC_EXPR, INIT_ASSIGN_EXPR, IN_BASIC_EXPR,
                    LESS_BASIC_EXPR, LESS_EQ_BASIC_EXPR, LITERAL_BASIC_EXPR, LOGICAL_NOT_BASIC_EXPR,
                    MINUS_BASIC_EXPR, MOD_BASIC_EXPR, MUL_BASIC_EXPR, NEXT_ASSIGN_EXPR,
                    NOT_EQUALITY_BASIC_EXPR, NOT_XOR_BASIC_EXPR, OR_BASIC_EXPR, PARENTHESIS_BASIC_EXPR,
                    PLUS_BASIC_EXPR, REFERENCE_BASIC_EXPR, RT_CTL_EXPR, SHIFT_LEFT_BASIC_EXPR,
                    SHIFT_RIGHT_BASIC_EXPR, SIMPLE_ASSIGN_EXPR, TERNARY_BASIC_EXPR, TIMED_LTL_EXPR,
                    UNARY_CTL_EXPR, UNARY_LTL_EXPR, UNARY_MINUS_BASIC_EXPR, UNION_BASIC_EXPR,
                    UNTIL_CTL_EXPR, XOR_BASIC_EXPR),
    };

    /* ********************************************************** */
    // ASSIGN_KW SingleAssignConstraint+
    public static boolean AssignConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "AssignConstraint")) return false;
        if (!nextTokenIs(b, ASSIGN_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, ASSIGN_KW);
        r = r && AssignConstraint_1(b, l + 1);
        exit_section_(b, m, ASSIGN_CONSTRAINT, r);
        return r;
    }

    // SingleAssignConstraint+
    private static boolean AssignConstraint_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "AssignConstraint_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = SingleAssignConstraint(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!SingleAssignConstraint(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "AssignConstraint_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // InitAssignExpr | NextAssignExpr | SimpleAssignExpr
    static boolean AssignExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "AssignExpr")) return false;
        boolean r;
        r = InitAssignExpr(b, l + 1);
        if (!r) r = NextAssignExpr(b, l + 1);
        if (!r) r = SimpleAssignExpr(b, l + 1);
        return r;
    }

    /* ********************************************************** */
    // SimpleIdentifier | SELF
    static boolean BaseIdentifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BaseIdentifier")) return false;
        if (!nextTokenIs(b, "", IDENTIFIER, SELF)) return false;
        boolean r;
        r = SimpleIdentifier(b, l + 1);
        if (!r) r = consumeToken(b, SELF);
        return r;
    }

    /* ********************************************************** */
    // Expr (COMMA Expr)*
    public static boolean BasicExprList(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BasicExprList")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BASIC_EXPR_LIST, "<basic expr list>");
        r = Expr(b, l + 1, -1);
        r = r && BasicExprList_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (COMMA Expr)*
    private static boolean BasicExprList_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BasicExprList_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!BasicExprList_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "BasicExprList_1", c)) break;
        }
        return true;
    }

    // COMMA Expr
    private static boolean BasicExprList_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BasicExprList_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // LTL_AT_LAST | LTL_AT_NEXT | LTL_TRIGGERED | LTL_SINCE | LTL_RELEASES | TL_UNTIL
    public static boolean BinaryLtlOp(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BinaryLtlOp")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BINARY_LTL_OP, "<binary ltl op>");
        r = consumeToken(b, LTL_AT_LAST);
        if (!r) r = consumeToken(b, LTL_AT_NEXT);
        if (!r) r = consumeToken(b, LTL_TRIGGERED);
        if (!r) r = consumeToken(b, LTL_SINCE);
        if (!r) r = consumeToken(b, LTL_RELEASES);
        if (!r) r = consumeToken(b, TL_UNTIL);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TRUE | FALSE
    public static boolean BooleanConstant(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BooleanConstant")) return false;
        if (!nextTokenIs(b, "<boolean constant>", FALSE, TRUE)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_CONSTANT, "<boolean constant>");
        r = consumeToken(b, TRUE);
        if (!r) r = consumeToken(b, FALSE);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TIME | PI
    public static boolean BuiltInConstant(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "BuiltInConstant")) return false;
        if (!nextTokenIs(b, "<built in constant>", PI, TIME)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BUILT_IN_CONSTANT, "<built in constant>");
        r = consumeToken(b, TIME);
        if (!r) r = consumeToken(b, PI);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // COMPASSION_KW LPAREN Expr COMMA Expr RPAREN SEMICOLON?
    public static boolean CompassionConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CompassionConstraint")) return false;
        if (!nextTokenIs(b, COMPASSION_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, COMPASSION_KW, LPAREN);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, COMMA);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, RPAREN);
        r = r && CompassionConstraint_6(b, l + 1);
        exit_section_(b, m, COMPASSION_CONSTRAINT, r);
        return r;
    }

    // SEMICOLON?
    private static boolean CompassionConstraint_6(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CompassionConstraint_6")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // BaseIdentifier (DOT SimpleIdentifier) * (LBRACKET Expr RBRACKET)?
    public static boolean ComplexIdentifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComplexIdentifier")) return false;
        if (!nextTokenIs(b, "<complex identifier>", IDENTIFIER, SELF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, COMPLEX_IDENTIFIER, "<complex identifier>");
        r = BaseIdentifier(b, l + 1);
        r = r && ComplexIdentifier_1(b, l + 1);
        r = r && ComplexIdentifier_2(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (DOT SimpleIdentifier) *
    private static boolean ComplexIdentifier_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComplexIdentifier_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!ComplexIdentifier_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "ComplexIdentifier_1", c)) break;
        }
        return true;
    }

    // DOT SimpleIdentifier
    private static boolean ComplexIdentifier_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComplexIdentifier_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, DOT);
        r = r && SimpleIdentifier(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // (LBRACKET Expr RBRACKET)?
    private static boolean ComplexIdentifier_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComplexIdentifier_2")) return false;
        ComplexIdentifier_2_0(b, l + 1);
        return true;
    }

    // LBRACKET Expr RBRACKET
    private static boolean ComplexIdentifier_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComplexIdentifier_2_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LBRACKET);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // (MIN_KW | MAX_KW) LBRACKET RtCtlExpr COMMA RtCtlExpr RBRACKET
    public static boolean ComputeExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeExpr")) return false;
        if (!nextTokenIs(b, "<compute expr>", MAX_KW, MIN_KW)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, COMPUTE_EXPR, "<compute expr>");
        r = ComputeExpr_0(b, l + 1);
        r = r && consumeToken(b, LBRACKET);
        r = r && RtCtlExpr(b, l + 1);
        r = r && consumeToken(b, COMMA);
        r = r && RtCtlExpr(b, l + 1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // MIN_KW | MAX_KW
    private static boolean ComputeExpr_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeExpr_0")) return false;
        boolean r;
        r = consumeToken(b, MIN_KW);
        if (!r) r = consumeToken(b, MAX_KW);
        return r;
    }

    /* ********************************************************** */
    // COMPUTE_KW ComputeExpr SEMICOLON?
    //     | COMPUTE_KW NAME_KW identifier ASSIGN ComplexIdentifier SEMICOLON?
    public static boolean ComputeSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeSpecification")) return false;
        if (!nextTokenIs(b, COMPUTE_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = ComputeSpecification_0(b, l + 1);
        if (!r) r = ComputeSpecification_1(b, l + 1);
        exit_section_(b, m, COMPUTE_SPECIFICATION, r);
        return r;
    }

    // COMPUTE_KW ComputeExpr SEMICOLON?
    private static boolean ComputeSpecification_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeSpecification_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMPUTE_KW);
        r = r && ComputeExpr(b, l + 1);
        r = r && ComputeSpecification_0_2(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // SEMICOLON?
    private static boolean ComputeSpecification_0_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeSpecification_0_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    // COMPUTE_KW NAME_KW identifier ASSIGN ComplexIdentifier SEMICOLON?
    private static boolean ComputeSpecification_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeSpecification_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, COMPUTE_KW, NAME_KW, IDENTIFIER, ASSIGN);
        r = r && ComplexIdentifier(b, l + 1);
        r = r && ComputeSpecification_1_5(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // SEMICOLON?
    private static boolean ComputeSpecification_1_5(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ComputeSpecification_1_5")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // ComplexIdentifier (COMMA ComplexIdentifier)*
    public static boolean ConstantsBody(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ConstantsBody")) return false;
        if (!nextTokenIs(b, "<constants body>", IDENTIFIER, SELF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONSTANTS_BODY, "<constants body>");
        r = ComplexIdentifier(b, l + 1);
        r = r && ConstantsBody_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (COMMA ComplexIdentifier)*
    private static boolean ConstantsBody_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ConstantsBody_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!ConstantsBody_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "ConstantsBody_1", c)) break;
        }
        return true;
    }

    // COMMA ComplexIdentifier
    private static boolean ConstantsBody_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ConstantsBody_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && ComplexIdentifier(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // CONSTANTS_KW ConstantsBody SEMICOLON
    public static boolean ConstantsDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ConstantsDeclaration")) return false;
        if (!nextTokenIs(b, CONSTANTS_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, CONSTANTS_KW);
        r = r && ConstantsBody(b, l + 1);
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, m, CONSTANTS_DECLARATION, r);
        return r;
    }

    /* ********************************************************** */
    // NAME_KW SimpleIdentifier ASSIGN
    static boolean CtlNamedSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CtlNamedSpecification")) return false;
        if (!nextTokenIs(b, NAME_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, NAME_KW);
        r = r && SimpleIdentifier(b, l + 1);
        r = r && consumeToken(b, ASSIGN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // CTLSPEC | SPEC
    static boolean CtlSpecKeyWord(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CtlSpecKeyWord")) return false;
        if (!nextTokenIs(b, "", CTLSPEC, SPEC)) return false;
        boolean r;
        r = consumeToken(b, CTLSPEC);
        if (!r) r = consumeToken(b, SPEC);
        return r;
    }

    /* ********************************************************** */
    // CtlSpecKeyWord CtlNamedSpecification? Expr SEMICOLON?
    public static boolean CtlSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CtlSpecification")) return false;
        if (!nextTokenIs(b, "<ctl specification>", CTLSPEC, SPEC)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, CTL_SPECIFICATION, "<ctl specification>");
        r = CtlSpecKeyWord(b, l + 1);
        p = r; // pin = 1
        r = r && report_error_(b, CtlSpecification_1(b, l + 1));
        r = p && report_error_(b, Expr(b, l + 1, -1)) && r;
        r = p && CtlSpecification_3(b, l + 1) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // CtlNamedSpecification?
    private static boolean CtlSpecification_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CtlSpecification_1")) return false;
        CtlNamedSpecification(b, l + 1);
        return true;
    }

    // SEMICOLON?
    private static boolean CtlSpecification_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CtlSpecification_3")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // ComplexIdentifier ASSIGN Expr SEMICOLON
    public static boolean DefineBody(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "DefineBody")) return false;
        if (!nextTokenIs(b, "<define body>", IDENTIFIER, SELF)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, DEFINE_BODY, "<define body>");
        r = ComplexIdentifier(b, l + 1);
        r = r && consumeToken(b, ASSIGN);
        r = r && Expr(b, l + 1, -1);
        p = r; // pin = 3
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    /* ********************************************************** */
    // DEFINE_KW DefineBody+
    public static boolean DefineDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "DefineDeclaration")) return false;
        if (!nextTokenIs(b, DEFINE_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, DEFINE_KW);
        r = r && DefineDeclaration_1(b, l + 1);
        exit_section_(b, m, DEFINE_DECLARATION, r);
        return r;
    }

    // DefineBody+
    private static boolean DefineDeclaration_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "DefineDeclaration_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = DefineBody(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!DefineBody(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "DefineDeclaration_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // EnumerationTypeValue (COMMA EnumerationTypeValue)*
    public static boolean EnumerationTypeBody(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "EnumerationTypeBody")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, ENUMERATION_TYPE_BODY, "<enumeration type body>");
        r = EnumerationTypeValue(b, l + 1);
        r = r && EnumerationTypeBody_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (COMMA EnumerationTypeValue)*
    private static boolean EnumerationTypeBody_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "EnumerationTypeBody_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!EnumerationTypeBody_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "EnumerationTypeBody_1", c)) break;
        }
        return true;
    }

    // COMMA EnumerationTypeValue
    private static boolean EnumerationTypeBody_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "EnumerationTypeBody_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && EnumerationTypeValue(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // WholeNumber | ComplexIdentifier
    public static boolean EnumerationTypeValue(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "EnumerationTypeValue")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, ENUMERATION_TYPE_VALUE, "<enumeration type value>");
        r = WholeNumber(b, l + 1);
        if (!r) r = ComplexIdentifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // FAIRNESS_KW Expr SEMICOLON?
    public static boolean FairnessConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FairnessConstraint")) return false;
        if (!nextTokenIs(b, FAIRNESS_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, FAIRNESS_KW);
        r = r && Expr(b, l + 1, -1);
        r = r && FairnessConstraint_2(b, l + 1);
        exit_section_(b, m, FAIRNESS_CONSTRAINT, r);
        return r;
    }

    // SEMICOLON?
    private static boolean FairnessConstraint_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FairnessConstraint_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // FROZENVAR SingleIvarDeclaration
    public static boolean FrozenVarDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FrozenVarDeclaration")) return false;
        if (!nextTokenIs(b, FROZENVAR)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, FROZEN_VAR_DECLARATION, null);
        r = consumeToken(b, FROZENVAR);
        p = r; // pin = 1
        r = r && SingleIvarDeclaration(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    /* ********************************************************** */
    // SimpleTypeSpecifier (MULT SimpleTypeSpecifier)*
    public static boolean FunctionArgTypesSpecifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionArgTypesSpecifier")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_ARG_TYPES_SPECIFIER, "<function arg types specifier>");
        r = SimpleTypeSpecifier(b, l + 1);
        r = r && FunctionArgTypesSpecifier_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (MULT SimpleTypeSpecifier)*
    private static boolean FunctionArgTypesSpecifier_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionArgTypesSpecifier_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!FunctionArgTypesSpecifier_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "FunctionArgTypesSpecifier_1", c)) break;
        }
        return true;
    }

    // MULT SimpleTypeSpecifier
    private static boolean FunctionArgTypesSpecifier_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionArgTypesSpecifier_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, MULT);
        r = r && SimpleTypeSpecifier(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // FUN_KW FunctionSpecification+
    public static boolean FunctionDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionDeclaration")) return false;
        if (!nextTokenIs(b, FUN_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, FUN_KW);
        r = r && FunctionDeclaration_1(b, l + 1);
        exit_section_(b, m, FUNCTION_DECLARATION, r);
        return r;
    }

    // FunctionSpecification+
    private static boolean FunctionDeclaration_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionDeclaration_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = FunctionSpecification(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!FunctionSpecification(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "FunctionDeclaration_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // INIT_FUN|NEXT_FUN|ABS_FUN|MAX_FUN|MIN_FUN|SIN_FUN|COS_FUN
    //     |EXP_FUN|TAN_FUN|LN_FUN|POW_FUN|ASIN_FUN|ACOS_FUN|ATAN_FUN|SQRT_FUN
    //     |TO_WORD1_FUN|TO_BOOL_FUN|TO_INT_FUN|COUNT_FUN|SWCONST_FUN|UWCONST_FUN
    //     |TO_SIGNED_FUN|TO_UNSIGNED_FUN|SIZEOF_FUN|FLOOR_FUN|EXTEND_FUN|RESIZE_FUN
    //     |TO_SIGNED_WORD_FUN|TO_UNSIGNED_WORD_FUN|READ_FUN|WRITE_FUN|CONSTARRAY_FUN|TYPE_OF_FUN
    //     | SimpleIdentifier
    public static boolean FunctionIdentifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionIdentifier")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_IDENTIFIER, "<function identifier>");
        r = consumeToken(b, INIT_FUN);
        if (!r) r = consumeToken(b, NEXT_FUN);
        if (!r) r = consumeToken(b, ABS_FUN);
        if (!r) r = consumeToken(b, MAX_FUN);
        if (!r) r = consumeToken(b, MIN_FUN);
        if (!r) r = consumeToken(b, SIN_FUN);
        if (!r) r = consumeToken(b, COS_FUN);
        if (!r) r = consumeToken(b, EXP_FUN);
        if (!r) r = consumeToken(b, TAN_FUN);
        if (!r) r = consumeToken(b, LN_FUN);
        if (!r) r = consumeToken(b, POW_FUN);
        if (!r) r = consumeToken(b, ASIN_FUN);
        if (!r) r = consumeToken(b, ACOS_FUN);
        if (!r) r = consumeToken(b, ATAN_FUN);
        if (!r) r = consumeToken(b, SQRT_FUN);
        if (!r) r = consumeToken(b, TO_WORD1_FUN);
        if (!r) r = consumeToken(b, TO_BOOL_FUN);
        if (!r) r = consumeToken(b, TO_INT_FUN);
        if (!r) r = consumeToken(b, COUNT_FUN);
        if (!r) r = consumeToken(b, SWCONST_FUN);
        if (!r) r = consumeToken(b, UWCONST_FUN);
        if (!r) r = consumeToken(b, TO_SIGNED_FUN);
        if (!r) r = consumeToken(b, TO_UNSIGNED_FUN);
        if (!r) r = consumeToken(b, SIZEOF_FUN);
        if (!r) r = consumeToken(b, FLOOR_FUN);
        if (!r) r = consumeToken(b, EXTEND_FUN);
        if (!r) r = consumeToken(b, RESIZE_FUN);
        if (!r) r = consumeToken(b, TO_SIGNED_WORD_FUN);
        if (!r) r = consumeToken(b, TO_UNSIGNED_WORD_FUN);
        if (!r) r = consumeToken(b, READ_FUN);
        if (!r) r = consumeToken(b, WRITE_FUN);
        if (!r) r = consumeToken(b, CONSTARRAY_FUN);
        if (!r) r = consumeToken(b, TYPE_OF_FUN);
        if (!r) r = SimpleIdentifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // ComplexIdentifier
    public static boolean FunctionName(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionName")) return false;
        if (!nextTokenIs(b, "<function name>", IDENTIFIER, SELF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_NAME, "<function name>");
        r = ComplexIdentifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // FunctionName COLON FunctionTypeSpecifier SEMICOLON
    public static boolean FunctionSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionSpecification")) return false;
        if (!nextTokenIs(b, "<function specification>", IDENTIFIER, SELF)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_SPECIFICATION, "<function specification>");
        r = FunctionName(b, l + 1);
        r = r && consumeToken(b, COLON);
        r = r && FunctionTypeSpecifier(b, l + 1);
        p = r; // pin = 3
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    /* ********************************************************** */
    // FunctionArgTypesSpecifier IMPLICATION SimpleTypeSpecifier
    public static boolean FunctionTypeSpecifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionTypeSpecifier")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_TYPE_SPECIFIER, "<function type specifier>");
        r = FunctionArgTypesSpecifier(b, l + 1);
        r = r && consumeToken(b, IMPLICATION);
        r = r && SimpleTypeSpecifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // identifier (COMMA identifier)*
    public static boolean IdList(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IdList")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        r = r && IdList_1(b, l + 1);
        exit_section_(b, m, ID_LIST, r);
        return r;
    }

    // (COMMA identifier)*
    private static boolean IdList_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IdList_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!IdList_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "IdList_1", c)) break;
        }
        return true;
    }

    // COMMA identifier
    private static boolean IdList_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IdList_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, COMMA, IDENTIFIER);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // INIT_FUN LPAREN ComplexIdentifier RPAREN ASSIGN Expr
    public static boolean InitAssignExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InitAssignExpr")) return false;
        if (!nextTokenIs(b, INIT_FUN)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, INIT_FUN, LPAREN);
        r = r && ComplexIdentifier(b, l + 1);
        r = r && consumeTokens(b, 0, RPAREN, ASSIGN);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, m, INIT_ASSIGN_EXPR, r);
        return r;
    }

    /* ********************************************************** */
    // INIT_KW Expr SEMICOLON?
    public static boolean InitConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InitConstraint")) return false;
        if (!nextTokenIs(b, INIT_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, INIT_KW);
        r = r && Expr(b, l + 1, -1);
        r = r && InitConstraint_2(b, l + 1);
        exit_section_(b, m, INIT_CONSTRAINT, r);
        return r;
    }

    // SEMICOLON?
    private static boolean InitConstraint_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InitConstraint_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // INVAR_KW Expr (IMPLICATION Expr)? SEMICOLON?
    public static boolean InvarConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarConstraint")) return false;
        if (!nextTokenIs(b, INVAR_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, INVAR_KW);
        r = r && Expr(b, l + 1, -1);
        r = r && InvarConstraint_2(b, l + 1);
        r = r && InvarConstraint_3(b, l + 1);
        exit_section_(b, m, INVAR_CONSTRAINT, r);
        return r;
    }

    // (IMPLICATION Expr)?
    private static boolean InvarConstraint_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarConstraint_2")) return false;
        InvarConstraint_2_0(b, l + 1);
        return true;
    }

    // IMPLICATION Expr
    private static boolean InvarConstraint_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarConstraint_2_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IMPLICATION);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, m, null, r);
        return r;
    }

    // SEMICOLON?
    private static boolean InvarConstraint_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarConstraint_3")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // INVARSPEC NamedInvarSpecification? Expr SEMICOLON?
    public static boolean InvarSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarSpecification")) return false;
        if (!nextTokenIs(b, INVARSPEC)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, INVAR_SPECIFICATION, null);
        r = consumeToken(b, INVARSPEC);
        p = r; // pin = 1
        r = r && report_error_(b, InvarSpecification_1(b, l + 1));
        r = p && report_error_(b, Expr(b, l + 1, -1)) && r;
        r = p && InvarSpecification_3(b, l + 1) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // NamedInvarSpecification?
    private static boolean InvarSpecification_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarSpecification_1")) return false;
        NamedInvarSpecification(b, l + 1);
        return true;
    }

    // SEMICOLON?
    private static boolean InvarSpecification_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "InvarSpecification_3")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // ISA_KW IDENTIFIER
    public static boolean IsaDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IsaDeclaration")) return false;
        if (!nextTokenIs(b, ISA_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, ISA_KW, IDENTIFIER);
        exit_section_(b, m, ISA_DECLARATION, r);
        return r;
    }

    /* ********************************************************** */
    // IVAR SingleIvarDeclaration+
    public static boolean IvarDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IvarDeclaration")) return false;
        if (!nextTokenIs(b, IVAR)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, IVAR_DECLARATION, null);
        r = consumeToken(b, IVAR);
        p = r; // pin = 1
        r = r && IvarDeclaration_1(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // SingleIvarDeclaration+
    private static boolean IvarDeclaration_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IvarDeclaration_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = SingleIvarDeclaration(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!SingleIvarDeclaration(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "IvarDeclaration_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // JUSTICE_KW Expr SEMICOLON?
    public static boolean JusticeConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "JusticeConstraint")) return false;
        if (!nextTokenIs(b, JUSTICE_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, JUSTICE_KW);
        r = r && Expr(b, l + 1, -1);
        r = r && JusticeConstraint_2(b, l + 1);
        exit_section_(b, m, JUSTICE_CONSTRAINT, r);
        return r;
    }

    // SEMICOLON?
    private static boolean JusticeConstraint_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "JusticeConstraint_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // NAME_KW SimpleIdentifier ASSIGN
    static boolean LtlNamedSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "LtlNamedSpecification")) return false;
        if (!nextTokenIs(b, NAME_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, NAME_KW);
        r = r && SimpleIdentifier(b, l + 1);
        r = r && consumeToken(b, ASSIGN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // LTLSPEC LtlNamedSpecification? Expr SEMICOLON?
    public static boolean LtlSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "LtlSpecification")) return false;
        if (!nextTokenIs(b, LTLSPEC)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, LTL_SPECIFICATION, null);
        r = consumeToken(b, LTLSPEC);
        p = r; // pin = 1
        r = r && report_error_(b, LtlSpecification_1(b, l + 1));
        r = p && report_error_(b, Expr(b, l + 1, -1)) && r;
        r = p && LtlSpecification_3(b, l + 1) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // LtlNamedSpecification?
    private static boolean LtlSpecification_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "LtlSpecification_1")) return false;
        LtlNamedSpecification(b, l + 1);
        return true;
    }

    // SEMICOLON?
    private static boolean LtlSpecification_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "LtlSpecification_3")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // MIRROR_KW VariableIdentifier SEMICOLON?
    public static boolean MirrorDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "MirrorDeclaration")) return false;
        if (!nextTokenIs(b, MIRROR_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, MIRROR_KW);
        r = r && VariableIdentifier(b, l + 1);
        r = r && MirrorDeclaration_2(b, l + 1);
        exit_section_(b, m, MIRROR_DECLARATION, r);
        return r;
    }

    // SEMICOLON?
    private static boolean MirrorDeclaration_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "MirrorDeclaration_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // ModuleElement+
    public static boolean ModuleBody(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleBody")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, MODULE_BODY, "<module body>");
        r = ModuleElement(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!ModuleElement(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "ModuleBody", c)) break;
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // MODULE ModuleName (LPAREN ModuleParameters RPAREN)?
    public static boolean ModuleDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleDeclaration")) return false;
        if (!nextTokenIs(b, MODULE)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, MODULE_DECLARATION, null);
        r = consumeToken(b, MODULE);
        p = r; // pin = 1
        r = r && report_error_(b, ModuleName(b, l + 1));
        r = p && ModuleDeclaration_2(b, l + 1) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // (LPAREN ModuleParameters RPAREN)?
    private static boolean ModuleDeclaration_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleDeclaration_2")) return false;
        ModuleDeclaration_2_0(b, l + 1);
        return true;
    }

    // LPAREN ModuleParameters RPAREN
    private static boolean ModuleDeclaration_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleDeclaration_2_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LPAREN);
        r = r && ModuleParameters(b, l + 1);
        r = r && consumeToken(b, RPAREN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // VarDeclaration
    //     | IvarDeclaration
    //     | FrozenVarDeclaration
    //     | FunctionDeclaration
    //     | DefineDeclaration
    //     | ConstantsDeclaration
    //     | AssignConstraint
    //     | TransConstraint
    //     | InitConstraint
    //     | InvarConstraint
    //     | FairnessConstraint
    //     | JusticeConstraint
    //     | CompassionConstraint
    //     | CtlSpecification
    //     | LtlSpecification
    //     | InvarSpecification
    //     | ComputeSpecification
    //     | ParameterSynthProblemDeclaration
    //     | IsaDeclaration
    //     | PredDeclaration
    //     | MirrorDeclaration
    static boolean ModuleElement(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleElement")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_);
        r = VarDeclaration(b, l + 1);
        if (!r) r = IvarDeclaration(b, l + 1);
        if (!r) r = FrozenVarDeclaration(b, l + 1);
        if (!r) r = FunctionDeclaration(b, l + 1);
        if (!r) r = DefineDeclaration(b, l + 1);
        if (!r) r = ConstantsDeclaration(b, l + 1);
        if (!r) r = AssignConstraint(b, l + 1);
        if (!r) r = TransConstraint(b, l + 1);
        if (!r) r = InitConstraint(b, l + 1);
        if (!r) r = InvarConstraint(b, l + 1);
        if (!r) r = FairnessConstraint(b, l + 1);
        if (!r) r = JusticeConstraint(b, l + 1);
        if (!r) r = CompassionConstraint(b, l + 1);
        if (!r) r = CtlSpecification(b, l + 1);
        if (!r) r = LtlSpecification(b, l + 1);
        if (!r) r = InvarSpecification(b, l + 1);
        if (!r) r = ComputeSpecification(b, l + 1);
        if (!r) r = ParameterSynthProblemDeclaration(b, l + 1);
        if (!r) r = IsaDeclaration(b, l + 1);
        if (!r) r = PredDeclaration(b, l + 1);
        if (!r) r = MirrorDeclaration(b, l + 1);
        exit_section_(b, l, m, r, false, NuXmvParser::module_element_recover);
        return r;
    }

    /* ********************************************************** */
    // IDENTIFIER
    public static boolean ModuleName(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleName")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        exit_section_(b, m, MODULE_NAME, r);
        return r;
    }

    /* ********************************************************** */
    // IDENTIFIER (COMMA IDENTIFIER)*
    public static boolean ModuleParameters(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleParameters")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        r = r && ModuleParameters_1(b, l + 1);
        exit_section_(b, m, MODULE_PARAMETERS, r);
        return r;
    }

    // (COMMA IDENTIFIER)*
    private static boolean ModuleParameters_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleParameters_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!ModuleParameters_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "ModuleParameters_1", c)) break;
        }
        return true;
    }

    // COMMA IDENTIFIER
    private static boolean ModuleParameters_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleParameters_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, COMMA, IDENTIFIER);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // SimpleIdentifier ParameterList
    public static boolean ModuleTypeSpecifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ModuleTypeSpecifier")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = SimpleIdentifier(b, l + 1);
        r = r && ParameterList(b, l + 1);
        exit_section_(b, m, MODULE_TYPE_SPECIFIER, r);
        return r;
    }

    /* ********************************************************** */
    // NAME_KW SimpleIdentifier ASSIGN
    static boolean NamedInvarSpecification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NamedInvarSpecification")) return false;
        if (!nextTokenIs(b, NAME_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, NAME_KW);
        r = r && SimpleIdentifier(b, l + 1);
        r = r && consumeToken(b, ASSIGN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // NEXT_FUN LPAREN ComplexIdentifier RPAREN ASSIGN Expr
    public static boolean NextAssignExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NextAssignExpr")) return false;
        if (!nextTokenIs(b, NEXT_FUN)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, NEXT_FUN, LPAREN);
        r = r && ComplexIdentifier(b, l + 1);
        r = r && consumeTokens(b, 0, RPAREN, ASSIGN);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, m, NEXT_ASSIGN_EXPR, r);
        return r;
    }

    /* ********************************************************** */
    // !<<eof>> NuXmvModule+
    static boolean NuXmvFile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NuXmvFile")) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_);
        r = NuXmvFile_0(b, l + 1);
        p = r; // pin = 1
        r = r && NuXmvFile_1(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // !<<eof>>
    private static boolean NuXmvFile_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NuXmvFile_0")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NOT_);
        r = !eof(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // NuXmvModule+
    private static boolean NuXmvFile_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NuXmvFile_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = NuXmvModule(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!NuXmvModule(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "NuXmvFile_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // ModuleDeclaration ModuleBody?
    public static boolean NuXmvModule(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NuXmvModule")) return false;
        if (!nextTokenIs(b, MODULE)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = ModuleDeclaration(b, l + 1);
        r = r && NuXmvModule_1(b, l + 1);
        exit_section_(b, m, NU_XMV_MODULE, r);
        return r;
    }

    // ModuleBody?
    private static boolean NuXmvModule_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "NuXmvModule_1")) return false;
        ModuleBody(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // LPAREN [Expr (COMMA Expr)*] RPAREN
    public static boolean ParameterList(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterList")) return false;
        if (!nextTokenIs(b, LPAREN)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LPAREN);
        r = r && ParameterList_1(b, l + 1);
        r = r && consumeToken(b, RPAREN);
        exit_section_(b, m, PARAMETER_LIST, r);
        return r;
    }

    // [Expr (COMMA Expr)*]
    private static boolean ParameterList_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterList_1")) return false;
        ParameterList_1_0(b, l + 1);
        return true;
    }

    // Expr (COMMA Expr)*
    private static boolean ParameterList_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterList_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = Expr(b, l + 1, -1);
        r = r && ParameterList_1_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // (COMMA Expr)*
    private static boolean ParameterList_1_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterList_1_0_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!ParameterList_1_0_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "ParameterList_1_0_1", c)) break;
        }
        return true;
    }

    // COMMA Expr
    private static boolean ParameterList_1_0_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterList_1_0_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // identifier ASSIGN LBRACE IdList OR [VALID_KW | SAT_KW] Expr SynthOpts RBRACE
    public static boolean ParameterSynthProblem(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterSynthProblem")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, IDENTIFIER, ASSIGN, LBRACE);
        r = r && IdList(b, l + 1);
        r = r && consumeToken(b, OR);
        r = r && ParameterSynthProblem_5(b, l + 1);
        r = r && Expr(b, l + 1, -1);
        r = r && SynthOpts(b, l + 1);
        r = r && consumeToken(b, RBRACE);
        exit_section_(b, m, PARAMETER_SYNTH_PROBLEM, r);
        return r;
    }

    // [VALID_KW | SAT_KW]
    private static boolean ParameterSynthProblem_5(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterSynthProblem_5")) return false;
        ParameterSynthProblem_5_0(b, l + 1);
        return true;
    }

    // VALID_KW | SAT_KW
    private static boolean ParameterSynthProblem_5_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterSynthProblem_5_0")) return false;
        boolean r;
        r = consumeToken(b, VALID_KW);
        if (!r) r = consumeToken(b, SAT_KW);
        return r;
    }

    /* ********************************************************** */
    // PARSYNTH_KW ParameterSynthProblem SEMICOLON?
    public static boolean ParameterSynthProblemDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterSynthProblemDeclaration")) return false;
        if (!nextTokenIs(b, PARSYNTH_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, PARSYNTH_KW);
        r = r && ParameterSynthProblem(b, l + 1);
        r = r && ParameterSynthProblemDeclaration_2(b, l + 1);
        exit_section_(b, m, PARAMETER_SYNTH_PROBLEM_DECLARATION, r);
        return r;
    }

    // SEMICOLON?
    private static boolean ParameterSynthProblemDeclaration_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParameterSynthProblemDeclaration_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // PRED_KW Expr SEMICOLON?
    //     | PRED_KW LESS Expr GREATER ASSIGN Expr SEMICOLON?
    public static boolean PredDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PredDeclaration")) return false;
        if (!nextTokenIs(b, PRED_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = PredDeclaration_0(b, l + 1);
        if (!r) r = PredDeclaration_1(b, l + 1);
        exit_section_(b, m, PRED_DECLARATION, r);
        return r;
    }

    // PRED_KW Expr SEMICOLON?
    private static boolean PredDeclaration_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PredDeclaration_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, PRED_KW);
        r = r && Expr(b, l + 1, -1);
        r = r && PredDeclaration_0_2(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // SEMICOLON?
    private static boolean PredDeclaration_0_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PredDeclaration_0_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    // PRED_KW LESS Expr GREATER ASSIGN Expr SEMICOLON?
    private static boolean PredDeclaration_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PredDeclaration_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, PRED_KW, LESS);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeTokens(b, 0, GREATER, ASSIGN);
        r = r && Expr(b, l + 1, -1);
        r = r && PredDeclaration_1_6(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // SEMICOLON?
    private static boolean PredDeclaration_1_6(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PredDeclaration_1_6")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // WholeNumber RANGE WholeNumber
    public static boolean RangeConstant(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RangeConstant")) return false;
        if (!nextTokenIs(b, "<range constant>", INTEGER_NUMBER, POSITIVE_INTEGER_NUMBER)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, RANGE_CONSTANT, "<range constant>");
        r = WholeNumber(b, l + 1);
        r = r && consumeToken(b, RANGE);
        r = r && WholeNumber(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // FLOAT_NUMBER | FRACTIONAL_NUMBER | EXPONENTIAL_NUMBER
    public static boolean RealNumber(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RealNumber")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, REAL_NUMBER, "<real number>");
        r = consumeToken(b, FLOAT_NUMBER);
        if (!r) r = consumeToken(b, FRACTIONAL_NUMBER);
        if (!r) r = consumeToken(b, EXPONENTIAL_NUMBER);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // Expr COLON Expr SEMICOLON
    public static boolean RegularCaseBody(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RegularCaseBody")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, REGULAR_CASE_BODY, "<regular case body>");
        r = Expr(b, l + 1, -1);
        r = r && consumeToken(b, COLON);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // Expr
    //     | (RTCTL_EBF | RTCTL_ABF | RTCTL_EBG | RTCTL_ABG) INTEGER_NUMBER RANGE INTEGER_NUMBER RtCtlExpr
    //     | (CTL_FORALL | CTL_EXISTS) LBRACKET RtCtlExpr RTCTL_BU RtCtlExpr RBRACKET
    public static boolean RtCtlExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RtCtlExpr")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _COLLAPSE_, RT_CTL_EXPR, "<rt ctl expr>");
        r = Expr(b, l + 1, -1);
        if (!r) r = RtCtlExpr_1(b, l + 1);
        if (!r) r = RtCtlExpr_2(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (RTCTL_EBF | RTCTL_ABF | RTCTL_EBG | RTCTL_ABG) INTEGER_NUMBER RANGE INTEGER_NUMBER RtCtlExpr
    private static boolean RtCtlExpr_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RtCtlExpr_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = RtCtlExpr_1_0(b, l + 1);
        r = r && consumeTokens(b, 0, INTEGER_NUMBER, RANGE, INTEGER_NUMBER);
        r = r && RtCtlExpr(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // RTCTL_EBF | RTCTL_ABF | RTCTL_EBG | RTCTL_ABG
    private static boolean RtCtlExpr_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RtCtlExpr_1_0")) return false;
        boolean r;
        r = consumeToken(b, RTCTL_EBF);
        if (!r) r = consumeToken(b, RTCTL_ABF);
        if (!r) r = consumeToken(b, RTCTL_EBG);
        if (!r) r = consumeToken(b, RTCTL_ABG);
        return r;
    }

    // (CTL_FORALL | CTL_EXISTS) LBRACKET RtCtlExpr RTCTL_BU RtCtlExpr RBRACKET
    private static boolean RtCtlExpr_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RtCtlExpr_2")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = RtCtlExpr_2_0(b, l + 1);
        r = r && consumeToken(b, LBRACKET);
        r = r && RtCtlExpr(b, l + 1);
        r = r && consumeToken(b, RTCTL_BU);
        r = r && RtCtlExpr(b, l + 1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    // CTL_FORALL | CTL_EXISTS
    private static boolean RtCtlExpr_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "RtCtlExpr_2_0")) return false;
        boolean r;
        r = consumeToken(b, CTL_FORALL);
        if (!r) r = consumeToken(b, CTL_EXISTS);
        return r;
    }

    /* ********************************************************** */
    // ComplexIdentifier ASSIGN Expr
    public static boolean SimpleAssignExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleAssignExpr")) return false;
        if (!nextTokenIs(b, "<simple assign expr>", IDENTIFIER, SELF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SIMPLE_ASSIGN_EXPR, "<simple assign expr>");
        r = ComplexIdentifier(b, l + 1);
        r = r && consumeToken(b, ASSIGN);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // IDENTIFIER
    public static boolean SimpleIdentifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleIdentifier")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        exit_section_(b, m, SIMPLE_IDENTIFIER, r);
        return r;
    }

    /* ********************************************************** */
    // BOOLEAN_TYPE
    //     | WORD_TYPE LBRACKET WholeNumber RBRACKET
    //     | UNSIGNED_WORD_TYPE LBRACKET WholeNumber RBRACKET
    //     | SIGNED_WORD_TYPE LBRACKET WholeNumber RBRACKET
    //     | INTEGER_TYPE
    //     | REAL_TYPE
    //     | CLOCK_TYPE
    //     | LBRACE EnumerationTypeBody RBRACE
    //     | WholeNumber RANGE WholeNumber
    //     | ARRAY_TYPE WholeNumber RANGE WholeNumber ARRAY_OF SimpleTypeSpecifier
    public static boolean SimpleTypeSpecifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SIMPLE_TYPE_SPECIFIER, "<simple type specifier>");
        r = consumeToken(b, BOOLEAN_TYPE);
        if (!r) r = SimpleTypeSpecifier_1(b, l + 1);
        if (!r) r = SimpleTypeSpecifier_2(b, l + 1);
        if (!r) r = SimpleTypeSpecifier_3(b, l + 1);
        if (!r) r = consumeToken(b, INTEGER_TYPE);
        if (!r) r = consumeToken(b, REAL_TYPE);
        if (!r) r = consumeToken(b, CLOCK_TYPE);
        if (!r) r = SimpleTypeSpecifier_7(b, l + 1);
        if (!r) r = SimpleTypeSpecifier_8(b, l + 1);
        if (!r) r = SimpleTypeSpecifier_9(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // WORD_TYPE LBRACKET WholeNumber RBRACKET
    private static boolean SimpleTypeSpecifier_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, WORD_TYPE, LBRACKET);
        r = r && WholeNumber(b, l + 1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    // UNSIGNED_WORD_TYPE LBRACKET WholeNumber RBRACKET
    private static boolean SimpleTypeSpecifier_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier_2")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, UNSIGNED_WORD_TYPE, LBRACKET);
        r = r && WholeNumber(b, l + 1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    // SIGNED_WORD_TYPE LBRACKET WholeNumber RBRACKET
    private static boolean SimpleTypeSpecifier_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier_3")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, SIGNED_WORD_TYPE, LBRACKET);
        r = r && WholeNumber(b, l + 1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    // LBRACE EnumerationTypeBody RBRACE
    private static boolean SimpleTypeSpecifier_7(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier_7")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LBRACE);
        r = r && EnumerationTypeBody(b, l + 1);
        r = r && consumeToken(b, RBRACE);
        exit_section_(b, m, null, r);
        return r;
    }

    // WholeNumber RANGE WholeNumber
    private static boolean SimpleTypeSpecifier_8(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier_8")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = WholeNumber(b, l + 1);
        r = r && consumeToken(b, RANGE);
        r = r && WholeNumber(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // ARRAY_TYPE WholeNumber RANGE WholeNumber ARRAY_OF SimpleTypeSpecifier
    private static boolean SimpleTypeSpecifier_9(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SimpleTypeSpecifier_9")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, ARRAY_TYPE);
        r = r && WholeNumber(b, l + 1);
        r = r && consumeToken(b, RANGE);
        r = r && WholeNumber(b, l + 1);
        r = r && consumeToken(b, ARRAY_OF);
        r = r && SimpleTypeSpecifier(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // AssignExpr SEMICOLON
    public static boolean SingleAssignConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SingleAssignConstraint")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SINGLE_ASSIGN_CONSTRAINT, "<single assign constraint>");
        r = AssignExpr(b, l + 1);
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // VarName COLON SimpleTypeSpecifier SEMICOLON
    public static boolean SingleIvarDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SingleIvarDeclaration")) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, SINGLE_IVAR_DECLARATION, "<single ivar declaration>");
        r = VarName(b, l + 1);
        r = r && consumeToken(b, COLON);
        r = r && SimpleTypeSpecifier(b, l + 1);
        p = r; // pin = 3
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    /* ********************************************************** */
    // VarName COLON TypeSpecifier SEMICOLON
    public static boolean SingleVarDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SingleVarDeclaration")) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, SINGLE_VAR_DECLARATION, "<single var declaration>");
        r = VarName(b, l + 1);
        r = r && consumeToken(b, COLON);
        r = r && TypeSpecifier(b, l + 1);
        p = r; // pin = 3
        r = r && consumeToken(b, SEMICOLON);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    /* ********************************************************** */
    // MONONEG_KW
    //     | MONOPOS_KW
    //     | (MIN_KW | MAX_KW) LPAREN Expr RPAREN
    public static boolean SynthOpt(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SynthOpt")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SYNTH_OPT, "<synth opt>");
        r = consumeToken(b, MONONEG_KW);
        if (!r) r = consumeToken(b, MONOPOS_KW);
        if (!r) r = SynthOpt_2(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (MIN_KW | MAX_KW) LPAREN Expr RPAREN
    private static boolean SynthOpt_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SynthOpt_2")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = SynthOpt_2_0(b, l + 1);
        r = r && consumeToken(b, LPAREN);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, RPAREN);
        exit_section_(b, m, null, r);
        return r;
    }

    // MIN_KW | MAX_KW
    private static boolean SynthOpt_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SynthOpt_2_0")) return false;
        boolean r;
        r = consumeToken(b, MIN_KW);
        if (!r) r = consumeToken(b, MAX_KW);
        return r;
    }

    /* ********************************************************** */
    // SynthOpt (COMMA SynthOpt)*
    public static boolean SynthOpts(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SynthOpts")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SYNTH_OPTS, "<synth opts>");
        r = SynthOpt(b, l + 1);
        r = r && SynthOpts_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (COMMA SynthOpt)*
    private static boolean SynthOpts_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SynthOpts_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!SynthOpts_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "SynthOpts_1", c)) break;
        }
        return true;
    }

    // COMMA SynthOpt
    private static boolean SynthOpts_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "SynthOpts_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && SynthOpt(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // LTL_TIME_SINCE | LTL_TIME_UNTIL
    public static boolean TimeLtlOp(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TimeLtlOp")) return false;
        if (!nextTokenIs(b, "<time ltl op>", LTL_TIME_SINCE, LTL_TIME_UNTIL)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TIME_LTL_OP, "<time ltl op>");
        r = consumeToken(b, LTL_TIME_SINCE);
        if (!r) r = consumeToken(b, LTL_TIME_UNTIL);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TRANS_KW Expr SEMICOLON?
    public static boolean TransConstraint(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TransConstraint")) return false;
        if (!nextTokenIs(b, TRANS_KW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TRANS_KW);
        r = r && Expr(b, l + 1, -1);
        r = r && TransConstraint_2(b, l + 1);
        exit_section_(b, m, TRANS_CONSTRAINT, r);
        return r;
    }

    // SEMICOLON?
    private static boolean TransConstraint_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TransConstraint_2")) return false;
        consumeToken(b, SEMICOLON);
        return true;
    }

    /* ********************************************************** */
    // SimpleTypeSpecifier | ModuleTypeSpecifier
    public static boolean TypeSpecifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TypeSpecifier")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TYPE_SPECIFIER, "<type specifier>");
        r = SimpleTypeSpecifier(b, l + 1);
        if (!r) r = ModuleTypeSpecifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // CTL_EXISTS_GLOBALLY | CTL_EXISTS_NEXT | CTL_EXISTS_FINALLY
    //     | CTL_FORALL_GLOBALLY | CTL_FORALL_NEXT | CTL_FORALL_FINALLY
    static boolean UnaryCtlOp(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UnaryCtlOp")) return false;
        boolean r;
        r = consumeToken(b, CTL_EXISTS_GLOBALLY);
        if (!r) r = consumeToken(b, CTL_EXISTS_NEXT);
        if (!r) r = consumeToken(b, CTL_EXISTS_FINALLY);
        if (!r) r = consumeToken(b, CTL_FORALL_GLOBALLY);
        if (!r) r = consumeToken(b, CTL_FORALL_NEXT);
        if (!r) r = consumeToken(b, CTL_FORALL_FINALLY);
        return r;
    }

    /* ********************************************************** */
    // LTL_ONCE | LTL_HISTORICALLY | LTL_NOT_PREVIOUS| LTL_PREVIOUS | LTL_FINALLY | LTL_GLOBALLY | LTL_NEXT
    public static boolean UnaryLtlOp(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UnaryLtlOp")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, UNARY_LTL_OP, "<unary ltl op>");
        r = consumeToken(b, LTL_ONCE);
        if (!r) r = consumeToken(b, LTL_HISTORICALLY);
        if (!r) r = consumeToken(b, LTL_NOT_PREVIOUS);
        if (!r) r = consumeToken(b, LTL_PREVIOUS);
        if (!r) r = consumeToken(b, LTL_FINALLY);
        if (!r) r = consumeToken(b, LTL_GLOBALLY);
        if (!r) r = consumeToken(b, LTL_NEXT);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // CTL_EXISTS | CTL_FORALL
    static boolean UntilCtlOp(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UntilCtlOp")) return false;
        if (!nextTokenIs(b, "", CTL_EXISTS, CTL_FORALL)) return false;
        boolean r;
        r = consumeToken(b, CTL_EXISTS);
        if (!r) r = consumeToken(b, CTL_FORALL);
        return r;
    }

    /* ********************************************************** */
    // VAR SingleVarDeclaration+
    public static boolean VarDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "VarDeclaration")) return false;
        if (!nextTokenIs(b, VAR)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, VAR_DECLARATION, null);
        r = consumeToken(b, VAR);
        p = r; // pin = 1
        r = r && VarDeclaration_1(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // SingleVarDeclaration+
    private static boolean VarDeclaration_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "VarDeclaration_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = SingleVarDeclaration(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!SingleVarDeclaration(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "VarDeclaration_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // ComplexIdentifier
    //     | "\"" ComplexIdentifier "\""
    public static boolean VarName(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "VarName")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, VAR_NAME, "<var name>");
        r = ComplexIdentifier(b, l + 1);
        if (!r) r = VarName_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // "\"" ComplexIdentifier "\""
    private static boolean VarName_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "VarName_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, "\"");
        r = r && ComplexIdentifier(b, l + 1);
        r = r && consumeToken(b, "\"");
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // ComplexIdentifier
    public static boolean VariableIdentifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "VariableIdentifier")) return false;
        if (!nextTokenIs(b, "<variable identifier>", IDENTIFIER, SELF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, VARIABLE_IDENTIFIER, "<variable identifier>");
        r = ComplexIdentifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // POSITIVE_INTEGER_NUMBER | INTEGER_NUMBER
    public static boolean WholeNumber(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WholeNumber")) return false;
        if (!nextTokenIs(b, "<whole number>", INTEGER_NUMBER, POSITIVE_INTEGER_NUMBER)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, WHOLE_NUMBER, "<whole number>");
        r = consumeToken(b, POSITIVE_INTEGER_NUMBER);
        if (!r) r = consumeToken(b, INTEGER_NUMBER);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // "0" ("u"|"s")? ["b" | "B"] WholeNumber? "_" HEX_NUMBER
    public static boolean WordConstant(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WordConstant")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, WORD_CONSTANT, "<word constant>");
        r = consumeToken(b, "0");
        r = r && WordConstant_1(b, l + 1);
        r = r && WordConstant_2(b, l + 1);
        r = r && WordConstant_3(b, l + 1);
        r = r && consumeToken(b, "_");
        r = r && consumeToken(b, HEX_NUMBER);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // ("u"|"s")?
    private static boolean WordConstant_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WordConstant_1")) return false;
        WordConstant_1_0(b, l + 1);
        return true;
    }

    // "u"|"s"
    private static boolean WordConstant_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WordConstant_1_0")) return false;
        boolean r;
        r = consumeToken(b, "u");
        if (!r) r = consumeToken(b, "s");
        return r;
    }

    // ["b" | "B"]
    private static boolean WordConstant_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WordConstant_2")) return false;
        WordConstant_2_0(b, l + 1);
        return true;
    }

    // "b" | "B"
    private static boolean WordConstant_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WordConstant_2_0")) return false;
        boolean r;
        r = consumeToken(b, "b");
        if (!r) r = consumeToken(b, "B");
        return r;
    }

    // WholeNumber?
    private static boolean WordConstant_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "WordConstant_3")) return false;
        WholeNumber(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // !(
    //     SEMICOLON | RBRACKET | RPAREN
    //     | VAR | IVAR | FROZENVAR
    //     | DEFINE_KW | CONSTANTS_KW | ASSIGN_KW | TRANS_KW | INIT_KW | INVAR_KW | COMPASSION_KW | FAIRNESS_KW | JUSTICE_KW
    //     | LTLSPEC | CTLSPEC | SPEC | INVARSPEC
    //     | COMPUTE_KW | PARSYNTH_KW
    //     | ISA_KW | PRED_KW | MIRROR_KW
    //     | MODULE
    //     )
    static boolean module_element_recover(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "module_element_recover")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NOT_);
        r = !module_element_recover_0(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // SEMICOLON | RBRACKET | RPAREN
    //     | VAR | IVAR | FROZENVAR
    //     | DEFINE_KW | CONSTANTS_KW | ASSIGN_KW | TRANS_KW | INIT_KW | INVAR_KW | COMPASSION_KW | FAIRNESS_KW | JUSTICE_KW
    //     | LTLSPEC | CTLSPEC | SPEC | INVARSPEC
    //     | COMPUTE_KW | PARSYNTH_KW
    //     | ISA_KW | PRED_KW | MIRROR_KW
    //     | MODULE
    private static boolean module_element_recover_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "module_element_recover_0")) return false;
        boolean r;
        r = consumeToken(b, SEMICOLON);
        if (!r) r = consumeToken(b, RBRACKET);
        if (!r) r = consumeToken(b, RPAREN);
        if (!r) r = consumeToken(b, VAR);
        if (!r) r = consumeToken(b, IVAR);
        if (!r) r = consumeToken(b, FROZENVAR);
        if (!r) r = consumeToken(b, DEFINE_KW);
        if (!r) r = consumeToken(b, CONSTANTS_KW);
        if (!r) r = consumeToken(b, ASSIGN_KW);
        if (!r) r = consumeToken(b, TRANS_KW);
        if (!r) r = consumeToken(b, INIT_KW);
        if (!r) r = consumeToken(b, INVAR_KW);
        if (!r) r = consumeToken(b, COMPASSION_KW);
        if (!r) r = consumeToken(b, FAIRNESS_KW);
        if (!r) r = consumeToken(b, JUSTICE_KW);
        if (!r) r = consumeToken(b, LTLSPEC);
        if (!r) r = consumeToken(b, CTLSPEC);
        if (!r) r = consumeToken(b, SPEC);
        if (!r) r = consumeToken(b, INVARSPEC);
        if (!r) r = consumeToken(b, COMPUTE_KW);
        if (!r) r = consumeToken(b, PARSYNTH_KW);
        if (!r) r = consumeToken(b, ISA_KW);
        if (!r) r = consumeToken(b, PRED_KW);
        if (!r) r = consumeToken(b, MIRROR_KW);
        if (!r) r = consumeToken(b, MODULE);
        return r;
    }

    /* ********************************************************** */
    // Expression root: Expr
    // Operator priority table:
    // 0: BINARY(ImplicationBasicExpr)
    // 1: BINARY(EquivalenceBasicExpr)
    // 2: BINARY(TernaryBasicExpr)
    // 3: BINARY(OrBasicExpr) BINARY(XorBasicExpr) BINARY(NotXorBasicExpr)
    // 4: BINARY(AndBasicExpr)
    // 5: BINARY(LessBasicExpr) BINARY(GreaterBasicExpr) BINARY(LessEqBasicExpr) BINARY(GreaterEqBasicExpr)
    //    BINARY(EqualityBasicExpr) BINARY(NotEqualityBasicExpr)
    // 6: BINARY(InBasicExpr)
    // 7: BINARY(UnionBasicExpr)
    // 8: BINARY(ShiftLeftBasicExpr) BINARY(ShiftRightBasicExpr)
    // 9: BINARY(PlusBasicExpr) BINARY(MinusBasicExpr)
    // 10: BINARY(MulBasicExpr) BINARY(DivBasicExpr) BINARY(ModBasicExpr)
    // 11: BINARY(ConcatBasicExpr)
    // 12: PREFIX(UnaryMinusBasicExpr)
    // 13: PREFIX(LogicalNotBasicExpr)
    // 14: POSTFIX(IndexBasicExpr)
    // 15: ATOM(CaseBasicExpr)
    // 16: ATOM(FunctionCallBasicExpr)
    // 17: PREFIX(UnaryCtlExpr) ATOM(UntilCtlExpr)
    // 18: PREFIX(UnaryLtlExpr) BINARY(BinaryLtlExpr) PREFIX(TimedLtlExpr)
    // 19: ATOM(ReferenceBasicExpr) ATOM(LiteralBasicExpr) PREFIX(ParenthesisBasicExpr)
    public static boolean Expr(PsiBuilder b, int l, int g) {
        if (!recursion_guard_(b, l, "Expr")) return false;
        addVariant(b, "<expr>");
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, "<expr>");
        r = UnaryMinusBasicExpr(b, l + 1);
        if (!r) r = LogicalNotBasicExpr(b, l + 1);
        if (!r) r = CaseBasicExpr(b, l + 1);
        if (!r) r = FunctionCallBasicExpr(b, l + 1);
        if (!r) r = UnaryCtlExpr(b, l + 1);
        if (!r) r = UntilCtlExpr(b, l + 1);
        if (!r) r = UnaryLtlExpr(b, l + 1);
        if (!r) r = TimedLtlExpr(b, l + 1);
        if (!r) r = ReferenceBasicExpr(b, l + 1);
        if (!r) r = LiteralBasicExpr(b, l + 1);
        if (!r) r = ParenthesisBasicExpr(b, l + 1);
        p = r;
        r = r && Expr_0(b, l + 1, g);
        exit_section_(b, l, m, null, r, p, null);
        return r || p;
    }

    public static boolean Expr_0(PsiBuilder b, int l, int g) {
        if (!recursion_guard_(b, l, "Expr_0")) return false;
        boolean r = true;
        while (true) {
            Marker m = enter_section_(b, l, _LEFT_, null);
            if (g < 0 && consumeTokenSmart(b, IMPLICATION)) {
                r = Expr(b, l, -1);
                exit_section_(b, l, m, IMPLICATION_BASIC_EXPR, r, true, null);
            } else if (g < 1 && consumeTokenSmart(b, EQUIVALENCE)) {
                r = Expr(b, l, 1);
                exit_section_(b, l, m, EQUIVALENCE_BASIC_EXPR, r, true, null);
            } else if (g < 2 && consumeTokenSmart(b, QUESTION_MARK)) {
                r = report_error_(b, Expr(b, l, 2));
                r = TernaryBasicExpr_1(b, l + 1) && r;
                exit_section_(b, l, m, TERNARY_BASIC_EXPR, r, true, null);
            } else if (g < 3 && consumeTokenSmart(b, OR)) {
                r = Expr(b, l, 3);
                exit_section_(b, l, m, OR_BASIC_EXPR, r, true, null);
            } else if (g < 3 && consumeTokenSmart(b, XOR)) {
                r = Expr(b, l, 3);
                exit_section_(b, l, m, XOR_BASIC_EXPR, r, true, null);
            } else if (g < 3 && consumeTokenSmart(b, NOT_XOR)) {
                r = Expr(b, l, 3);
                exit_section_(b, l, m, NOT_XOR_BASIC_EXPR, r, true, null);
            } else if (g < 4 && consumeTokenSmart(b, AND)) {
                r = Expr(b, l, 4);
                exit_section_(b, l, m, AND_BASIC_EXPR, r, true, null);
            } else if (g < 5 && consumeTokenSmart(b, LESS)) {
                r = Expr(b, l, 5);
                exit_section_(b, l, m, LESS_BASIC_EXPR, r, true, null);
            } else if (g < 5 && consumeTokenSmart(b, GREATER)) {
                r = Expr(b, l, 5);
                exit_section_(b, l, m, GREATER_BASIC_EXPR, r, true, null);
            } else if (g < 5 && consumeTokenSmart(b, LESS_EQ)) {
                r = Expr(b, l, 5);
                exit_section_(b, l, m, LESS_EQ_BASIC_EXPR, r, true, null);
            } else if (g < 5 && consumeTokenSmart(b, GREATER_EQ)) {
                r = Expr(b, l, 5);
                exit_section_(b, l, m, GREATER_EQ_BASIC_EXPR, r, true, null);
            } else if (g < 5 && consumeTokenSmart(b, EQUALITY)) {
                r = Expr(b, l, 5);
                exit_section_(b, l, m, EQUALITY_BASIC_EXPR, r, true, null);
            } else if (g < 5 && consumeTokenSmart(b, NOT_EQUALITY)) {
                r = Expr(b, l, 5);
                exit_section_(b, l, m, NOT_EQUALITY_BASIC_EXPR, r, true, null);
            } else if (g < 6 && consumeTokenSmart(b, IN)) {
                r = Expr(b, l, 6);
                exit_section_(b, l, m, IN_BASIC_EXPR, r, true, null);
            } else if (g < 7 && consumeTokenSmart(b, UNION)) {
                r = Expr(b, l, 7);
                exit_section_(b, l, m, UNION_BASIC_EXPR, r, true, null);
            } else if (g < 8 && consumeTokenSmart(b, SHIFT_LEFT)) {
                r = Expr(b, l, 8);
                exit_section_(b, l, m, SHIFT_LEFT_BASIC_EXPR, r, true, null);
            } else if (g < 8 && consumeTokenSmart(b, SHIFT_RIGHT)) {
                r = Expr(b, l, 8);
                exit_section_(b, l, m, SHIFT_RIGHT_BASIC_EXPR, r, true, null);
            } else if (g < 9 && consumeTokenSmart(b, PLUS)) {
                r = Expr(b, l, 9);
                exit_section_(b, l, m, PLUS_BASIC_EXPR, r, true, null);
            } else if (g < 9 && consumeTokenSmart(b, MINUS)) {
                r = Expr(b, l, 9);
                exit_section_(b, l, m, MINUS_BASIC_EXPR, r, true, null);
            } else if (g < 10 && consumeTokenSmart(b, MULT)) {
                r = Expr(b, l, 10);
                exit_section_(b, l, m, MUL_BASIC_EXPR, r, true, null);
            } else if (g < 10 && consumeTokenSmart(b, DIV)) {
                r = Expr(b, l, 10);
                exit_section_(b, l, m, DIV_BASIC_EXPR, r, true, null);
            } else if (g < 10 && consumeTokenSmart(b, MOD)) {
                r = Expr(b, l, 10);
                exit_section_(b, l, m, MOD_BASIC_EXPR, r, true, null);
            } else if (g < 11 && consumeTokenSmart(b, CONCAT)) {
                r = Expr(b, l, 11);
                exit_section_(b, l, m, CONCAT_BASIC_EXPR, r, true, null);
            } else if (g < 14 && IndexBasicExpr_0(b, l + 1)) {
                r = true;
                exit_section_(b, l, m, INDEX_BASIC_EXPR, r, true, null);
            } else if (g < 18 && BinaryLtlOp(b, l + 1)) {
                r = Expr(b, l, 18);
                exit_section_(b, l, m, BINARY_LTL_EXPR, r, true, null);
            } else {
                exit_section_(b, l, m, null, false, false, null);
                break;
            }
        }
        return r;
    }

    // COLON Expr
    private static boolean TernaryBasicExpr_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TernaryBasicExpr_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COLON);
        r = r && Expr(b, l + 1, -1);
        exit_section_(b, m, null, r);
        return r;
    }

    public static boolean UnaryMinusBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UnaryMinusBasicExpr")) return false;
        if (!nextTokenIsSmart(b, MINUS)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, null);
        r = consumeTokenSmart(b, MINUS);
        p = r;
        r = p && Expr(b, l, 12);
        exit_section_(b, l, m, UNARY_MINUS_BASIC_EXPR, r, p, null);
        return r || p;
    }

    public static boolean LogicalNotBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "LogicalNotBasicExpr")) return false;
        if (!nextTokenIsSmart(b, NOT)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, null);
        r = consumeTokenSmart(b, NOT);
        p = r;
        r = p && Expr(b, l, 13);
        exit_section_(b, l, m, LOGICAL_NOT_BASIC_EXPR, r, p, null);
        return r || p;
    }

    // LBRACKET WholeNumber (SEMICOLON WholeNumber)? RBRACKET
    private static boolean IndexBasicExpr_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IndexBasicExpr_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokenSmart(b, LBRACKET);
        r = r && WholeNumber(b, l + 1);
        r = r && IndexBasicExpr_0_2(b, l + 1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    // (SEMICOLON WholeNumber)?
    private static boolean IndexBasicExpr_0_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IndexBasicExpr_0_2")) return false;
        IndexBasicExpr_0_2_0(b, l + 1);
        return true;
    }

    // SEMICOLON WholeNumber
    private static boolean IndexBasicExpr_0_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "IndexBasicExpr_0_2_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokenSmart(b, SEMICOLON);
        r = r && WholeNumber(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // CASE RegularCaseBody+ ESAC
    public static boolean CaseBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CaseBasicExpr")) return false;
        if (!nextTokenIsSmart(b, CASE)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokenSmart(b, CASE);
        r = r && CaseBasicExpr_1(b, l + 1);
        r = r && consumeToken(b, ESAC);
        exit_section_(b, m, CASE_BASIC_EXPR, r);
        return r;
    }

    // RegularCaseBody+
    private static boolean CaseBasicExpr_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "CaseBasicExpr_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = RegularCaseBody(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!RegularCaseBody(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "CaseBasicExpr_1", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    // FunctionIdentifier LPAREN BasicExprList RPAREN
    public static boolean FunctionCallBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "FunctionCallBasicExpr")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_CALL_BASIC_EXPR, "<function call basic expr>");
        r = FunctionIdentifier(b, l + 1);
        r = r && consumeToken(b, LPAREN);
        r = r && BasicExprList(b, l + 1);
        r = r && consumeToken(b, RPAREN);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    public static boolean UnaryCtlExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UnaryCtlExpr")) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, null);
        r = UnaryCtlOp(b, l + 1);
        p = r;
        r = p && Expr(b, l, 17);
        exit_section_(b, l, m, UNARY_CTL_EXPR, r, p, null);
        return r || p;
    }

    // UntilCtlOp LBRACKET Expr TL_UNTIL Expr RBRACKET
    public static boolean UntilCtlExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UntilCtlExpr")) return false;
        if (!nextTokenIsSmart(b, CTL_EXISTS, CTL_FORALL)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, UNTIL_CTL_EXPR, "<until ctl expr>");
        r = UntilCtlOp(b, l + 1);
        r = r && consumeToken(b, LBRACKET);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, TL_UNTIL);
        r = r && Expr(b, l + 1, -1);
        r = r && consumeToken(b, RBRACKET);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    public static boolean UnaryLtlExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "UnaryLtlExpr")) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, null);
        r = UnaryLtlOp(b, l + 1);
        p = r;
        r = p && Expr(b, l, 18);
        exit_section_(b, l, m, UNARY_LTL_EXPR, r, p, null);
        return r || p;
    }

    public static boolean TimedLtlExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TimedLtlExpr")) return false;
        if (!nextTokenIsSmart(b, LTL_TIME_SINCE, LTL_TIME_UNTIL)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, null);
        r = TimedLtlExpr_0(b, l + 1);
        p = r;
        r = p && Expr(b, l, 18);
        r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
        exit_section_(b, l, m, TIMED_LTL_EXPR, r, p, null);
        return r || p;
    }

    // TimeLtlOp LPAREN
    private static boolean TimedLtlExpr_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "TimedLtlExpr_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = TimeLtlOp(b, l + 1);
        r = r && consumeToken(b, LPAREN);
        exit_section_(b, m, null, r);
        return r;
    }

    // ComplexIdentifier | SimpleIdentifier
    public static boolean ReferenceBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ReferenceBasicExpr")) return false;
        if (!nextTokenIsSmart(b, IDENTIFIER, SELF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, REFERENCE_BASIC_EXPR, "<reference basic expr>");
        r = ComplexIdentifier(b, l + 1);
        if (!r) r = SimpleIdentifier(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // BooleanConstant
    //     | WholeNumber
    //     | RealNumber
    //     | WordConstant
    //     | RangeConstant
    //     | BuiltInConstant
    public static boolean LiteralBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "LiteralBasicExpr")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, LITERAL_BASIC_EXPR, "<literal basic expr>");
        r = BooleanConstant(b, l + 1);
        if (!r) r = WholeNumber(b, l + 1);
        if (!r) r = RealNumber(b, l + 1);
        if (!r) r = WordConstant(b, l + 1);
        if (!r) r = RangeConstant(b, l + 1);
        if (!r) r = BuiltInConstant(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    public static boolean ParenthesisBasicExpr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ParenthesisBasicExpr")) return false;
        if (!nextTokenIsSmart(b, LPAREN)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, null);
        r = consumeTokenSmart(b, LPAREN);
        p = r;
        r = p && Expr(b, l, -1);
        r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
        exit_section_(b, l, m, PARENTHESIS_BASIC_EXPR, r, p, null);
        return r || p;
    }

}
