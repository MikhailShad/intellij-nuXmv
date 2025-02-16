// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NuXmvComplexIdentifier extends PsiElement {

    @Nullable
    NuXmvBasicExpr getBasicExpr();

    @NotNull
    List<NuXmvSimpleIdentifier> getSimpleIdentifierList();

}
