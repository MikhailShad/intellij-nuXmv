package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvInvarSpecification
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import javax.swing.Icon

abstract class NuXmvInvarSpecificationMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvInvarSpecification,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String {
                val sb = StringBuilder("Invariant Specification")
                val namedSpec = namedSpecification
                if (namedSpec != null) {
                    sb.append(" ${namedSpec.name}")
                }
                return sb.toString()
            }

            override fun getIcon(unused: Boolean): Icon = AllIcons.Nodes.Artifact // TODO: fix
        }
    }
}
