// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvReferenceBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvReferenceBasicExprImpl extends NuXmvExprImpl implements NuXmvReferenceBasicExpr {

    public NuXmvReferenceBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitReferenceBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvIdentifierUsage getIdentifierUsage() {
        return findNotNullChildByClass(NuXmvIdentifierUsage.class);
    }

}
