package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.findParentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvElementFactory
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope
import dev.mikhailshad.nuxmvplugin.language.psi.scope.submoduleTypeOf

/**
 * Resolves dotted identifier usages such as `submodule.field.subfield` against the cached
 * module scope. Each hop is an O(1) map lookup; nothing here walks the PSI subtree.
 */
class NuXmvIdentifierReference(element: NuXmvIdentifierUsage) :
    NuXmvReferenceBase(element, TextRange.from(0, element.textLength)) {

    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val file = element.containingFile as? NuXmvFile ?: return emptyList()
        val rawIdentifier = element.text ?: return emptyList()
        val identifier = stripArrayIndex(rawIdentifier)

        var currentModule: NuXmvModule = element.findParentOfType<NuXmvModule>()
            ?: return resolveAtFileScope(file, identifier)

        currentModule.scope().declarations[identifier]?.let { return listOf(it) }

        val parts = identifier.split('.')
        for (i in 0 until parts.size - 1) {
            val partName = parts[i]
            val scope = currentModule.scope()
            scope.variables[partName] ?: return emptyList()
            val moduleType = currentModule.submoduleTypeOf(partName) ?: return emptyList()
            currentModule = NuXmvScopes.modulesIn(file)[moduleType] ?: return emptyList()
        }

        val lastPart = parts.last()
        return currentModule.scope().declarations[lastPart]?.let { listOf(it) } ?: emptyList()
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        return psiElement.replace(NuXmvElementFactory.createIdentifier(psiElement.project, newElementName))
    }

    private fun resolveAtFileScope(file: NuXmvFile, identifier: String): List<PsiElement> {
        val module = NuXmvScopes.modulesIn(file)[identifier] ?: return emptyList()
        return listOf(module)
    }

    private fun stripArrayIndex(identifier: String): String {
        val bracket = identifier.indexOf('[')
        return if (bracket >= 0) identifier.substring(0, bracket) else identifier
    }
}
