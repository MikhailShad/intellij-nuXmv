package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNamedElement

class NuXmvReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withLanguage(NuXmvLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    if (element !is NuXmvNamedElement) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val elementName = element.name ?: return PsiReference.EMPTY_ARRAY
                    var offset = 0
                    val references: MutableList<NuXmvReference> = mutableListOf()
                    for (identifier in elementName.split(".")) {
                        val reference = NuXmvReference(element, TextRange.from(offset, offset + identifier.length))
                        offset += identifier.length + 1
                        references.add(reference)
                    }

                    return references.toTypedArray()
                }
            }
        )
    }
}
