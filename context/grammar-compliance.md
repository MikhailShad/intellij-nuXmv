# Сверка лексера и парсера плагина с грамматикой nuXmv 2.2.0

- **Эталон** — `context/nuxmv-2.2.0-grammar.md`: приложение C мануала плюс раздел «Чего нет в приложении, но есть в гл. 2».
- **Сверяемые файлы:** `src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/nuXmv.flex` и `nuXmv.bnf`, состояние на коммите `bb0429f`.
- **Дата:** 2026-10-20.

**Как проверялось.** Спорные случаи прогнаны на двух сторонах:
- на бинарнике `/opt/nuXmv-2.1.0` (`read_model` + `flatten_hierarchy`, TTS — с `-time`);
- на парсере плагина, временным light-тестом: есть ли `PsiErrorElement`.

Мини-модели лежали во временном каталоге и в репозиторий не попали. 2.2.0 под рукой нет. По сравнению с 2.1.0 изменилось только `function_declaration :: complex_identifier`, и плагин это уже поддерживает.

**Статусы:**
- ✅ соответствует;
- ➕ плагин принимает больше, чем грамматика (сознательно или безвредно);
- ⚠️ расходится;
- ❌ не поддержано.

## Сводка расхождений

| # | Что | Статус | nuXmv 2.1.0 | Плагин | Где чинить |
|---|---|---|---|---|---|
| R1 | Индекс перед точкой: `a[1].b` (по приложению `complex_identifier` леворекурсивен) | ✅ исправлено (D-008) | принимает в объявлении и в использовании | ошибка разбора | `ComplexIdentifier` в bnf |
| R2 | `unsigned word` / `signed word` через несколько пробелов или перевод строки | ✅ исправлено | принимает | `<type specifier> expected` | flex: токены `UNSIGNED_WORD_TYPE`/`SIGNED_WORD_TYPE` |
| R3 | Пустые скобки `MODULE m()` и `VAR i : m();` | ✅ исправлено | принимает | `')' unexpected` | `ModuleDeclaration` в bnf |
| R4 | Сегмент имени, начинающийся с цифры: `a.1.b`, `cb__77$main.SSA.1.ssa` | ✅ исправлено (D-008) | принимает; в корпусе 18 ошибок (`svcomp`) | `IDENTIFIER expected` | flex/bnf |
| R5 | Имена в кавычках: `"x.y" : boolean;` | ✅ исправлено (D-008) | принимает; в корпусе 9 ошибок (`vis`) | `"` даёт `BAD_CHARACTER`; альтернатива `VarName ::= QUOTES …` недостижима | flex: токен `QUOTES` |
| R6 | `Clock` как тип в TTS (приложение: «clock and Clock are … type specifiers») | ✅ исправлено | принимает | разбирается как `ModuleTypeSpecifier` (экземпляр модуля `Clock`), ошибки разбора нет | `NuXmvParserUtil.clockTypeName` |
| R7 | `simple_expr` / `next_expr`: `next` запрещён в INIT, INVAR, `init()`/обычном ASSIGN, CTLSPEC, `parameter_list`, PRED | ✅ исправлено (аннотатор) | `unexpected 'next' operator` при `flatten_hierarchy` (проверено на INIT и CTLSPEC) | не диагностируется | аннотатор |
| R8 | Приоритеты: хвост `?:` поглощает `<->`/`->`; LTL/CTL-операторы связывают сильнее basic-операторов | ⚠️ | по мануалу (стр. 15) | дерево другое, ошибок разбора нет | `Expr` в bnf; известно с этапа 3 |
| R9 | `word_value` с `_` в начале (`0b__1`), ширина 0 (`0b0_1`) | ➕ | ошибка | принимает | flex `WORD` или аннотатор |
| R10 | Токен `HEX_NUMBER` | ⚠️ | `12ab` — ошибка | лексится, но ни одно правило его не использует: ошибка разбора вместо понятной лексической | flex |
| R11 | `;` после `PSLSPEC` | ➕ | обязательна (`did you forget a semicolon`) | необязательна | аннотатор (по желанию) |
| R12 | `ISA identifier` | ⚠️ | ссылка на модуль | `IDENTIFIER` без ссылки (M7 отложен, ISA deprecated) | — |
| R13 | Спецификация в контексте: `LTLSPEC expr IN module` (NuSMV; нет ни в приложении, ни в гл. 2) | ✅ исправлено | принимает (`test3.smv`) | `<binary ltl op> or ; expected, got 'IN'` | bnf: `… (IN_KW IdentifierUsage)?` у спецификаций |
| R14 | Переменная внешнего `#FOR` внутри вложенного `#FOR` (`x[i]` под `#FOR j`) | ✅ исправлено | — | `i` не резолвится: `NuXmvForMacroLoopVarReference` смотрит только ближайший макрос | `NuXmvForMacroLoopVarReference.isLoopVarReference` |

## Подробная таблица

### Лексика (`nuXmv.flex`)

| Правило | Плагин | Статус | Комментарий |
|---|---|---|---|
| `identifier` | `IDENTIFIER={LETTER}({LETTER}\|{DIGIT}\|[\$\-\#])*` | ✅ | `x-1` — один идентификатор, как у nuXmv (D-004) |
| Комментарии `--…`, `/-- … --/` | `LINE_COMMENT`, состояние `IN_BLOCK_COMMENT` | ✅ | |
| Зарезервированные слова (стр. 7) | все есть как токены | ✅ | `Clock`/`clock`/`time` намеренно `IDENTIFIER` (D-003); `MONOPOS`/`MONONEG` — из гл. 2 (PARSYNTH) |
| `at next`, `at last` | `"at"{LINE_WS}+"next"` | ✅ | из гл. 2 |
| `@F~`, `@O~`, `X~`, `Y~`, `+oo` | есть | ✅ | `X~`/`Y~`/`+oo` — из гл. 2 |
| `unsigned word`, `signed word` | `"unsigned" {WHITE_SPACE} "word"` | ✅ | R2 исправлено |
| `pos_integer_number`, `integer_number` | `POSITIVE_INTEGER_NUMBER`, знак — `WholeNumber ::= MINUS? …` | ✅ | D-004 |
| `float_number` | `{POS} "." {POS}` | ✅ | |
| `fractional_number` | `[fF] "'" {POS} "/" {POS}` | ✅ | в мануале типографский `’`, nuXmv принимает ASCII `'` |
| `exponential_number` | `({POS}\|{FLOAT}) [eE] "-"? {POS}` | ✅ | |
| `word_constant` | `0 [us]? [bBdDoOhH] {DIGIT}* _ [0-9a-fA-F_]+` | ➕ | R9: значение может начинаться с `_`, ширина 0 допускается |
| hex-числа вне word | `HEX_NUMBER` | ⚠️ | R10: в языке такого токена нет |
| `"…"` | `QUOTED_IDENTIFIER = "[^"]*"` | ✅ | R5 исправлено; кавычки — часть имени (D-008) |
| Лишние зарезервированные `COMPID`, `COMPWFF`, `CONSTRAINT`, `CTLWFF`, `ITYPE`, `LTLWFF`, `MDEFINE`, `NEXTWFF`, `noncontinuous`, `PREDICATES`, `SIMPWFF`, `Integer`, `Real`, `Word`, `IN` | лексятся, в грамматике не используются | ✅ | верно: слова зарезервированы и не могут быть идентификаторами |
| `#`, `#FOR`, `#END` | `MACRO_PREFIX`, `FOR_MACRO_KW`, `END_MACRO_KW` | ➕ | расширение плагина (макрос `#FOR`), в языке nuXmv нет |
| `%` | `PERCENT` | ✅ | нужен только для PSL |

### Идентификаторы

| Правило | Плагин | Статус | Комментарий |
|---|---|---|---|
| `complex_identifier :: identifier \| ci . identifier \| ci [ simple_expression ] \| self` | `ComplexIdentifier ::= BaseIdentifier (DOT NameSegment \| LBRACKET Expr RBRACKET)*` | ✅ | R1/R4 исправлены; `NameSegment` включает числовой сегмент (D-008) |
| `variable_identifier`, `define_identifier` | `IdentifierUsage` | ✅ | резолв по отрезкам, D-007 |
| `function_call` (гл. 2) | `FunctionCallBasicExpr`, `FunctionIdentifier ::= BaseIdentifier (DOT SimpleIdentifier)*` | ✅ | `m.f(x)` поддержан (G7) |

### Константы и выражения

| Правило | Плагин | Статус | Комментарий |
|---|---|---|---|
| `constant` (boolean/integer/real/symbolic/word/range) | `LiteralBasicExpr` + `ReferenceBasicExpr` | ✅ | symbolic-константа — это `IdentifierUsage`, резолв идёт через scope |
| `time` (только TTS) | `IdentifierUsage`, аннотатор + `isTimed` | ✅ | D-003 |
| `pi` | `BuiltInConstant ::= PI` | ✅ | |
| Встроенные функции приложения (`abs`…`sqrt`, `word1`, `bool`, `toint`, `signed`, `unsigned`, `extend`, `resize`, `count`, `floor`, `next`) | `FunctionNameUsage` | ✅ | арность не проверяется |
| Функции гл. 2 (`swconst`, `uwconst`, `sizeof`, `READ`, `WRITE`, `CONSTARRAY`, `typeof`, `signed/unsigned word[N](e)`) | `FunctionNameUsage`, `ConstArrayBasicExpr`, `TypeOfArgument`, `WordConversionBasicExpr` | ✅ | |
| Бинарные и унарные операторы | `*BasicExpr` в `Expr` | ✅ | порядок альтернатив по стр. 15, но см. R8 |
| `basic_expr [ index ]` | `IndexBasicExpr` и индекс внутри `ComplexIdentifier` | ✅ | |
| `basic_expr [ integer_number : integer_number ]` | `IndexBasicExpr ::= Expr [ Expr (: Expr)? ]` | ➕ | приложение — числа, гл. 2 — `basic_expr` |
| `union`, `{ set_body_expr }`, `in` | `UnionBasicExpr`, `SetBasicExpr`, `InBasicExpr` | ✅ | |
| `?:` | `TernaryBasicExpr` | ⚠️ | R8 |
| `case_expr` / `case_body` | `CaseBasicExpr`, `RegularCaseBody` | ✅ | `;` после каждой ветки обязательна, как в приложении |
| `simple_expr` vs `next_expr` | грамматика не различает; аннотатор (`NuXmvNextOperator`) проверяет по поведению nuXmv | ✅ | R7 исправлено |

### Типы

| Правило | Плагин | Статус | Комментарий |
|---|---|---|---|
| `type_specifier :: simple \| module` | `TypeSpecifier ::= SimpleTypeSpecifier \| ModuleTypeSpecifier` | ✅ | |
| `boolean`, `integer`, `real` | есть | ✅ | |
| `word/unsigned word/signed word [ integer_number ]` | `WordType … [ TypeBound ]` | ➕ | D-005: допускаются константы/DEFINE (гл. 2: `basic_expr`); см. также R2 |
| `clock` | `ClockType ::= <<clockTypeName>>` | ✅ | |
| `Clock` | `ClockType` | ✅ | R6 исправлено |
| `{ enumeration_type_body }` | `EnumerationTypeBody` (`WholeNumber \| ComplexIdentifier`) | ✅ | |
| `integer_number .. integer_number` | `RangeType ::= TypeBound RANGE TypeBound` | ➕ | D-005 |
| `array integer_number .. integer_number of simple_type_specifier` | `ARRAY_TYPE (RangeType \| WordType \| INTEGER_TYPE) ARRAY_OF …` | ➕ | `array word[N] of`, `array integer of` — из гл. 2 |
| `module_type_specifier :: identifier [ ( [ parameter_list ] ) ]` | `SimpleIdentifier ParameterList?`, `ParameterList ::= ( [Expr (, Expr)*] )` | ✅ | пустые скобки в типе разбираются; в объявлении модуля — нет, см. R3 |

### Секции модуля

| Правило | Плагин | Статус | Комментарий |
|---|---|---|---|
| `time_domain_annotation` | `TimeDomainAnnotation ::= TIME_DOMAIN_KW IDENTIFIER` + аннотатор `none\|continuous` | ✅ | |
| `module :: MODULE identifier [(module_parameters)] [module_body]` | `ModuleDeclaration`, `ModuleBody?` | ✅ | R3 исправлено: пустые скобки допускаются, как у nuXmv |
| `var_list`, `simple_var_list` (`complex_identifier : …`) | `SingleVarDeclaration`, `SingleIvarDeclaration` | ✅ | D-007; IVAR/FROZENVAR — только `SimpleTypeSpecifier` |
| `FUN function_list` | `FunctionDeclaration`, `FunctionSpecification` | ✅ | |
| `DEFINE define_body` (+ array DEFINE из гл. 2) | `DefineBody ::= DefineName := (ArrayLiteral \| Expr) ;` | ✅ | |
| `CONSTANTS constants_body ;` | `ConstantsDeclaration` | ✅ | |
| `ASSIGN assign_list` (три формы) | `SimpleAssignExpr`, `InitAssignExpr`, `NextAssignExpr` | ✅ | |
| `TRANS`, `INIT`, `INVAR` (обе формы), `FAIRNESS`, `JUSTICE`, `COMPASSION` | есть, `;` необязательна | ✅ | |
| `URGENT` (гл. 2) | `UrgentConstraint` | ✅ | |
| `PRED` (две формы), `MIRROR variable_identifier` | `PredDeclaration`, `MirrorDeclaration ::= MIRROR IdentifierUsage` | ✅ | |
| `ISA identifier` | `IsaDeclaration ::= ISA IDENTIFIER` | ⚠️ | R12 |
| `parameter_synth_problem` (гл. 2) | `ParameterSynthProblemDeclaration` | ✅ | |

### Спецификации

| Правило | Плагин | Статус | Комментарий |
|---|---|---|---|
| `CTLSPEC`/`SPEC` (+ `NAME`) | `CtlSpecification`, `;` необязательна | ✅ | nuXmv 2.1.0 тоже принимает `CTLSPEC p` без `;`, вопреки первой форме приложения |
| `ctl_expr` с `E [ U ]` / `A [ U ]` | `UnaryCtlExpr`, `UntilCtlExpr ::= (E\|A) [ Expr (BU range Expr)? ]` | ✅ | D-006 |
| `INVARSPEC` (+ `NAME`) | `InvarSpecification` | ✅ | nuXmv принимает без `;` |
| `ltl_expr` приложения | `UnaryLtlExpr`, `BinaryLtlExpr`, `TimedLtlExpr` | ✅ | |
| Bounded LTL, `X~`/`Y~`, `time_until(e) op e` (гл. 2) | `Bound`, `LTL_TIMED_*`, `TimedLtlExpr` | ✅ | `Bound` допускается после любого унарного LTL-оператора (мягко) |
| `rtctl_expr` (`EBF/ABF/EBG/ABG range`, `A/E [ … BU range … ]`) | `RtCtlUnaryExpr`, хвост `UntilCtlExpr` | ✅ | |
| `COMPUTE` (+ `NAME`) | `ComputeSpecification` | ✅ | |
| `PSLSPEC … ;` | `PslSpecification` с мягким телом | ➕ | D-002; `;` необязательна (R11); `case … endcase` не поддержан — nuXmv 2.1.0 на нашем примере с `case` тоже дал syntax error |

## Прогон по корпусу после R1–R5

1324 файла `test-for-nuxvm-plugin`, временный light-тест: `PsiErrorElement` + неразрешённая первая ссылка, как в `NuXmvWarningAnnotator`.
- До R1–R5 было 54 ошибки разбора в 176 проверенных файлах.
- После: 21 ошибка разбора и 4 неразрешённые ссылки на всём корпусе:
  - 20 — `array_max-*.smv`: переменная `max` (зарезервированное слово), nuXmv 2.1.0 тоже даёт `syntax error`, плагин прав;
  - 1 + 2 неразрешённых — `test3.smv`: R13;
  - 2 неразрешённых — `forMacro.smv`: R14.

После R13/R14 на всём корпусе остаются только 20 ошибок разбора в `array_max-*.smv` и ни одной неразрешённой ссылки.

**R13, как устроено:**
- `SpecContext ::= IN_KW IdentifierUsage` у CTLSPEC/SPEC/LTLSPEC/INVARSPEC/COMPUTE/PSLSPEC; так у nuXmv 2.1.0, проверено для всех шести;
- имена внутри спецификации резолвятся в модуле экземпляра-контекста (`NuXmvIdentifierReference.contextModuleOf`), `self` — это контекст;
- сам `ctx` резолвится в модуле, где стоит спецификация;
- `IN` только в верхнем регистре: строчное `in` — проверка вхождения во множество.

**R7, где nuXmv 2.1.0 отвергает `next`** (`unexpected 'next' operator` / `Nested next operator.` / `Unexpected next operator.`), проверено на мини-моделях:
- **запрещён:** INIT, INVAR, FAIRNESS, JUSTICE, COMPASSION, CTLSPEC/SPEC, COMPUTE, PRED, `x := …`, `init(x) := …`, `next` внутри `next`, а также DEFINE с `next`, использованный в любом из этих мест;
- **разрешён:** TRANS, INVARSPEC, LTLSPEC, DEFINE, `next(x) := …`, URGENT, параметры экземпляра модуля (вопреки `parameter_list :: simple_expr` приложения).

Аннотатор повторяет именно это поведение. На корпусе срабатываний 0.

## Что из этого следует

1. **Ошибки разбора на валидных моделях:** R1–R5, R13, R14 исправлены. На корпусе остались только `array_max-*.smv` с переменной `max`, которые отвергает и nuXmv.
2. **Семантика:** R6, R7, R12. Нужны правки mixin/аннотатора, грамматика затрагивается мало.
3. **Приоритеты:** R8 — отдельная задача. Требует перестройки `Expr`, правка затронет эталоны PSI.
4. **Мягкость:** R9–R11 — по желанию, можно решить аннотатором.
