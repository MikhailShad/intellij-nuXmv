// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvGreaterEqBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvGreaterEqBasicExprImpl extends NuXmvExprImpl implements NuXmvGreaterEqBasicExpr {

    public NuXmvGreaterEqBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitGreaterEqBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<NuXmvExpr> getExprList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvExpr.class);
    }

}
