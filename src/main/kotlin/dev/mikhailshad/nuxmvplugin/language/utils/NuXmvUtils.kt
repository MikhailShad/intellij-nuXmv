package dev.mikhailshad.nuxmvplugin.language.utils


import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlSpecification
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvInvarSpecification
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvLtlSpecification
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor

object NuXmvUtils {
    private val LOG = Logger.getInstance(NuXmvUtils::class.java)

    data class ModelSpecifications(
        val hasCtlSpecs: Boolean = false,
        val hasLtlSpecs: Boolean = false,
        val hasInvarSpecs: Boolean = false
    )

    /**
     * Analyze a model file to detect specifications
     */
    fun analyzeModelSpecifications(project: Project, virtualFile: VirtualFile): ModelSpecifications {
        try {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return ModelSpecifications()
            val visitor = SpecificationVisitor()
            psiFile.accept(visitor)
            return ModelSpecifications(
                hasCtlSpecs = visitor.hasCtlSpecs,
                hasLtlSpecs = visitor.hasLtlSpecs,
                hasInvarSpecs = visitor.hasInvarSpecs
            )
        } catch (e: Exception) {
            LOG.warn("Error analyzing model file: ${virtualFile.path}", e)
            return ModelSpecifications()
        }
    }

    private class SpecificationVisitor : NuXmvVisitor() {
        var hasCtlSpecs = false
        var hasLtlSpecs = false
        var hasInvarSpecs = false

        override fun visitElement(element: PsiElement) {
            when (element) {
                is NuXmvCtlSpecification -> hasCtlSpecs = true
                is NuXmvLtlSpecification -> hasLtlSpecs = true
                is NuXmvInvarSpecification -> hasInvarSpecs = true
            }

            element.acceptChildren(this)

            if (hasCtlSpecs && hasLtlSpecs && hasInvarSpecs) {
                return
            }
        }
    }
}
