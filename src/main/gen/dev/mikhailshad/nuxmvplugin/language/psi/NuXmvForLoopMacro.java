// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NuXmvForLoopMacro extends NuXmvMacro {

    @NotNull
    List<NuXmvAssignConstraint> getAssignConstraintList();

    @NotNull
    List<NuXmvCompassionConstraint> getCompassionConstraintList();

    @NotNull
    List<NuXmvComputeSpecification> getComputeSpecificationList();

    @NotNull
    List<NuXmvConstantsDeclaration> getConstantsDeclarationList();

    @NotNull
    List<NuXmvCtlSpecification> getCtlSpecificationList();

    @NotNull
    List<NuXmvDefineDeclaration> getDefineDeclarationList();

    @NotNull
    List<NuXmvFairnessConstraint> getFairnessConstraintList();

    @NotNull
    List<NuXmvForLoopMacro> getForLoopMacroList();

    @Nullable
    NuXmvForLoopVariable getForLoopVariable();

    @NotNull
    List<NuXmvFrozenVarDeclaration> getFrozenVarDeclarationList();

    @NotNull
    List<NuXmvFunctionDeclaration> getFunctionDeclarationList();

    @NotNull
    List<NuXmvInitConstraint> getInitConstraintList();

    @NotNull
    List<NuXmvInvarConstraint> getInvarConstraintList();

    @NotNull
    List<NuXmvInvarSpecification> getInvarSpecificationList();

    @NotNull
    List<NuXmvIsaDeclaration> getIsaDeclarationList();

    @NotNull
    List<NuXmvIvarDeclaration> getIvarDeclarationList();

    @NotNull
    List<NuXmvJusticeConstraint> getJusticeConstraintList();

    @NotNull
    List<NuXmvLtlSpecification> getLtlSpecificationList();

    @NotNull
    List<NuXmvMirrorDeclaration> getMirrorDeclarationList();

    @NotNull
    List<NuXmvParameterSynthProblemDeclaration> getParameterSynthProblemDeclarationList();

    @NotNull
    List<NuXmvPredDeclaration> getPredDeclarationList();

    @Nullable
    NuXmvRangeConstant getRangeConstant();

    @NotNull
    List<NuXmvTransConstraint> getTransConstraintList();

    @NotNull
    List<NuXmvVarDeclaration> getVarDeclarationList();

}
