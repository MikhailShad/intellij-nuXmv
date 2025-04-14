package dev.mikhailshad.nuxmvplugin.ide.marker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleTypeSpecifier

class NuXmvLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        when (element) {
            is NuXmvModuleName -> {
                val moduleName = element.text
                val currentFile = element.containingFile as? NuXmvFile ?: return
                val targets = findModuleUsages(currentFile, moduleName)

                if (targets.isNotEmpty()) {
                    val builder = NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod)
                        .setTargets(targets)
                        .setTooltipText("Navigate to module usages")

                    result.add(builder.createLineMarkerInfo(element))
                }
            }

            is NuXmvModuleTypeSpecifier -> {
                val simpleIdentifier = element.firstChild
                val moduleName = simpleIdentifier.text
                val currentFile = element.containingFile as? NuXmvFile ?: return
                val target = findModuleDeclaration(currentFile, moduleName)

                if (target != null) {
                    val builder = NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementingMethod)
                        .setTarget(target)
                        .setTooltipText("Navigate to module declaration")

                    result.add(builder.createLineMarkerInfo(element))
                }
            }
        }
    }

    private fun findModuleUsages(file: NuXmvFile, moduleName: String): List<PsiElement> {
        val result = mutableListOf<PsiElement>()

        val moduleTypeSpecifiers = PsiTreeUtil.findChildrenOfType(file, NuXmvModuleTypeSpecifier::class.java)
        for (specifier in moduleTypeSpecifiers) {
            val identifier = specifier.firstChild
            if (identifier.text == moduleName) {
                result.add(specifier)
            }
        }

        return result
    }

    private fun findModuleDeclaration(file: NuXmvFile, moduleName: String): PsiElement? {
        val modules = PsiTreeUtil.findChildrenOfType(file, NuXmvModule::class.java)
        for (module in modules) {
            val name = module.moduleDeclaration.moduleName?.name
            if (name == moduleName) {
                return module.moduleDeclaration
            }
        }

        return null
    }
}
