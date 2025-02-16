// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForAllNextStateCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvForAllNextStateCtlExprImpl extends NuXmvCtlExprImpl implements NuXmvForAllNextStateCtlExpr {

    public NuXmvForAllNextStateCtlExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitForAllNextStateCtlExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvCtlExpr getCtlExpr() {
        return findNotNullChildByClass(NuXmvCtlExpr.class);
    }

}
