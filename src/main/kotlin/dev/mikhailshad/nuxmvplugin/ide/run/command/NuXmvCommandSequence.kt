package dev.mikhailshad.nuxmvplugin.ide.run.command

/**
 * Feeds the commands of a [NuXmvLaunchPlan] to the interactive shell one prompt at a time.
 * When the plan checks unnamed specifications, it first asks nuXmv for its property list and picks indices from it.
 *
 * @param onProblem receives messages about specifications that could not be found in the nuXmv property database
 */
class NuXmvCommandSequence(private val plan: NuXmvLaunchPlan, private val onProblem: (String) -> Unit = {}) {
    private val pending = ArrayDeque<String>()
    private var checksQueued = false

    init {
        pending += plan.setup
        if (plan.needsPropertyList) pending += SHOW_PROPERTY else queueChecks(emptyList())
    }

    /** The next command to send, given the command whose output ended with the prompt; `null` when nothing is left. */
    fun next(lastCommand: String?, lastOutput: String): String? {
        if (!checksQueued && lastCommand == SHOW_PROPERTY) {
            queueChecks(NuXmvPropertyResolver.parsePropertyList(lastOutput))
        }
        return pending.removeFirstOrNull()
    }

    private fun queueChecks(properties: List<NuXmvProperty>) {
        checksQueued = true
        for (check in plan.checks) {
            pending += when (check) {
                is NuXmvPlannedCheck.AllOfKind -> listOf(check.algorithm.commandLine(plan.bmcBound))
                is NuXmvPlannedCheck.Single -> selectorsOf(check, properties)
                    .map { check.algorithm.commandLine(plan.bmcBound, it) }
            }
        }
        pending += plan.simulation
        pending += plan.tail
    }

    private fun selectorsOf(check: NuXmvPlannedCheck.Single, properties: List<NuXmvProperty>): List<String> {
        NuXmvPropertyResolver.namedSelectors(check.spec)?.let { return it }
        val selectors = NuXmvPropertyResolver.indexSelectors(check.spec, properties)
        if (selectors.isEmpty()) {
            onProblem(
                "${check.spec.kind} specification '${check.spec.text}' was not found in the nuXmv property list; " +
                        "give it a NAME to select it reliably"
            )
        }
        return selectors
    }

    companion object {
        const val SHOW_PROPERTY = "show_property"
    }
}
