package dev.mikhailshad.nuxmvplugin.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object NuXmvFileType : LanguageFileType(NuXmvLanguage) {
    override fun getName(): String = "NuXmv File"

    override fun getDescription(): String = "NuXmv language file"

    override fun getDefaultExtension(): String = "nuxmv"

    override fun getIcon(): Icon = NuXmvIcons.FILE
}
