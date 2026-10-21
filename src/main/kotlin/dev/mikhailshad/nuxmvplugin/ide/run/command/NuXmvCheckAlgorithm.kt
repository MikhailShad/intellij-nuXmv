package dev.mikhailshad.nuxmvplugin.ide.run.command

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType.*
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecFeature
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind.*

/**
 * A nuXmv command that checks specifications of one kind in one domain.
 *
 * @property command the command without a property selector and without a bound
 * @property usesBound the command takes the BMC bound as `-k`
 * @property needsBooleanModel on a finite domain the command needs `go_bmc` after `go`
 * @property unsupported formula features the command rejects
 */
enum class NuXmvCheckAlgorithm(
    val kind: NuXmvSpecKind,
    val domain: NuXmvDomainType,
    val displayName: String,
    val command: String,
    val usesBound: Boolean = false,
    val needsBooleanModel: Boolean = false,
    val unsupported: Set<NuXmvSpecFeature> = emptySet()
) {
    CTL_BDD(CTL, FINITE_DOMAIN, "BDD", "check_ctlspec"),
    COMPUTE_BDD(COMPUTE, FINITE_DOMAIN, "BDD", "check_compute"),
    PSL_BDD(PSL, FINITE_DOMAIN, "BDD", "check_pslspec"),

    LTL_BDD(LTL, FINITE_DOMAIN, "BDD", "check_ltlspec"),
    LTL_BMC(LTL, FINITE_DOMAIN, "BMC", "check_ltlspec_bmc", usesBound = true, needsBooleanModel = true),
    LTL_IC3(LTL, FINITE_DOMAIN, "IC3", "check_ltlspec_ic3", needsBooleanModel = true),
    INVAR_BDD(INVAR, FINITE_DOMAIN, "BDD", "check_invar"),
    INVAR_K_INDUCTION(
        INVAR, FINITE_DOMAIN, "k-induction", "check_invar_bmc -a een-sorensson",
        usesBound = true, needsBooleanModel = true
    ),
    INVAR_IC3(INVAR, FINITE_DOMAIN, "IC3", "check_invar_ic3", needsBooleanModel = true),

    MSAT_LTL_BMC(
        LTL, INFINITE_DOMAIN, "BMC (SMT)", "msat_check_ltlspec_bmc",
        usesBound = true, unsupported = setOf(NuXmvSpecFeature.PAST)
    ),
    MSAT_LTL_IC3(LTL, INFINITE_DOMAIN, "IC3 (SMT)", "check_ltlspec_ic3"),
    MSAT_INVAR_BMC(INVAR, INFINITE_DOMAIN, "BMC induction (SMT)", "msat_check_invar_bmc"),
    MSAT_INVAR_K_INDUCTION(
        INVAR, INFINITE_DOMAIN, "k-induction (SMT)", "msat_check_invar_bmc -a een-sorensson", usesBound = true
    ),
    MSAT_INVAR_IC3(INVAR, INFINITE_DOMAIN, "IC3 (SMT)", "check_invar_ic3"),

    TIMED_LTL_BMC(LTL, TIMED_DOMAIN, "BMC", "timed_check_ltlspec -b", usesBound = true),
    TIMED_LTL_IC3(LTL, TIMED_DOMAIN, "IC3", "timed_check_ltlspec"),
    TIMED_INVAR_IC3(INVAR, TIMED_DOMAIN, "IC3", "timed_check_invar"),
    TIMED_INVAR_BMC(INVAR, TIMED_DOMAIN, "BMC", "timed_check_invar -b", usesBound = true);

    /** The command line for nuXmv; [selector] is `-n idx` or `-P name`, absent to check every property of the kind. */
    fun commandLine(bound: Int, selector: String? = null): String = buildString {
        append(command)
        if (usesBound) append(" -k ").append(bound)
        if (selector != null) append(' ').append(selector)
    }

    override fun toString(): String = displayName

    companion object {
        fun of(domain: NuXmvDomainType, kind: NuXmvSpecKind): List<NuXmvCheckAlgorithm> =
            entries.filter { it.domain == domain && it.kind == kind }

        fun fromName(name: String?): NuXmvCheckAlgorithm? = entries.firstOrNull { it.name == name }
    }
}
