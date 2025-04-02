package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration

abstract class SingleVarDeclarationMixin(node: ASTNode) : AbstractDeclarationMixin(node),
    NuXmvSingleVarDeclaration
