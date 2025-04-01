// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NuXmvComplexIdentifierImpl extends ASTWrapperPsiElement implements NuXmvComplexIdentifier {

    public NuXmvComplexIdentifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitComplexIdentifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvBaseIdentifier getBaseIdentifier() {
        return findNotNullChildByClass(NuXmvBaseIdentifier.class);
    }

    @Override
    @Nullable
    public NuXmvExpr getExpr() {
        return findChildByClass(NuXmvExpr.class);
    }

    @Override
    @NotNull
    public List<NuXmvSimpleIdentifier> getSimpleIdentifierList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvSimpleIdentifier.class);
    }

}
