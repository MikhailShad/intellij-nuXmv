# Changelog

## [Unreleased]

- Fixed binary minus before a number (`x -1`, `3-1`): the sign is no longer glued to the number by the lexer
- Fixed word constants without a sign specifier and with `_` separators (`0b_0101_1111`)
- Fixed fractional (`f'1/3`) and exponential with fractional mantissa (`1.5E-7`) real constants
- Fixed `O` (once) being treated as `H`, and swapped `AX`/`AG` token types
- Support `PRED`, `READ`, `WRITE`, `CONSTARRAY`, `typeof`, `pi` keywords, `@TIME_DOMAIN`, `X~`, `Y~`, `+oo`
- `at next` / `at last` accept any amount of whitespace between words
- `time`, `clock`, `Clock` are no longer reserved outside timed models
- Support set expressions `{a, b}`, bit selection `w[7:4]`, `signed/unsigned word[N](e)` conversions, `CONSTARRAY(typeof(a), e)`, array DEFINEs `[[1, 2], [3, 4]]`
- Support calls of functions declared with a complex identifier (`m.f(x)`)
- Support module instances without parentheses (`VAR inst : counter;`) and constants/DEFINEs in word widths and range bounds (`word[N]`, `0..N`)
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
- Timed models: `time` is a built-in constant; `clock`, `URGENT`, `X~`, `Y~`, `time_until`, `time_since` are reported outside `@TIME_DOMAIN continuous`; unknown time domains are errors
- Identifiers inside `PSLSPEC` are not reported as unresolved; PSL keywords are highlighted
- Completion: `FUN`, `PSLSPEC`, `PARSYNTH`, `URGENT` sections, `typeof`, `pi`, word/int array types; `clock`, `X~`, `Y~`, `time`, `time_until`, `time_since` in timed models only
- Folding, structure view and breadcrumbs for `PSLSPEC`, `URGENT`, `PARSYNTH`
- Formatter keeps PSL bodies intact and formats LTL bounds as `G [0, 5] p`, `F [2, +oo) p`
- Fixed navigation bar on IntelliJ 2026.2+ (`StructureAwareNavBarModelExtension` was removed from the platform)
- Structure view navigation no longer calls the override-only `Navigatable.navigate` (IntelliJ 2026.3)
- Replaced deprecated `FileChooserDescriptorFactory.createSingleFileDescriptor()` in the nuXmv executable setting

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
