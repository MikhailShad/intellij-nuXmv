// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvForAllUntilCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvForAllUntilCtlExprImpl extends NuXmvCtlExprImpl implements NuXmvForAllUntilCtlExpr {

    public NuXmvForAllUntilCtlExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitForAllUntilCtlExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<NuXmvCtlExpr> getCtlExprList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvCtlExpr.class);
    }

}
