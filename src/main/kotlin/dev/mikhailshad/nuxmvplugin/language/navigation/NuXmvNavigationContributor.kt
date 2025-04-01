package dev.mikhailshad.nuxmvplugin.language.navigation

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvNavigationContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        val result = HashSet<String>()

        val nuxmvFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))

        for (virtualFile in nuxmvFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue

            val modules = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvNuXmvModule::class.java)
            for (module in modules) {
                module.moduleDeclaration.moduleName?.text?.let { result.add(it) }

                val moduleBody = module.moduleBody ?: continue

                val varDeclarations = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleVarDeclaration::class.java)
                for (varDecl in varDeclarations) {
                    varDecl.varName.text?.let { result.add(it) }
                }

                val ivarDeclarations =
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleIvarDeclaration::class.java)
                for (ivarDecl in ivarDeclarations) {
                    ivarDecl.varName.text?.let { result.add(it) }
                }

                val defineDeclarations = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvDefineBody::class.java)
                for (defineDecl in defineDeclarations) {
                    defineDecl.complexIdentifier.text?.let { result.add(it) }
                }
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

        // Get all NuXmv files in the project
        val nuxmvFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))

        for (virtualFile in nuxmvFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue

            val modules = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvNuXmvModule::class.java)
            for (module in modules) {
                if (module.moduleDeclaration.moduleName?.text == name) {
                    result.add(module as NavigationItem)
                }

                val moduleBody = module.moduleBody ?: continue

                val varDeclarations = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleVarDeclaration::class.java)
                for (varDecl in varDeclarations) {
                    if (varDecl.varName.text == name) {
                        result.add(varDecl as NavigationItem)
                    }
                }

                val ivarDeclarations =
                    PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleIvarDeclaration::class.java)
                for (ivarDecl in ivarDeclarations) {
                    if (ivarDecl.varName.text == name) {
                        result.add(ivarDecl as NavigationItem)
                    }
                }

                val defineDeclarations = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvDefineBody::class.java)
                for (defineDecl in defineDeclarations) {
                    if (defineDecl.complexIdentifier.text == name) {
                        result.add(defineDecl as NavigationItem)
                    }
                }
            }
        }

        return result.toTypedArray()
    }
}
