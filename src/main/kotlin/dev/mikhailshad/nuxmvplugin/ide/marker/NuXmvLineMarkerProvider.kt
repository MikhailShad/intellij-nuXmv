package dev.mikhailshad.nuxmvplugin.ide.marker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.parser.NuXmvParserDefinition
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIsaModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleTypeSpecifier
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes

class NuXmvLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (!NuXmvParserDefinition.Util.IDENTIFIER.contains(element.node.elementType)) return
        val file = element.containingFile as? NuXmvFile ?: return

        when (val parent = element.parent) {
            is NuXmvModuleName -> {
                val usages = moduleUsages(file, element.text)
                if (usages.isNotEmpty()) {
                    val builder = NavigationGutterIconBuilder.create(NuXmvIcons.MODULE_LINK_FROM)
                        .setTargets(usages)
                        .setTooltipText("Navigate to module usages")
                    result.add(builder.createLineMarkerInfo(element))
                }
            }

            is NuXmvModuleTypeSpecifier, is NuXmvIsaModuleName -> {
                if (parent.firstChild !== element) return
                val declaration = NuXmvScopes.modulesIn(file)[element.text]?.moduleDeclaration ?: return
                val builder = NavigationGutterIconBuilder.create(NuXmvIcons.MODULE_LINK_TO)
                    .setTarget(declaration)
                    .setTooltipText("Navigate to module declaration")
                result.add(builder.createLineMarkerInfo(element))
            }
        }
    }

    private fun moduleUsages(file: NuXmvFile, moduleName: String): List<PsiElement> =
        PsiTreeUtil.findChildrenOfAnyType(file, NuXmvModuleTypeSpecifier::class.java, NuXmvIsaModuleName::class.java)
            .filter { it.firstChild?.text == moduleName }
}
