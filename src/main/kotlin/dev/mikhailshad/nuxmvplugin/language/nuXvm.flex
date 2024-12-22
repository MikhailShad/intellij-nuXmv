/* JFlex specification for nuXmv lexer */

package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes;

%%

%class NuXmvLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%line
%column

%{
    // Helper method to create tokens
    private IElementType token(int type) {
        return NuXmvTypes.Factory.createElement(type);
    }
%}

// Definitions
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

// Comments
Comment = "--".*{LineTerminator}

// Identifiers
Identifier = [a-zA-Z_][a-zA-Z0-9_\-]*

// Numbers
Integer = 0 | [1-9][0-9]*
Real = {Integer}\.{Integer}

// Keywords
Keyword = "MODULE" | "VAR" | "IVAR" | "FROZENVAR" | "DEFINE" | "CONSTANTS" | "ASSIGN" | "TRANS" | "INIT" | "INVAR" | "FAIRNESS" | "JUSTICE" | "COMPASSION" | "CTLSPEC" | "SPEC" | "LTLSPEC" | "INVARSPEC" | "PSLSPEC" | "FUN" | "ISA" | "PRED" | "MIRROR" | "COMPUTE"

// Operators and punctuation
Operator = "+" | "-" | "*" | "/" | "mod" | "&" | "|" | "!" | "xor" | "xnor" | "->" | "<->" | "=" | "!=" | "<" | ">" | "<=" | ">=" | "<<" | ">>" | "pi" | "abs" | "max" | "min" | "sin" | "cos" | "tan" | "exp" | "ln" | "pow" | "asin" | "acos" | "atan" | "sqrt" | "bool" | "toint" | "signed" | "unsigned" | "extend" | "resize" | "union" | "in" | "count" | "floor" | "next" | "case" | "esac" | "TRUE" | "FALSE" | "self" | "EG" | "EX" | "EF" | "AG" | "AX" | "AF" | "E[" | "A[" | "X" | "G" | "F" | "U" | "V" | "Y" | "Z" | "H" | "O" | "S" | "T" | "time_until" | "time_since" | "EBF" | "ABF" | "EBG" | "ABG" | "BU" | "forall" | "exists" | "always" | "never" | "next_event" | "next_event_a" | "next_event_e" | "within" | "whilenot" | "sequence" | "SERE" | "OBE" | "FL" | "OBE_property" | "FL_property"

Punctuation = "{" | "}" | "(" | ")" | "[" | "]" | "." | "," | ";" | ":" | ":="

%%

// Lexical rules

// Keywords
{Keyword}        { return token(NuXmvTypes.KEYWORD); }

// Identifiers
{Identifier}     { return token(NuXmvTypes.IDENTIFIER); }

// Numbers
{Integer}        { return token(NuXmvTypes.INTEGER); }
{Real}           { return token(NuXmvTypes.REAL); }

// Operators
{Operator}       { return token(NuXmvTypes.OPERATOR); }

// Punctuation
{Punctuation}    { return token(NuXmvTypes.PUNCTUATION); }

// Comments
{Comment}        { /* Ignore comments */ }

// Whitespace
{WhiteSpace}     { /* Ignore whitespace */ }

// Error fallback
[^]              { throw new Error("Illegal character: " + yytext()); }

<<EOF>>          { return token(NuXmvTypes.EOF); }
