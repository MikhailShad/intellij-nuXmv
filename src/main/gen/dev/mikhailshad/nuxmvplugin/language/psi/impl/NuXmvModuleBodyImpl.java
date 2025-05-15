// This is a generated file. Not intended for manual editing.
package dev.mikhailshad.nuxmvplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import dev.mikhailshad.nuxmvplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuXmvModuleBodyImpl extends ASTWrapperPsiElement implements NuXmvModuleBody {

    public NuXmvModuleBodyImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull NuXmvVisitor visitor) {
        visitor.visitModuleBody(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof NuXmvVisitor) accept((NuXmvVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<NuXmvAssignConstraint> getAssignConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvAssignConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvCompassionConstraint> getCompassionConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvCompassionConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvComputeSpecification> getComputeSpecificationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvComputeSpecification.class);
    }

    @Override
    @NotNull
    public List<NuXmvConstantsDeclaration> getConstantsDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvConstantsDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvCtlSpecification> getCtlSpecificationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvCtlSpecification.class);
    }

    @Override
    @NotNull
    public List<NuXmvDefineDeclaration> getDefineDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvDefineDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvFairnessConstraint> getFairnessConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvFairnessConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvForLoopMacro> getForLoopMacroList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvForLoopMacro.class);
    }

    @Override
    @NotNull
    public List<NuXmvFrozenVarDeclaration> getFrozenVarDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvFrozenVarDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvFunctionDeclaration> getFunctionDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvFunctionDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvInitConstraint> getInitConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvInitConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvInvarConstraint> getInvarConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvInvarConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvInvarSpecification> getInvarSpecificationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvInvarSpecification.class);
    }

    @Override
    @NotNull
    public List<NuXmvIsaDeclaration> getIsaDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvIsaDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvIvarDeclaration> getIvarDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvIvarDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvJusticeConstraint> getJusticeConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvJusticeConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvLtlSpecification> getLtlSpecificationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvLtlSpecification.class);
    }

    @Override
    @NotNull
    public List<NuXmvMirrorDeclaration> getMirrorDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvMirrorDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvParameterSynthProblemDeclaration> getParameterSynthProblemDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvParameterSynthProblemDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvPredDeclaration> getPredDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvPredDeclaration.class);
    }

    @Override
    @NotNull
    public List<NuXmvTransConstraint> getTransConstraintList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvTransConstraint.class);
    }

    @Override
    @NotNull
    public List<NuXmvVarDeclaration> getVarDeclarationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, NuXmvVarDeclaration.class);
    }

}
