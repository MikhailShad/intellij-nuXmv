package dev.mikhailshad.nuxmvplugin.language.reference

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule

class NuXmvIdentifierReferenceTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "references"

    fun testSegmentEventResolvesIntoT2() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t2.EVENT", "EVENT")
        assertEquals("EVENT", target.text)
        assertEquals("T2", target.enclosingModuleName())
    }

    fun testSegmentT2ResolvesToVarInMain() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t2.EVENT", "t2")
        assertEquals("t2", target.text)
        assertEquals("main", target.enclosingModuleName())
    }

    fun testSegmentEventOfT1ResolvesIntoT1() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t1.EVENT", "EVENT")
        assertEquals("EVENT", target.text)
        assertEquals("T1", target.enclosingModuleName())
    }

    fun testSubmoduleArgumentResolvesIntoTargetModule() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t1.i", "i")
        assertEquals("i", target.text)
        assertEquals("T1", target.enclosingModuleName())
    }

    fun testSubmoduleArgumentBaseSegmentResolvesInMain() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t1.i", "t1")
        assertEquals("t1", target.text)
        assertEquals("main", target.enclosingModuleName())
    }

    fun testSimpleNameInsideModuleResolvesLocally() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        // Inside T2: TRANS uses `EVENT != stutter`. Caret on this bare `EVENT`.
        val target = resolveSegment("EVENT != stutter", "EVENT")
        assertEquals("EVENT", target.text)
        assertEquals("T2", target.enclosingModuleName())
    }

    fun testEnumValueResolvesToDeclarationInsideIvar() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("EVENT != stutter", "stutter")
        assertEquals("stutter", target.text)
        assertEquals("T2", target.enclosingModuleName())
    }

    fun testUnknownSubmoduleFieldResolvesToNull() {
        myFixture.configureByFile("unknown_submodule_field.smv")
        val ref = referenceAt("sub.unknown_field", "unknown_field")
        assertNull("Expected no resolution for unknown submodule field", ref.resolve())
    }

    fun testUnknownBaseSegmentResolvesToNull() {
        val source = "MODULE main\nVAR\nx : boolean;\nINVAR (nosuch.field = TRUE)\n"
        myFixture.configureByText(NuXmvFileType, source)
        val ref = referenceAt("nosuch.field", "nosuch")
        assertNull("Expected no resolution for an undeclared base segment", ref.resolve())
    }

    fun testSelfBaseSegmentResolvesToContainingModule() {
        myFixture.configureByFile("self_keyword.smv")
        val target = resolveSegment("self.x", "self")
        assertTrue("self should resolve to the containing module", target is NuXmvModule)
        assertEquals("main", (target as NuXmvModule).moduleDeclaration.moduleName?.name)
    }

    fun testSelfFieldResolvesToLocalVar() {
        myFixture.configureByFile("self_keyword.smv")
        val target = resolveSegment("self.x", "x")
        assertEquals("x", target.text)
        assertEquals("main", target.enclosingModuleName())
    }

    fun testEnumLiteralResolvesIntoSubmoduleAfterT2Event() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t2.EVENT = not_timed_or_stutter", "not_timed_or_stutter")
        assertEquals("not_timed_or_stutter", target.text)
        assertEquals("T2", target.enclosingModuleName())
    }

    fun testEnumLiteralResolvesIntoSubmoduleAfterT1Event() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val target = resolveSegment("t1.EVENT = stutter", "stutter")
        assertEquals("stutter", target.text)
        assertEquals("T1", target.enclosingModuleName())
    }

    fun testEnumLiteralUnknownNameReturnsNull() {
        myFixture.configureByFile("enum_unknown_value.smv")
        val ref = referenceAt("sub.flag = bogus_value", "bogus_value")
        assertNull("Unknown name in submodule type must not resolve", ref.resolve())
    }

    fun testBareIdentifierWithoutContextStaysUnresolved() {
        val source = "MODULE main\nVAR\nx : boolean;\nINVAR x = stranger\n"
        myFixture.configureByText(NuXmvFileType, source)
        val ref = referenceAt("x = stranger", "stranger")
        assertNull("Bare name with no context-providing operand must not resolve", ref.resolve())
    }

    fun testDottedIdentifierProducesOneReferencePerSegment() {
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val text = myFixture.file.text
        val baseOffset = text.indexOf("t2.EVENT")
        require(baseOffset >= 0)
        val leaf = checkNotNull(myFixture.file.findElementAt(baseOffset))
        val usage = checkNotNull(leaf.parentOfType<NuXmvIdentifierUsage>())
        val refs = usage.references
        assertEquals("Expected two segment references for t2.EVENT", 2, refs.size)
        assertEquals("t2", refs[0].resolve()?.text)
        assertEquals("EVENT", refs[1].resolve()?.text)
        assertEquals("main", (refs[0].resolve() as PsiElement).enclosingModuleName())
        assertEquals("T2", (refs[1].resolve() as PsiElement).enclosingModuleName())
    }

    private fun resolveSegment(snippet: String, segment: String): PsiElement {
        val ref = referenceAt(snippet, segment)
        return checkNotNull(ref.resolve()) {
            "Reference for segment '$segment' inside '$snippet' did not resolve"
        }
    }

    private fun referenceAt(snippet: String, segment: String): PsiReference {
        val text = myFixture.file.text
        val snippetOffset = text.indexOf(snippet)
        require(snippetOffset >= 0) { "Snippet '$snippet' not found in fixture" }
        val segmentOffset = snippet.indexOf(segment)
        require(segmentOffset >= 0) { "Segment '$segment' not in snippet '$snippet'" }
        // Aim at the middle of the segment to stay clear of TextRange boundary edge cases.
        val targetOffset = snippetOffset + segmentOffset + segment.length / 2
        return checkNotNull(myFixture.file.findReferenceAt(targetOffset)) {
            "No reference found at offset $targetOffset for segment '$segment' in '$snippet'"
        }
    }

    private fun PsiElement.enclosingModuleName(): String? =
        parentOfType<NuXmvModule>()?.moduleDeclaration?.moduleName?.name
}
