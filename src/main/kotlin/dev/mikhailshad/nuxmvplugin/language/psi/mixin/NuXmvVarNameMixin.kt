package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName

abstract class NuXmvVarNameMixin(node: ASTNode) : NuXmvNamedElementMixin(node), NuXmvVarName {
    override fun getNameIdentifier(): PsiElement? {
        return if (firstChild.elementType == NuXmvTypes.IDENTIFIER) {
            // ComplexIdentifier
            firstChild
        } else {
            // QUOTES ComplexIdentifier QUOTES
            firstChild.nextSibling
        }
    }
}
