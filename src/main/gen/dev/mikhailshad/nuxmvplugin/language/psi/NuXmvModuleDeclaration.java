// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NuXmvModuleDeclaration extends PsiElement {

    @Nullable
    NuXmvModuleName getModuleName();

    @NotNull
    List<NuXmvModuleParameter> getModuleParameterList();

}
