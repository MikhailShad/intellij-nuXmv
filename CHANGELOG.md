# Changelog

## [0.1.0]

NuXmv 2.2.0 language support and run configurations that adapt to your model: the plugin now understands real-world
models without false errors, tells you exactly what nuXmv can check in them and runs any specification in one click.

### Added

- **Run configurations that know your model.** The plugin detects whether a model is finite, infinite (`integer`,
  `real`, `FUN`) or timed (`@TIME_DOMAIN continuous`) and shows its specifications as a tree grouped by kind. Pick the
  whole model, one kind or individual specifications, and choose how to check them: BDD, BMC, k-induction or IC3.
  Options that nuXmv cannot handle for this model are disabled with an explanation (for example, past operators with
  SMT-based BMC), and a live preview shows the exact commands that will be sent to nuXmv.
- **One-click runs from the gutter.** A run icon on `MODULE main` checks the whole model or starts a simulation, and an
  icon on every specification checks just that one. Actions and created configurations are named after what they run
  (`LTL s2`, `LTL at line 17`, `all specifications`).
- **More of nuXmv from the IDE:**
    - timed models (run with `-time`);
    - `PSLSPEC` and `COMPUTE` checks;
    - a simulation mode that picks the right engine for the model's domain.
- **Interactive sessions.** Keep the nuXmv shell open after the run and keep typing commands. Counterexamples and
  simulation traces from your own commands are visualized too.
- **Complete nuXmv 2.2.0 syntax:**
    - bounded LTL (`G [0, 5] p`, `F [2, +oo) p`, `p U [l, u] q`) and RTCTL (`EBF 0..3 p`, `A [p BU 1..2 q]`);
    - PSL specifications;
    - timed models: `clock`, `URGENT`, `X~`, `Y~`, `time_until`, `time_since`;
    - set expressions, bit selection `w[7:4]`, word conversions, `CONSTARRAY`/`typeof`, array DEFINEs, word and integer
      arrays;
    - `PRED`, `MIRROR`, `PARSYNTH`, `ISA`, named `COMPUTE`;
    - specifications evaluated `IN` an instance.
- **Flattened and generated models.** Output of `write_flat_model` (`con.s : real;`), quoted identifiers
  (`"$auto$rename.cc:157:execute$26"`), numeric name segments (`a.SSA.1.ssa`) and `a[1].b` are fully supported:
  navigation, rename, structure view and visualization use their full names.
- **Smarter editing:**
    - completion of variables, defines, constants, enum values, module parameters and functions, including declarations
      inherited through `ISA`, and the fields of an instance after `instance.`;
    - completion templates for bounded LTL operators;
    - quick documentation for temporal operators, taken from the nuXmv manual;
    - `ISA` navigates to its module and supports Find Usages and rename;
    - folding, structure view and breadcrumbs for PSL, `URGENT` and `PARSYNTH`.
- **Errors reported like nuXmv reports them:**
    - `next` where nuXmv rejects it (in `INIT`, `INVAR`, fairness constraints, `CTLSPEC`, `COMPUTE`, nested or through
      DEFINEs);
    - malformed word constants (width, digits, value range);
    - timed-only constructs outside timed models;
    - unknown time domains;
    - unknown, recursive or parameterized `ISA` modules;
    - unresolved names, with only the unknown segment of a dotted name highlighted.

### Changed

- **Operator precedence follows nuXmv.** Temporal operators bind weaker than comparisons and stronger than `&`:
  `G a & b`
  is `(G a) & b`. `?:` is right-associative.
- **`time`, `clock` and `Clock` are ordinary identifiers outside timed models.**
- **Syntax is as strict as nuXmv:**
    - `PSLSPEC` requires a terminating `;`;
    - `at next` / `at last` take exactly one space;
    - `12ab` is a syntax error, as in nuXmv.
- **Formatter:**
    - every declaration of `VAR`, `IVAR`, `FROZENVAR`, `ASSIGN` and `FUN` goes on its own line;
    - PSL bodies are kept intact;
    - LTL bounds are formatted as `G [0, 5] p`.
- **Find Usages** no longer lists the declaration itself.

### Fixed

- Counterexamples of failed invariants and values of module instance variables (`m.c`) now appear in the visualization.
- Fixed parsing of:
    - negative numbers (`x -1`, `3-1`);
    - word constants without a sign or with `_` separators (`0b_0101_1111`);
    - fractional and exponential reals (`f'1/3`, `1.5E-7`);
    - ranges inside expressions (`x in 1..3`);
    - CTL until (`E [p U q]`);
    - `time_until(…)` / `time_since(…)` with complex arguments;
    - a `FUN` section after another section;
    - `unsigned word` split across lines;
    - empty parameter lists (`MODULE m()`);
    - modules instantiated without parentheses.
- Fixed `O` (once) behaving like `H`, and swapped `AX`/`AG`.
- Fixed resolving an outer `#FOR` variable inside a nested `#FOR`, and constants and DEFINEs used as word widths or
  range
  bounds (`word[N]`, `0..N`).
- Better error recovery after a broken module section.
- Fixed a memory leak in the run console.
- Navigation bar and structure view work on IntelliJ IDEA 2026.2 and later.

## [0.0.3]

- Fixed missing referencing for constants in SPEC blocks
- Support multiple references for complex identifiers

## [0.0.2]

- Improved visualization of the model graph and control panel
- Fixed bug with unescaped nested FOR macro

## [0.0.1]

- Initial release with language support for NuXmv
- Run configuration for model verification
- Interactive model visualization
- FOR loop macro extension for compact model description

[0.1.0]: https://github.com/MikhailShad/intellij-nuXmv/compare/0.0.3...0.1.0
[0.0.3]: https://github.com/MikhailShad/intellij-nuXmv/compare/0.0.2...0.0.3
[0.0.2]: https://github.com/MikhailShad/intellij-nuXmv/compare/0.0.1...0.0.2
[0.0.1]: https://github.com/MikhailShad/intellij-nuXmv/commits/0.0.1
[Unreleased]: https://github.com/MikhailShad/intellij-nuXmv/compare/main...develop
