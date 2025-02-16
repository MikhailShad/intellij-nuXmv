// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvLtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvParenthesisLtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvParenthesisLtlExprImpl extends NuXmvLtlExprImpl implements NuXmvParenthesisLtlExpr {

    public NuXmvParenthesisLtlExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitParenthesisLtlExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvLtlExpr getLtlExpr() {
        return findNotNullChildByClass(NuXmvLtlExpr.class);
    }

}
