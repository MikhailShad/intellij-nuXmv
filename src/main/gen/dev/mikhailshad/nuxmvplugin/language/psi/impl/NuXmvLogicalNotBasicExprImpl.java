// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvLogicalNotBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvLogicalNotBasicExprImpl extends NuXmvExprImpl implements NuXmvLogicalNotBasicExpr {

    public NuXmvLogicalNotBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitLogicalNotBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvExpr getExpr() {
        return findChildByClass(NuXmvExpr.class);
    }

}
