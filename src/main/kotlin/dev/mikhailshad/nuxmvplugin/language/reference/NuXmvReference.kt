package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCachedValuesManager
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComplexIdentifier
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule

class NuXmvReference(element: NuXmvComplexIdentifier, range: TextRange) :
    PsiReferenceBase<NuXmvComplexIdentifier>(element, range) {

    override fun resolve(): PsiElement? {
        val file = element.containingFile as? NuXmvFile
            ?: return null

        val identifierText = element.text

        if (!identifierText.contains('.')) {
            val currentModule = PsiTreeUtil.getParentOfType(element, NuXmvModule::class.java)
            return findReferenceInModule(identifierText, currentModule)
        }

        val parts = identifierText.split('.')
        val modulePath = parts.dropLast(1)
        val varName = parts.last()

        var currentModule: NuXmvModule? = findModuleByName(file, modulePath.first())
        for (i in 1 until modulePath.size) {
            currentModule = findModuleByName(file, modulePath[i], currentModule)
            if (currentModule == null) return null
        }

        // Look for the variable in the final module
        return findReferenceInModule(varName, currentModule)
    }

    private fun findModuleByName(file: NuXmvFile, name: String, parentModule: NuXmvModule? = null): NuXmvModule? {
        if (parentModule == null) {
            val modulesCache = NuXmvCachedValuesManager.getModulesInFile(file)
            return modulesCache[name]
        }

        return null
    }

    private fun findReferenceInModule(name: String, module: NuXmvModule?): PsiElement? {
        if (module == null) return null

        val variable = NuXmvCachedValuesManager.getVariablesInModule(module)[name]
        if (variable != null) {
            return variable
        }

        val ivar = NuXmvCachedValuesManager.getInvariantsInModule(module)[name]
        if (ivar != null) {
            return ivar
        }

        return null
    }
}
