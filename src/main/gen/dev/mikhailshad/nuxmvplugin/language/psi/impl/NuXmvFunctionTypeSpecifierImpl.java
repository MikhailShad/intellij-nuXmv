// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionArgTypesSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

public class NuXmvFunctionTypeSpecifierImpl extends ASTWrapperPsiElement implements NuXmvFunctionTypeSpecifier {

    public NuXmvFunctionTypeSpecifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitFunctionTypeSpecifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvFunctionArgTypesSpecifier getFunctionArgTypesSpecifier() {
        return findNotNullChildByClass(NuXmvFunctionArgTypesSpecifier.class);
    }

    @Override
    @NotNull
    public NuXmvSimpleTypeSpecifier getSimpleTypeSpecifier() {
        return findNotNullChildByClass(NuXmvSimpleTypeSpecifier.class);
    }

}
