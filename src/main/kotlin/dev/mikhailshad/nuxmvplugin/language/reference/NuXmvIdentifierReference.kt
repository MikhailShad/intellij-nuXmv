package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.findParentOfType
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope
import dev.mikhailshad.nuxmvplugin.language.psi.scope.submoduleTypeOf
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvIdentifierReference.Companion.MAX_CONTEXT_DEPTH

/**
 * Resolves dotted identifier usages such as `submodule.field.subfield` against the cached
 * module scope. Each hop is an O(1) map lookup; nothing here walks the PSI subtree.
 */
class NuXmvIdentifierReference(
    element: NuXmvIdentifierUsage,
    private val segments: List<Segment>,
    private val index: Int
) : NuXmvReferenceBase(
    element,
    TextRange.from(segments[index].offset, segments[index].text.length)
) {

    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val file = element.containingFile as? NuXmvFile ?: return emptyList()

        var currentModule: NuXmvModule = element.findParentOfType<NuXmvModule>()
            ?: return resolveAtFileScope(file)

        // Walk hops 0..index-1, switching the current module via the submodule edge for each
        // intermediate segment. `self` keeps the current module.
        for (i in 0 until index) {
            val part = segments[i].text
            if (part == SELF) continue
            val moduleType = currentModule.submoduleTypeOf(part) ?: return emptyList()
            currentModule = NuXmvScopes.modulesIn(file)[moduleType] ?: return emptyList()
        }

        val part = segments[index].text
        if (part == SELF) {
            // `self` resolves to the containing module itself (goto navigates to the module header).
            return listOf(currentModule)
        }
        currentModule.scope().declarations[part]?.let { return listOf(it) }

        // Context-driven fallback: a bare identifier may name something that lives in a sibling
        // operand's owning module (e.g. an enum value of an IVAR whose type is declared inside
        // a submodule, a defined of a referenced submodule, etc.). Only meaningful for a
        // single-segment usage — anything dotted has already been resolved hop-by-hop.
        if (segments.size == 1) {
            resolveInContextModule(part)?.let { return listOf(it) }
        }
        return emptyList()
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val segmentLeaf = findSegmentLeaf() ?: return element
        val newLeaf = NuXmvElementFactory.createIdentifier(element.project, newElementName)
        segmentLeaf.replace(newLeaf)
        return element
    }

    // Override the base equality (which keys on psiElement only) so that ResolveCache and any
    // other consumer can tell two segment references on the same identifier usage apart.
    override fun equals(other: Any?): Boolean =
        other is NuXmvIdentifierReference && other.element === element && other.index == index

    override fun hashCode(): Int = element.hashCode() * 31 + index

    private fun findSegmentLeaf(): PsiElement? {
        var child: PsiElement? = element.firstChild
        var seen = 0
        while (child != null) {
            val type = child.node.elementType
            if (type == NuXmvTypes.IDENTIFIER || type == NuXmvTypes.SELF_KW) {
                if (seen == index) return child
                seen++
            }
            child = child.nextSibling
        }
        return null
    }

    private fun resolveInContextModule(name: String): PsiElement? {
        val contextUsage = findContextSourceUsage(element) ?: return null
        val contextDeclaration = contextUsage.references.lastOrNull()?.resolve() ?: return null
        val contextModule = contextDeclaration.parentOfType<NuXmvModule>() ?: return null
        if (contextModule === element.findParentOfType<NuXmvModule>()) return null
        return contextModule.scope().declarations[name]
    }

    /**
     * Walks the PSI tree upward from [start] looking for a sibling expression whose owning
     * module is the right place to look up a bare identifier. Returns that operand as a
     * [NuXmvIdentifierUsage], or null if no useful context is found within [MAX_CONTEXT_DEPTH].
     */
    private fun findContextSourceUsage(start: PsiElement): NuXmvIdentifierUsage? {
        var node: PsiElement = start
        repeat(MAX_CONTEXT_DEPTH) {
            val parent = node.parent ?: return null
            when (parent) {
                is NuXmvEqualityBasicExpr -> return otherOperandUsage(parent.exprList, node)
                is NuXmvNotEqualityBasicExpr -> return otherOperandUsage(parent.exprList, node)
                is NuXmvSimpleAssignExpr -> return parent.identifierUsage
                is NuXmvInitAssignExpr -> return parent.identifierUsage
                is NuXmvNextAssignExpr -> return parent.identifierUsage
                is NuXmvInBasicExpr -> {
                    val operands = parent.exprList
                    return if (operands.size >= 2 && !PsiTreeUtil.isAncestor(operands[0], node, false)) {
                        unwrapIdentifierUsage(operands[0])
                    } else {
                        null
                    }
                }
                // Transparent wrappers — keep climbing.
                is NuXmvParenthesisBasicExpr,
                is NuXmvCaseBasicExpr,
                is NuXmvRegularCaseBody,
                is NuXmvReferenceBasicExpr -> {
                    node = parent
                }

                else -> return null
            }
        }
        return null
    }

    private fun otherOperandUsage(operands: List<NuXmvExpr>, self: PsiElement): NuXmvIdentifierUsage? {
        val other = operands.firstOrNull { !PsiTreeUtil.isAncestor(it, self, false) } ?: return null
        return unwrapIdentifierUsage(other)
    }

    private fun unwrapIdentifierUsage(expr: NuXmvExpr?): NuXmvIdentifierUsage? = when (expr) {
        is NuXmvIdentifierUsage -> expr
        is NuXmvReferenceBasicExpr -> expr.identifierUsage
        is NuXmvParenthesisBasicExpr -> unwrapIdentifierUsage(expr.expr)
        else -> null
    }

    private fun resolveAtFileScope(file: NuXmvFile): List<PsiElement> {
        // File-level resolution only makes sense for the very first segment, used when the
        // identifier sits outside any module (effectively syntactic noise — kept defensive).
        if (index != 0) return emptyList()
        val part = segments[index].text
        return NuXmvScopes.modulesIn(file)[part]?.let { listOf(it) } ?: emptyList()
    }

    /** A single named segment inside a `NuXmvIdentifierUsage`. */
    data class Segment(val text: String, val offset: Int)

    companion object {
        private const val SELF = "self"
        private const val MAX_CONTEXT_DEPTH = 8

        /** Extracts the identifier / `self` tokens from [usage] in source order. */
        fun segmentsOf(usage: NuXmvIdentifierUsage): List<Segment> {
            val result = ArrayList<Segment>()
            var child: PsiElement? = usage.firstChild
            while (child != null) {
                val type = child.node.elementType
                if (type == NuXmvTypes.IDENTIFIER || type == NuXmvTypes.SELF_KW) {
                    result.add(Segment(child.text, child.startOffsetInParent))
                }
                child = child.nextSibling
            }
            return result
        }

        /** Builds one reference per segment of [usage]. */
        fun referencesFor(usage: NuXmvIdentifierUsage): Array<PsiReference> {
            val segments = segmentsOf(usage)
            if (segments.isEmpty()) return EMPTY_ARRAY
            return Array(segments.size) { i -> NuXmvIdentifierReference(usage, segments, i) }
        }
    }
}
