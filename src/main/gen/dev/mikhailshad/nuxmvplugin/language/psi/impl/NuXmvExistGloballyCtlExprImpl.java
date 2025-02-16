// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExistGloballyCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvExistGloballyCtlExprImpl extends NuXmvCtlExprImpl implements NuXmvExistGloballyCtlExpr {

    public NuXmvExistGloballyCtlExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitExistGloballyCtlExpr(this);
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
