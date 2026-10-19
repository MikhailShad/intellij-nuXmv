package dev.mikhailshad.nuxmvplugin.language.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

class NuXmvParserUtil : GeneratedParserUtilBase() {
    companion object {
        private const val CLOCK_TYPE_NAME = "clock"

        private val PSL_KEYWORDS = setOf(
            "always", "never", "eventually", "until", "until_", "before", "before_",
            "next_a", "next_e", "next_event", "next_event_a", "next_event_e",
            "within", "within_", "whilenot", "whilenot_", "abort", "forall", "inf",
            "sequence", "select", "endcase", "W",
        )

        @JvmStatic
        @Suppress("UNUSED_PARAMETER")
        fun clockTypeName(builder: PsiBuilder, level: Int): Boolean =
            consumeIdentifier(builder) { it == CLOCK_TYPE_NAME }

        @JvmStatic
        @Suppress("UNUSED_PARAMETER")
        fun pslKeyword(builder: PsiBuilder, level: Int): Boolean = consumeIdentifier(builder) { it in PSL_KEYWORDS }

        @JvmStatic
        @Suppress("UNUSED_PARAMETER")
        fun anyToken(builder: PsiBuilder, level: Int): Boolean {
            if (builder.eof()) {
                return false
            }
            builder.advanceLexer()
            return true
        }

        private fun consumeIdentifier(builder: PsiBuilder, predicate: (String) -> Boolean): Boolean {
            val text = builder.tokenText
            if (builder.tokenType != NuXmvTypes.IDENTIFIER || text == null || !predicate(text)) {
                return false
            }
            builder.advanceLexer()
            return true
        }
    }
}
