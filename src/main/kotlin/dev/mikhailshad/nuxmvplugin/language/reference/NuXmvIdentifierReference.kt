package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.findParentOfType
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope
import dev.mikhailshad.nuxmvplugin.language.psi.scope.submoduleTypeOf
import dev.mikhailshad.nuxmvplugin.language.reference.NuXmvIdentifierReference.Companion.MAX_CONTEXT_DEPTH

/**
 * Resolves a span of segments of a dotted identifier usage such as `submodule.field` or a flat
 * name `con.s` against the cached module scope. A span is the longest run of segments that is
 * declared under one name in [module]; see [referencesFor]. Nothing here walks the PSI subtree.
 */
class NuXmvIdentifierReference(
    element: NuXmvIdentifierUsage,
    private val segments: List<Segment>,
    private val from: Int,
    private val to: Int,
    /** Module whose declarations this span is looked up in; null when the preceding span is unknown. */
    val module: NuXmvModule?
) : NuXmvReferenceBase(
    element,
    TextRange(segments[from].offset, segments[to].offset + segments[to].text.length)
) {

    /** True when this span follows another one, as `field` in `instance.field`. */
    val isQualified: Boolean get() = from > 0

    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val file = element.containingFile as? NuXmvFile ?: return emptyList()
        val currentModule = module ?: return if (from == 0 && element.findParentOfType<NuXmvModule>() == null) {
            resolveAtFileScope(file)
        } else {
            emptyList()
        }

        val name = nameOf(element, segments, from, to)
        if (name == SELF) {
            // `self` resolves to the containing module itself (goto navigates to the module header).
            return listOf(currentModule)
        }
        currentModule.scope().declarations[name]?.let { return listOf(it) }

        // Context-driven fallback: a bare identifier may name something that lives in a sibling
        // operand's owning module (e.g. an enum value of an IVAR whose type is declared inside
        // a submodule, a defined of a referenced submodule, etc.). Only meaningful for a
        // single-segment usage — anything dotted has already been resolved span-by-span.
        if (segments.size == 1) {
            resolveInContextModule(name)?.let { return listOf(it) }
        }
        return emptyList()
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val firstLeaf = findSegmentLeaf(from) ?: return element
        val lastLeaf = findSegmentLeaf(to) ?: return element
        val replacement = NuXmvElementFactory.createVarName(element.project, newElementName)
        element.addRangeBefore(replacement.firstChild, replacement.lastChild, firstLeaf)
        element.deleteChildRange(firstLeaf, lastLeaf)
        return element
    }

    // Override the base equality (which keys on psiElement only) so that ResolveCache and any
    // other consumer can tell two span references on the same identifier usage apart.
    override fun equals(other: Any?): Boolean =
        other is NuXmvIdentifierReference && other.element === element && other.from == from

    override fun hashCode(): Int = element.hashCode() * 31 + from

    private fun findSegmentLeaf(index: Int): PsiElement? {
        var child: PsiElement? = element.firstChild
        var seen = 0
        while (child != null) {
            val type = child.node.elementType
            if (type in SEGMENT_TOKENS) {
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
        if (from != 0) return emptyList()
        val part = segments[from].text
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
                if (type in SEGMENT_TOKENS) {
                    result.add(Segment(child.text, child.startOffsetInParent))
                }
                child = child.nextSibling
            }
            return result
        }

        /**
         * Builds one reference per span of [usage]. Starting in the usage's module, each span is the
         * longest run of segments declared under one name there, so a flat name `con.s` wins over the
         * hierarchical `con` + `s`, as in nuXmv. A span naming a submodule instance switches the module
         * for the next span; after an unknown hop the remaining segments get unresolvable references.
         */
        fun referencesFor(usage: NuXmvIdentifierUsage): Array<PsiReference> {
            val segments = segmentsOf(usage)
            if (segments.isEmpty()) return EMPTY_ARRAY
            val file = usage.containingFile as? NuXmvFile
            var module: NuXmvModule? = contextModuleOf(usage, file) ?: usage.findParentOfType<NuXmvModule>()
            val references = ArrayList<PsiReference>()
            var from = 0
            while (from < segments.size) {
                val to = module?.let { longestDeclaredSpan(it, usage, segments, from) } ?: from
                references.add(NuXmvIdentifierReference(usage, segments, from, to, module))
                module = nextModule(module, file, nameOf(usage, segments, from, to))
                from = to + 1
            }
            return references.toTypedArray()
        }

        /**
         * For a usage inside `SPEC … IN ctx` (but not inside `ctx` itself) returns the module of the
         * instance `ctx`: nuXmv resolves such a specification as if it were written in that instance.
         */
        private fun contextModuleOf(usage: NuXmvIdentifierUsage, file: NuXmvFile?): NuXmvModule? {
            if (file == null) return null
            val specification = PsiTreeUtil.getParentOfType(usage, *CONTEXT_SPECIFICATIONS) ?: return null
            val context = PsiTreeUtil.getChildOfType(specification, NuXmvSpecContext::class.java) ?: return null
            if (PsiTreeUtil.isAncestor(context, usage, false)) return null
            val contextUsage = context.identifierUsage ?: return null
            val segments = segmentsOf(contextUsage)
            var module: NuXmvModule? = specification.findParentOfType<NuXmvModule>()
            var from = 0
            while (from < segments.size && module != null) {
                val to = longestDeclaredSpan(module, contextUsage, segments, from) ?: from
                module = nextModule(module, file, nameOf(contextUsage, segments, from, to))
                from = to + 1
            }
            return module
        }

        private val CONTEXT_SPECIFICATIONS = arrayOf(
            NuXmvCtlSpecification::class.java,
            NuXmvLtlSpecification::class.java,
            NuXmvInvarSpecification::class.java,
            NuXmvComputeSpecification::class.java,
            NuXmvPslSpecification::class.java,
        )

        private fun longestDeclaredSpan(
            module: NuXmvModule,
            usage: PsiElement,
            segments: List<Segment>,
            from: Int
        ): Int? {
            val declarations = module.scope().declarations
            for (to in segments.lastIndex downTo from + 1) {
                if (nameOf(usage, segments, from, to) in declarations) return to
            }
            return null
        }

        private fun nextModule(module: NuXmvModule?, file: NuXmvFile?, name: String): NuXmvModule? {
            if (module == null || file == null) return null
            if (name == SELF) return module
            val moduleType = module.submoduleTypeOf(name) ?: return null
            return NuXmvScopes.modulesIn(file)[moduleType]
        }

        private val SEGMENT_TOKENS = TokenSet.create(
            NuXmvTypes.IDENTIFIER,
            NuXmvTypes.QUOTED_IDENTIFIER,
            NuXmvTypes.POSITIVE_INTEGER_NUMBER,
            NuXmvTypes.SELF_KW
        )

        // The span is named by its source text, so indices between segments (`a[1].b`) are kept.
        private fun nameOf(usage: PsiElement, segments: List<Segment>, from: Int, to: Int): String {
            val start = segments[from].offset
            val end = segments[to].offset + segments[to].text.length
            val name = StringBuilder()
            var child: PsiElement? = usage.firstChild
            while (child != null && child.startOffsetInParent < end) {
                if (child.startOffsetInParent >= start && child !is PsiWhiteSpace && child !is PsiComment) {
                    name.append(child.text)
                }
                child = child.nextSibling
            }
            return name.toString()
        }
    }
}
