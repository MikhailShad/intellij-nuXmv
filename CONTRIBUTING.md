# Contributing to NuXmv Plugin

Thank you for considering contributing to the NuXmv Plugin for IntelliJ IDEA. This document provides guidelines for
contributing to the project and explains the architecture to help you understand how different parts of the plugin work
together.

## Project Architecture

The NuXmv Plugin is built using the IntelliJ Platform SDK and leverages the Grammar-Kit for parser and lexer generation.
The project is primarily written in Kotlin.

The project is organized into two main packages:

### 1. `language` Package

Contains the core language implementation, including:

- **Core Language Files**: Base language definition, file type registration, and icons
- **lexer**: Defines lexical analysis (tokenization) for the NuXmv language
- **parser**: Contains the parser definition and implementation
- **psi**: Program Structure Interface definitions and implementations
    - **impl**: Contains implementation classes for PSI elements
    - **mixin**: Contains PSI element implementations with custom behavior
- **completion**: Provides code completion for various language constructs (keywords, operators, variables, etc.)
- **documentation**: Handles documentation popups and quick help
- **reference**: Manages references between declarations and usages
- **utils**: Utility functions for working with the NuXmv language
    - **MacroExpansionService**: Handles FOR loop macro expansion

### 2. `ide` Package

Contains IDE-specific features and integrations:

- **annotator**: Provides semantic highlighting and error annotations
- **breadcrumbs**: Shows element navigation breadcrumbs at the top of the editor
- **configuration**: Plugin settings and configuration
- **editor**: Editor-specific features and customizations
- **findusages**: Implements "Find Usages" functionality
- **folding**: Code folding support for NuXmv constructs
- **format**: Code formatting and commenter support
- **highlighter**: Syntax highlighting definitions
- **marker**: Line markers for navigation and actions
- **navigation**: Navigation-related features
- **refactoring**: Support for refactoring operations
- **run**: Run configuration and execution support
    - **command**: NuXmv command definitions for different engines (BDD, MSAT)
    - **configuration**: Run configuration types and settings
    - **visualization**: Model and counterexample visualization
        - **model**: State model representation classes
        - **parser**: Output parsing for visualization
- **settings**: Code style settings
- **structure**: Structure view implementation for file outline

## Key Files

- **nuXmv.flex**: JFlex lexer definition that tokenizes the NuXmv language
- **nuXmv.bnf**: Grammar-Kit BNF grammar definition for the NuXmv language parser
- **MacroExpansionService.kt**: Implementation of the FOR loop macro functionality
- **NuXmvModelVisualizer.kt**: Implementation of the state model visualization
- **ModelStateAnalyzer.kt**: Analyzer for state transitions and model properties

## Development Workflow

### Setting Up the Environment

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Ensure you have the [Grammar-Kit](https://plugins.jetbrains.com/plugin/6606-grammar-kit)
   and [PsiViewer](https://plugins.jetbrains.com/plugin/227-psiviewer) plugins installed in your development environment
4. Run `./gradlew build` to build the project

### Making Changes

1. **Lexer Changes**:
    - Modify `nuXmv.flex`
    - Run the JFlex generator to produce the lexer class

2. **Grammar Changes**:
    - Modify `nuXmv.bnf`
    - Run the Grammar-Kit generator to produce parser and PSI classes

3. **Behavior Changes**:
    - Implement or modify classes in the appropriate packages

4. **Visualization Changes**:
    - Update HTML/JS in `resources/visualization`
    - Modify visualization model classes and parsers

5. **Macro Support**:
    - Enhance the macro expansion service for additional functionality

### Testing Your Changes

Run the plugin in a development instance of IDEA:

```
./gradlew runIdeWithPsiViewer
```

Execute tests with:

```
./gradlew test
```

## Style Guidelines

- Use Kotlin idioms and follow Kotlin coding conventions
- Keep code modular and focused on single responsibilities
- Write unit tests for new features
- Comment complex code sections
- Keep the BNF grammar readable and well-structured
- Maintain clear separation between parsing and PSI implementation

## Submitting Changes

1. Create a new branch for your feature or bugfix
2. Make your changes following the guidelines above
3. Write appropriate tests
4. Submit a pull request with a clear description of the changes
5. Ensure CI checks pass

## Resources

- [IntelliJ Platform SDK Documentation](https://plugins.jetbrains.com/docs/intellij/welcome.html)
- [Grammar-Kit Documentation](https://github.com/JetBrains/Grammar-Kit)
- [NuXmv Documentation](https://nuxmv.fbk.eu/documentation.html)
- [JCEF Documentation](https://plugins.jetbrains.com/docs/intellij/jcef.html) (for visualization)
