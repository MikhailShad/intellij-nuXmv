package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvReference(element: PsiElement, range: TextRange) : PsiReferenceBase<PsiElement>(element, range) {
    private val referenceName: String = element.text.substring(range.startOffset, range.endOffset)

    override fun resolve(): PsiElement? {
        if (element is NuXmvComplexIdentifier) {
            val complexIdentifier = element as NuXmvComplexIdentifier
            if (complexIdentifier.simpleIdentifierList.size > 1) {
                // TODO: Complex module reference resolution is more complex
                return null
            }
        }

        val simpleIdentifier = if (element is NuXmvSimpleIdentifier) element as NuXmvSimpleIdentifier else null
        val symbolName = simpleIdentifier?.text ?: referenceName
        val containingModule = PsiTreeUtil.getParentOfType(element, NuXmvNuXmvModule::class.java) ?: return null
        return findDeclarationInModule(containingModule, symbolName)
    }

    private fun findDeclarationInModule(module: NuXmvNuXmvModule, symbolName: String): PsiElement? {
        val moduleBody = module.moduleBody ?: return null
        PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleVarDeclaration::class.java).forEach {
            val varName = it.varName.text ?: return@forEach
            if (varName == symbolName) {
                return it.varName
            }
        }

        PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSingleIvarDeclaration::class.java).forEach {
            val varName = it.varName.text ?: return@forEach
            if (varName == symbolName) {
                return it.varName
            }
        }

        PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvDefineBody::class.java).forEach {
            val defineName = it.complexIdentifier.text ?: return@forEach
            if (defineName == symbolName) {
                return it.complexIdentifier
            }
        }

        val moduleParameters = module.moduleDeclaration.moduleParameters
        if (moduleParameters != null) {
            val identifiers = PsiTreeUtil.findChildrenOfType(moduleParameters, PsiElement::class.java)
            for (identifier in identifiers) {
                if (identifier.text == symbolName) {
                    return identifier
                }
            }
        }

        return null
    }

    override fun getVariants(): Array<Any> {
        return emptyArray()
    }
}
