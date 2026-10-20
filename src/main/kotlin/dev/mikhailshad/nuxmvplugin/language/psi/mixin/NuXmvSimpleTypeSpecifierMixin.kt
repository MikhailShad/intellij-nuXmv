package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleTypeSpecifier
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType
import dev.mikhailshad.nuxmvplugin.language.psi.type.TypeResolvable

abstract class NuXmvSimpleTypeSpecifierMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvSimpleTypeSpecifier,
    TypeResolvable {
    override fun resolveType(): NuXmvBuiltInType {
        if (rangeType != null) {
            return NuXmvBuiltInType.SET
        }

        return when (firstChild.elementType) {
            NuXmvTypes.IDENTIFIER -> NuXmvBuiltInType.CLOCK
            NuXmvTypes.INTEGER_TYPE -> NuXmvBuiltInType.INTEGER
            NuXmvTypes.REAL_TYPE -> NuXmvBuiltInType.REAL
            NuXmvTypes.BOOLEAN_TYPE -> NuXmvBuiltInType.BOOLEAN
            NuXmvTypes.LBRACE -> NuXmvBuiltInType.SET
            NuXmvTypes.WORD_TYPE, NuXmvTypes.SIGNED_WORD_TYPE, NuXmvTypes.UNSIGNED_WORD_TYPE -> NuXmvBuiltInType.WORD
            else -> NuXmvBuiltInType.CUSTOM
        }
    }
}
