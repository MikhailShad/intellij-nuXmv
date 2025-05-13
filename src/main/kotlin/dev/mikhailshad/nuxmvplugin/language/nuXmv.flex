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
WORD = 0 [us] [bBdDoOhH] {DIGIT}* _ {HEX_NUMBER}

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

    // Reserved operators
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
    "union"      {return UNION;}
    "in"         {return IN;}
    "?"          {return QUESTION_MARK;}

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
    "VAR"            {return VAR_KW;}
    "IVAR"           {return IVAR_KW;}
    "FROZENVAR"      {return FROZENVAR_KW;}

    // Timed Logic Specific Keywords
    "U"           {return TL_UNTIL;}

    // LTL Specific Keywords
    "LTLSPEC"     {return LTLSPEC_KW; }
    "at next"     {return LTL_AT_NEXT;}
    "@F~"         {return LTL_AT_NEXT;}
    "at last"     {return LTL_AT_LAST;}
    "@O~"         {return LTL_AT_LAST;}
    "X"           {return LTL_NEXT;}
    "G"           {return LTL_GLOBALLY;}
    "F"           {return LTL_FINALLY;}
    "V"           {return LTL_RELEASES;}
    "Y"           {return LTL_PREVIOUS;}
    "Z"           {return LTL_NOT_PREVIOUS;}
    "H"           {return LTL_HISTORICALLY;}
    "O"           {return LTL_HISTORICALLY;}
    "S"           {return LTL_SINCE;}
    "T"           {return LTL_TRIGGERED;}
    "time_since"  {return LTL_TIME_SINCE;}
    "time_until"  {return LTL_TIME_UNTIL;}

    // CTL Specific Keywords
    "CTLSPEC"   {return CTLSPEC_KW;}
    "SPEC"      {return SPEC_KW;}
    "A"         {return CTL_FORALL;}
    "E"         {return CTL_EXISTS;}
    "EG"        {return CTL_EXISTS_GLOBALLY;}
    "EX"        {return CTL_EXISTS_NEXT;}
    "EF"        {return CTL_EXISTS_FINALLY;}
    "AX"        {return CTL_FORALL_GLOBALLY;}
    "AG"        {return CTL_FORALL_NEXT;}
    "AF"        {return CTL_FORALL_FINALLY;}
    "EBF"       {return RTCTL_EBF;}
    "ABF"       {return RTCTL_ABF;}
    "EBG"       {return RTCTL_EBG;}
    "ABG"       {return RTCTL_ABG;}
    "BU"        {return RTCTL_BU;}

    // Built-in Functions
    "init"       {return INIT_FUN;}
    "next"       {return NEXT_FUN;}
    "abs"        {return ABS_FUN;}
    "max"        {return MAX_FUN;}
    "min"        {return MIN_FUN;}
    "sin"        {return SIN_FUN;}
    "cos"        {return COS_FUN;}
    "exp"        {return EXP_FUN;}
    "tan"        {return TAN_FUN;}
    "ln"         {return LN_FUN;}
    "pow"        {return POW_FUN;}
    "asin"       {return ASIN_FUN;}
    "acos"       {return ACOS_FUN;}
    "atan"       {return ATAN_FUN;}
    "sqrt"       {return SQRT_FUN;}
    "word1"      {return TO_WORD1_FUN;}
    "bool"       {return TO_BOOL_FUN;}
    "toint"      {return TO_INT_FUN;}
    "count"      {return COUNT_FUN;}
    "swconst"    {return SWCONST_FUN;}
    "uwconst"    {return UWCONST_FUN;}
    "signed"     {return TO_SIGNED_FUN;}
    "unsigned"   {return TO_UNSIGNED_FUN;}
    "sizeof"     {return SIZEOF_FUN;}
    "floor"      {return FLOOR_FUN;}
    "extend"     {return EXTEND_FUN;}
    "resize"     {return RESIZE_FUN;}

    // Other Reserved Keywords
    "INVARSPEC"      {return INVARSPEC_KW;}
    "PSLSPEC"        {return PSLSPEC_KW;}
    "MODULE"         {return MODULE_KW;}
    "self"           {return SELF_KW;}
    "TRUE"           {return TRUE_KW;}
    "FALSE"          {return FALSE_KW;}
    "FUN"            {return FUN_KW;}
    "DEFINE"         {return DEFINE_KW;}
    "CONSTANTS"      {return CONSTANTS_KW;}
    "ASSIGN"         {return ASSIGN_KW;}
    "INIT"           {return INIT_KW;}
    "TRANS"          {return TRANS_KW;}
    "INVAR"          {return INVAR_KW;}
    "FAIRNESS"       {return FAIRNESS_KW;}
    "JUSTICE"        {return JUSTICE_KW;}
    "COMPASSION"     {return COMPASSION_KW;}
    "@TIME DOMAIN"   {return TIME_DOMAIN_KW;}
    "MIRROR"         {return MIRROR_KW;}
    "ISA"            {return ISA_KW;}
    "NAME"           {return NAME_KW;}
    "COMPUTE"        {return COMPUTE_KW;}
    "MIN"            {return MIN_KW;}
    "MAX"            {return MAX_KW;}
    "PARSYNTH"       {return PARSYNTH_KW;}
    "VALID"          {return VALID_KW;}
    "SAT"            {return SAT_KW;}
    "MONOPOS"        {return MONOPOS_KW;}
    "MONONEG"        {return MONONEG_KW;}
    "case"           {return CASE_KW;}
    "esac"           {return ESAC_KW;}
    "Clock"          {return CLOCK_KW;}
    "COMPID"         {return COMPID_KW;}
    "COMPWFF"        {return COMPWFF_KW;}
    "CONSTRAINT"     {return CONSTRAINT_KW;}
    "CTLWFF"         {return CTLWFF_KW;}
    "IN"             {return IN_KW;}
    "Integer"        {return INTEGER_KW;}
    "ITYPE"          {return ITYPE_KW;}
    "LTLWFF"         {return LTLWFF_KW;}
    "MDEFINE"        {return MDEFINE_KW;}
    "NEXTWFF"        {return NEXTWFF_KW;}
    "noncontinuous"  {return NONCONTINUOUS_KW;}
    "PREDICATES"     {return PREDICATES_KW;}
    "Real"           {return REAL_KW;}
    "SIMPWFF"        {return SIMPWFF_KW;}
    "URGENT"         {return URGENT_KW;}
    "Word"           {return WORD_KW;}
    
    // Macro keywords
    "%FOR"           {return FOR_MACRO_KW;}
    "%END"           {return END_MACRO_KW;}

    // Common tokens
    {IDENTIFIER}                 {return IDENTIFIER;}
    {WORD}                       {return WORD;}
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
