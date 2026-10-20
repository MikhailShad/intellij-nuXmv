package dev.mikhailshad.nuxmvplugin.language.reference

import NuXmvCodeInsightFixtureTestCase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvDefineName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModule
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvModuleName
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvVarName

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

    fun testDefineInWordWidthResolves() {
        val source = "MODULE main\nVAR\nw : unsigned word[N];\nr : 0..N;\nDEFINE\nN := 4;\n"
        myFixture.configureByText(NuXmvFileType, source)
        assertEquals("N", resolveSegment("word[N]", "N").text)
        assertEquals("N", resolveSegment("0..N", "N").text)
    }

    fun testTypeOfArgumentResolvesToVariable() {
        val source = "MODULE main\nVAR\na : array integer of integer;\nINIT a = CONSTARRAY(typeof(a), 0);\n"
        myFixture.configureByText(NuXmvFileType, source)
        val target = resolveSegment("typeof(a)", "a")
        assertEquals("a", target.text)
    }

    fun testFlatVariableResolvesAsOneReference() {
        myFixture.configureByFile("flat_model.smv")
        val usage = usageAt("next(con.s)", "con.s")
        assertEquals(1, usage.references.size)
        val target = checkNotNull(usage.references[0].resolve())
        assertInstanceOf(target, NuXmvVarName::class.java)
        assertEquals("con.s", (target as NuXmvVarName).name)
    }

    fun testFlatDefineResolves() {
        myFixture.configureByFile("flat_model.smv")
        val target = resolveSegment("INVARSPEC con.TARGET", "TARGET")
        assertInstanceOf(target, NuXmvDefineName::class.java)
        assertEquals("con.TARGET", (target as NuXmvDefineName).name)
    }

    fun testFlatNameDeclaredBesideInstanceWins() {
        myFixture.configureByFile("flat_model.smv")
        val flat = resolveSegment("| c.y", "y")
        assertEquals("c.y", (flat as NuXmvVarName).name)
        assertEquals("main", flat.enclosingModuleName())

        val hierarchical = resolveSegment("| c.x", "x")
        assertEquals("x", (hierarchical as NuXmvVarName).name)
        assertEquals("sub", hierarchical.enclosingModuleName())
    }

    fun testFlatNameInsideSubmoduleResolves() {
        myFixture.configureByFile("flat_model.smv")
        val target = resolveSegment("inst.a.b", "b")
        assertEquals("a.b", (target as NuXmvVarName).name)
        assertEquals("m", target.enclosingModuleName())
        assertEquals("inst", resolveSegment("inst.a.b", "inst").text)
    }

    fun testPrefixOfFlatNameAloneDoesNotResolve() {
        val source = "MODULE main\nVAR\na.b : boolean;\nINVARSPEC a | TRUE;\n"
        myFixture.configureByText(NuXmvFileType, source)
        assertNull(referenceAt("a | TRUE", "a").resolve())
    }

    fun testQuotedNameResolvesOnlyQuoted() {
        val source = "MODULE main\nVAR\n\"amount\" : boolean;\nINVARSPEC \"amount\" | amount;\n"
        myFixture.configureByText(NuXmvFileType, source)
        val quoted = resolveSegment("INVARSPEC \"amount\"", "\"amount\"")
        assertEquals("\"amount\"", (quoted as NuXmvVarName).name)
        assertNull(referenceAt("| amount", "amount").resolve())
    }

    fun testQuotedNameWithSpecialCharactersResolves() {
        val source = "MODULE main\nVAR\n\"x y:z\" : boolean;\nINVARSPEC ! \"x y:z\";\n"
        myFixture.configureByText(NuXmvFileType, source)
        assertEquals("\"x y:z\"", (resolveSegment("! \"x y:z\"", "x y:z") as NuXmvVarName).name)
    }

    fun testNumericSegmentNameResolves() {
        val source = "MODULE main\nVAR\nx.1 : boolean;\nx.1.y : boolean;\nINVARSPEC x.1 | x.1.y;\n"
        myFixture.configureByText(NuXmvFileType, source)
        assertEquals("x.1", (resolveSegment("INVARSPEC x.1", "1") as NuXmvVarName).name)
        assertEquals("x.1.y", (resolveSegment("| x.1.y", "y") as NuXmvVarName).name)
    }

    fun testIndexBeforeDotNameResolves() {
        val source = "MODULE main\nVAR\na[1].b : boolean;\nINVARSPEC a[1].b;\n"
        myFixture.configureByText(NuXmvFileType, source)
        val usage = usageAt("INVARSPEC a[1].b", "a[1].b")
        assertEquals("a[1].b", (checkNotNull(usage.reference?.resolve()) as NuXmvVarName).name)
    }

    fun testSpecificationInContextResolvesInsideInstance() {
        myFixture.configureByFile("spec_context.smv")
        val target = resolveSegment("INVARSPEC t <= 3 IN m", "t")
        assertEquals("sub", target.enclosingModuleName())
        val instance = resolveSegment("IN m;", "m")
        assertEquals("m", instance.text)
        assertEquals("main", instance.enclosingModuleName())
    }

    fun testSpecificationInNestedContextResolves() {
        myFixture.configureByFile("spec_context.smv")
        assertEquals("sub", resolveSegment("LTLSPEC G (f) IN n.s", "f").enclosingModuleName())
        assertEquals("inner", resolveSegment("IN n.s;", "s").enclosingModuleName())
    }

    fun testSelfInContextSpecificationIsTheContextModule() {
        myFixture.configureByFile("spec_context.smv")
        val target = resolveSegment("CTLSPEC AG self.f IN m", "self")
        assertEquals("sub", (target as NuXmvModule).moduleDeclaration.moduleName?.name)
    }

    fun testOuterForLoopVariableResolvesInsideNestedLoop() {
        val source = "MODULE main\nVAR x : array 1..5 of boolean;\n#FOR i IN 1..2\n  #FOR j IN 1..2\n" +
                "    ASSIGN x[i] := j mod 2 = 0;\n  #END\n#END\n"
        myFixture.configureByText(NuXmvFileType, source)
        assertEquals("i", resolveSegment("x[i]", "i").text)
        assertEquals("j", resolveSegment("j mod", "j").text)
    }

    fun testIsaReferencesModule() {
        myFixture.configureByFile("isa.smv")
        val target = resolveSegment("ISA base", "base")
        assertInstanceOf(target, NuXmvModuleName::class.java)
        assertEquals("base", (target as NuXmvModuleName).name)
    }

    fun testIsaIncludesDeclarationsTransitively() {
        myFixture.configureByFile("isa.smv")
        assertEquals("base", resolveSegment("w | z | dz", "z").enclosingModuleName())
        assertEquals("base", resolveSegment("| dz | y", "dz").enclosingModuleName())
        assertEquals("middle", resolveSegment("| dz | y", "y").enclosingModuleName())
        assertEquals("base", resolveSegment("d.z | d.dz", "z").enclosingModuleName())
    }

    private fun usageAt(snippet: String, segment: String): NuXmvIdentifierUsage {
        val text = myFixture.file.text
        val offset = text.indexOf(snippet) + snippet.indexOf(segment)
        val leaf = checkNotNull(myFixture.file.findElementAt(offset))
        return checkNotNull(leaf.parentOfType<NuXmvIdentifierUsage>())
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
