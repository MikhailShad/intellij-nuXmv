// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvComplexIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvEnumerationTypeValue;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvWholeNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvEnumerationTypeValueImpl extends ASTWrapperPsiElement implements NuXmvEnumerationTypeValue {

    public NuXmvEnumerationTypeValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitEnumerationTypeValue(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvComplexIdentifier getComplexIdentifier() {
        return findChildByClass(NuXmvComplexIdentifier.class);
    }

    @Override
    @Nullable
    public NuXmvWholeNumber getWholeNumber() {
        return findChildByClass(NuXmvWholeNumber.class);
    }

}
