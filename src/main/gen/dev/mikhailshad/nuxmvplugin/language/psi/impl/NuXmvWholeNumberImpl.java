// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvWholeNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.INTEGER_NUMBER;
import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.POSITIVE_INTEGER_NUMBER;

public class NuXmvWholeNumberImpl extends ASTWrapperPsiElement implements NuXmvWholeNumber {

    public NuXmvWholeNumberImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitWholeNumber(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getIntegerNumber() {
        return findChildByType(INTEGER_NUMBER);
    }

    @Override
    @Nullable
    public PsiElement getPositiveIntegerNumber() {
        return findChildByType(POSITIVE_INTEGER_NUMBER);
    }

}
