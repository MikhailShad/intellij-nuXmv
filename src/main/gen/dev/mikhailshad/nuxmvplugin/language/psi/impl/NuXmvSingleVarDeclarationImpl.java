// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleVarDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.SingleVarDeclarationMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvSingleVarDeclarationImpl extends SingleVarDeclarationMixin implements NuXmvSingleVarDeclaration {

    public NuXmvSingleVarDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitSingleVarDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvTypeSpecifier getTypeSpecifier() {
        return findChildByClass(NuXmvTypeSpecifier.class);
    }

    @Override
    @NotNull
    public NuXmvVarName getVarName() {
        return findNotNullChildByClass(NuXmvVarName.class);
    }

}
