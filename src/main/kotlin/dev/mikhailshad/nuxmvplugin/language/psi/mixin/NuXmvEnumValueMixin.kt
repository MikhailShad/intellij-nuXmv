package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvEnumerationTypeValue
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes

abstract class NuXmvEnumValueMixin(node: ASTNode) : NuXmvNamedElementMixin(node), NuXmvEnumerationTypeValue {
    override val nameFirstLeaf: PsiElement?
        get() {
            val enumValueType = firstChild.elementType
            return if (enumValueType == NuXmvTypes.IDENTIFIER || enumValueType == NuXmvTypes.SELF_KW) firstChild else null
        }
    override val nameLastLeaf: PsiElement? get() = nameFirstLeaf
}
