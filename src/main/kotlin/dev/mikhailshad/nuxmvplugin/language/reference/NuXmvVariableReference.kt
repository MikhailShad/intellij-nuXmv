package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.index.NuXmvVariableIndex
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration

/**
 * Reference implementation for variables in nuXmv
 */
class NuXmvVariableReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private val variableName: String = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results = mutableListOf<ResolveResult>()

        // Find the closest containing module
        val containingModule =
            PsiTreeUtil.getParentOfType(element, NuXmvModule::class.java) ?: return ResolveResult.EMPTY_ARRAY

        // First try to find a VAR declaration
        val varDeclaration = NuXmvVariableIndex.findVarDeclarationInModule(containingModule, variableName)
        if (varDeclaration != null) {
            results.add(PsiElementResolveResult(varDeclaration))
        }

        // Then try to find an IVAR declaration
        val ivarDeclaration = NuXmvVariableIndex.findIvarDeclarationInModule(containingModule, variableName)
        if (ivarDeclaration != null) {
            results.add(PsiElementResolveResult(ivarDeclaration))
        }

        return results.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val containingModule = PsiTreeUtil.getParentOfType(element, NuXmvModule::class.java) ?: return emptyArray()
        val variables = NuXmvVariableIndex.getAllVariablesInModule(containingModule)

        return variables.map {
            when (it) {
                is NuXmvSingleVarDeclaration -> {
                    LookupElementBuilder.create(it, it.varName.text)
                        .withIcon(NuXmvIcons.VAR)
                        .withTypeText("VAR")
                        .withTailText(" : ${it.typeSpecifier?.text ?: ""}")
                }

                is NuXmvSingleIvarDeclaration -> {
                    LookupElementBuilder.create(it, it.varName.text)
                        .withIcon(NuXmvIcons.IVAR)
                        .withTypeText("IVAR")
                        .withTailText(" : ${it.simpleTypeSpecifier?.text ?: ""}")
                }

                else -> null
            }
        }.filterNotNull().toTypedArray()
    }
}
