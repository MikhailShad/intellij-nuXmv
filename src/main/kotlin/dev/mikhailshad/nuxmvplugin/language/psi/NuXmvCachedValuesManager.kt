package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.SingleIvarDeclarationMixin
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.SingleVarDeclarationMixin

object NuXmvCachedValuesManager {
    private const val UNKNOWN = "UNKNOWN"

    fun getModulesInFile(file: NuXmvFile): Map<String, NuXmvModule> {
        return CachedValuesManager.getCachedValue(file) {
            val modules = PsiTreeUtil.findChildrenOfType(file, NuXmvModule::class.java)
                .associateBy { it.moduleDeclaration.moduleName?.name ?: "" }

            CachedValueProvider.Result.create(
                modules,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    fun getVariablesInModule(module: NuXmvModule): Map<String, NuXmvSingleVarDeclaration> {
        return CachedValuesManager.getCachedValue(module) {
            val varDeclaration = PsiTreeUtil.findChildrenOfType(module, SingleVarDeclarationMixin::class.java)
            val variables = varDeclaration.associateBy { it.varName.name ?: UNKNOWN }

            CachedValueProvider.Result.create(
                variables,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    fun getInvariantsInModule(module: NuXmvModule): Map<String, NuXmvSingleIvarDeclaration> {
        return CachedValuesManager.getCachedValue(module) {
            val varDeclaration = PsiTreeUtil.findChildrenOfType(module, SingleIvarDeclarationMixin::class.java)
            val variables = varDeclaration.associateBy { it.varName.name ?: UNKNOWN }

            CachedValueProvider.Result.create(
                variables,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    fun getAllNamedElements(file: NuXmvFile): Map<String, NuXmvNamedElement> {
        return CachedValuesManager.getCachedValue(file) {
            val modules = getModulesInFile(file).toMap()
            val allNamedElements = mutableMapOf<String, NuXmvNamedElement>()
            modules.filter { it.key != "" }
                .forEach { allNamedElements[it.key] = it.value.moduleDeclaration.moduleName!! }

            for (module in modules.values) {
                getVariablesInModule(module).forEach { allNamedElements[it.key] = it.value.varName }
                getInvariantsInModule(module).forEach { allNamedElements[it.key] = it.value.varName }
            }

            CachedValueProvider.Result.create(
                allNamedElements,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }
}
