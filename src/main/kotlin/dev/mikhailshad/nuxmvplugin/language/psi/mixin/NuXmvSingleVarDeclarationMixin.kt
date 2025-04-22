package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration
import javax.swing.Icon

abstract class NuXmvSingleVarDeclarationMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvSingleVarDeclaration,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = "${varName.name}: ${typeSpecifier?.text}"

            override fun getIcon(unused: Boolean): Icon = NuXmvIcons.VAR
        }
    }
}
