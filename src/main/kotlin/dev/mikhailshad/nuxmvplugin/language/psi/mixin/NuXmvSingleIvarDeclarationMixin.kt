package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration
import javax.swing.Icon

abstract class NuXmvSingleIvarDeclarationMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvSingleIvarDeclaration,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = "${varName.name}: ${simpleTypeSpecifier?.text}"

            override fun getIcon(unused: Boolean): Icon = AllIcons.Nodes.Variable
        }
    }
}
