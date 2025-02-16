// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvRealNumber;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.*;

public class NuXmvRealNumberImpl extends ASTWrapperPsiElement implements NuXmvRealNumber {

    public NuXmvRealNumberImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitRealNumber(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getExponentialNumber() {
        return findChildByType(EXPONENTIAL_NUMBER);
    }

    @Override
    @Nullable
    public PsiElement getFloatNumber() {
        return findChildByType(FLOAT_NUMBER);
    }

    @Override
    @Nullable
    public PsiElement getFractionalNumber() {
        return findChildByType(FRACTIONAL_NUMBER);
    }

}
