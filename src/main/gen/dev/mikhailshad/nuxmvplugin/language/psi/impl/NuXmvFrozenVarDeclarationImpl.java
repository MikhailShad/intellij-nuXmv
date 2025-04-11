// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFrozenVarDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvFrozenVarDeclarationImpl extends ASTWrapperPsiElement implements NuXmvFrozenVarDeclaration {

    public NuXmvFrozenVarDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitFrozenVarDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<NuXmvSingleIvarDeclaration> getSingleIvarDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvSingleIvarDeclaration.class);
    }

}
