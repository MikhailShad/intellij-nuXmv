package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvReference(element: NuXmvNamedElement, textRange: TextRange) :
    PsiReferenceBase<NuXmvNamedElement>(element, textRange), PsiPolyVariantReference {
    private val referenceName = element.text

    override fun resolve(): PsiElement? {
        if (isDeclaration(element)) {
            return null
        }

        val file = element.containingFile
        val module = PsiTreeUtil.getParentOfType(element, NuXmvModule::class.java) ?: return null

        // Check variables in current module
        val varResult = resolveInModule(module, referenceName)
        if (varResult != null) return varResult

        // Check for global modules
        val modules = findModules(file)
        val moduleResult = modules.find { it.name == referenceName }
        return moduleResult
    }

    private fun resolveInModule(module: NuXmvModule, name: String): PsiElement? {
        // Check module parameters
        val parameters = PsiTreeUtil.findChildrenOfType(module, NuXmvModuleParameter::class.java)
        parameters.find { it.name == name }?.let { return it }

        // Check variable declarations
        val vars = findVarDeclarations(module)
        vars.find { it.name == name }?.let { return it }

        // Check define declarations
        val defines = findDefineDeclarations(module)
        defines.find { it.text.contains(name) }?.let { return it }

        return null
    }

    private fun findModules(file: PsiFile): List<NuXmvModuleName> {
        return PsiTreeUtil.findChildrenOfType(file, NuXmvModuleName::class.java).toList()
    }

    private fun findVarDeclarations(module: NuXmvModule): List<NuXmvVarName> {
        return PsiTreeUtil.findChildrenOfType(module, NuXmvVarName::class.java).toList()
    }

    private fun findDefineDeclarations(module: NuXmvModule): List<NuXmvDefineName> {
        val defineDeclarations = PsiTreeUtil.findChildrenOfType(module, NuXmvDefineDeclaration::class.java)
        return defineDeclarations.map { PsiTreeUtil.findChildrenOfType(it, NuXmvDefineName::class.java) }
            .flatten()
            .toList()
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val result = resolve()
        return if (result != null) {
            arrayOf(PsiElementResolveResult(result))
        } else {
            ResolveResult.EMPTY_ARRAY
        }
    }

    private fun isDeclaration(element: PsiElement): Boolean {
        val parent = element.parent
        return parent is NuXmvVarName
                || parent is NuXmvModuleName
                || parent is NuXmvModuleParameter
                || parent is NuXmvDefineName
    }
}
