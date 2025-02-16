// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleParameters;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvModuleDeclarationImpl extends ASTWrapperPsiElement implements NuXmvModuleDeclaration {

    public NuXmvModuleDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitModuleDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvModuleName getModuleName() {
        return findChildByClass(NuXmvModuleName.class);
    }

    @Override
    @Nullable
    public NuXmvModuleParameters getModuleParameters() {
        return findChildByClass(NuXmvModuleParameters.class);
    }

}
