package dev.mikhailshad.nuxmvplugin.ide.marker

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import dev.mikhailshad.nuxmvplugin.ide.run.command.NuXmvApplicability
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile

/** Run icons on `MODULE main` (all checks or simulation) and on every specification that can be checked. */
class NuXmvRunLineMarkerContributor : RunLineMarkerContributor() {
    private val specKeywords = TokenSet.create(
        NuXmvTypes.CTLSPEC_KW,
        NuXmvTypes.SPEC_KW,
        NuXmvTypes.LTLSPEC_KW,
        NuXmvTypes.INVARSPEC_KW,
        NuXmvTypes.PSLSPEC_KW,
        NuXmvTypes.COMPUTE_KW
    )

    override fun getInfo(element: PsiElement): Info? {
        val type = element.node?.elementType ?: return null
        val file = element.containingFile as? NuXmvFile ?: return null
        return when {
            type == NuXmvTypes.MODULE_KW && isMainModule(element) -> Info(
                AllIcons.RunConfigurations.TestState.Run,
                ExecutorAction.getActions(0)
            ) { "Run nuXmv on the model" }

            type in specKeywords -> specInfo(file, element)
            else -> null
        }
    }

    private fun specInfo(file: NuXmvFile, element: PsiElement): Info? {
        val profile = NuXmvModelProfile.of(file)
        val spec = profile.specAt(element) ?: return null
        if (spec.pointer.element?.firstChild != element) return null
        if (spec.instances.isEmpty()) return null
        if (NuXmvApplicability.kindUnavailableReason(profile.domain, spec.kind) != null) return null
        if (NuXmvApplicability.defaultFor(profile.domain, spec.kind, listOf(spec)) == null) return null
        return Info(AllIcons.RunConfigurations.TestState.Run, ExecutorAction.getActions(0)) {
            "Check ${spec.kind} ${spec.name ?: "specification"} (${domainName(profile.domain)})"
        }
    }

    private fun isMainModule(element: PsiElement): Boolean {
        val declaration = element.parent as? NuXmvModuleDeclaration ?: return false
        return declaration.parent is NuXmvModule && declaration.moduleName?.name == NuXmvModelProfile.MAIN_MODULE
    }

    private fun domainName(domain: NuXmvDomainType): String = domain.stringRepresentation.lowercase()
}
