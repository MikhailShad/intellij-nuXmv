package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvDefineBody
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import javax.swing.Icon

abstract class NuXmvDefineBodyMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvDefineBody,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = "${defineName.name} := ${expr?.text}"

            override fun getIcon(unused: Boolean): Icon = NuXmvIcons.DEFINE
        }
    }
}
