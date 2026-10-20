package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvElementFactory
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes

/** Resolves a module name written in the source (`ISA base`) to the `MODULE base` declaration. */
class NuXmvModuleReference(element: PsiElement) : NuXmvReferenceBase(element, TextRange(0, element.textLength)) {
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val file = element.containingFile as? NuXmvFile ?: return emptyList()
        val moduleName = NuXmvScopes.modulesIn(file)[element.text]?.moduleDeclaration?.moduleName ?: return emptyList()
        return listOf(moduleName)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val leaf = element.firstChild ?: return element
        leaf.replace(NuXmvElementFactory.createVarName(element.project, newElementName).firstChild)
        return element
    }
}
