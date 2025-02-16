// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvSingleAssignConstraintImpl extends ASTWrapperPsiElement implements NuXmvSingleAssignConstraint {

    public NuXmvSingleAssignConstraintImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitSingleAssignConstraint(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvInitAssignExpr getInitAssignExpr() {
        return findChildByClass(NuXmvInitAssignExpr.class);
    }

    @Override
    @Nullable
    public NuXmvNextAssignExpr getNextAssignExpr() {
        return findChildByClass(NuXmvNextAssignExpr.class);
    }

    @Override
    @Nullable
    public NuXmvSimpleAssignExpr getSimpleAssignExpr() {
        return findChildByClass(NuXmvSimpleAssignExpr.class);
    }

}
