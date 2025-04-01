// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFunctionIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSimpleIdentifier;
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVisitor;
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.FunctionIdentifierMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NuXmvFunctionIdentifierImpl extends FunctionIdentifierMixin implements NuXmvFunctionIdentifier {

    public NuXmvFunctionIdentifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitFunctionIdentifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public NuXmvSimpleIdentifier getSimpleIdentifier() {
        return findChildByClass(NuXmvSimpleIdentifier.class);
    }

}
