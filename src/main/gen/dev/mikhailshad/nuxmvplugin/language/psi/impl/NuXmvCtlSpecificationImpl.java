// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCtlSpecification;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvCtlSpecificationImpl extends ASTWrapperPsiElement implements NuXmvCtlSpecification {

    public NuXmvCtlSpecificationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitCtlSpecification(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvCtlExpr getCtlExpr() {
        return findChildByClass(NuXmvCtlExpr.class);
    }

    @Override
    @Nullable
    public NuXmvSimpleIdentifier getSimpleIdentifier() {
        return findChildByClass(NuXmvSimpleIdentifier.class);
    }

}
