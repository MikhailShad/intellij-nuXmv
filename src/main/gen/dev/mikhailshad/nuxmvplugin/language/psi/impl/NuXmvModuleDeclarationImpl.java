// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleParameter;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.ModuleDeclarationMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NuXmvModuleDeclarationImpl extends ModuleDeclarationMixin implements NuXmvModuleDeclaration {

    public NuXmvModuleDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitModuleDeclaration(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvModuleName getModuleName() {
        return findChildByClass(NuXmvModuleName.class);
    }

    @Override
    @NotNull
    public List<NuXmvModuleParameter> getModuleParameterList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvModuleParameter.class);
    }

}
