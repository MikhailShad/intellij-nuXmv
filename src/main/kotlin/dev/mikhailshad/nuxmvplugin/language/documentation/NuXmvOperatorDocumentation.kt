package dev.mikhailshad.nuxmvplugin.language.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.tree.IElementType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

/** Short descriptions of temporal operators, following the semantics in the nuXmv 2.2.0 manual, §2.5.1–2.5.4. */
object NuXmvOperatorDocumentation {
    private class OperatorDoc(val syntax: String, val description: String, val section: String)

    private const val CTL = "§2.5.1 CTL Specifications"
    private const val LTL = "§2.5.3 LTL Specifications"
    private const val TTS = "§2.5.3 LTL Specifications in TTS"
    private const val RTCTL = "§2.5.4 Real Time CTL Specifications"

    private val docs: Map<IElementType, OperatorDoc> = mapOf(
        NuXmvTypes.LTL_NEXT to OperatorDoc("X p", "next state: X p is true at time t if p is true at time t + 1.", LTL),
        NuXmvTypes.LTL_GLOBALLY to OperatorDoc(
            "G p, G [l, u] p",
            "globally: G p is true at time t if p is true at all times t' ≥ t. " +
                    "With a bound, p must hold at all times t + l ≤ t' ≤ t + u.",
            LTL
        ),
        NuXmvTypes.LTL_FINALLY to OperatorDoc(
            "F p, F [l, u] p",
            "finally: F p is true at time t if p is true at some time t' ≥ t. " +
                    "With a bound, p must hold at some time t + l ≤ t' ≤ t + u.",
            LTL
        ),
        NuXmvTypes.TL_UNTIL to OperatorDoc(
            "p U q, p U [l, u] q, E [p U q], A [p U q]",
            "until: p U q is true at time t if q is true at some time t' ≥ t and p is true at all times t ≤ t'' < t'. " +
                    "With a bound, t + l ≤ t' ≤ t + u. In CTL, E [p U q] and A [p U q] quantify over paths.",
            LTL
        ),
        NuXmvTypes.LTL_RELEASES to OperatorDoc(
            "p V q, p V [l, u] q",
            "releases: q holds at all time steps t' ≥ t up to and including the step where p also holds; " +
                    "if p never holds, q must hold at all time steps t' ≥ t.",
            LTL
        ),
        NuXmvTypes.LTL_PREVIOUS to OperatorDoc(
            "Y p",
            "previous state: Y p is true at time t > t0 if p holds at time t − 1; Y p is false at time t0.",
            LTL
        ),
        NuXmvTypes.LTL_NOT_PREVIOUS to OperatorDoc(
            "Z p",
            "not previous state not: equivalent to Y p, except that Z p is true at time t0.",
            LTL
        ),
        NuXmvTypes.LTL_HISTORICALLY to OperatorDoc(
            "H p, H [l, u] p",
            "historically: H p is true at time t if p holds in all previous time steps t' ≤ t. " +
                    "With a bound, in all steps t − u ≤ t' ≤ t − l.",
            LTL
        ),
        NuXmvTypes.LTL_ONCE to OperatorDoc(
            "O p, O [l, u] p",
            "once: O p is true at time t if p held in at least one of the previous time steps t' ≤ t. " +
                    "With a bound, in some step t − u ≤ t' ≤ t − l.",
            LTL
        ),
        NuXmvTypes.LTL_SINCE to OperatorDoc(
            "p S q, p S [l, u] q",
            "since: p S q is true at time t if q held at time t' ≤ t and p holds in all time steps t'' with t' < t'' ≤ t.",
            LTL
        ),
        NuXmvTypes.LTL_TRIGGERED to OperatorDoc(
            "p T q, p T [l, u] q",
            "triggered: p T q is true at time t if p held at time t' ≤ t and q holds in all time steps t'' with t' ≤ t'' ≤ t; " +
                    "if p has never been true, q must hold in all time steps.",
            LTL
        ),
        NuXmvTypes.LTL_AT_NEXT to OperatorDoc(
            "expr @F~ p, expr at next p",
            "at next: the value expr will have the next time p holds. The left operand cannot contain temporal operators.",
            LTL
        ),
        NuXmvTypes.LTL_AT_LAST to OperatorDoc(
            "expr @O~ p, expr at last p",
            "at last: the value expr had the last time p held. The left operand cannot contain temporal operators.",
            LTL
        ),
        NuXmvTypes.LTL_TIMED_NEXT to OperatorDoc(
            "X~ p",
            "timed next state (timed models only): from the current configuration there is a time elapse " +
                    "and p holds in the left-open interval (t, t + ε].",
            TTS
        ),
        NuXmvTypes.LTL_TIMED_PREVIOUS to OperatorDoc(
            "Y~ p",
            "timed previous state (timed models only): p holds in the right-open interval [t − ε, t); Y~ p is false at time 0.",
            TTS
        ),
        NuXmvTypes.LTL_TIME_UNTIL to OperatorDoc(
            "time_until(p)",
            "time until (timed models only): the time elapse required to reach the next state in which p holds.",
            TTS
        ),
        NuXmvTypes.LTL_TIME_SINCE to OperatorDoc(
            "time_since(p)",
            "time since (timed models only): the time elapsed from the last state in which p held.",
            TTS
        ),
        NuXmvTypes.CTL_EXISTS_NEXT to OperatorDoc(
            "EX p",
            "exists next state: true in a state s if there exists a state s' such that a transition goes from s to s' and p is true in s'.",
            CTL
        ),
        NuXmvTypes.CTL_FORALL_NEXT to OperatorDoc(
            "AX p",
            "forall next state: true in a state s if for all states s' where there is a transition from s to s', p is true in s'.",
            CTL
        ),
        NuXmvTypes.CTL_EXISTS_FINALLY to OperatorDoc(
            "EF p",
            "exists finally: true in a state s0 if there exists a series of transitions s0 → … → sn such that p is true in sn.",
            CTL
        ),
        NuXmvTypes.CTL_FORALL_FINALLY to OperatorDoc(
            "AF p",
            "forall finally: true in a state s0 if for all series of transitions s0 → … → sn, p is true in sn.",
            CTL
        ),
        NuXmvTypes.CTL_EXISTS_GLOBALLY to OperatorDoc(
            "EG p",
            "exists globally: true in a state s0 if there exists an infinite series of transitions s0 → s1 → … such that p is true in every si.",
            CTL
        ),
        NuXmvTypes.CTL_FORALL_GLOBALLY to OperatorDoc(
            "AG p",
            "forall globally: true in a state s0 if for all infinite series of transitions s0 → s1 → …, p is true in every si.",
            CTL
        ),
        NuXmvTypes.CTL_EXISTS to OperatorDoc(
            "E [p U q], E [p BU m..n q]",
            "exists until: there exists a series of transitions s0 → … → sn such that p is true in every state from s0 to sn−1 and q is true in sn.",
            CTL
        ),
        NuXmvTypes.CTL_FORALL to OperatorDoc(
            "A [p U q], A [p BU m..n q]",
            "forall until: for all series of transitions s0 → … → sn, p is true in every state from s0 to sn−1 and q is true in sn.",
            CTL
        ),
        NuXmvTypes.RTCTL_EBF to OperatorDoc(
            "EBF m..n p",
            "there exists a path starting from a state such that p holds in a future time instant i, with m ≤ i ≤ n.",
            RTCTL
        ),
        NuXmvTypes.RTCTL_ABF to OperatorDoc(
            "ABF m..n p",
            "for all paths starting from a state, p holds in a future time instant i, with m ≤ i ≤ n.",
            RTCTL
        ),
        NuXmvTypes.RTCTL_EBG to OperatorDoc(
            "EBG m..n p",
            "there exists a path starting from a state such that p holds in all future time instants i, with m ≤ i ≤ n.",
            RTCTL
        ),
        NuXmvTypes.RTCTL_ABG to OperatorDoc(
            "ABG m..n p",
            "for all paths starting from a state, p holds in all future time instants i, with m ≤ i ≤ n.",
            RTCTL
        ),
        NuXmvTypes.RTCTL_BU to OperatorDoc(
            "E [p BU m..n q], A [p BU m..n q]",
            "bounded until: q holds in a future time instant i, with m ≤ i ≤ n, and p holds in all future time instants j, with m ≤ j < i.",
            RTCTL
        ),
    )

    /** True if [tokenType] is a temporal operator described here. */
    fun isDocumented(tokenType: IElementType?): Boolean = tokenType in docs

    /** HTML documentation for the operator [tokenType], or null if it is not a documented operator. */
    fun render(tokenType: IElementType?): String? {
        val doc = docs[tokenType] ?: return null
        return DocumentationMarkup.DEFINITION_START + doc.syntax + DocumentationMarkup.DEFINITION_END +
                DocumentationMarkup.CONTENT_START + doc.description + DocumentationMarkup.CONTENT_END +
                DocumentationMarkup.SECTIONS_START +
                DocumentationMarkup.SECTION_HEADER_START + "Manual:" + DocumentationMarkup.SECTION_SEPARATOR +
                doc.section + DocumentationMarkup.SECTION_END +
                DocumentationMarkup.SECTIONS_END
    }
}
