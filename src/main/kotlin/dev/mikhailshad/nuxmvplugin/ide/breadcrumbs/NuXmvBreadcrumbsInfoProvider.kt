package dev.mikhailshad.nuxmvplugin.ide.breadcrumbs

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvBreadcrumbsInfoProvider : BreadcrumbsProvider {
    override fun getLanguages(): Array<Language> {
        return arrayOf(NuXmvLanguage)
    }

    override fun acceptElement(element: PsiElement): Boolean {
        return element is NuXmvModule ||
                element is NuXmvVarDeclaration ||
                element is NuXmvIvarDeclaration ||
                element is NuXmvDefineDeclaration ||
                element is NuXmvAssignConstraint ||
                element is NuXmvLtlSpecification ||
                element is NuXmvCtlSpecification ||
                element is NuXmvInvarSpecification
    }

    override fun getElementInfo(element: PsiElement): String {
        return when (element) {
            is NuXmvModule -> {
                val moduleName = element.moduleDeclaration.moduleName?.name
                "MODULE $moduleName"
            }

            is NuXmvVarDeclaration -> "VAR"
            is NuXmvIvarDeclaration -> "IVAR"
            is NuXmvDefineDeclaration -> "DEFINE"
            is NuXmvAssignConstraint -> "ASSIGN"
            is NuXmvLtlSpecification -> "LTLSPEC"
            is NuXmvCtlSpecification -> {
                if (element.text.startsWith("CTLSPEC")) "CTLSPEC" else "SPEC"
            }

            is NuXmvInvarSpecification -> "INVARSPEC"
            else -> element.text.take(20)
        }
    }

    override fun getElementTooltip(element: PsiElement): String? {
        return when (element) {
            is NuXmvModule -> {
                val moduleName = element.moduleDeclaration.moduleName?.name
                "Module: $moduleName"
            }

            is NuXmvVarDeclaration -> "State variables declaration section"
            is NuXmvIvarDeclaration -> "Input variables declaration section"
            is NuXmvDefineDeclaration -> "Define section - macros and shortcuts"
            is NuXmvAssignConstraint -> "Assignments section - initial states and transitions"
            is NuXmvLtlSpecification -> "Linear Temporal Logic specification"
            is NuXmvCtlSpecification -> "Computation Tree Logic specification"
            is NuXmvInvarSpecification -> "Invariant specification"
            else -> element.text
        }
    }
}
