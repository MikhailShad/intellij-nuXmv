// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIndexBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvWholeNumber;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvIndexBasicExprImpl extends NuXmvExprImpl implements NuXmvIndexBasicExpr {

    public NuXmvIndexBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitIndexBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvExpr getExpr() {
        return findNotNullChildByClass(NuXmvExpr.class);
    }

    @Override
    @NotNull
    public List<NuXmvWholeNumber> getWholeNumberList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvWholeNumber.class);
    }

}
