package dev.mikhailshad.nuxmvplugin.ide.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserDefinition
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

class NuXmvBraceMatcher : PairedBraceMatcher {
    private val bracePairs = arrayOf(
        BracePair(NuXmvTypes.LPAREN, NuXmvTypes.RPAREN, false),
        BracePair(NuXmvTypes.LBRACE, NuXmvTypes.RBRACE, true),
        BracePair(NuXmvTypes.LBRACKET, NuXmvTypes.RBRACKET, false)
    )

    override fun getPairs(): Array<BracePair> = bracePairs

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean =
        !NuXmvParserDefinition.Util.IDENTIFIER.contains(contextType)
                && contextType != NuXmvTypes.LPAREN
                && contextType != NuXmvTypes.LBRACE
                && contextType != NuXmvTypes.LBRACKET

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
