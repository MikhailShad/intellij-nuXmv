// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvConstant;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvConstantMixin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvConstantImpl extends NuXmvConstantMixin implements NuXmvConstant {

    public NuXmvConstantImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitConstant(this);
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
