package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvConstant
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import javax.swing.Icon

abstract class NuXmvConstantMixin(node: ASTNode) : NuXmvNamedElementMixin(node), NuXmvConstant,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation? {
        return object : ItemPresentation {
            override fun getPresentableText(): String = text

            override fun getIcon(unused: Boolean): Icon = AllIcons.Nodes.Constant
        }
    }
}
