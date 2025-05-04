package dev.mikhailshad.nuxmvplugin.language.psi.type

import com.intellij.psi.PsiElement

interface TypeResolvable : PsiElement {
    fun resolveType(): NuXmvBuiltInType
}
