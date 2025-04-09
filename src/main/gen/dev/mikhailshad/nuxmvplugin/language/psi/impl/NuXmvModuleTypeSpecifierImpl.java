// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvParameterList;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.IDENTIFIER;

public class NuXmvModuleTypeSpecifierImpl extends ASTWrapperPsiElement implements NuXmvModuleTypeSpecifier {

    public NuXmvModuleTypeSpecifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitModuleTypeSpecifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvParameterList getParameterList() {
        return findNotNullChildByClass(NuXmvParameterList.class);
    }

    @Override
    @NotNull
    public PsiElement getIdentifier() {
        return findNotNullChildByType(IDENTIFIER);
    }

}
