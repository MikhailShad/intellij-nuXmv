// src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/reference/NuXmvReferenceContributor.kt
package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComplexIdentifier

class NuXmvReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(NuXmvComplexIdentifier::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val complexId = element as NuXmvComplexIdentifier
                    val identifier = complexId.text
                    val range = TextRange(0, identifier.length)
                    return arrayOf(NuXmvReference(complexId, range))
                }
            }
        )
    }
}
