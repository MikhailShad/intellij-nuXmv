// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvWordConstant;
import org.jetbrains.annotations.NotNull;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.WORD;

public class NuXmvWordConstantImpl extends ASTWrapperPsiElement implements NuXmvWordConstant {

    public NuXmvWordConstantImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitWordConstant(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public PsiElement getWord() {
        return findNotNullChildByType(WORD);
    }

}
