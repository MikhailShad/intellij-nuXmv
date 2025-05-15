package dev.mikhailshad.nuxmvplugin.ide.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.mixin.NuXmvForLoopMacroMixin

class NuXmvFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(
        root: PsiElement,
        document: Document,
        quick: Boolean
    ): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        collectDescriptors(root, descriptors)
        return descriptors.toTypedArray()
    }

    private fun collectDescriptors(element: PsiElement, descriptors: MutableList<FoldingDescriptor>) {
        when (element) {
            is NuXmvModule,
            is NuXmvAssignConstraint,
            is NuXmvInitConstraint,
            is NuXmvJusticeConstraint,
            is NuXmvTransConstraint,
            is NuXmvInvarConstraint,
            is NuXmvFairnessConstraint,
            is NuXmvCompassionConstraint,
            is NuXmvConstantsDeclaration,
            is NuXmvDefineDeclaration,
            is NuXmvFrozenVarDeclaration,
            is NuXmvFunctionDeclaration,
            is NuXmvIsaDeclaration,
            is NuXmvIvarDeclaration,
            is NuXmvMirrorDeclaration,
            is NuXmvVarDeclaration,
            is NuXmvComputeSpecification,
            is NuXmvCtlSpecification,
            is NuXmvInvarSpecification,
            is NuXmvLtlSpecification,
            is NuXmvForLoopMacro -> {
                val range = element.textRange
                if (range.length > 50 || element.children.size > 10) {
                    descriptors.add(FoldingDescriptor(element.node, range))
                }
            }
        }
        if (element.elementType == NuXmvTypes.BLOCK_COMMENT) {
            descriptors.add(FoldingDescriptor(element, element.textRange))
        }

        element.children.forEach { collectDescriptors(it, descriptors) }
    }

    override fun getPlaceholderText(node: ASTNode): String {
        val psiNode = node.psi
        return when (psiNode) {
            is NuXmvModule -> "MODULE..."
            is NuXmvAssignConstraint -> "ASSIGN..."
            is NuXmvInitConstraint -> "INIT..."
            is NuXmvJusticeConstraint -> "JUSTICE..."
            is NuXmvTransConstraint -> "TRANS..."
            is NuXmvInvarConstraint -> "INVAR..."
            is NuXmvFairnessConstraint -> "FAIRNESS..."
            is NuXmvCompassionConstraint -> "COMPASSION..."
            is NuXmvConstantsDeclaration -> "CONSTANTS..."
            is NuXmvDefineDeclaration -> "DEFINE..."
            is NuXmvFrozenVarDeclaration -> "FROZENVAR..."
            is NuXmvFunctionDeclaration -> "FUNCTION..."
            is NuXmvIsaDeclaration -> "ISA..."
            is NuXmvIvarDeclaration -> "IVAR..."
            is NuXmvMirrorDeclaration -> "MIRROR..."
            is NuXmvVarDeclaration -> "VAR..."
            is NuXmvComputeSpecification -> "COMPUTE..."
            is NuXmvCtlSpecification -> "CTL..."
            is NuXmvInvarSpecification -> "INVAR..."
            is NuXmvLtlSpecification -> "LTL..."
            is NuXmvForLoopMacroMixin -> "FOR ${psiNode.loopVariableName} IN ${psiNode.loopRange} ..."

            else -> "..."
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
