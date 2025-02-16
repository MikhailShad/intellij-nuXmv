// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

public class NuXmvParameterSynthProblemImpl extends ASTWrapperPsiElement implements NuXmvParameterSynthProblem {

    public NuXmvParameterSynthProblemImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitParameterSynthProblem(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvIdList getIdList() {
        return findNotNullChildByClass(NuXmvIdList.class);
    }

    @Override
    @NotNull
    public NuXmvLtlExpr getLtlExpr() {
        return findNotNullChildByClass(NuXmvLtlExpr.class);
    }

    @Override
    @NotNull
    public NuXmvSynthOpts getSynthOpts() {
        return findNotNullChildByClass(NuXmvSynthOpts.class);
    }

}
