# Changelog

## [Unreleased]

- Fixed binary minus before a number (`x -1`, `3-1`): the sign is no longer glued to the number by the lexer
- Fixed word constants without a sign specifier and with `_` separators (`0b_0101_1111`)
- Fixed fractional (`f'1/3`) and exponential with fractional mantissa (`1.5E-7`) real constants
- Fixed `O` (once) being treated as `H`, and swapped `AX`/`AG` token types
- Support `PRED`, `READ`, `WRITE`, `CONSTARRAY`, `typeof`, `pi` keywords, `@TIME_DOMAIN`, `X~`, `Y~`, `+oo`
- `at next` / `at last` accept any amount of whitespace between words
- `time`, `clock`, `Clock` are no longer reserved outside timed models

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
