package dev.mikhailshad.nuxmvplugin.language.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

object NuXmvUtils {
    private val logger = Logger.getInstance(NuXmvUtils::class.java)

    /**
     * Analyze a model file to detect specifications and domain type
     */
    fun analyzeModelSpecifications(project: Project, virtualFile: VirtualFile): ModelSpecifications {
        try {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return ModelSpecifications()
            val visitor = ModelAnalysisVisitor()
            psiFile.accept(visitor)
            return ModelSpecifications(
                hasCtlSpecs = visitor.hasCtlSpecs,
                hasLtlSpecs = visitor.hasLtlSpecs,
                hasInvarSpecs = visitor.hasInvarSpecs,
                domainType = visitor.domainType
            )
        } catch (e: Exception) {
            logger.warn("Error analyzing model file: ${virtualFile.path}", e)
            return ModelSpecifications()
        }
    }

}

