package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface representing NuXmv macro elements
 * Macros are preprocessor-like constructs that expand into regular NuXmv code
 */
interface NuXmvMacro : PsiElement {
    /**
     * Expands the macro into regular NuXmv code
     * @return String containing the expanded code that should replace the macro
     */
    fun expand(): String
}
