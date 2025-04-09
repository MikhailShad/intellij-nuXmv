// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface NuXmvSimpleTypeSpecifier extends PsiElement {

    @Nullable
    NuXmvEnumerationTypeBody getEnumerationTypeBody();

    @Nullable
    NuXmvRangeConstant getRangeConstant();

    @Nullable
    NuXmvSimpleTypeSpecifier getSimpleTypeSpecifier();

    @Nullable
    NuXmvWholeNumber getWholeNumber();

    @Nullable
    PsiElement getIdentifier();

}
