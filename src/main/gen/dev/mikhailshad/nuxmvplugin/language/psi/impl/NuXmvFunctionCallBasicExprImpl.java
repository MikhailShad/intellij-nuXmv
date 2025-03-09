// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvBasicExprList;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionCallBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvFunctionCallBasicExprImpl extends NuXmvExprImpl implements NuXmvFunctionCallBasicExpr {

    public NuXmvFunctionCallBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitFunctionCallBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvBasicExprList getBasicExprList() {
        return findNotNullChildByClass(NuXmvBasicExprList.class);
    }

    @Override
    @NotNull
    public NuXmvFunctionIdentifier getFunctionIdentifier() {
        return findNotNullChildByClass(NuXmvFunctionIdentifier.class);
    }

}
