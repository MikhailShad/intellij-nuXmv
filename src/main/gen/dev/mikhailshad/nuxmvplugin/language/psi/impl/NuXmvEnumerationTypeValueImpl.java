// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvEnumerationTypeValue;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvWholeNumber;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvEnumValueMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NuXmvEnumerationTypeValueImpl extends NuXmvEnumValueMixin implements NuXmvEnumerationTypeValue {

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
    @NotNull
    public List<NuXmvExpr> getExprList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvExpr.class);
    }

    @Override
    @Nullable
    public NuXmvWholeNumber getWholeNumber() {
        return findChildByClass(NuXmvWholeNumber.class);
    }

}
