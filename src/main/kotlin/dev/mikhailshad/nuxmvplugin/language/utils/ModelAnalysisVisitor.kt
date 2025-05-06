package dev.mikhailshad.nuxmvplugin.language.utils

import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvBuiltInType
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType

class ModelAnalysisVisitor : NuXmvVisitor() {
    var hasCtlSpecs = false
    var hasLtlSpecs = false
    var hasInvarSpecs = false
    private val typesInModel = mutableSetOf<NuXmvBuiltInType>()
    val domainType: NuXmvDomainType
        get() {
            for (typeInModel in typesInModel) {
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
