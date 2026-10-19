# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an IntelliJ IDEA plugin for the NuXmv formal verification tool. The plugin provides language support, run configurations, and visualization tools for NuXmv model files (.smv, .xmv, .nuxmv).

## Development Commands

```bash
./gradlew build                 # Build the plugin
./gradlew runIdeWithPsiViewer   # Launch a sandbox IDE with the PsiViewer plugin preinstalled
./gradlew test                  # Run tests
./gradlew verifyPlugin          # Validate plugin.xml and compatibility
./gradlew publishPlugin         # Publish to JetBrains Marketplace (requires .env)
```

Signing and publishing require a project-root `.env` defining `CERTIFICATE_CHAIN_FILE`, `PRIVATE_KEY_FILE`, `PRIVATE_KEY_PASSWORD`, and `PUBLISH_TOKEN` — see the `signing { }` / `publishing { }` blocks in `build.gradle.kts`. `./gradlew build` works without these.

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

`annotator/`, `breadcrumbs/`, `configuration/`, `editor/`, `findusages/`, `folding/`, `format/`, `highlighter/`, `marker/`, `navigation/`, `refactoring/`, `settings/`, `structure/` — one package per IntelliJ extension point family. New IDE features generally add a class here and register it in `plugin.xml`.

`ide/run/` is the largest subtree:
- `command/` — builds CLI invocations per verification engine (BDD, MSAT) and spec type (CTL, LTL, INVAR).
- `configuration/` — `NuXmvRunConfiguration` and settings editor.
- `visualization/` — JCEF browser visualization. `model/` holds the state/trace data classes; `parser/` (`CommandOutputParser` and its per-command subclasses) parses the NuXmv stdout into traces; the HTML/JS shell lives in `src/main/resources/visualization/`.

NuXmv has two domain modes the plugin distinguishes throughout: **Finite** (BDD-based) and **Infinite** (SMT/MSAT-based). The run/command layer branches on this.

## Regenerating the Lexer and Parser

Regeneration is part of the Gradle build: `compileKotlin` depends on `generateNuXmvLexer` → `generateNuXmvParser` → `cleanNuXmvGrammarOutputs` (see `build.gradle.kts`). Any `./gradlew compileKotlin`/`test`/`build` rewrites `src/main/gen/` from the grammar — no IDE action is needed. The generated lexer lands in `gen/.../language/lexer/_NuXmvLexer.java`, the parser in `gen/.../language/parser/`, PSI in `gen/.../language/psi/`.

For the full checklist of adding a language construct (lexer → grammar → mixin → highlighting/completion/references → tests), use the `add-language-feature` skill.

When changing PSI mixin assignments in `nuXmv.bnf` (the `mixin=` / `implements=` attributes), the corresponding class must already exist under `language/psi/mixin/` or `gen/` compilation will fail.

## Working with `plugin.xml`

Every IDE-facing class must be registered in `src/main/resources/META-INF/plugin.xml` under the appropriate `<extensions>` element. Adding a Kotlin class alone has no effect.

## Entry-Point Classes

When orienting in the codebase, these are the load-bearing classes other code hangs off of:

- `NuXmvLanguage` / `NuXmvFileType` — language and `.smv`/`.xmv`/`.nuxmv` file type registration.
- `NuXmvRunConfiguration` — run configuration; see `ide/run/configuration/`.
- `NuXmvModelVisualizer` — JCEF visualization host.
- `CommandOutputParser` (+ `ShowTracesOutputParser`, `InvarCheckCommandOutputParser`, `TimedLogicCheckCommandOutputParser`) — parse NuXmv stdout into the counterexample/trace model.
- `MacroExpansionService` — `#FOR ... #END ENDFOR` macro expansion.

## Design Decisions

`context/decisions.md` is the project's decision log (D-001, D-002, …). Check it before changing grammar, lexer or
run/visualization behavior — an existing entry may already settle the question. When a task involves a non-obvious
choice (rejected alternatives, a trade-off, a deviation from the nuXmv manual), add an entry and a row to its index
table; when a decision is revised, mark the old entry `заменено D-xxx` instead of deleting it.

## Agent Tooling

- The IDEA MCP server (`mcp__idea__*`) is connected to this project and is the primary code-intelligence tool; the Kotlin/Java LSP plugins are disabled here (no binaries installed).
  - After editing Kotlin, run `get_file_problems` on the changed files — it reports unresolved IntelliJ Platform symbols that grep cannot catch.
  - Use `search_symbol` / `get_symbol_info` to navigate PSI and platform APIs, `rename_refactoring` for renames across Kotlin, `.bnf` and `plugin.xml`.
  - Prefer `./gradlew test --tests '<Class>.<method>'` over run configurations for tests; output is easier to read.
- Project hooks (`.claude/hooks/`, wired in `.claude/settings.json`):
  - edits under `src/main/gen/` are rejected;
  - editing `nuXmv.bnf` / `nuXmv.flex` triggers `./gradlew compileKotlin`, and failures come back as hook feedback — fix them before continuing;
  - publishing/signing the plugin and reading `.env` are blocked for every tool; these steps belong to the user.

## Code Style

Code should be self-explanatory without comments. Public classes and their public members may carry KDoc; private helpers and one-off implementation details should not. Tests use the IntelliJ Platform testing framework with fixtures in `src/test/testData/`.
