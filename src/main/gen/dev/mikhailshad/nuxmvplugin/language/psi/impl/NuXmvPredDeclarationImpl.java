// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPredDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvPredDeclarationImpl extends ASTWrapperPsiElement implements NuXmvPredDeclaration {

    public NuXmvPredDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitPredDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<NuXmvBasicExpr> getBasicExprList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvBasicExpr.class);
    }

}
