// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvConstantsBody;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvConstantsDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvConstantsDeclarationImpl extends ASTWrapperPsiElement implements NuXmvConstantsDeclaration {

    public NuXmvConstantsDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitConstantsDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvConstantsBody getConstantsBody() {
        return findChildByClass(NuXmvConstantsBody.class);
    }

}
