# Способы запуска nuXmv и признаки их применимости

- **Источники** — мануал 2.2.0 (`context/nuxmv-user-manual-2.2.0.pdf`): гл. 3 «Running nuXmv interactively» (стр. 52–55), гл.
  4–6 (команды NuSMV, nuXmv и timed nuXmv, стр. 56–157), гл. 7 «Running nuXmv batch» (стр. 158–161).
- **Проверка** — локальный бинарник nuXmv 2.1.0 на мини-моделях: конечная, с `integer`/`real`, с `FUN`, timed, с
  экземплярами модулей. Пометка «(2.1.0)» значит, что утверждение проверено на бинарнике, а в мануале его нет или оно
  сформулировано иначе.
- **Реализация в плагине** — `NuXmvModelProfile` (признаки модели), `NuXmvCheckAlgorithm`/`NuXmvApplicability`
  (таблица C), `NuXmvLaunchPlanner` (сборка команд); решение — D-010 в `decisions.md`.
- **Дата:** 2026-10-21.

## A. Режимы процесса

| Режим | Запуск | Когда применим | Замечания |
|---|---|---|---|
| Batch | `nuXmv [opts] model` без `-int` | только конечная модель без `@TIME_DOMAIN continuous` | Строит модель и проверяет все спецификации (CTL, COMPUTE, LTL, PSL, INVAR) через BDD. На `integer`/`real` печатает «batch mode is not available with models containing infinite domain variables» и аварийно завершается (2.1.0). Приглашения `nuXmv >` нет. |
| Interactive | `nuXmv -int [opts] model`, команды в stdin | любая модель | Основной режим плагина: следующая команда уходит по приглашению `nuXmv >`. |
| Script | `nuXmv -source file [opts] model` | любая модель | Подразумевает `-int`. При ошибке скрипт прерывается (`aborting 'source …'`), а оболочка остаётся открытой и читает stdin (2.1.0). |
| Timed | флаг `-time` с любым режимом | файл начинается с `@TIME_DOMAIN continuous` | Без `-time` такая модель даёт parser error. Batch с `-time` тоже недоступен, потому что домен бесконечный (2.1.0). |

Опции командной строки из гл. 7, полезные для run configuration:

- `-coi` — cone of influence;
- `-dcx` — без контрпримеров;
- `-is/-ic/-ils/-ips/-ii` — не проверять CTL/COMPUTE/LTL/PSL/INVAR в batch;
- `-bmc -bmc_length k`;
- `-cpp`/`-pre pps` — препроцессоры;
- `-i/-o` — порядок переменных;
- `-s` — не читать `.nuxmvrc`;
- `-v n` — подробность вывода.

В плагине они вводятся в поле «Command line options». Флаги, которые нужны домену (`-time`), добавляются автоматически.

## B. Инициализация модели (движок)

| Домен | Команда | Признак в модели | Что доступно потом |
|---|---|---|---|
| Finite / BDD | `go` | нет `integer`/`real` (в том числе в `IVAR`, `FROZENVAR`, `array … of`), нет `FUN`, модель не timed | CTL, LTL, INVAR, PSL, COMPUTE через BDD; BDD-симуляция |
| Finite / SAT | `go_bmc` после `go`: выполняет недостающие `build_boolean_model`, `bmc_setup` | то же | `check_*_bmc`, `check_*_ic3`; без этого шага «The boolean model must be built before» (2.1.0) |
| Infinite / SMT | `go_msat` | есть `integer`, `real` или `FUN` | LTL и INVAR через MathSAT/IC3. CTL, PSL и COMPUTE недоступны: «A model must be built before» (2.1.0) |
| Timed / SMT | `-time` + `go_time` | `@TIME_DOMAIN continuous` | `timed_check_invar`, `timed_check_ltlspec`, timed-симуляция, timed reqan |

## C. Проверка спецификаций: команда и алгоритм

| Спецификация (PSI) | Finite | Infinite | Timed |
|---|---|---|---|
| `CTLSPEC`/`SPEC` (`NuXmvCtlSpecification`) | BDD `check_ctlspec` | — | — |
| `COMPUTE` (`NuXmvComputeSpecification`) | BDD `check_compute` | — | — |
| `LTLSPEC` (`NuXmvLtlSpecification`) | BDD `check_ltlspec`; BMC `check_ltlspec_bmc -k K`\*; IC3 `check_ltlspec_ic3`\* | BMC `msat_check_ltlspec_bmc -k K`; IC3 `check_ltlspec_ic3` | BMC `timed_check_ltlspec -b -k K`; IC3 `timed_check_ltlspec` |
| `PSLSPEC` (`NuXmvPslSpecification`) | BDD `check_pslspec` | — | — |
| `INVARSPEC` (`NuXmvInvarSpecification`) | BDD `check_invar`; k-индукция `check_invar_bmc -a een-sorensson -k K`\*; IC3 `check_invar_ic3`\* | BMC-индукция `msat_check_invar_bmc`; k-индукция `msat_check_invar_bmc -a een-sorensson -k K`; IC3 `check_invar_ic3` | IC3 `timed_check_invar`; BMC `timed_check_invar -b -k K` |

\* — требуется `go_bmc`.

Строки упорядочены так, как nuXmv нумерует свойства. Первым в ячейке стоит алгоритм по умолчанию в плагине; для timed
LTL это BMC, потому что IC3 на `G F s` не завершился за 2 минуты (2.1.0).

Ограничения (2.1.0):

- у `check_invar_bmc` и `msat_check_invar_bmc` ключ `-k` допустим только с `-a een-sorensson` («Option -k can be used only
  when 'een-sorensson' algorithm is selected»);
- IC3 на бесконечном домене может ответить «is unknown» или не завершиться;
- COMPUTE корректен только при тотальном FSM (`check_fsm`, мануал стр. 71).

Вне плагина пока остаются:

- `PARSYNTH` → `synth_param` (стр. 149);
- reqan: `reqan_check_consistency/assertion/possibility` и timed-аналоги;
- инкрементальный COI: `check_*_inc_coi*`;
- `check_ltlspec_compositional`, `check_invar_guided`, CEGAR/implabs;
- `check_fsm`, `compute_reachable`, `print_reachable_states`;
- конвертеры: `write_flat_model`, `write_aiger_model`, `write_vmt_model`, `write_untimed_model`.

## D. Симуляция

| Домен | Команды | Опции |
|---|---|---|
| Finite | `go`, `pick_state [-r]`, `simulate [-r] -k N` | `-r` — случайный выбор, иначе детерминированный; `-i` — интерактивный выбор, `-c`/`-t` — ограничения |
| Infinite | `go_msat`, `msat_pick_state`, `msat_simulate -k N` | `-i`, `-c`/`-t`/`-p`; случайного выбора нет |
| Timed | `-time`, `go_time`, `timed_pick_state`, `timed_simulate -k N` | `-i`, `-c`/`-t`; случайного выбора нет |

Вывод у всех один: `Trace Type: Simulation`, состояния `-> State: n.m <-`. В timed между состояниями идут строки
`-- [ time elapse: … ] --` и `-- [ discrete transition ] --`.

## E. Спецификации модели и база свойств nuXmv (2.1.0)

- После `go` все спецификации лежат в базе свойств (`show_property`). Индексы раздаются **по видам**, а не по порядку в
  файле: CTL → COMPUTE → LTL → PSL → INVAR.
- Спецификация модуля размножается по экземплярам. `LTLSPEC NAME live` в `counter` при `a, b : counter` даёт свойства
  `a.live` и `b.live`; безымянная спецификация печатается с контекстом: `c < 4 IN a`.
- Как выбрать свойство:
  - `-P name` — для именованных (`-P p1`, `-P a.live`);
  - `-n idx` — требует совпадения вида («specified property type is LTL, but type Invar was expected»);
  - `-p "… IN ctx"` — добавляет в базу новое свойство-дубликат.
- Команда без `-n/-P/-p` проверяет все свойства своего вида.
- nuXmv перепечатывает формулы с другой расстановкой скобок и пробелов (`G (s -> Y !s | !s)` → `G (s -> ( Y !s | !s))`).
  Плагин сравнивает формулы после удаления пробелов и скобок.
- Ограничения на уровне формулы:
  - past-операторы (`Y Z H O S T Y~ @O~`) не поддерживает `msat_check_ltlspec_bmc` («past operators are currently
    unsupported by mathsat LTL BMC»); `check_ltlspec_bmc`, `check_ltlspec_ic3` и BDD их принимают;
  - bounded LTL (`F [1, 3]`) принимают все LTL-алгоритмы;
  - `next` в `INVARSPEC` принимают `check_invar`, `check_invar_bmc`, `check_invar_ic3`, `msat_check_invar_bmc`;
  - RTCTL (`EBF m..n`, `A [p BU m..n q]`) — только в CTL, то есть только BDD;
  - инвариантные алгоритмы и `check_property_as_invar_ic3` игнорируют `FAIRNESS`/`JUSTICE`/`COMPASSION` (стр. 125).
- Спецификации внутри `#FOR` (макрос плагина) nuXmv видит уже развёрнутыми, поэтому отдельно их выбрать нельзя — только
  весь вид целиком.

## F. Выходные данные, которые разбирает плагин

| Строка | Где встречается |
|---|---|
| `-- specification F is true/false` | CTL, LTL (BDD/BMC), PSL |
| `-- LTL specification F is true/false/unknown` | `check_ltlspec_ic3`, `timed_check_ltlspec` |
| `-- invariant F is true/false` | все INVAR-проверки |
| `-- cannot prove the invariant F is true or false : the induction failed` | `msat_check_invar_bmc` (classic), трейс «Induction Sequence» |
| `-- the result of MIN [ … ] is N` | `check_compute`, трейса нет |
| `Trace Description:` / `Trace Type:` / `-> State: n.m <-` / `-- Loop starts here` | любой трейс |
| `<!-- ### Trace number: N ### -->` | `show_traces -a -v` |
