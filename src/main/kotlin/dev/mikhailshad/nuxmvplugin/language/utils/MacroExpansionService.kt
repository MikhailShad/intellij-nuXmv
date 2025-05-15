package dev.mikhailshad.nuxmvplugin.language.utils

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForLoopMacro
import java.io.File
import java.nio.file.Path

@Service(Service.Level.PROJECT)
class MacroExpansionService(private val project: Project) {
    private val logger = Logger.getInstance(MacroExpansionService::class.java)

    fun expandMacrosInFile(modelFile: VirtualFile): VirtualFile {
        logger.info("Expanding macros in file: ${modelFile.path}")

        // Parse the file
        val psiFile = PsiManager.getInstance(project).findFile(modelFile)
        if (psiFile !is NuXmvFile) {
            logger.error("File is not a NuXmv file: ${modelFile.path}")
            return modelFile
        }

        // Find all FOR macros in the file
        val forMacros = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvForLoopMacro::class.java)
        if (forMacros.isEmpty()) {
            logger.info("No FOR macros found in file: ${modelFile.path}")
            return modelFile
        }

        // Create a new file with the expanded macros
        val originalContent = modelFile.contentsToByteArray().toString(Charsets.UTF_8)
        var expandedContent = originalContent

        // Process all macros
        for (macro in forMacros) {
            val expandedMacro = macro.expand()
            val macroText = macro.text

            // Replace the macro with its expanded form
            expandedContent = expandedContent.replace(macroText, expandedMacro)
        }

        // Write the expanded content to a new file
        val expandedFilePath = getExpandedFilePath(modelFile.path)
        val expandedFile = File(expandedFilePath)

        expandedFile.writeText(expandedContent)

        // Refresh to make sure VFS picks up the changes
        val expandedVirtualFile = VfsUtil.findFile(Path.of(expandedFilePath), true)

        if (expandedVirtualFile != null) {
            VfsUtil.markDirtyAndRefresh(true, false, false, expandedVirtualFile)
            logger.info("Macros expanded successfully, new file: ${expandedVirtualFile.path}")
            return expandedVirtualFile
        }

        logger.error("Failed to create expanded file: $expandedFilePath")
        return modelFile
    }

    private fun getExpandedFilePath(originalPath: String): String {
        val file = File(originalPath)
        val directory = file.parentFile ?: File(".")
        val baseName = file.nameWithoutExtension
        val extension = file.extension

        return File(directory, "${baseName}_expanded.${extension}").absolutePath
    }

    companion object {
        fun getInstance(project: Project): MacroExpansionService = project.service()
    }
}
