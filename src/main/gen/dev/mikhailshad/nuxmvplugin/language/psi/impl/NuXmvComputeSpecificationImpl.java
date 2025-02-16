// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComplexIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComputeExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComputeSpecification;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvComputeSpecificationImpl extends ASTWrapperPsiElement implements NuXmvComputeSpecification {

    public NuXmvComputeSpecificationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitComputeSpecification(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvComplexIdentifier getComplexIdentifier() {
        return findChildByClass(NuXmvComplexIdentifier.class);
    }

    @Override
    @Nullable
    public NuXmvComputeExpr getComputeExpr() {
        return findChildByClass(NuXmvComputeExpr.class);
    }

}
