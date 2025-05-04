// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import com.intellij.psi.PsiElement;
import dev.mikhailshad.nuxmvplugin.language.psi.type.TypeResolvable;
import org.jetbrains.annotations.Nullable;

public interface NuXmvSimpleTypeSpecifier extends TypeResolvable {

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
