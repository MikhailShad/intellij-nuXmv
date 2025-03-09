// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvCaseBasicExpr;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvRegularCaseBody;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvCaseBasicExprImpl extends NuXmvExprImpl implements NuXmvCaseBasicExpr {

    public NuXmvCaseBasicExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitCaseBasicExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<NuXmvRegularCaseBody> getRegularCaseBodyList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvRegularCaseBody.class);
    }

}
