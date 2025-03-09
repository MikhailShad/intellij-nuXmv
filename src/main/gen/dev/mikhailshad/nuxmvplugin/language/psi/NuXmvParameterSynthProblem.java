// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface NuXmvParameterSynthProblem extends PsiElement {

    @NotNull
    NuXmvExpr getExpr();

    @NotNull
    NuXmvIdList getIdList();

    @NotNull
    NuXmvSynthOpts getSynthOpts();

}
