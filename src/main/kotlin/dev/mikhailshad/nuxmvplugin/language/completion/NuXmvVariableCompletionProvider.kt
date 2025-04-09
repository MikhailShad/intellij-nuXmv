// NuXmvVariableCompletionProvider.kt
package dev.mikhailshad.nuxmvplugin.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import dev.mikhailshad.nuxmvplugin.language.psi.*

object NuXmvVariableCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val position = parameters.position
        val file = position.containingFile

        if (file !is NuXmvFile) return

        val varDeclarations = PsiTreeUtil.findChildrenOfType(file, NuXmvSingleVarDeclaration::class.java)
        val ivarDeclarations = PsiTreeUtil.findChildrenOfType(file, NuXmvSingleIvarDeclaration::class.java)
        val frozenVarDeclarations = PsiTreeUtil.findChildrenOfType(file, NuXmvSingleIvarDeclaration::class.java)
            .filter { it.parent is NuXmvFrozenVarDeclaration }

        val defineDeclarations = PsiTreeUtil.findChildrenOfType(file, NuXmvDefineBody::class.java)

        val containingModule = PsiTreeUtil.getParentOfType(position, NuXmvModule::class.java) ?: return

        varDeclarations.forEach { declaration ->
            if (PsiTreeUtil.isAncestor(containingModule, declaration, false)) {
                val varName = declaration.varName.text
                val typeText = declaration.typeSpecifier?.text
                resultSet.addElement(
                    LookupElementBuilder.create(varName)
                        .withTypeText("VAR: $typeText")
                )
            }
        }

        ivarDeclarations.forEach { declaration ->
            if (PsiTreeUtil.isAncestor(containingModule, declaration, false)) {
                val varName = declaration.varName.text
                val typeText = declaration.simpleTypeSpecifier?.text
                resultSet.addElement(
                    LookupElementBuilder.create(varName)
                        .withTypeText("IVAR: $typeText")
                )
            }
        }

        frozenVarDeclarations.forEach { declaration ->
            if (PsiTreeUtil.isAncestor(containingModule, declaration, false)) {
                val varName = declaration.varName.text
                val typeText = declaration.simpleTypeSpecifier?.text
                resultSet.addElement(
                    LookupElementBuilder.create(varName)
                        .withTypeText("FROZENVAR: $typeText")
                )
            }
        }

        defineDeclarations.forEach { declaration ->
            if (PsiTreeUtil.isAncestor(containingModule, declaration, false)) {
                val defineName = declaration.defineName.text
                resultSet.addElement(
                    LookupElementBuilder.create(defineName)
                        .withTypeText("DEFINE")
                )
            }
        }
    }
}
