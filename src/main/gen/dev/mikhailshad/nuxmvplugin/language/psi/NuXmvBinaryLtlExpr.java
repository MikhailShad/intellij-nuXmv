// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface NuXmvBinaryLtlExpr extends NuXmvExpr {

    @NotNull
    NuXmvBinaryLtlOp getBinaryLtlOp();

    @NotNull
    List<NuXmvExpr> getExprList();

}
