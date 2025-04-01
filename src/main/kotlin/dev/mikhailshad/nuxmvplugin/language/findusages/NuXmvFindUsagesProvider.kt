package dev.mikhailshad.nuxmvplugin.language.findusages

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import dev.mikhailshad.nuxmvplugin.language.lexer.NuXmvLexerAdapter
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            NuXmvLexerAdapter(),
            TokenSet.create(NuXmvTypes.IDENTIFIER),
            TokenSet.create(NuXmvTypes.LINE_COMMENT, NuXmvTypes.BLOCK_COMMENT),
            TokenSet.EMPTY
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is NuXmvModuleName ||
                psiElement is NuXmvVarName ||
                psiElement is NuXmvComplexIdentifier ||
                psiElement is NuXmvSimpleIdentifier
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getType(element: PsiElement): String {
        return when {
            element is NuXmvModuleName -> "module"
            element is NuXmvVarName -> "variable"
            element.parent is NuXmvDefineBody -> "define"
            element is NuXmvComplexIdentifier && element.parent is NuXmvDefineBody -> "define"
            element is NuXmvSimpleIdentifier -> "identifier"
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when {
            element is PsiNamedElement -> element.name ?: ""
            element is NuXmvModuleName -> element.text
            element is NuXmvVarName -> element.text
            element is NuXmvComplexIdentifier -> element.text
            element is NuXmvSimpleIdentifier -> element.text
            else -> ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }
}
