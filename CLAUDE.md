# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an IntelliJ IDEA plugin for the NuXmv formal verification tool. The plugin provides language support, run
configurations, and visualization tools for NuXmv model files (.smv, .xmv, .nuxmv).

## Development Commands

```bash
./gradlew buildPlugin           # Build the plugin
./gradlew runIdeWithPsiViewer   # Launch a sandbox IDE with the PsiViewer plugin preinstalled
./gradlew test                  # Run tests
./gradlew verifyPlugin          # Validate plugin.xml and compatibility
```

Signing the plugin and publishing it to JetBrains Marketplace are manual release steps done by the maintainer; agents
never run them. `./gradlew buildPlugin` does not need the release credentials.

Plugin metadata (id, name, version, `sinceBuild`) lives in `gradle.properties`, not `build.gradle.kts`.

## Source Layout

```
src/main/kotlin/   Hand-written Kotlin sources
src/main/gen/      Generated lexer/parser/PSI (registered as a java srcDir, gitignored, produced by Gradle)
src/main/resources/META-INF/plugin.xml  Extension point registrations
src/test/kotlin/   Tests (JUnit 4)
src/test/testData/ Test fixtures
```

`src/main/gen` is **git-ignored** — never edit or commit it; it is rebuilt from `nuXmv.flex`/`nuXmv.bnf` on every
compile.

## Architecture

Two top-level packages under `dev.mikhailshad.nuxmvplugin`:

### `language/` — language definition

- `nuXmv.flex`, `nuXmv.bnf` — JFlex lexer and Grammar-Kit grammar; the source of truth for `src/main/gen`.
- `psi/` — handwritten PSI extensions; `psi/mixin/` holds the mixin classes referenced from `nuXmv.bnf` via `mixin=`
  (custom behavior for generated PSI nodes). Implementation classes generated into `gen/.../psi/impl/`.
- `completion/`, `documentation/`, `reference/` — IntelliJ extensions hanging off the PSI.
- `utils/MacroExpansionService` — expands the `#FOR ... #END ENDFOR` macro documented in README.md.

### `ide/` — IDE integrations

`annotator/`, `breadcrumbs/`, `configuration/`, `editor/`, `findusages/`, `folding/`, `format/`, `highlighter/`,
`marker/`, `navigation/`, `refactoring/`, `settings/`, `structure/` — one package per IntelliJ extension point family.
New IDE features generally add a class here and register it in `plugin.xml`.

`ide/run/` is the largest subtree:

- `command/` — turns a model profile and a run configuration into nuXmv commands: `NuXmvCheckAlgorithm` (spec kind ×
  domain → command), `NuXmvApplicability`, `NuXmvLaunchPlanner`, `NuXmvCommandSequence` (resolves unnamed specs through
  `show_property` at run time). The run modes and their sources are in `context/nuxmv-run-modes.md`.
- `configuration/` — `NuXmvRunConfiguration` and settings editor.
- `visualization/` — JCEF browser visualization. `model/` holds the state/trace data classes; `parser/`
  (`CommandOutputParser` and its subclasses) parses the NuXmv stdout into traces; the HTML/JS shell lives in
  `src/main/resources/visualization/`.

NuXmv has three domain modes the plugin distinguishes throughout: **Finite** (BDD-based), **Infinite**
(SMT/MSAT-based) and **Timed** (`@TIME_DOMAIN continuous`, run with `-time`). The run/command layer branches on this.

## Regenerating the Lexer and Parser

Regeneration is part of the Gradle build: `compileKotlin` depends on `generateNuXmvLexer` → `generateNuXmvParser` →
`cleanNuXmvGrammarOutputs` (see `build.gradle.kts`). Any `./gradlew compileKotlin`/`test`/`build` rewrites
`src/main/gen/` from the grammar — no IDE action is needed. The generated lexer lands in
`gen/.../language/lexer/_NuXmvLexer.java`, the parser in `gen/.../language/parser/`, PSI in `gen/.../language/psi/`.

For the full checklist of adding a language construct (lexer → grammar → mixin → highlighting/completion/references →
tests), use the `add-language-feature` skill.

When changing PSI mixin assignments in `nuXmv.bnf` (the `mixin=` / `implements=` attributes), the corresponding class
must already exist under `language/psi/mixin/` or `gen/` compilation will fail.

## Working with `plugin.xml`

Every IDE-facing class must be registered in `src/main/resources/META-INF/plugin.xml` under the appropriate
`<extensions>` element. Adding a Kotlin class alone has no effect.

## Entry-Point Classes

When orienting in the codebase, these are the load-bearing classes other code hangs off of:

- `NuXmvLanguage` / `NuXmvFileType` — language and `.smv`/`.xmv`/`.nuxmv` file type registration.
- `NuXmvRunConfiguration` — run configuration; see `ide/run/configuration/`.
- `NuXmvModelVisualizer` — JCEF visualization host.
- `NuXmvModelProfile` (`language/utils/`) — cached domain and specification list of a model; the run configuration,
  producers and run gutter icons decide what is available from it.
- `NuXmvLaunchPlanner` — builds the commands of a run from the profile and the configuration.
- `CommandOutputParser` (+ `TraceBlockOutputParser`, `ShowTracesOutputParser`) — parse NuXmv stdout into the
  counterexample/trace model by output content, not by the command that produced it.
- `MacroExpansionService` — `#FOR ... #END ENDFOR` macro expansion.
- `NuXmvScopes` (`language/psi/scope/`) — cached per-module declaration tables (including `ISA`-included modules) and
  per-file module tables; resolve, completion and annotators go through it instead of walking PSI.
- `NuXmvIdentifierReference` — one reference per span of a dotted name (`inst.field`, flat `con.s`); exposes the span's
  lookup `module` and `isQualified`.
- `NuXmvWarningAnnotator` — semantic errors nuXmv reports but the tolerant grammar accepts (unresolved names, `next`
  placement, timed-only constructs, word constants, `ISA`).

## Design Decisions

`context/decisions.md` is the project's decision log (D-001, D-002, …). Check it before changing grammar, lexer or
run/visualization behavior — an existing entry may already settle the question. When a task involves a non-obvious
choice (rejected alternatives, a trade-off, a deviation from the nuXmv manual), add an entry and a row to its index
table; when a decision is revised, mark the old entry `заменено D-xxx` instead of deleting it.

`context/nuxmv-2.2.0-grammar.md` transcribes the manual's grammar; `context/grammar-compliance.md` records how the lexer
and parser compare with it (R1, R2, …).

## Verification Against nuXmv

The nuXmv binary is the ground truth for syntax and semantic errors: check fixtures and new diagnostics on it, and when
it disagrees with the manual, follow the binary and record a decision. Grammar and resolve changes are also checked
against a local corpus of models. Neither is part of the repository; where they live and how to run them is
machine-specific and kept in local project memory.

## Workflow

- Every user-visible fix or feature gets a one-line bullet in `CHANGELOG.md` under `[Unreleased]`, in the same commit.
- Commit only after the user agrees; a commit that resolves a GitHub issue ends its message body with `Closes #N`.

## Agent Tooling

- The IDEA MCP server (`mcp__idea__*`) is connected to this project and is the primary code-intelligence tool; the
  Kotlin/Java LSP plugins are disabled here (no binaries installed).
    - After editing, run `reformat_file` and then `get_file_problems` on every changed `.kt`/`.kts`/`.bnf`/`.flex`/`.md`
      file — the latter reports unresolved IntelliJ Platform symbols that grep cannot catch. Never reformat
      `src/test/testData/` (it rewrites inputs and expected outputs) or `src/main/gen/`.
    - Use `search_symbol` / `get_symbol_info` to navigate PSI and platform APIs, `rename_refactoring` for renames across
      Kotlin, `.bnf` and `plugin.xml`.
    - Prefer `./gradlew test --tests '<Class>.<method>'` over run configurations for tests; output is easier to read.
- GitHub issues live at `MikhailShad/intellij-nuXmv`. If neither `gh` nor the GitHub MCP server is available, read them
  through the public REST API (`curl https://api.github.com/repos/MikhailShad/intellij-nuXmv/issues`).
- Project hooks (`.claude/hooks/`, wired in `.claude/settings.json`):
    - edits under `src/main/gen/` are rejected;
    - editing `nuXmv.bnf` / `nuXmv.flex` triggers `./gradlew compileKotlin`, and failures come back as hook feedback —
      fix them before continuing;
    - running the Gradle publish/sign tasks and reading `.env` are blocked for every tool; these steps belong to the
      user.

## Code Style

Code should be self-explanatory without comments. Public classes and their public members may carry KDoc; private
helpers and one-off implementation details should not. Tests use the IntelliJ Platform testing framework with fixtures
in `src/test/testData/`.
