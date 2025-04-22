package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvLtlSpecification
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import javax.swing.Icon

abstract class NuXmvLtlSpecificationMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvLtlSpecification,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String {
                val sb = StringBuilder("LTL Specification")
                val namedSpec = namedSpecification
                if (namedSpec != null) {
                    sb.append(" ${namedSpec.name}")
                }
                return sb.toString()
            }

            override fun getIcon(unused: Boolean): Icon = NuXmvIcons.LTL_SPEC
        }
    }
}
