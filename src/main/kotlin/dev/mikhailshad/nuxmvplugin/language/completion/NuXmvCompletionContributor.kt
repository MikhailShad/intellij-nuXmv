package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withLanguage(NuXmvLanguage)
                .andOr(
                    psiElement().withParent(NuXmvModuleBody::class.java),
                    psiElement().withParent(NuXmvFile::class.java)
                ),
            NuXmvModuleKeywordCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement().withLanguage(NuXmvLanguage)
                .withAncestor(
                    2,
                    StandardPatterns.or(
                        psiElement(NuXmvModule::class.java),
                        psiElement(NuXmvMacro::class.java)
                    )
                ).andNot(
                    psiElement().withParent(NuXmvFile::class.java)
                ),
            NuXmvModuleElementSectionKeywordCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement().withLanguage(NuXmvLanguage)
                .withAncestor(
                    2,
                    StandardPatterns.or(
                        psiElement(NuXmvModuleDeclaration::class.java),
                        psiElement(NuXmvModule::class.java),
                    )
                ),
            NuXmvMacroCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(
                    StandardPatterns
                        .or(
                            psiElement(NuXmvSingleVarDeclaration::class.java),
                            psiElement(NuXmvSingleIvarDeclaration::class.java)
                        )
                )
                .afterLeaf(psiElement(NuXmvTypes.COLON)),
            NuXmvTypeCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(psiElement(NuXmvSingleVarDeclaration::class.java))
                .afterLeaf(NuXmvTypes.COLON.toString()),
            NuXmvModuleInstanceCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement().andOr(
                psiElement().inside(NuXmvExpr::class.java),
            ),
            NuXmvFunctionCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(NuXmvExpr::class.java)
                .afterLeaf(psiElement(NuXmvExpr::class.java)),
            NuXmvOperatorCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement().inside(NuXmvCtlSpecification::class.java),
            NuXmvCtlOperatorCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement().inside(NuXmvLtlSpecification::class.java),
            NuXmvLtlOperatorCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(NuXmvExpr::class.java)
                .afterLeaf(
                    StandardPatterns.or(
                        psiElement(NuXmvTypes.ASSIGN),
                        // TODO: по умному смотреть на операторы
                    )
                ),
            NuXmvVariableCompletionProvider
        )
    }
}
