package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage

class NuXmvFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, NuXmvLanguage) {
    override fun getFileType(): FileType = NuXmvFileType
}
