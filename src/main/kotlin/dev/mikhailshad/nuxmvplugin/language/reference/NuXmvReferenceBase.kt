package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement

abstract class NuXmvReferenceBase(@JvmField protected val psiElement: PsiElement, range: TextRange) :
    PsiReferenceBase<PsiElement?>(psiElement, range), PsiPolyVariantReference {

    protected abstract fun resolveInner(incompleteCode: Boolean): List<PsiElement>

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        return ResolveCache.getInstance(psiElement.project).resolveWithCaching(
            this, { nuXmvReferenceBase, _ ->
                nuXmvReferenceBase.resolveInner(false)
                    .map { PsiElementResolveResult(it) }
                    .toTypedArray()
            },
            true, false
        )
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        return resolveInner(true).toTypedArray()
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        return if (element is PsiNamedElement && psiElement !is PsiNamedElement) {
            val newName = element.name
            val oldName = psiElement.text
            if (newName != null && oldName != newName) {
                handleElementRename(newName)
            } else {
                psiElement
            }
        } else {
            super.bindToElement(element)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is NuXmvReferenceBase) psiElement == other.psiElement else false
    }

    override fun hashCode(): Int {
        return psiElement.hashCode()
    }

    protected val namedElementOwner: PsiElement?
        get() {
            val namedElement = psiElement.parentOfType<NuXmvNamedElement>()
            return if (namedElement?.nameIdentifier === psiElement) namedElement else null
        }
}
