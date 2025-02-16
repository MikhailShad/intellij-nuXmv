// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComplexIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNextAssignExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvNextAssignExprImpl extends ASTWrapperPsiElement implements NuXmvNextAssignExpr {

    public NuXmvNextAssignExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitNextAssignExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvBasicExpr getBasicExpr() {
        return findNotNullChildByClass(NuXmvBasicExpr.class);
    }

    @Override
    @NotNull
    public NuXmvComplexIdentifier getComplexIdentifier() {
        return findNotNullChildByClass(NuXmvComplexIdentifier.class);
    }

}
