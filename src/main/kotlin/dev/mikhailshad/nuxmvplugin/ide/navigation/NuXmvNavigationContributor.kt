package dev.mikhailshad.nuxmvplugin.ide.navigation

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope

class NuXmvNavigationContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        val result = HashSet<String>()
        for (file in nuXmvFiles(project)) {
            for ((moduleName, module) in NuXmvScopes.modulesIn(file)) {
                result.add(moduleName)
                result.addAll(module.scope().declarations.keys)
            }
        }
        return result.toTypedArray()
    }

    override fun getItemsByName(
        name: String,
        pattern: String,
        project: Project,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val result = ArrayList<NavigationItem>()
        for (file in nuXmvFiles(project)) {
            val modules = NuXmvScopes.modulesIn(file)
            modules[name]?.let { result.add(it as NavigationItem) }
            for (module in modules.values) {
                val declaration = module.scope().declarations[name] ?: continue
                if (declaration is NavigationItem) {
                    result.add(declaration)
                }
            }
        }
        return result.toTypedArray()
    }

    private fun nuXmvFiles(project: Project): Sequence<NuXmvFile> {
        val virtualFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))
        val psiManager = PsiManager.getInstance(project)
        return virtualFiles.asSequence().mapNotNull { psiManager.findFile(it) as? NuXmvFile }
    }
}
