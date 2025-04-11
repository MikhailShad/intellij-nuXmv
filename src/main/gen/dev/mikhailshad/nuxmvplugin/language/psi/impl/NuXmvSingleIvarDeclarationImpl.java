// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvSingleIvarDeclarationMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvSingleIvarDeclarationImpl extends NuXmvSingleIvarDeclarationMixin implements NuXmvSingleIvarDeclaration {

    public NuXmvSingleIvarDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitSingleIvarDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvSimpleTypeSpecifier getSimpleTypeSpecifier() {
        return findChildByClass(NuXmvSimpleTypeSpecifier.class);
    }

    @Override
    @NotNull
    public NuXmvVarName getVarName() {
        return findNotNullChildByClass(NuXmvVarName.class);
    }

}
