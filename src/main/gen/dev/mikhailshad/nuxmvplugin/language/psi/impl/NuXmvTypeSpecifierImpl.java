// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvTypeSpecifierImpl extends ASTWrapperPsiElement implements NuXmvTypeSpecifier {

    public NuXmvTypeSpecifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitTypeSpecifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvModuleTypeSpecifier getModuleTypeSpecifier() {
        return findChildByClass(NuXmvModuleTypeSpecifier.class);
    }

    @Override
    @Nullable
    public NuXmvSimpleTypeSpecifier getSimpleTypeSpecifier() {
        return findChildByClass(NuXmvSimpleTypeSpecifier.class);
    }

}
