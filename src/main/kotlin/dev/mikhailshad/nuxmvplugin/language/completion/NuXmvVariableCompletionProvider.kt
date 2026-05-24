// NuXmvVariableCompletionProvider.kt
package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule

object NuXmvVariableCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val position = parameters.position
        if (position.containingFile !is NuXmvFile) return

        val containingModule = position.parentOfType<NuXmvModule>() ?: return
        val moduleBody = containingModule.moduleBody ?: return

        for (decl in moduleBody.varDeclarationList) {
            for (single in decl.singleVarDeclarationList) {
                resultSet.addElement(
                    LookupElementBuilder.create(single.varName.text)
                        .withTypeText("VAR: ${single.typeSpecifier?.text}")
                )
            }
        }

        for (decl in moduleBody.ivarDeclarationList) {
            for (single in decl.singleIvarDeclarationList) {
                resultSet.addElement(
                    LookupElementBuilder.create(single.varName.text)
                        .withTypeText("IVAR: ${single.simpleTypeSpecifier?.text}")
                )
            }
        }

        for (decl in moduleBody.frozenVarDeclarationList) {
            for (single in decl.singleIvarDeclarationList) {
                resultSet.addElement(
                    LookupElementBuilder.create(single.varName.text)
                        .withTypeText("FROZENVAR: ${single.simpleTypeSpecifier?.text}")
                )
            }
        }

        for (decl in moduleBody.defineDeclarationList) {
            for (define in decl.defineBodyList) {
                resultSet.addElement(
                    LookupElementBuilder.create(define.defineName.text)
                        .withTypeText("DEFINE")
                )
            }
        }
    }
}
