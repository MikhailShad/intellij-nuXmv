package dev.mikhailshad.nuxmvplugin.language.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import java.util.*

object EditorUtils {
    @JvmStatic
    fun getActiveNuXmvFilePath(project: Project): VirtualFile? {
        val editor = FileEditorManager.getInstance(project).selectedEditor
        val file = editor?.file
        return if (file?.extension?.lowercase(Locale.getDefault()) == NuXmvFileType.defaultExtension) file else null
    }
}
