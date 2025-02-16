// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleBody;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNuXmvModule;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvNuXmvModuleImpl extends ASTWrapperPsiElement implements NuXmvNuXmvModule {

    public NuXmvNuXmvModuleImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitNuXmvModule(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvModuleBody getModuleBody() {
        return findChildByClass(NuXmvModuleBody.class);
    }

    @Override
    @NotNull
    public NuXmvModuleDeclaration getModuleDeclaration() {
        return findNotNullChildByClass(NuXmvModuleDeclaration.class);
    }

}
