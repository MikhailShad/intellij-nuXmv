package dev.mikhailshad.nuxmvplugin.language.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

class NuXmvParserUtil : GeneratedParserUtilBase() {
    companion object {
        private const val CLOCK_TYPE_NAME = "clock"

        @JvmStatic
        @Suppress("UNUSED_PARAMETER")
        fun clockTypeName(builder: PsiBuilder, level: Int): Boolean {
            if (builder.tokenType != NuXmvTypes.IDENTIFIER || builder.tokenText != CLOCK_TYPE_NAME) {
                return false
            }
            builder.advanceLexer()
            return true
        }
    }
}
