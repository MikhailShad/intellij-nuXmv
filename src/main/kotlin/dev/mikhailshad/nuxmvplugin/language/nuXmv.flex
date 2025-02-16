/* JFlex specification for nuXmv lexer */

package dev.mikhailshad.nuxmvplugin.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.*;

%%
%{
    _NuXmvLexer() {
      this(null);
  }
%}

%class _NuXmvLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

LETTER=[A-Za-z_]
DIGIT=[0-9]
IDENTIFIER={LETTER} ({LETTER} | {DIGIT} | [\$\-\#])*

POSITIVE_INTEGER_NUMBER = {DIGIT}+
INTEGER_NUMBER = "-"? {POSITIVE_INTEGER_NUMBER}
FLOAT_NUMBER = {POSITIVE_INTEGER_NUMBER} "." {POSITIVE_INTEGER_NUMBER}
FRACTIONAL_NUMBER = [fF] "’" {POSITIVE_INTEGER_NUMBER} "/" {POSITIVE_INTEGER_NUMBER}
EXPONENTIAL_NUMBER = ({POSITIVE_INTEGER_NUMBER} | {FRACTIONAL_NUMBER}) [eE] {INTEGER_NUMBER}
HEX_NUMBER = [0-9a-fA-F]+

EOL_WS           = \n | \r | \r\n
LINE_WS          = [\ \t]
WHITE_SPACE_CHAR = {EOL_WS} | {LINE_WS}
WHITE_SPACE      = {WHITE_SPACE_CHAR}+

LINE_COMMENT=("--")[^\r\n]*

%state IN_BLOCK_COMMENT

%%
<YYINITIAL> {
    {WHITE_SPACE}                {return TokenType.WHITE_SPACE;}
    {LINE_COMMENT}               {return LINE_COMMENT;}
    "/--"                        {yybegin(IN_BLOCK_COMMENT); yypushback(3);}

    // Reserved words
    "LTLSPEC"     { return LTLSPEC; }
    "CTLSPEC"     { return CTLSPEC; }
    "SPEC"        { return SPEC; }
    "INVARSPEC"   { return INVARSPEC; }

    "MODULE"     {return MODULE;}
    "self"       {return SELF;}
    "TRUE"       {return TRUE;}
    "FALSE"      {return FALSE;}
    "!"          {return NOT;}
    "&"          {return AND;}
    "|"          {return OR;}
    "xor"        {return XOR;}
    "xnor"       {return NOT_XOR;}
    "->"         {return IMPLICATION;}
    "<->"        {return EQUIVALENCE;}
    ":="         {return ASSIGN;}
    "="          {return EQUALITY;}
    "!="         {return NOT_EQUALITY;}
    "<"          {return LESS;}
    ">"          {return GREATER;}
    "<="         {return LESS_EQ;}
    ">="         {return GREATER_EQ;}
    "-"          {return MINUS;}
    "+"          {return PLUS;}
    "*"          {return MULT;}
    "/"          {return DIV;}
    "mod"        {return MOD;}
    ">>"         {return SHIFT_RIGHT;}
    "<<"         {return SHIFT_LEFT;}
    "("          { return LPAREN; }
    ")"          { return RPAREN; }
    "{"          { return LBRACE; }
    "}"          { return RBRACE; }
    "["          { return LBRACKET; }
    "]"          { return RBRACKET; }
    "."          { return DOT; }
    ","          { return COMMA; }
    ";"          { return SEMICOLON; }
    ":"          { return COLON; }
    ".."         {return RANGE;}
    "::"         {return CONCAT;}
    "word1"      {return TO_WORD1;}
    "bool"       {return TO_BOOL;}
    "toint"      {return TO_INT;}
    "signed"     {return TO_SIGNED;}
    "unsigned"   {return TO_UNSIGNED;}
    "extend"     {return EXTEND;}
    "resize"     {return RESIZE;}
    "union"      {return UNION;}
    "in"         {return IN;}
    "?"          {return QUESTION_MARK;}
    "count"      {return COUNT;}
    "floor"      {return FLOOR;}
    "next"       {return NEXT;}
    "case"       {return CASE;}
    "esac"       {return ESAC;}

    // Type Spcifier
    "boolean"        {return BOOLEAN_TYPE;}
    "word"           {return WORD_TYPE;}
    "unsigned word"  {return UNSIGNED_WORD_TYPE;}
    "signed word"    {return SIGNED_WORD_TYPE;}
    "integer"        {return INTEGER_TYPE;}
    "real"           {return REAL_TYPE;}
    "clock"          {return CLOCK_TYPE;}
    "array"          {return ARRAY_TYPE;}
    "of"             {return ARRAY_OF;}

    // State, Input, Frozen Variables
    "VAR"            {return VAR;}
    "IVAR"           {return IVAR;}
    "FROZENVAR"      {return FROZENVAR;}

    // Other
    "FUN"            {return FUN_KW;}
    "DEFINE"         {return DEFINE_KW;}
    "CONSTANTS"      {return CONSTANTS_KW;}
    "ASSIGN"         {return ASSIGN_KW;}
    "init"           {return INIT_FUN;}
    "INIT"           {return INIT_KW;}
    "TRANS"          {return TRANS_KW;}
    "INVAR"          {return INVAR_KW;}
    "FAIRNESS"       {return FAIRNESS_KW;}
    "JUSTICE"        {return JUSTICE_KW;}
    "COMPASSION"     {return COMPASSION_KW;}
    "@TIME DOMAIN"   {return TIME_DOMAIN_KW;}
    "PRED"           {return PRED_KW;}
    "MIRROR"         {return MIRROR_KW;}
    "ISA"            {return ISA_KW;}
    "NAME"           {return NAME_KW;}
    "MIN"            {return MIN_KW;}
    "MAX"            {return MAX_KW;}

    // Common tokens
    {IDENTIFIER}                 {return IDENTIFIER;}
    {POSITIVE_INTEGER_NUMBER}    {return POSITIVE_INTEGER_NUMBER;}
    {INTEGER_NUMBER}             {return INTEGER_NUMBER;}
    {FLOAT_NUMBER}               {return FLOAT_NUMBER;}
    {FRACTIONAL_NUMBER}          {return FRACTIONAL_NUMBER;}
    {EXPONENTIAL_NUMBER}         {return EXPONENTIAL_NUMBER;}
    {HEX_NUMBER}                 {return HEX_NUMBER;}
}

<IN_BLOCK_COMMENT> {
  "--/"                          { yybegin(YYINITIAL); return BLOCK_COMMENT;}
  <<EOF>>                        { yybegin(YYINITIAL); return BLOCK_COMMENT;}
  [^]                            { }
}

[^]                  { return TokenType.BAD_CHARACTER; }
