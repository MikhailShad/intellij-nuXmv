package dev.mikhailshad.nuxmvplugin.language.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        // Handle module documentation
        if (element is NuXmvModule) {
            return generateModuleDoc(element)
        }

        // Handle variable documentation
        if (element is NuXmvVarName) {
            return when (element.parent) {
                is NuXmvSingleVarDeclaration -> generateVarDoc(element.parent as NuXmvSingleVarDeclaration)
                is NuXmvSingleIvarDeclaration -> generateIvarDoc(element.parent as NuXmvSingleIvarDeclaration)
                else -> null
            }
        }

        // Handle define documentation
        if (element is NuXmvDefineName) {
            return generateDefineDoc(element.parent as NuXmvDefineBody)
        }

        return null
    }

    private fun generateModuleDoc(module: NuXmvModule): String {
        val moduleName = module.moduleDeclaration.moduleName?.name ?: "module_name"

        val builder = StringBuilder()

        // Definition section
        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("MODULE ")
        builder.append(moduleName)

        // Parameters
        val params = module.moduleDeclaration.moduleParameterList
        if (params.isNotEmpty()) {
            builder.append(params.map { it.name }.joinToString(", ", "(", ")"))
        }
        builder.append(DocumentationMarkup.DEFINITION_END)

        // Content section
        builder.append(DocumentationMarkup.CONTENT_START)

        // Get state variables
        val stateVars = PsiTreeUtil.findChildrenOfType(module, NuXmvSingleVarDeclaration::class.java)
        if (stateVars.isNotEmpty()) {
            builder.append("<b>State Variables:</b><br>")
            stateVars.forEach {
                val varName = it.varName.text ?: "unnamed"
                val typeText = it.typeSpecifier?.text ?: "unknown type"
                builder.append("$varName : $typeText<br>")
            }
            builder.append("<br>")
        }

        // Get input variables
        val inputVars = PsiTreeUtil.findChildrenOfType(module, NuXmvSingleIvarDeclaration::class.java)
        if (inputVars.isNotEmpty()) {
            builder.append("<b>Input Variables:</b><br>")
            inputVars.forEach {
                val varName = it.varName.text ?: "unnamed"
                val typeText = it.simpleTypeSpecifier?.text ?: "unknown type"
                builder.append("$varName : $typeText<br>")
            }
            builder.append("<br>")
        }

        // Get DEFINEs
        val defines = PsiTreeUtil.findChildrenOfType(module, NuXmvDefineBody::class.java)
        if (defines.isNotEmpty()) {
            builder.append("<b>Defines:</b><br>")
            defines.forEach {
                val defineName = it.defineName.name ?: "unnamed"
                builder.append("$defineName<br>")
            }
        }

        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }

    private fun generateVarDoc(varDecl: NuXmvSingleVarDeclaration): String {
        val varName = varDecl.varName.name ?: "unnamed"
        val typeText = varDecl.typeSpecifier?.text ?: "unknown type"

        val builder = StringBuilder()

        // Definition section
        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("State Variable: ")
        builder.append(varName)
        builder.append(DocumentationMarkup.DEFINITION_END)

        // Content section
        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append("<b>Type:</b> $typeText<br>")

        // Find assignments to this variable
        val module = PsiTreeUtil.getParentOfType(varDecl, NuXmvModule::class.java) ?: return builder.toString()
        val moduleBody = module.moduleBody ?: return builder.toString()

        // Find simple assignments
        val simpleAssigns = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvSimpleAssignExpr::class.java)
            .filter { it.firstChild.text == varName }

        // Find init assignments
        val initAssigns = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvInitAssignExpr::class.java)
            .filter { it.firstChild.nextSibling.text == varName }

        // Find next assignments
        val nextAssigns = PsiTreeUtil.findChildrenOfType(moduleBody, NuXmvNextAssignExpr::class.java)
            .filter { it.firstChild.firstChild.text == varName }

        if (initAssigns.isNotEmpty()) {
            builder.append("<b>Initial Value:</b><br>")
            initAssigns.forEach {
                builder.append("init($varName) := ${it.expr?.text}}<br>")
            }
            builder.append("<br>")
        }

        if (nextAssigns.isNotEmpty()) {
            builder.append("<b>Next Value:</b><br>")
            nextAssigns.forEach {
                builder.append("next($varName) := ${it.expr?.text}}<br>")
            }
            builder.append("<br>")
        }

        if (simpleAssigns.isNotEmpty()) {
            builder.append("<b>Assignments:</b><br>")
            simpleAssigns.forEach {
                builder.append("$varName := ${it.expr?.text}}<br>")
            }
        }

        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }

    private fun generateIvarDoc(ivarDecl: NuXmvSingleIvarDeclaration): String {
        val varName = ivarDecl.varName.text ?: "unnamed"
        val typeText = ivarDecl.simpleTypeSpecifier?.text ?: "unknown type"

        val builder = StringBuilder()

        // Definition section
        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("Input Variable: ")
        builder.append(varName)
        builder.append(DocumentationMarkup.DEFINITION_END)

        // Content section
        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append("<b>Type:</b> $typeText<br>")
        builder.append("<br>")
        builder.append("Input variables are used to model external inputs to the system.")
        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }

    private fun generateDefineDoc(defineBody: NuXmvDefineBody): String {
        val defineName = defineBody.defineName.name ?: "unnamed"
        val exprText = defineBody.expr?.text?.take(100) ?: "..."

        val builder = StringBuilder()

        // Definition section
        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append("Define: ")
        builder.append(defineName)
        builder.append(DocumentationMarkup.DEFINITION_END)

        // Content section
        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append("<b>Expression:</b><br>")
        builder.append("$defineName := $exprText")
        if (exprText.length >= 100) {
            builder.append("...")
        }
        builder.append("<br><br>")
        builder.append("DEFINEs in nuXmv are used to create macros or shortcuts for expressions.")
        builder.append(DocumentationMarkup.CONTENT_END)

        return builder.toString()
    }
}
