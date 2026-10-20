# Грамматика nuXmv 2.2.0 (приложение C мануала)

- **Источник:** `context/nuxmv-user-manual-2.2.0.pdf`, Appendix C «Production Rules», стр. 172–183 (страницы PDF 174–185). Раздел «Лексика» взят из гл. 2, стр. 7–8.
- **Способ чтения:** страницы отрендерены в PNG (Ghostscript, 120–140 dpi) и переписаны вручную. Текстовый экстрактор PDF теряет `_` и `~` в жирном шрифте, поэтому по тексту не сверялось.
- **Дата:** 2026-10-20.

**Нотация мануала** (стр. 7): нетерминалы — моноширинным шрифтом, терминалы — жирным. Здесь терминалы-слова набраны как есть (`MODULE`, `case`).
- `[ … ]` — необязательная часть;
- `|` — альтернатива;
- `one of` — выбор одного из перечисленных символов;
- `--` после правила — комментарий мануала;
- в PSL-части комментарии начинаются с `;;`.

Опечатки мануала сохранены и помечены `[sic]`.

---

## 0. Лексика (гл. 2, стр. 7–8)

```
identifier ::
        identifier_first_character
      | identifier identifier_consecutive_character

identifier_first_character :: one of
        A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
        a b c d e f g h i j k l m n o p q r s t u v w x y z _

identifier_consecutive_character ::
        identifier_first_character
      | digit
      | one of $ # -

digit :: one of 0 1 2 3 4 5 6 7 8 9
```

Пробельные символы — `<SPACE>`, `<TAB>`, `<RET>`. Строка от `--` до конца строки — комментарий. Многострочный комментарий открывается `/--` и закрывается `--/`.

**Зарезервированные слова** (стр. 7, порядок мануала):

```
@F~, @O~, A, ABF, ABG, abs, acos, AF, AG, array, asin, atan, ASSIGN, at next, at last,
AX, bool, boolean, BU, case, Clock, clock, COMPASSION, COMPID, COMPUTE, COMPWFF,
CONSTANTS, CONSTARRAY, CONSTRAINT, cos, count, CTLSPEC, CTLWFF, DEFINE, E, EBF,
EBG, EF, EG, esac, EX, exp, extend, F, FAIRNESS, FALSE, floor, FROZENVAR, FUN, G, H,
IN, in, INIT, init, Integer, integer, INVAR, INVARSPEC, ISA, ITYPE, IVAR, JUSTICE,
ln, LTLSPEC, LTLWFF, MAX, max, MDEFINE, MIN, min, MIRROR, mod, MODULE, NAME, next,
NEXTWFF, noncontinuous, O, of, PRED, PREDICATES, pi, pow, PSLSPEC, PARSYNTH, READ, Real,
real, resize, S, SAT, self, signed, SIMPWFF, sin, sizeof, SPEC, sqrt, swconst, T, tan,
time, time_since, time_until, toint, TRANS, TRUE, typeof, U, union, unsigned, URGENT,
uwconst, V, VALID, VAR, Word, word, word1, WRITE, X, xnor, xor, X~ Y [sic: «X~, Y»], Y~, Z
```

> Note (стр. 8): `Clock`, `clock` and `time` are reserved keywords only in TTS (2.4).

---

## C. Production Rules

### Identifiers

```
identifier ::
        identifier_first_character
      | identifier identifier_consecutive_character

identifier_first_character :: one of
        A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
        a b c d e f g h i j k l m n o p q r s t u v w x y z _

identifier_consecutive_character ::
        identifier_first_character
      | digit
      | one of $ # -

digit :: one of 0 1 2 3 4 5 6 7 8 9
```

> Note that there are certain reserved keyword which cannot be used as identifiers (see page 7).

### Variable and DEFINE Identifiers

```
define_identifier :: complex_identifier

variable_identifier :: complex_identifier
```

### Complex Identifiers

```
complex_identifier ::
        identifier
      | complex_identifier . identifier
      | complex_identifier [ simple_expression ]
      | self
```

### Integer Numbers

```
integer_number ::
        pos_integer_number
      | - pos_integer_number

pos_integer_number ::
        digit
      | pos_integer_number digit
```

### Real Numbers

```
real_number ::
        float_number
      | fractional_number
      | exponential_number

float_number ::
        pos_integer_number . pos_integer_number

fractional_number ::
        fraction_prefix ' pos_integer_number / pos_integer_number

fraction_prefix ::
        one of f F

exponential_number ::
        pos_integer_number exponential_prefix integer_number
      | float_number exponential_prefix integer_number

exponential_prefix ::
        one of e E
```

### Constants

```
constant ::
        boolean_constant
      | integer_constant
      | real_constant
      | symbolic_constant
      | word_constant
      | range_constant

boolean_constant :: one of
        FALSE TRUE

integer_constant :: integer_number

real_constant :: real_number
```

> Note that time is interpreted as a Clock constant only in timed nuXmv models.

```
symbolic_constant :: complex_identifier

word_constant :: 0 [word_sign_specifier] word_base [word_width] _ word_value

word_sign_specifier :: one of
        u s

word_width :: pos_integer_number      -- strictly greater than 0

word_base :: b | B | o | O | d | D | h | H

word_value ::
        hex_digit
      | word_value hex_digit
      | word_value _

hex_digit :: one of
        0 1 2 3 4 5 6 7 8 9 a b c d e f A B C D E F
```

> Note that there are some additional restrictions on the exact format of word constants (see page 13).

```
range_constant ::
        integer_number .. integer_number
```

### Basic Expressions

```
basic_expr ::
        constant                          -- a constant
      | time                              -- available only in timed models
      | variable_identifier               -- a variable identifier
      | define_identifier                 -- a define identifier
      | function_call                     -- a call to a function
      | ( basic_expr )
      | pi                                -- the pi constant
      | abs ( basic_expr )                -- absolute value
      | max ( basic_expr , basic_expr )   -- max
      | min ( basic_expr , basic_expr )   -- min
      | sin ( basic_expr )                -- sin
      | cos ( basic_expr )                -- cos
      | exp ( basic_expr )                -- exp
      | tan ( basic_expr )                -- tan
      | ln ( basic_expr )                 -- ln
      | pow ( basic_expr , simple_expr )  -- pow
      | asin ( basic_expr )               -- asin
      | acos ( basic_expr )               -- acos
      | atan ( basic_expr )               -- atan
      | sqrt ( basic_expr )               -- sqrt
      | ! basic_expr                      -- logical/bitwise NOT
      | basic_expr & basic_expr           -- logical/bitwise AND
      | basic_expr | basic_expr           -- logical/bitwise OR
      | basic_expr xor basic_expr         -- logical/bitwise exclusive OR
      | basic_expr xnor basic_expr        -- logical/bitwise NOT xor
      | basic_expr -> basic_expr          -- logical/bitwise implication
      | basic_expr <-> basic_expr         -- logical/bitwise equivalence
      | basic_expr = basic_expr           -- equality
      | basic_expr != basic_expr          -- inequality
      | basic_expr < basic_expr           -- less than
      | basic_expr > basic_expr           -- greater than
      | basic_expr <= basic_expr          -- less than or equal
      | basic_expr >= basic_expr          -- greater than or equal
      | - basic_expr                      -- unary minus
      | basic_expr + basic_expr           -- integer addition
      | basic_expr - basic_expr           -- integer subtraction
      | basic_expr * basic_expr           -- integer multiplication
      | basic_expr / basic_expr           -- integer division
      | basic_expr mod basic_expr         -- integer remainder
      | basic_expr >> basic_expr          -- bit shift right
      | basic_expr << basic_expr          -- bit shift left
      | basic_expr [ index ]              -- index subscript
      | basic_expr [ integer_number : integer_number ]
                                          -- word bits selection
      | basic_expr :: basic_expr          -- word concatenation
      | word1 ( basic_expr )
                                          -- boolean to word[1] convertion
      | bool ( basic_expr )
                                          -- word[1] and integer to boolean convertion
      | toint ( basic_expr )
                                          -- word[N] and boolean to integer convertion
      | signed ( basic_expr )
                                          -- unsigned to signed word convertion
      | unsigned ( basic_expr )
                                          -- signed to unsigned word convertion
      | extend ( basic_expr , basic_expr )
                                          -- word width increase
      | resize ( basic_expr , basic_expr )
                                          -- word width resizing
      | basic_expr union basic_expr
                                          -- union of set expressions
      | { set_body_expr }                 -- set expression
      | basic_expr in basic_expr          -- inclusion expression
      | basic_expr ? basic_expr : basic_expr
                                          -- if-then-else expression
      | count ( basic_expr_list )
                                          -- count of TRUE boolean expressions
      | floor ( basic_expr )
      | case_expr                         -- case expression
      | next ( basic_expr )               -- next expression

basic_expr_list ::
        basic_expr
      | basic_expr_list , basic_expr

set_body_expr ::
        basic_expr
      | set_body_expr , basic_expr
```

Нетерминал `index` в приложении не определён. В гл. 2 (§2.2.3, «Index Subscript Operator») индекс — `basic_expr`. `function_call` в приложении тоже не определён, см. гл. 2, стр. 16: `function_call :: function_identifier ( fun_args_list )`, `function_identifier :: complex_identifier`.

### Case Expression and If-Then-Else Expression

```
case_expr :: case case_body esac

case_body ::
        basic_expr : basic_expr ;
      | case_body basic_expr : basic_expr ;

basic_expr ? basic_expr : basic_expr
```

### Simple Expression

```
simple_expr :: basic_expr
```

> Note that simple expressions cannot contain next operators.

### Next Expression

```
next_expr :: basic_expr
```

### Type Specifier

```
type_specifier ::
        simple_type_specifier
      | module_type_spicifier [sic]

simple_type_specifier ::
        boolean
      | word [ integer_number ]
      | unsigned word [ integer_number ]
      | signed word [ integer_number ]
      | integer
      | real
      | clock
      | { enumeration_type_body }
      | integer_number .. integer_number
      | array integer_number .. integer_number
              of simple_type_specifier

enumeration_type_body ::
        enumeration_type_value
      | enumeration_type_body , enumeration_type_value

enumeration_type_value ::
        symbolic_constant
      | integer_number
```

> Note that clock and Clock are interpreted as type specifiers only in timed nuXmv models.

### Module Type Specifier

```
module_type_specifier ::
        identifier [ ( [ parameter_list ] ) ]

parameter_list ::
        simple_expr
      | parameter_list , simple_expr
```

### State, Input and Frozen Variables

```
var_declaration :: VAR var_list

ivar_declaration :: IVAR simple_var_list

frozenvar_declaration :: FROZENVAR simple_var_list

var_list :: complex_identifier : type_specifier ;
          | var_list complex_identifier : type_specifier ;

simple_var_list :: complex_identifier : simple_type_specifier ;
          | simple_var_list complex_identifier : simple_type_specifier ;
```

### Functions

```
function_declaration :: FUN function_list

function_list :: function_declaration
               | function_list function_declaration

function_declaration :: complex_identifier : function_type_specifier ;
function_type_specifier :: function_args_type_specifier -> simple_type_specifier

function_args_type_specifier :: simple_type_specifier
               | function_args_type_specifier * simple_type_specifier
```

Имя `function_declaration` в мануале используется дважды: для секции `FUN` и для одного объявления внутри неё.

### DEFINE Declaration

```
define_declaration :: DEFINE define_body

define_body :: complex_identifier := next_expr ;
             | define_body complex_identifier := next_expr ;
```

### CONSTANTS Declaration

```
constants_declaration :: CONSTANTS constants_body ;

constants_body :: complex_identifier
               | constants_body , complex_identifier
```

### ASSIGN Declaration

```
assign_constraint :: ASSIGN assign_list

assign_list :: assign ;
             | assign_list assign ;

assign ::
        complex_identifier          := simple_expr
      | init ( complex_identifier ) := simple_expr
      | next ( complex_identifier ) := next_expr
```

### TRANS Statement

```
trans_constraint :: TRANS next_expr [;]
```

### INIT Statement

```
init_constrain [sic] :: INIT simple_expr [;]
```

### INVAR Statement

```
invar_constraint :: INVAR simple_expr [;]

invar_constraint :: INVAR simple_expr -> simple_expr [;]
```

> Clock variables are allowed to occur only in the second form above, and only in the rightmost simple expression, which is furthermore required to be convex (i.e. a conjunction of atoms).

### FAIRNESS Constraints

```
fairness_constraint ::
        FAIRNESS simple_expr [;]
      | JUSTICE simple_expr [;]
      | COMPASSION ( simple_expr , simple_expr ) [;]
```

### Time domain annotation

```
time_domain_annotation :: @TIME_DOMAIN time_domain
time_domain :: none | continuous
```

> Note that the time domain annotation must precede every mudule declarations. If it is missing the model is assumed to have time domain none.

### Module Declarations

```
module :: MODULE identifier [(module_parameters)] [module_body]

module_parameters ::
        identifier
      | module_parameters , identifier

module_body ::
        module_element
      | module_body module_element

module_element ::
        var_declaration
      | ivar_declaration
      | frozenvar_declaration% [sic]
      | function_declaration
      | define_declaration
      | constants_declaration
      | assign_constraint
      | trans_constraint
      | init_constraint
      | invar_constraint
      | fairness_constraint
      | ctl_specification
      | invar_specification
      | ltl_specification
      | compute_specification
      | parameter_synth_problem

      | isa_declaration
      | pred_declaration
      | mirror_declaration
```

### PRED and MIRROR Declarations

```
pred_declaration :: PRED simple_expression [;]
                  | PRED < identifier > := simple_expression [;]

mirror_declaration :: MIRROR variable_identifier [;]
```

### ISA Declaration

```
isa_declaration :: ISA identifier
```

> Warning: this is a deprecated feature and will eventually be removed from NuSMV. Use module instances instead.

### CTL Specification

```
ctl_specification :: CTLSPEC ctl_expr ;
                   | SPEC ctl_expr [;]
                   | CTLSPEC NAME identifier := ctl_expr [;]
                   | SPEC NAME identifier := ctl_expr [;]

ctl_expr ::
        simple_expr                     -- a simple boolean expression
      | ( ctl_expr )
      | ! ctl_expr                      -- logical not
      | ctl_expr & ctl_expr             -- logical and
      | ctl_expr | ctl_expr             -- logical or
      | ctl_expr xor ctl_expr           -- logical exclusive or
      | ctl_expr xnor ctl_expr          -- logical NOT exclusive or
      | ctl_expr -> ctl_expr            -- logical implies
      | ctl_expr <-> ctl_expr           -- logical equivalence
      | EG ctl_expr                     -- exists globally
      | EX ctl_expr                     -- exists next state
      | EF ctl_expr                     -- exists finally
      | AG ctl_expr                     -- forall globally
      | AX ctl_expr                     -- forall next state
      | AF ctl_expr                     -- forall finally
      | E [ ctl_expr U ctl_expr ]       -- exists until
      | A [ ctl_expr U ctl_expr ]       -- forall until
```

### INVAR Specification

```
invar_specification :: INVARSPEC next_expr ;
                     | INVARSPEC NAME identifier := next_expr [;]
```

> This is equivalent to `SPEC AG next_expr ;` but is checked by a specialised algorithm during reachability analysis.

### LTL Specification

```
ltl_specification :: LTLSPEC ltl_expr [;]
                   | LTLSPEC NAME identifier := ltl_expr [;]

ltl_expr ::
        next_expr                -- a boolean expression with possibly next operator
      | ( ltl_expr )
      | ! ltl_expr               -- logical not
      | ltl_expr & ltl_expr      -- logical and
      | ltl_expr | ltl_expr      -- logical or
      | ltl_expr xor ltl_expr    -- logical exclusive or
      | ltl_expr xnor ltl_expr   -- logical NOT exclusive or
      | ltl_expr -> ltl_expr     -- logical implies
      | ltl_expr <-> ltl_expr    -- logical equivalence
      -- FUTURE
      | X ltl_expr               -- next state
      | G ltl_expr               -- globally
      | F ltl_expr               -- finally
      | ltl_expr U ltl_expr      -- until
      | ltl_expr V ltl_expr      -- releases
      | ltl_expr @F~ ltl_expr    -- at next, lhs cannot contain temporal operators
      | time_until ( next_expr ) -- time until, available only in timed models
      -- PAST
      | Y ltl_expr               -- previous state
      | Z ltl_expr               -- not previous state not
      | H ltl_expr               -- historically
      | O ltl_expr               -- once
      | ltl_expr S ltl_expr      -- since
      | ltl_expr T ltl_expr      -- triggered
      | ltl_expr @O~ ltl_expr    -- at last, lhs cannot contain temporal operators
      | time_since ( next_expr ) -- time since, available only in timed models
```

### Real Time CTL Specification

```
rtctl_specification :: CTLSPEC rtctl_expr [;]
                     | SPEC rtctl_expr [;]
                     | CTLSPEC NAME identifier := rtctl_expr [;]
                     | SPEC NAME identifier := rtctl_expr [;]

rtctl_expr ::
        ctl_expr
      | EBF range rtctl_expr
      | ABF range rtctl_expr
      | EBG range rtctl_expr
      | ABG range rtctl_expr
      | A [ rtctl_expr BU range rtctl_expr ]
      | E [ rtctl_expr BU range rtctl_expr ]
range  :: integer_number .. integer_number
```

> It is also possible to compute quantative information for the FSM:

```
compute_specification :: COMPUTE compute_expr [;]
                       | COMPUTE NAME identifier := compute_expr [;]

compute_expr :: MIN [ rtctl_expr , rtctl_expr ]
              | MAX [ rtctl_expr , rtctl_expr ]
```

### PSL Specification

```
pslspec_declaration :: PSLSPEC psl_expr ;
                     | PSLSPEC NAME identifier := psl_expr ;
```

> Notice that here the ; is mandatory.

```
psl_expr ::
   psl_primary_expr
 | psl_unary_expr
 | psl_binary_expr
 | psl_conditional_expr
 | psl_case_expr
 | psl_property

number :: integer_number
identifier::
   variable_identifier
 | define_identifier
psl_primary_expr ::
   constant
 | identifier                              ;; an identifier
 | { psl_expr , ... , psl_expr }
 | { psl_expr "{" psl_expr , ... , "psl_expr" }}
 | ( psl_expr )
psl_unary_expr ::
   + psl_primary_expr
 | - psl_primary_expr
 | ! psl_primary_expr
psl_binary_expr ::
   psl_expr + psl_expr
 | psl_expr union psl_expr
 | psl_expr in psl_expr
 | psl_expr - psl_expr
 | psl_expr * psl_expr
 | psl_expr / psl_expr
 | psl_expr % psl_expr
 | psl_expr == psl_expr
 | psl_expr != psl_expr
 | psl_expr < psl_expr
 | psl_expr <= psl_expr
 | psl_expr > psl_expr
 | psl_expr >= psl_expr
 | psl_expr & psl_expr
 | psl_expr | psl_expr
 | psl_expr xor psl_expr
psl_conditional_expr ::
 psl_expr ? psl_expr : psl_expr
psl_case_expr ::
 case
     psl_expr : psl_expr ;
     ...
     psl_expr : psl_expr ;
 endcase
```

> Among the subclasses of psl_expr we depict the class psl_bexpr that will be used in the following to identify purely boolean, i.e. not temporal, expressions.

```
psl_property ::
   replicator psl_expr ;; a replicated property
 | FL_property abort psl_bexpr
 | psl_expr <-> psl_expr
 | psl_expr -> psl_expr
 | FL_property
 | OBE_property
replicator ::
   forall var_id [index_range] in value_set :
index_range ::
   [ range ]
range ::
   low_bound : high_bound
low_bound ::
   number
 | identifier
high_bound ::
   number
 | identifier
 | inf             ;; inifite [sic] high bound
value_set ::
   { value_range , ... , value_range }
 | boolean
value_range ::
   psl_expr
 | range

FL_property ::
 ;; PRIMITIVE LTL OPERATORS
   X FL_property
 | X! FL_property
 | F FL_property
 | G FL_property
 | [ FL_property U FL_property ]
 | [ FL_property W FL_property ]
 ;; SIMPLE TEMPORAL OPERATORS
 | always FL_property
 | never FL_property
 | next FL_property
 | next! FL_property
 | eventually! FL_property
 | FL_property until! FL_property
 | FL_property until FL_property
 | FL_property until!_ FL_property
 | FL_property until_ FL_property
 | FL_property before! FL_property
 | FL_property before FL_property
 | FL_property before!_ FL_property
 | FL_property before_ FL_property
 ;; EXTENDED NEXT OPERATORS
 | X [number] ( FL_property )
 | X! [number] ( FL_property )
 | next [number] ( FL_property )
 | next! [number] ( FL_property )
 ;;
 | next_a [range] ( FL_property )
 | next_a! [range] ( FL_property )
 | next_e [range] ( FL_property )
 | next_e! [range] ( FL_property )
 ;;
 | next_event! ( psl_bexpr ) ( FL_property )
 | next_event ( psl_bexpr ) ( FL_property )
 | next_event! ( psl_bexpr ) [ number ] ( FL_property )
 | next_event ( psl_bexpr ) [ number ] ( FL_property )
 ;;
 | next_event_a! ( psl_bexpr ) [psl_expr] ( FL_property )
 | next_event_a ( psl_bexpr ) [psl_expr] ( FL_property )
 | next_event_e! ( psl_bexpr ) [psl_expr] ( FL_property )
 | next_event_e ( psl_bexpr ) [psl_expr] ( FL_property )
 ;; OPERATORS ON SEREs
 | sequence ( FL_property )
 | sequence |-> sequence [!]
 | sequence |=> sequence [!]
 ;;
 | always sequence
 | G sequence
 | never sequence
 | eventually! sequence
 ;;
 | within! ( sequence_or_psl_bexpr , psl_bexpr ) sequence
 | within ( sequence_or_psl_bexpr , psl_bexpr ) sequence
 | within!_ ( sequence_or_psl_bexpr , psl_bexpr ) sequence
 | within_ ( sequence_or_psl_bexpr , psl_bexpr ) sequence
 ;;
 | whilenot! ( psl_bexpr ) sequence
 | whilenot ( psl_bexpr ) sequence
 | whilenot!_ ( psl_bexpr ) sequence
 | whilenot_ ( psl_bexpr ) sequence
sequence_or_psl_bexpr ::
   sequence
 | psl_bexpr

sequence ::
   { SERE }
SERE ::
   sequence
 | psl_bexpr
 ;; COMPOSITION OPERATORS
 | SERE ; SERE
 | SERE : SERE
 | SERE & SERE
 | SERE && SERE
 | SERE | SERE
 ;; RegExp QUALIFIERS
 | SERE [* [count] ]
 | [* [count] ]
 | SERE [+]
 | [+]
 ;;
 | psl_bexpr [= count ]
 | psl_bexpr [-> count ]
count ::
   number
 | range

OBE_property ::
   AX OBE_property
 | AG OBE_property
 | AF OBE_property
 | A [ OBE_property U OBE_property ]
 | EX OBE_property
 | EG OBE_property
 | EF OBE_property
 | E [ OBE_property U OBE_property ]
```

В PSL-продукциях скобки `[number]`, `[range]`, `[psl_expr]`, `[!]`, `[count]` — это терминалы `[`/`]` языка PSL (жирный шрифт), а не признак необязательности. Исключения: `[index_range]` в `replicator` и `[count]` внутри `[* … ]`, которые по смыслу необязательны.

---

## Чего нет в приложении C, но есть в гл. 2

Приложение отстаёт от гл. 2. Ниже конструкции, описанные только в гл. 2, со ссылками на страницы мануала:

| Конструкция | Где в гл. 2 |
|---|---|
| `swconst`, `uwconst`, `sizeof`, `READ`, `WRITE`, `CONSTARRAY(typeof(v), e)`, `CONSTARRAY(array … of …, e)`, `signed word[N](e)`, `unsigned word[N](e)` в `basic_expr` | §2.2.3, стр. 13–14, 21–22 |
| `typeof ( variable_identifier )` | §2.2.3, стр. 22 |
| `word [ basic_expr ]`, `basic_expr .. basic_expr`, `array basic_expr .. basic_expr of`, `array word [ basic_expr ] of`, `array integer of` в типах | §2.3.1, стр. 25–26; §2.1.8–2.1.9, стр. 9–10 |
| Бит-селекция `basic_expr [ basic_expr : basic_expr ]` (в приложении — `integer_number`) | §2.2.3, стр. 14 |
| Array DEFINE: `DEFINE complex_identifier := array_expression ;` | §2.3.3, стр. 29 |
| `urgent_constraint :: URGENT next_expr [;]` и `pslspec_specification` в `module_element` | §2.3.11, стр. 33; §2.4.4, стр. 39 |
| `parameter_synth_problem` (в `module_element` приложения есть, продукции нет) | §2.5.5, стр. 45–46 |
| Bounded LTL: `G/F/H/O bound ltl_expr`, `ltl_expr U/V/S/T bound ltl_expr`, `bound :: [ integer_number , integer_number ] \| [ integer_number , +oo )` | §2.5.3, стр. 41 |
| `at next` / `at last` словами наравне с `@F~` / `@O~` | §2.5.3, стр. 41 |
| TTS: `X~ ltl_expr`, `Y~ ltl_expr`, `time_until ( ltl_expr ) op at_expr`, `time_since ( ltl_expr ) op at_expr`, `bound :: [ number , +oo ) \| [ 0 , number ]` | §2.5.3, стр. 43 |
| TTS: `time op untimed_expr`, `time @F~ expr op untimed_expr`, `time @O~ expr op untimed_expr` | §2.4.2, стр. 38 |
| `function_call :: function_identifier ( fun_args_list )`, `function_identifier :: complex_identifier` | §2.2.3, стр. 15–16 |

Противоречия между приложением и гл. 2, не связанные с пробелами:
- `;` обязательна в `CTLSPEC ctl_expr ;` (первая форма), `INVARSPEC next_expr ;` и `PSLSPEC … ;`, а в гл. 2 для CTLSPEC и INVARSPEC стоит `[;]`;
- `var_list`/`simple_var_list` используют `complex_identifier`, а в гл. 2 (§2.3.1) — `identifier`. См. D-007.
