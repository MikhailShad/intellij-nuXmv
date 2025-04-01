// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleBody;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleDeclaration;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvModuleImpl extends ASTWrapperPsiElement implements NuXmvModule {

    public NuXmvModuleImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitModule(this);
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
