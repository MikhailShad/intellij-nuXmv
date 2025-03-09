// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import org.jetbrains.annotations.Nullable;

public interface NuXmvLiteralBasicExpr extends NuXmvExpr {

    @Nullable
    NuXmvBooleanConstant getBooleanConstant();

    @Nullable
    NuXmvBuiltInConstant getBuiltInConstant();

    @Nullable
    NuXmvRangeConstant getRangeConstant();

    @Nullable
    NuXmvRealNumber getRealNumber();

    @Nullable
    NuXmvWholeNumber getWholeNumber();

    @Nullable
    NuXmvWordConstant getWordConstant();

}
