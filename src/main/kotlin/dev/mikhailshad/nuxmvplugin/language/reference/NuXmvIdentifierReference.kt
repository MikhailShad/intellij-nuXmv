package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.findParentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvIdentifierUsageMixin

class NuXmvIdentifierReference(element: NuXmvIdentifierUsage) :
    NuXmvReferenceBase(element, TextRange.from(0, element.textLength)) {
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val file = element.containingFile
        val identifierUsage = element as NuXmvIdentifierUsageMixin
        var identifier = identifierUsage.text
        val braceIndex = identifier.indexOf('[')
        if (braceIndex >= 0) {
            identifier = identifier.substring(0, braceIndex)
        }

        // At first look for as-is declaration of identifier
        var currentModule: PsiElement = element.findParentOfType<NuXmvModule>() ?: element.containingFile
        val asIsDeclaration = findDeclarationsInScope(currentModule, identifier)
        if (asIsDeclaration.isNotEmpty()) {
            return asIsDeclaration
        }

        // then we try to resolve partial declaration (module + identifier)
        val partsIdentifier = identifier.split('.')
        for (i in 0 until partsIdentifier.size - 1) {
            val partName = partsIdentifier[i]
            val variable = findVariableInScope(currentModule, partName)
                ?: return emptyList()
            val moduleType = getModuleTypeOfVariable(variable)
                ?: return emptyList()
            currentModule = findModule(file, moduleType)
                ?: return emptyList()
        }

        val lastPart = partsIdentifier.last()
        return findDeclarationsInScope(currentModule, lastPart)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        return psiElement.replace(NuXmvElementFactory.createIdentifier(psiElement.project, newElementName))
    }

    private fun findVariableInScope(scope: PsiElement, name: String): PsiElement? {
        return PsiTreeUtil.findChildrenOfType(scope, NuXmvVarName::class.java)
            .firstOrNull { it.text == name }
    }

    private fun getModuleTypeOfVariable(variable: PsiElement): String? {
        val declaration = variable.parent
        if (declaration is NuXmvSingleVarDeclaration) {
            val moduleType = declaration.typeSpecifier?.moduleTypeSpecifier
            if (moduleType != null) {
                return moduleType.identifier.text
            }
        }
        return null
    }

    private fun findModule(file: PsiElement, moduleName: String): PsiElement? {
        val modules = PsiTreeUtil.findChildrenOfType(file, NuXmvModuleDeclaration::class.java)
        for (module in modules) {
            val name = module.moduleName?.text
            if (name == moduleName) {
                return module.findParentOfType<NuXmvModule>()
            }
        }

        return null
    }

    private fun findDeclarationsInScope(scope: PsiElement, name: String): List<PsiElement> {
        return listOf(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvModuleParameter::class.java),
            PsiTreeUtil.findChildrenOfType(scope, NuXmvVarName::class.java),
            PsiTreeUtil.findChildrenOfType(scope, NuXmvFunctionName::class.java),
            PsiTreeUtil.findChildrenOfType(scope, NuXmvDefineName::class.java),
            PsiTreeUtil.findChildrenOfType(scope, NuXmvConstant::class.java),
            PsiTreeUtil.findChildrenOfType(scope, NuXmvEnumerationTypeValue::class.java)
        ).flatten().filter { it.text == name }.distinct()
    }

}
