package dev.mikhailshad.nuxmvplugin.language.structure

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.lang.Language
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import javax.swing.Icon

class NuXmvStructureAwareNavbar : StructureAwareNavBarModelExtension() {
    override val language: Language = NuXmvLanguage

    override fun getPresentableText(any: Any?): String? {
        if (any !is NuXmvPresentableElement) {
            return null
        }

        return any.presentation?.presentableText
    }

    override fun getIcon(any: Any?): Icon? {
        if (any !is NuXmvPresentableElement) {
            return null
        }

        return any.presentation?.getIcon(false)
    }
}
