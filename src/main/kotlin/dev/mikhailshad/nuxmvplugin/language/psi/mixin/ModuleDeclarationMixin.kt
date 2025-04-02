// src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/psi/impl/mixin/NuXmvModuleDeclarationMixinImpl.kt
package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration

abstract class ModuleDeclarationMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvModuleDeclaration {

    fun getParameters(): List<String> {
        return this.moduleParameterList
            .map { it.simpleIdentifier.text }
    }
}
