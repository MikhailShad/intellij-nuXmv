package dev.mikhailshad.nuxmvplugin.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.findParentOfType
import dev.mikhailshad.nuxmvplugin.language.psi.*
import dev.mikhailshad.nuxmvplugin.language.psi.scope.NuXmvScopes
import dev.mikhailshad.nuxmvplugin.language.psi.scope.scope
import dev.mikhailshad.nuxmvplugin.language.psi.scope.submoduleTypeOf

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
        return currentModule.scope().declarations[part]?.let { listOf(it) } ?: emptyList()
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
