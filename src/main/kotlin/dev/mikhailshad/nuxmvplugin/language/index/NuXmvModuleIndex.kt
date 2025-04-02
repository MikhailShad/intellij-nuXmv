package dev.mikhailshad.nuxmvplugin.language.index

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName

object NuXmvModuleIndex {

    fun getAllModules(project: Project): List<NuXmvModule> {
        val result = mutableListOf<NuXmvModule>()

        val virtualFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))
        for (virtualFile in virtualFiles) {
            val nuXmvFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue
            val modules = PsiTreeUtil.findChildrenOfType(nuXmvFile, NuXmvModule::class.java)
            result.addAll(modules)
        }

        return result
    }

    fun getAllModuleNames(project: Project): List<NuXmvModuleName> {
        val result = mutableListOf<NuXmvModuleName>()

        val virtualFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))
        for (virtualFile in virtualFiles) {
            val nuXmvFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue
            val moduleNames = PsiTreeUtil.findChildrenOfType(nuXmvFile, NuXmvModuleName::class.java)
            result.addAll(moduleNames)
        }

        return result
    }

    fun findModule(project: Project, moduleName: String): NuXmvModule? {
        val virtualFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))
        for (virtualFile in virtualFiles) {
            val nuXmvFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue
            val modules = PsiTreeUtil.findChildrenOfType(nuXmvFile, NuXmvModule::class.java)

            val foundModule = modules.find {
                val moduleNameElement = it.moduleDeclaration.moduleName
                moduleNameElement?.text == moduleName
            }

            if (foundModule != null) {
                return foundModule
            }
        }

        return null
    }
}
