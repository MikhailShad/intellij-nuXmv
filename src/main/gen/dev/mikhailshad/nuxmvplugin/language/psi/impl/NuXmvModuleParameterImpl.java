// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleParameter;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.ModuleParameterMixin;
import org.jetbrains.annotations.NotNull;

public class NuXmvModuleParameterImpl extends ModuleParameterMixin implements NuXmvModuleParameter {

    public NuXmvModuleParameterImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitModuleParameter(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public NuXmvSimpleIdentifier getSimpleIdentifier() {
        return findNotNullChildByClass(NuXmvSimpleIdentifier.class);
    }

}
