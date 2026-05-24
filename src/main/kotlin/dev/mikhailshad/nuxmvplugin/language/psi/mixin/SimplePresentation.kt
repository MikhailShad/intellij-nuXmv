package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.navigation.ItemPresentation
import javax.swing.Icon

internal class SimplePresentation(
    private val text: String,
    private val icon: Icon
) : ItemPresentation {
    override fun getPresentableText(): String = text
    override fun getIcon(unused: Boolean): Icon = icon
}
