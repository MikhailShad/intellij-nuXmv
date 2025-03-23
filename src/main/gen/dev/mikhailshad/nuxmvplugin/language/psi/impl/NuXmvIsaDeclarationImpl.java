// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIsaDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.IDENTIFIER;

public class NuXmvIsaDeclarationImpl extends ASTWrapperPsiElement implements NuXmvIsaDeclaration {

    public NuXmvIsaDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitIsaDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getIdentifier() {
        return findChildByType(IDENTIFIER);
    }

}
