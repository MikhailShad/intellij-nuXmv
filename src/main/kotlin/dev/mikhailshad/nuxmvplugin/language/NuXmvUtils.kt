package dev.mikhailshad.nuxmvplugin.language

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType

object NuXmvUtils {
    private val logger = Logger.getInstance(NuXmvUtils::class.java)

    data class ModelSpecifications(
        val hasCtlSpecs: Boolean = false,
        val hasLtlSpecs: Boolean = false,
        val hasInvarSpecs: Boolean = false,
        val domainType: NuXmvDomainType = NuXmvDomainType.FINITE_DOMAIN
    )

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

    private class ModelAnalysisVisitor : NuXmvVisitor() {
        var hasCtlSpecs = false
        var hasLtlSpecs = false
        var hasInvarSpecs = false
        private val typesInModel = mutableSetOf<NuXmvBuiltInType>()
        val domainType: NuXmvDomainType
            get() {
                for (typeInModel in typesInModel) {
                    if (typeInModel == NuXmvBuiltInType.CLOCK) {
                        return NuXmvDomainType.TIMED_DOMAIN
                    }
                    if (typeInModel == NuXmvBuiltInType.INTEGER || typeInModel == NuXmvBuiltInType.REAL) {
                        return NuXmvDomainType.INFINITE_DOMAIN
                    }
                }

                return NuXmvDomainType.FINITE_DOMAIN
            }

        override fun visitElement(element: PsiElement) {
            when (element) {
                is NuXmvCtlSpecification -> hasCtlSpecs = true
                is NuXmvLtlSpecification -> hasLtlSpecs = true
                is NuXmvInvarSpecification -> hasInvarSpecs = true
                is NuXmvSingleVarDeclaration -> {
                    val typeSpecifier = element.typeSpecifier?.simpleTypeSpecifier
                    val resolvedVarType = typeSpecifier?.resolveType()
                    if (resolvedVarType != null) {
                        typesInModel.add(resolvedVarType)
                    }
                }
            }

            element.acceptChildren(this)
        }
    }
}
