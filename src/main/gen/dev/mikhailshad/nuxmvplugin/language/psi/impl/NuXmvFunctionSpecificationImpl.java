// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionName;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionSpecification;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionTypeSpecifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvFunctionSpecificationImpl extends ASTWrapperPsiElement implements NuXmvFunctionSpecification {

    public NuXmvFunctionSpecificationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitFunctionSpecification(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvFunctionName getFunctionName() {
        return findNotNullChildByClass(NuXmvFunctionName.class);
    }

    @Override
    @Nullable
    public NuXmvFunctionTypeSpecifier getFunctionTypeSpecifier() {
        return findChildByClass(NuXmvFunctionTypeSpecifier.class);
    }

}
