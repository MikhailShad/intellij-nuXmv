// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.*;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvSimpleTypeSpecifierMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.mikhailshad.nuxmvplugin.language.psi.NuXmvTypes.IDENTIFIER;

public class NuXmvSimpleTypeSpecifierImpl extends NuXmvSimpleTypeSpecifierMixin implements NuXmvSimpleTypeSpecifier {

    public NuXmvSimpleTypeSpecifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitSimpleTypeSpecifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvEnumerationTypeBody getEnumerationTypeBody() {
        return findChildByClass(NuXmvEnumerationTypeBody.class);
    }

    @Override
    @Nullable
    public NuXmvRangeConstant getRangeConstant() {
        return findChildByClass(NuXmvRangeConstant.class);
    }

    @Override
    @Nullable
    public NuXmvSimpleTypeSpecifier getSimpleTypeSpecifier() {
        return findChildByClass(NuXmvSimpleTypeSpecifier.class);
    }

    @Override
    @Nullable
    public NuXmvWholeNumber getWholeNumber() {
        return findChildByClass(NuXmvWholeNumber.class);
    }

    @Override
    @Nullable
    public PsiElement getIdentifier() {
        return findChildByType(IDENTIFIER);
    }

}
