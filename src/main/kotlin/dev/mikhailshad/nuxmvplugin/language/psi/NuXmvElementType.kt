package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.psi.tree.IElementType
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

class NuXmvElementType(@NotNull @NonNls debugName: String) : IElementType(debugName, NuXmvLanguage) {
    override fun toString(): String {
        return "NuXmvElementType.${super.toString()}"
    }
}
