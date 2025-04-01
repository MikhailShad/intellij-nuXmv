package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvReferenceBasicExpr

class NuXmvReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(NuXmvReferenceBasicExpr::class.java)
                .withLanguage(NuXmvLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val expr = element as NuXmvReferenceBasicExpr
                    val complexIdentifier =
                        expr.complexIdentifier ?: expr.simpleIdentifier ?: return PsiReference.EMPTY_ARRAY

                    val text = complexIdentifier.text
                    if (text.isEmpty()) return PsiReference.EMPTY_ARRAY

                    val range = TextRange(0, text.length)

                    return arrayOf(NuXmvReference(complexIdentifier, range))
                }
            }
        )
    }
}
