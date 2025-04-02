package dev.mikhailshad.nuxmvplugin.language.index

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration

/**
 * Index class for tracking variables and their types in nuXmv projects
 */
object NuXmvVariableIndex {

    /**
     * Get all VAR declarations in the project
     */
    fun getAllVarDeclarations(project: Project): List<NuXmvSingleVarDeclaration> {
        val result = mutableListOf<NuXmvSingleVarDeclaration>()

        val virtualFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))
        for (virtualFile in virtualFiles) {
            val nuXmvFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue
            val varDeclarations = PsiTreeUtil.findChildrenOfType(nuXmvFile, NuXmvSingleVarDeclaration::class.java)
            result.addAll(varDeclarations)
        }

        return result
    }

    /**
     * Get all IVAR declarations in the project
     */
    fun getAllIvarDeclarations(project: Project): List<NuXmvSingleIvarDeclaration> {
        val result = mutableListOf<NuXmvSingleIvarDeclaration>()

        val virtualFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))
        for (virtualFile in virtualFiles) {
            val nuXmvFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue
            val ivarDeclarations = PsiTreeUtil.findChildrenOfType(nuXmvFile, NuXmvSingleIvarDeclaration::class.java)
            result.addAll(ivarDeclarations)
        }

        return result
    }

    /**
     * Get variable declarations (VAR) by name in a specific module
     */
    fun findVarDeclarationInModule(module: NuXmvModule, name: String): NuXmvSingleVarDeclaration? {
        val varDeclarations = PsiTreeUtil.findChildrenOfType(module, NuXmvSingleVarDeclaration::class.java)
        return varDeclarations.find { it.varName.text == name }
    }

    /**
     * Get input variable declarations (IVAR) by name in a specific module
     */
    fun findIvarDeclarationInModule(module: NuXmvModule, name: String): NuXmvSingleIvarDeclaration? {
        val ivarDeclarations = PsiTreeUtil.findChildrenOfType(module, NuXmvSingleIvarDeclaration::class.java)
        return ivarDeclarations.find { it.varName.text == name }
    }

    /**
     * Get all variable declarations (both VAR and IVAR) in a specific module
     */
    fun getAllVariablesInModule(module: NuXmvModule): List<PsiElement> {
        val result = mutableListOf<PsiElement>()

        val varDeclarations = PsiTreeUtil.findChildrenOfType(module, NuXmvSingleVarDeclaration::class.java)
        val ivarDeclarations = PsiTreeUtil.findChildrenOfType(module, NuXmvSingleIvarDeclaration::class.java)

        result.addAll(varDeclarations)
        result.addAll(ivarDeclarations)

        return result
    }
}
