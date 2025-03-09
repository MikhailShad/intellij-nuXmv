// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvLiteralBasicExprImpl extends NuXmvExprImpl implements NuXmvLiteralBasicExpr {

    public NuXmvLiteralBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitLiteralBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvBooleanConstant getBooleanConstant() {
        return findChildByClass(NuXmvBooleanConstant.class);
    }

    @Override
    @Nullable
    public NuXmvBuiltInConstant getBuiltInConstant() {
        return findChildByClass(NuXmvBuiltInConstant.class);
    }

    @Override
    @Nullable
    public NuXmvRangeConstant getRangeConstant() {
        return findChildByClass(NuXmvRangeConstant.class);
    }

    @Override
    @Nullable
    public NuXmvRealNumber getRealNumber() {
        return findChildByClass(NuXmvRealNumber.class);
    }

    @Override
    @Nullable
    public NuXmvWholeNumber getWholeNumber() {
        return findChildByClass(NuXmvWholeNumber.class);
    }

    @Override
    @Nullable
    public NuXmvWordConstant getWordConstant() {
        return findChildByClass(NuXmvWordConstant.class);
    }

}
