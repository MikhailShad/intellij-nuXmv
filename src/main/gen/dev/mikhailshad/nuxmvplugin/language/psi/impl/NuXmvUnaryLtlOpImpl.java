// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvUnaryLtlOp;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvUnaryLtlOpImpl extends ASTWrapperPsiElement implements NuXmvUnaryLtlOp {

    public NuXmvUnaryLtlOpImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitUnaryLtlOp(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

}
