package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement

/**
 * Marker interface for elements implementing [com.intellij.psi.impl.PsiElementBase.getPresentation]
 */
interface NuXmvPresentableElement : NavigatablePsiElement {
    override fun getPresentation(): ItemPresentation?
}
