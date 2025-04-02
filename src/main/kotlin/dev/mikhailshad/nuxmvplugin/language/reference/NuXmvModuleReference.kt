package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.index.NuXmvModuleIndex

/**
 * Reference implementation for modules in nuXmv
 */
class NuXmvModuleReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private val moduleName: String = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = element.project
        val moduleDefinition = NuXmvModuleIndex.findModule(project, moduleName)

        return if (moduleDefinition != null) {
            arrayOf(PsiElementResolveResult(moduleDefinition.moduleDeclaration))
        } else {
            ResolveResult.EMPTY_ARRAY
        }
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val project = element.project
        val moduleNames = NuXmvModuleIndex.getAllModuleNames(project)

        return moduleNames.map { moduleName ->
            LookupElementBuilder.create(moduleName, moduleName.text)
                .withIcon(NuXmvIcons.MODULE)
                .withTypeText("MODULE")
        }.toTypedArray()
    }
}
