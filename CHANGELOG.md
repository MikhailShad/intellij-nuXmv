# Changelog

## [Unreleased]

- Fixed binary minus before a number (`x -1`, `3-1`): the sign is no longer glued to the number by the lexer
- Fixed word constants without a sign specifier and with `_` separators (`0b_0101_1111`)
- Fixed fractional (`f'1/3`) and exponential with fractional mantissa (`1.5E-7`) real constants
- Fixed `O` (once) being treated as `H`, and swapped `AX`/`AG` token types
- Support `PRED`, `READ`, `WRITE`, `CONSTARRAY`, `typeof`, `pi` keywords, `@TIME_DOMAIN`, `X~`, `Y~`, `+oo`
- `time`, `clock`, `Clock` are no longer reserved outside timed models
- Support set expressions `{a, b}`, bit selection `w[7:4]`, `signed/unsigned word[N](e)` conversions,
  `CONSTARRAY(typeof(a), e)`, array DEFINEs `[[1, 2], [3, 4]]`
- Support calls of functions declared with a complex identifier (`m.f(x)`)
- Support module instances without parentheses (`VAR inst : counter;`) and constants/DEFINEs in word widths and range
  bounds (`word[N]`, `0..N`)
- Support `array word[N] of …` and `array integer of …` types
- Fixed range constants inside expressions (`x in 1..3`)
- Fixed parsing of a `FUN` section following another module section
- `unsigned word[N]` variables are recognized as words
- Support `@TIME_DOMAIN` annotation, `URGENT` constraints and `clock` type of timed models
- Support `PSLSPEC` (tolerant parsing, SEREs with `;` inside braces)
- Support `COMPUTE NAME id := …`, `PARSYNTH` without options, `PRED <name> := …`
- `MIRROR` now references a declared variable instead of declaring a new one
- Fixed `time_until(p = q)` / `time_since(…)` with a non-trivial argument
- Improved error recovery after a broken module section
- Support bounded LTL operators `G/F/H/O [l, u]`, `U/V/S/T [l, u]` and `[l, +oo)` intervals
- Support RTCTL operators (`EBF/ABF/EBG/ABG m..n`, `A [p BU m..n q]`) in `CTLSPEC`
- Fixed CTL until `E [p U q]` / `A [p U q]`
- Timed models: `time` is a built-in constant; `clock`, `URGENT`, `X~`, `Y~`, `time_until`, `time_since` are reported
  outside `@TIME_DOMAIN continuous`; unknown time domains are errors
- Identifiers inside `PSLSPEC` are not reported as unresolved; PSL keywords are highlighted
- Completion: `FUN`, `PSLSPEC`, `PARSYNTH`, `URGENT` sections, `typeof`, `pi`, word/int array types; `clock`, `X~`,
  `Y~`, `time`, `time_until`, `time_since` in timed models only
- Folding, structure view and breadcrumbs for `PSLSPEC`, `URGENT`, `PARSYNTH`
- Formatter keeps PSL bodies intact and formats LTL bounds as `G [0, 5] p`, `F [2, +oo) p`
- Fixed navigation bar on IntelliJ 2026.2+ (`StructureAwareNavBarModelExtension` was removed from the platform)
- Structure view navigation no longer calls the override-only `Navigatable.navigate` (IntelliJ 2026.3)
- Replaced deprecated `FileChooserDescriptorFactory.createSingleFileDescriptor()` in the nuXmv executable setting
- Support flattened models (`write_flat_model` output): declarations with dotted names such as `con.s : real;` now
  resolve, navigate, rename and show in the structure view and visualization under their full name
- Ctrl+click on any segment of a dotted declaration (`con.s`) shows its usages specifically
- Support quoted identifiers (`"$auto$rename.cc:157:execute$26" : boolean;`), numeric name segments (`a.SSA.1.ssa`) and
  indices before a dot (`a[1].b`)
- Support `unsigned word` / `signed word` split by several spaces or a line break, and empty module parameter lists
  (`MODULE m()`)
- Support specifications evaluated in an instance context (`INVARSPEC t <= 3 IN m;`); identifiers inside resolve in the
  context instance
- Fixed resolving an outer `#FOR` loop variable inside a nested `#FOR`
- `Clock` is recognized as a type in timed models, like `clock`
- Report `next` where nuXmv rejects it: in INIT, INVAR, FAIRNESS, JUSTICE, COMPASSION, CTLSPEC, COMPUTE, PRED, `x :=` /
  `init(x) :=`, nested in another `next`, and through DEFINEs
- Word constants are checked like in nuXmv: the value must start with a digit, width must be in 1..2147483647, decimal
  constants need a width, digits must match the base and the value must fit the width
- Removed the stray hex-number token: `12ab` is now reported as a syntax error at `ab`, like in nuXmv
- `PSLSPEC` requires a terminating `;`, as in nuXmv
- Operator precedence follows nuXmv: `a ? b : c <-> d` is `(a ? b : c) <-> d` and `?:` is right-associative; temporal
  operators bind weaker than comparisons and stronger than `&` (`G a & b` is `(G a) & b`, `EF x = 1` is `EF (x = 1)`,
  `a & b U c` is `a & (b U c)`); `@F~`/`@O~` bind weakest
- `at next` / `at last` require exactly one space between the words, as in nuXmv
- `ISA` references its module: navigation, find usages and rename work, declarations of the included module
  (transitively) resolve in the including one, and unknown, recursive or parameterized `ISA` modules are reported
- Formatter puts every declaration of VAR, IVAR, FROZENVAR, ASSIGN and FUN sections on its own line
- Module gutter icons are registered on leaf elements (fixes a platform performance warning) and also cover `ISA`
- Find Usages no longer lists the declaration itself
- Completion offers bounded LTL templates (`G [l, u]`, `F [l, +oo)`, `p U [l, u] q`, …)
- Quick documentation for temporal operators (LTL, CTL, RTCTL, timed) with their meaning from the nuXmv manual
- Identifier completion offers constants, enum values, module parameters, functions and declarations included
  through `ISA`; after `instance.` it offers the fields of the instance module
- Unresolved reference check covers every segment of a dotted name (`inst.nope`) and highlights only the unknown
  segment

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

[0.0.3]: https://github.com/MikhailShad/intellij-nuXmv/compare/0.0.2...0.0.3
[0.0.2]: https://github.com/MikhailShad/intellij-nuXmv/compare/0.0.1...0.0.2
[0.0.1]: https://github.com/MikhailShad/intellij-nuXmv/commits/0.0.1
[Unreleased]: https://github.com/MikhailShad/intellij-nuXmv/compare/main...develop
