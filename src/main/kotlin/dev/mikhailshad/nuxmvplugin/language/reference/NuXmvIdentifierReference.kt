package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.findParentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvIdentifierReference(element: NuXmvIdentifierUsage) :
    NuXmvReferenceBase(element, TextRange.from(0, element.textLength)) {
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val file = element.containingFile

        var identifier = element.text
        val braceIndex = identifier.indexOf('[')
        if (braceIndex >= 0) {
            identifier = identifier.substring(0, braceIndex)
        }

        val partsIdentifier = identifier.split('.')

        if (partsIdentifier.size == 1) {
            return findDeclarationsInScope(file, identifier)
        }

        var currentModule: PsiElement = element.findParentOfType<NuXmvModule>() ?: element.containingFile
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
        val result = mutableListOf<PsiElement>()
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvModuleParameter::class.java)
                .filter { it.text == name })
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvVarName::class.java)
                .filter { it.text == name })
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvNamedSpecification::class.java)
                .filter { it.text == name })
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvFunctionName::class.java)
                .filter { it.text == name })
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvDefineName::class.java)
                .filter { it.text == name })
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvConstant::class.java)
                .filter { it.text == name })
        result.addAll(
            PsiTreeUtil.findChildrenOfType(scope, NuXmvEnumerationTypeValue::class.java)
                .filter { it.text == name })
        return result
    }

}
