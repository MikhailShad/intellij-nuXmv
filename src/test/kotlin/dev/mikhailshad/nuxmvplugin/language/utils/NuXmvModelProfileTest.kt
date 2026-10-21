package dev.mikhailshad.nuxmvplugin.language.utils

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType

class NuXmvModelProfileTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "run"

    private fun profile(fileName: String): NuXmvModelProfile =
        NuXmvModelProfile.of(myFixture.configureByFile(fileName) as NuXmvFile)

    fun testFiniteModelWithEveryKind() {
        val profile = profile("MixedSpecs.smv")
        assertEquals(NuXmvDomainType.FINITE_DOMAIN, profile.domain)
        assertTrue(profile.hasFairness)
        assertEquals(
            mapOf(
                NuXmvSpecKind.CTL to 1,
                NuXmvSpecKind.COMPUTE to 1,
                NuXmvSpecKind.LTL to 2,
                NuXmvSpecKind.INVAR to 4
            ),
            profile.specs.groupingBy { it.kind }.eachCount()
        )
    }

    fun testSpecificationAttributes() {
        val profile = profile("MixedSpecs.smv")
        val inner = profile.specs.single { it.name == "inner" }
        assertEquals("counter", inner.module)
        assertEquals(listOf("m"), inner.instances)
        assertEquals("c < 4", inner.text)

        val past = profile.specs.single { it.name == "past" }
        assertEquals(setOf(NuXmvSpecFeature.PAST), past.features)
        assertEquals(listOf(""), past.instances)

        val next = profile.specs.single { it.text == "next(s) != s" }
        assertEquals(setOf(NuXmvSpecFeature.NEXT), next.features)
        assertNull(next.name)

        assertEquals("MIN[s = 0, s = 3]", profile.specsOf(NuXmvSpecKind.COMPUTE).single().text)
    }

    fun testKeysAreStableAndDistinct() {
        val profile = profile("MixedSpecs.smv")
        assertEquals(profile.specs.size, profile.specs.map { it.key }.toSet().size)
        assertEquals("INVAR:main:p1", profile.specs.single { it.name == "p1" }.key)
        assertEquals("INVAR:main:#nexts!=s", profile.specs.single { it.text == "next(s) != s" }.key)
    }

    fun testSpecificationOfInstantiatedModuleHasEveryInstance() {
        val profile = profile("ModuleInstances.smv")
        assertEquals(listOf("a", "b"), profile.specs.single { it.name == "live" }.instances)
        assertEquals(
            NuXmvSpecKind.PSL,
            profile.specs.single { it.module == "main" && it.kind == NuXmvSpecKind.PSL }.kind
        )
    }

    fun testIntegerAndRealMakeTheDomainInfinite() {
        val profile = profile("Infinite.smv")
        assertEquals(NuXmvDomainType.INFINITE_DOMAIN, profile.domain)
        assertEquals("x : integer at line 3", profile.domainReason)
    }

    fun testUninterpretedFunctionMakesTheDomainInfinite() {
        assertEquals(NuXmvDomainType.INFINITE_DOMAIN, profile("FunctionDomain.smv").domain)
    }

    fun testTimeDomainAnnotationMakesTheDomainTimed() {
        assertEquals(NuXmvDomainType.TIMED_DOMAIN, profile("Timed.smv").domain)
    }

    fun testBoundedOperatorsAreDetected() {
        val profile = profile("FormulaFeatures.smv")
        assertEquals(setOf(NuXmvSpecFeature.BOUNDED), profile.specs.single { it.name == "bnd" }.features)
    }

    fun testSpecificationsGeneratedByMacroAreMarked() {
        val profile = profile("MacroSpecs.smv")
        val invariants = profile.specsOf(NuXmvSpecKind.INVAR)
        assertEquals(2, invariants.size)
        assertTrue(NuXmvSpecFeature.MACRO in invariants.single { it.name == null }.features)
        assertFalse(NuXmvSpecFeature.MACRO in invariants.single { it.name == "plain" }.features)
    }
}
