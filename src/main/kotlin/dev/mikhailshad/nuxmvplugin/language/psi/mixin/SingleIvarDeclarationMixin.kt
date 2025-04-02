package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.lang.ASTNode
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration

abstract class SingleIvarDeclarationMixin(node: ASTNode) : AbstractDeclarationMixin(node), NuXmvSingleIvarDeclaration
