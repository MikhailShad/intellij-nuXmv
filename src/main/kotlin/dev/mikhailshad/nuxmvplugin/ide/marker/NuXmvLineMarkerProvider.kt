package dev.mikhailshad.nuxmvplugin.ide.marker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleTypeSpecifier

class NuXmvLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        // Add markers for module declarations that can be navigated to
        if (element is NuXmvModuleName) {
            val moduleName = element.text
            val targets = findModuleUsages(element, moduleName)

            if (targets.isNotEmpty()) {
                val builder = NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod)
                    .setTargets(targets)
                    .setTooltipText("Navigate to module usages")

                result.add(builder.createLineMarkerInfo(element))
            }
        }

        // Add markers for module instantiations that can be navigated to the declaration
        if (element is NuXmvModuleTypeSpecifier) {
            val simpleIdentifier = element.firstChild
            val moduleName = simpleIdentifier.text
            val target = findModuleDeclaration(element, moduleName)

            if (target != null) {
                val builder = NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementingMethod)
                    .setTarget(target)
                    .setTooltipText("Navigate to module declaration")

                result.add(builder.createLineMarkerInfo(element))
            }
        }
    }

    private fun findModuleUsages(element: PsiElement, moduleName: String): List<PsiElement> {
        val result = mutableListOf<PsiElement>()
        val project = element.project

        // Search for module instantiations in all NuXmv files
        val nuxmvFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))

        for (virtualFile in nuxmvFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue

            // Find module type specifiers that reference this module
            val moduleTypeSpecifiers = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvModuleTypeSpecifier::class.java)
            for (specifier in moduleTypeSpecifiers) {
                val identifier = specifier.firstChild
                if (identifier.text == moduleName) {
                    result.add(specifier)
                }
            }
        }

        return result
    }

    private fun findModuleDeclaration(element: PsiElement, moduleName: String): PsiElement? {
        val project = element.project

        // Search for module declarations in all NuXmv files
        val nuxmvFiles = FileTypeIndex.getFiles(NuXmvFileType, GlobalSearchScope.allScope(project))

        for (virtualFile in nuxmvFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile ?: continue

            // Find module declarations
            val modules = PsiTreeUtil.findChildrenOfType(psiFile, NuXmvModule::class.java)
            for (module in modules) {
                val name = module.moduleDeclaration.moduleName?.text
                if (name == moduleName) {
                    return module.moduleDeclaration
                }
            }
        }

        return null
    }
}
