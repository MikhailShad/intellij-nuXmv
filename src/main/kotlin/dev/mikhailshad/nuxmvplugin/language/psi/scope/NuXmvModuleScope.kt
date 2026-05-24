package dev.mikhailshad.nuxmvplugin.language.psi.scope

import dev.mikhailshad.nuxmvplugin.language.psi.*

/**
 * Pre-computed lookup tables for a single `NuXmvModule`.
 *
 * The same `NuXmvNamedElement` references appear both in [declarations] and in the kind-specific
 * maps — there is no duplication of underlying PSI, only of map entries. The instance is cached
 * per-module via `CachedValuesManager` and invalidated on `PsiModificationTracker.MODIFICATION_COUNT`.
 *
 * See `NuXmvScopes.of(module)` for the cached accessor.
 */
class NuXmvModuleScope(
    val module: NuXmvModule,
    /** Combined namespace of every named element declared in the module. First-wins on collisions. */
    val declarations: Map<String, NuXmvNamedElement>,
    /** State / input / frozen variable names declared in the module. */
    val variables: Map<String, NuXmvVarName>,
    /** Defines (macros) declared in the module. */
    val defines: Map<String, NuXmvDefineName>,
    /** Module parameters declared on the `MODULE` header. */
    val parameters: Map<String, NuXmvModuleParameter>,
    /** `varName -> moduleType` for variables instantiated as submodules. */
    val submoduleEdges: Map<String, String>,
    /** Assign constraints grouped by the variable they target (raw text of the LHS identifier). */
    val assignmentsByVar: Map<String, List<NuXmvSingleAssignConstraint>>,
    /** Names that were declared more than once inside this module. */
    val ambiguous: Set<String>,
    /** All ASSIGN sections of the module, exposed for callers that iterate without grouping. */
    val assignConstraints: List<NuXmvAssignConstraint>
)
