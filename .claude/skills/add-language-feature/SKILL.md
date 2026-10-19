---
name: add-language-feature
description: Checklist for adding or fixing a nuXmv language construct end to end — lexer token, grammar rule, PSI mixin, highlighting, completion, references, docs and tests. Use whenever a task touches nuXmv.flex or nuXmv.bnf, adds a keyword/operator/section/expression form, or fixes a construct that parses with PsiErrorElement.
---

# Adding a nuXmv language construct

Work test-first and verify after every step. Each step ends with a command whose output is the ground truth; do not move on while it fails.

Paths below are relative to `src/main/kotlin/dev/mikhailshad/nuxmvplugin/` unless they start with `src/`.

## 0. Pin down the syntax

- Source of truth is the nuXmv 2.2.0 manual: `context/nuxmv-2.2.0-syntax.md` if it exists, otherwise `context/nuxmv-user-manual-2.2.0.pdf`. Text extracted from the PDF drops `_` and `~` in bold — when the spelling matters, check the rendered page.
- Check `context/decisions.md` for an entry covering this construct (e.g. D-002 PSL, D-003 `time`/`clock`, D-004 negative numbers) and follow it. Record any new trade-off there.
- If `context/syntax-gap-analysis.md` exists, find the row for this construct and update its status at the end.
- Write down 2–5 concrete snippets that must parse, including edge cases (negative numbers, nesting, operator precedence against neighbours).

## 1. Failing test first

Pick the narrowest test that captures the change:

| Change | Test | Fixture |
|---|---|---|
| New/changed token | `src/test/kotlin/.../language/lexer/NuXmvLexerAdapterTest.kt` (`LexerTestCase`, inline input + expected token dump) | inline |
| New/changed grammar rule | `src/test/kotlin/.../language/parser/NuXmvParserTest.kt` — add `fun testFoo() = doTest(true, true)` | `src/test/testData/parser/Foo.smv` |
| Reference / find usages | `NuXmvIdentifierReferenceTest`, `NuXmvFindUsagesTest` | `src/test/testData/references/` |
| Completion | `NuXmvCompletionTest` | `src/test/testData/completion/` with `<caret>` |
| Formatting | `NuXmvFormattingTest` | `src/test/testData/format/{input,expected}/` |

`doTest(true, true)` fails on any `PsiErrorElement`. When the expected `Foo.txt` is missing, the first run creates it and fails — read the generated PSI tree, confirm it has the intended shape, then rerun.

```bash
./gradlew test --tests '*NuXmvParserTest.testFoo'
```

## 2. Lexer — `language/nuXmv.flex`

- Add the rule inside `<YYINITIAL>`. JFlex prefers the longest match, then the earliest rule: put multi-character tokens (`X~`, `@F~`) before their prefixes, and keywords before `IDENTIFIER`.
- Every token returned by the lexer must also be declared in the `tokens=[...]` block of `nuXmv.bnf` — the `NuXmvTypes` constant is generated from there.
- Contextual words (e.g. `time`, `clock`) stay `IDENTIFIER` and are resolved in grammar or references; do not reserve them globally.

## 3. Grammar — `language/nuXmv.bnf`

- Expression alternatives in `Expr` are ordered by precedence (manual p. 15). Insert new operators at the right level and cover the neighbouring levels in a parser fixture.
- Custom PSI behaviour goes through `mixin="...psi.mixin.NuXmvFooMixin"` plus `implements="..."`. The mixin class must exist under `language/psi/mixin/` before generation, or compilation of `src/main/gen` fails. Reuse existing mixins first: `NuXmvNamedElementMixin` (declarations), `NuXmvIdentifierUsageMixin` (usages), `NuXmvPresentableElementMixin` (structure view items).
- Static helpers referenced from the grammar live in `language/psi/NuXmvPsiUtils.kt` (`psiImplUtilClass`).

## 4. Regenerate and compile

Generation is part of the Gradle build — no IDE action is needed. `compileKotlin` depends on `generateNuXmvLexer` → `generateNuXmvParser` → `cleanNuXmvGrammarOutputs`.

```bash
./gradlew compileKotlin        # regenerates src/main/gen, then compiles
./gradlew test --tests '*NuXmvParserTest*' --tests '*NuXmvLexerAdapterTest*'
```

`src/main/gen` is gitignored: never edit or commit it. If the build fails in `src/main/gen`, the cause is in `.bnf`/`.flex` or a missing mixin.

## 5. IDE wiring — go through each item, skip only when it clearly does not apply

- **Token sets** — `language/parser/NuXmvParserDefinition.kt`, `Util`: add the token to exactly one of `KEYWORDS`, `TL_OPERATORS`, `BOOL_OPERATORS`, `MATH_OPERATORS`, `BUILTIN_OPERATORS`, `CONSTANTS`, `TYPES`, `NUMBERS`, `FUNCTION_CALLS`, `MACRO_KEYWORDS`. This drives syntax highlighting via `ide/highlighter/NuXmvSyntaxHighlighter.kt`; a token missing here is not highlighted.
- **Semantic highlighting** — `ide/highlighter/NuXmvHighlighterAnnotator.kt`, for new named PSI elements.
- **Completion** — the matching `language/completion/*CompletionProvider.kt` (keyword lists, LTL/CTL operator maps, types, section keywords), plus the pattern in `NuXmvCompletionContributor.kt` if the construct appears in a new context. Update the expected lists in `NuXmvCompletionTest`.
- **References** — new usage sites must resolve: check `language/reference/` and `language/psi/scope/NuXmvScopes.kt`.
- **Documentation** — `language/documentation/NuXmvDocumentationProvider.kt` for new declaration kinds.
- **Formatter / folding / structure** — `ide/format/NuXmvFormattingModelBuilder.kt` (spacing/indent rules use `NuXmvTypes.*`), `ide/folding/NuXmvFoldingBuilder.kt`, `ide/structure/` for new module sections.
- **Brace matcher** — `ide/editor/NuXmvBraceMatcher.kt` for new paired tokens.
- **Macro** — if the change touches numbers, ranges or identifiers, run `NuXmvForMacroMixinTest` (`#FOR` expansion in `language/utils/MacroExpansionService.kt`).
- **plugin.xml** — only if a new extension class was created.

## 6. Done criteria

```bash
./gradlew test
```

The whole suite passes, the new fixtures have no `PsiErrorElement`, and the gap-analysis row (if any) is marked done with the test name. Report which items of step 5 were changed and which were skipped as not applicable.
