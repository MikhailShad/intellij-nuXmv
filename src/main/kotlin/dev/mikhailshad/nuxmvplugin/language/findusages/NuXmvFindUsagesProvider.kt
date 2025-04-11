package dev.mikhailshad.nuxmvplugin.language.findusages

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import dev.mikhailshad.nuxmvplugin.language.lexer.NuXmvLexerAdapter
import dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserDefinition
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            NuXmvLexerAdapter(),
            NuXmvParserDefinition.Util.IDENTIFIER,
            NuXmvParserDefinition.Util.COMMENTS,
            TokenSet.EMPTY
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is NuXmvNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getType(element: PsiElement): String {
        return when (element) {
            is NuXmvModuleDeclaration -> "module"
            is NuXmvModuleParameter -> "module parameter"
            is NuXmvVarName -> "variable"
            is NuXmvNamedSpecification -> "spec"
            is NuXmvDefineName -> "define"
            is NuXmvFunctionName -> "function"
            is NuXmvConstant -> "constant"
            is NuXmvEnumerationTypeValue -> "enum value"
            else -> "identifier"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return (element as NuXmvNamedElement).name ?: ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }
}
