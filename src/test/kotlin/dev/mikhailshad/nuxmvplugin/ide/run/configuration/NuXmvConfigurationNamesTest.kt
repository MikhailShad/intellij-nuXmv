package dev.mikhailshad.nuxmvplugin.ide.run.configuration

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.ide.run.command.*
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind

class NuXmvConfigurationNamesTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "run"

    private fun profile(fileName: String): NuXmvModelProfile =
        NuXmvModelProfile.of(myFixture.configureByFile(fileName) as NuXmvFile)

    private fun describe(profile: NuXmvModelProfile, selection: NuXmvCheckSelection) =
        NuXmvConfigurationNames.describe(profile, NuXmvLaunchSettings(selection = selection))

    fun testNamedSpecificationIsDescribedByKindAndName() {
        val profile = profile("Atm.smv")
        val s2 = profile.specs.single { it.name == "s2" }
        assertEquals("LTL s2", describe(profile, NuXmvCheckSelection.single(s2.kind, s2.key)))
    }

    fun testUnnamedSpecificationIsDescribedByKindAndLine() {
        val profile = profile("Atm.smv")
        val unnamed = profile.specs.single { it.name == null }
        assertEquals("LTL at line 17", describe(profile, NuXmvCheckSelection.single(unnamed.kind, unnamed.key)))
    }

    fun testWholeModelAndWholeKind() {
        val profile = profile("MixedSpecs.smv")
        assertEquals("all specifications", describe(profile, NuXmvCheckSelection.ALL))
        assertEquals(
            "all INVAR",
            describe(profile, NuXmvCheckSelection(mapOf(NuXmvSpecKind.INVAR to NuXmvKindSelection(NuXmvKindMode.ALL))))
        )
        assertEquals(
            "selected LTL, INVAR",
            describe(
                profile,
                NuXmvCheckSelection(
                    mapOf(
                        NuXmvSpecKind.LTL to NuXmvKindSelection(NuXmvKindMode.ALL),
                        NuXmvSpecKind.INVAR to NuXmvKindSelection(NuXmvKindMode.ALL)
                    )
                )
            )
        )
    }

    fun testSimulation() {
        assertEquals(
            "simulation",
            NuXmvConfigurationNames.describe(profile("Atm.smv"), NuXmvLaunchSettings(runMode = NuXmvRunMode.SIMULATION))
        )
    }

    fun testSelectionsWrittenDifferentlyRunTheSameChecks() {
        val profile = profile("Atm.smv")
        val explicit = NuXmvCheckSelection(
            NuXmvSpecKind.entries.associateWith { NuXmvKindSelection(if (it == NuXmvSpecKind.LTL) NuXmvKindMode.ALL else NuXmvKindMode.NONE) },
            profile.specs.associate { it.key to null }
        )
        assertTrue(
            NuXmvConfigurationNames.sameChecks(
                profile,
                NuXmvLaunchSettings(selection = NuXmvCheckSelection.ALL),
                NuXmvLaunchSettings(selection = explicit)
            )
        )
        val s2 = profile.specs.single { it.name == "s2" }
        assertFalse(
            NuXmvConfigurationNames.sameChecks(
                profile,
                NuXmvLaunchSettings(selection = NuXmvCheckSelection.ALL),
                NuXmvLaunchSettings(selection = NuXmvCheckSelection.single(s2.kind, s2.key))
            )
        )
        assertFalse(
            NuXmvConfigurationNames.sameChecks(
                profile,
                NuXmvLaunchSettings(),
                NuXmvLaunchSettings(runMode = NuXmvRunMode.SIMULATION)
            )
        )
    }
}
