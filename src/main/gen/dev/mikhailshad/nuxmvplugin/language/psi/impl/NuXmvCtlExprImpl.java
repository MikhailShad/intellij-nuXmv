// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvParenthesisCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvCtlExprImpl extends ASTWrapperPsiElement implements NuXmvCtlExpr {

    public NuXmvCtlExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitCtlExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvBasicExpr getBasicExpr() {
        return findChildByClass(NuXmvBasicExpr.class);
    }

    @Override
    @Nullable
    public NuXmvParenthesisCtlExpr getParenthesisCtlExpr() {
        return findChildByClass(NuXmvParenthesisCtlExpr.class);
    }

}
