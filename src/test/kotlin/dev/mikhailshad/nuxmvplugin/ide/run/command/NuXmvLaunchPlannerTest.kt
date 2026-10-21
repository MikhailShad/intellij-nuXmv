package dev.mikhailshad.nuxmvplugin.ide.run.command

import NuXmvCodeInsightFixtureTestCase
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind
import java.io.File

class NuXmvLaunchPlannerTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "run"

    private fun profile(fileName: String): NuXmvModelProfile =
        NuXmvModelProfile.of(myFixture.configureByFile(fileName) as NuXmvFile)

    private fun output(fileName: String): String = File("$testDataPath/output/$fileName").readText()

    private fun commands(plan: NuXmvLaunchPlan, propertyList: String = ""): List<String> {
        val sequence = NuXmvCommandSequence(plan)
        val sent = mutableListOf<String>()
        var last: String? = null
        while (true) {
            val next =
                sequence.next(last, if (last == NuXmvCommandSequence.SHOW_PROPERTY) propertyList else "") ?: break
            sent += next
            last = next
        }
        return sent
    }

    fun testWholeModelOnFiniteDomainUsesBdd() {
        val plan = NuXmvLaunchPlanner.plan(profile("MixedSpecs.smv"), NuXmvLaunchSettings())
        assertEquals(
            listOf("go", "check_ctlspec", "check_compute", "check_ltlspec", "check_invar", "show_traces -a -v", "quit"),
            commands(plan)
        )
        assertEmpty(plan.cliParams)
        assertEmpty(plan.problems)
    }

    fun testFiniteSatAlgorithmsNeedBooleanModel() {
        val selection = NuXmvCheckSelection(
            mapOf(NuXmvSpecKind.INVAR to NuXmvKindSelection(NuXmvKindMode.ALL, NuXmvCheckAlgorithm.INVAR_K_INDUCTION))
        )
        val plan =
            NuXmvLaunchPlanner.plan(profile("MixedSpecs.smv"), NuXmvLaunchSettings(selection = selection, bmcBound = 7))
        assertEquals(
            listOf("go", "go_bmc", "check_invar_bmc -a een-sorensson -k 7", "show_traces -a -v", "quit"),
            commands(plan)
        )
    }

    fun testInfiniteDomainSkipsBddOnlyKindsAndAvoidsBmcForPastOperators() {
        val plan = NuXmvLaunchPlanner.plan(
            profile("MixedSpecs.smv"),
            NuXmvLaunchSettings(domainOverride = NuXmvDomainType.INFINITE_DOMAIN)
        )
        assertEquals(listOf("go_msat"), plan.setup)
        assertEquals(
            listOf(NuXmvCheckAlgorithm.MSAT_LTL_IC3, NuXmvCheckAlgorithm.MSAT_INVAR_BMC),
            plan.checks.map { it.algorithm }
        )
        assertTrue(plan.problems.any { it.message.startsWith("CTL is not supported") })
        assertTrue(plan.problems.any { it.message.startsWith("COMPUTE is not supported") })
    }

    fun testInfiniteModelKeepsFormerCommands() {
        val plan = NuXmvLaunchPlanner.plan(profile("Infinite.smv"), NuXmvLaunchSettings())
        assertEquals(
            listOf("go_msat", "msat_check_ltlspec_bmc -k 10", "msat_check_invar_bmc", "show_traces -a -v", "quit"),
            commands(plan)
        )
    }

    fun testTimedModelRunsWithTimeFlag() {
        val plan = NuXmvLaunchPlanner.plan(profile("Timed.smv"), NuXmvLaunchSettings())
        assertEquals(listOf("-time"), plan.cliParams)
        assertEquals(
            listOf("go_time", "timed_check_ltlspec -b -k 10", "timed_check_invar", "show_traces -a -v", "quit"),
            commands(plan)
        )
    }

    fun testInteractiveSessionDoesNotQuit() {
        val plan = NuXmvLaunchPlanner.plan(profile("Infinite.smv"), NuXmvLaunchSettings(interactiveSession = true))
        assertEquals("show_traces -a -v", commands(plan).last())
    }

    fun testSimulationEngineFollowsTheDomain() {
        val simulation =
            NuXmvLaunchSettings(runMode = NuXmvRunMode.SIMULATION, simulationSteps = 5, randomSimulation = true)
        assertEquals(
            listOf("go", "pick_state -r", "simulate -r -k 5", "show_traces -a -v", "quit"),
            commands(NuXmvLaunchPlanner.plan(profile("MixedSpecs.smv"), simulation))
        )
        assertEquals(
            listOf("go_msat", "msat_pick_state", "msat_simulate -k 5", "show_traces -a -v", "quit"),
            commands(NuXmvLaunchPlanner.plan(profile("Infinite.smv"), simulation))
        )
        assertEquals(
            listOf("go_time", "timed_pick_state", "timed_simulate -k 5", "show_traces -a -v", "quit"),
            commands(NuXmvLaunchPlanner.plan(profile("Timed.smv"), simulation))
        )
    }

    fun testNamedSpecificationIsSelectedByQualifiedName() {
        val profile = profile("MixedSpecs.smv")
        val inner = profile.specs.single { it.name == "inner" }
        val plan = NuXmvLaunchPlanner.plan(
            profile,
            NuXmvLaunchSettings(selection = NuXmvCheckSelection.single(inner.kind, inner.key))
        )
        assertFalse(plan.needsPropertyList)
        assertEquals(listOf("go", "check_invar -P m.inner", "show_traces -a -v", "quit"), commands(plan))
    }

    fun testSelectingEverySpecificationOfKindChecksTheKindAtOnce() {
        val profile = profile("ModuleInstances.smv")
        val live = profile.specs.single { it.name == "live" }
        val plan = NuXmvLaunchPlanner.plan(
            profile,
            NuXmvLaunchSettings(selection = NuXmvCheckSelection.single(live.kind, live.key))
        )
        assertEquals(listOf("go", "check_ltlspec", "show_traces -a -v", "quit"), commands(plan))
    }

    fun testUnnamedSpecificationIsSelectedByIndexFromPropertyList() {
        val profile = profile("ModuleInstances.smv")
        val invariant = profile.specs.single { it.module == "counter" && it.kind == NuXmvSpecKind.INVAR }
        val plan = NuXmvLaunchPlanner.plan(
            profile,
            NuXmvLaunchSettings(selection = NuXmvCheckSelection.single(invariant.kind, invariant.key))
        )
        assertTrue(plan.needsPropertyList)
        assertEquals(
            listOf("go", "show_property", "check_invar -n 3", "check_invar -n 4", "show_traces -a -v", "quit"),
            commands(plan, output("show_property_instances.txt"))
        )
    }

    fun testUnnamedSpecificationsOfMainAreMatchedAcrossReformatting() {
        val profile = profile("MixedSpecs.smv")
        val keys =
            profile.specs.filter { it.name == null && it.kind != NuXmvSpecKind.COMPUTE && it.kind != NuXmvSpecKind.CTL }
        val selection = NuXmvCheckSelection(
            mapOf(
                NuXmvSpecKind.LTL to NuXmvKindSelection(NuXmvKindMode.SOME),
                NuXmvSpecKind.INVAR to NuXmvKindSelection(NuXmvKindMode.SOME)
            ),
            keys.associate { it.key to null }
        )
        val plan = NuXmvLaunchPlanner.plan(profile, NuXmvLaunchSettings(selection = selection))
        assertEquals(
            listOf(
                "go",
                "show_property",
                "check_ltlspec -n 2",
                "check_invar -n 6",
                "check_invar -n 7",
                "show_traces -a -v",
                "quit"
            ),
            commands(plan, output("show_property_mixed.txt"))
        )
    }

    fun testOverrideForOneSpecificationSplitsTheKind() {
        val profile = profile("MixedSpecs.smv")
        val p1 = profile.specs.single { it.name == "p1" }
        val selection = NuXmvCheckSelection(
            mapOf(NuXmvSpecKind.INVAR to NuXmvKindSelection(NuXmvKindMode.ALL)),
            mapOf(p1.key to NuXmvCheckAlgorithm.INVAR_IC3)
        )
        val plan = NuXmvLaunchPlanner.plan(profile, NuXmvLaunchSettings(selection = selection))
        assertEquals(listOf("go", "go_bmc"), plan.setup)
        assertEquals(4, plan.checks.size)
        assertTrue(plan.checks.all { it is NuXmvPlannedCheck.Single })
        assertEquals(
            NuXmvCheckAlgorithm.INVAR_IC3,
            plan.checks.single { (it as NuXmvPlannedCheck.Single).spec == p1 }.algorithm
        )
    }

    fun testRemovedSpecificationIsReported() {
        val selection = NuXmvCheckSelection.single(NuXmvSpecKind.INVAR, "INVAR:main:gone")
        val plan = NuXmvLaunchPlanner.plan(profile("MixedSpecs.smv"), NuXmvLaunchSettings(selection = selection))
        assertTrue(plan.problems.any { it.message.contains("no longer in the model") })
        assertTrue(plan.problems.any { it.isError })
    }

    fun testMacroSpecificationCannotBeSelectedAlone() {
        val profile = profile("MacroSpecs.smv")
        val generated = profile.specs.single { it.name == null }
        val plan = NuXmvLaunchPlanner.plan(
            profile,
            NuXmvLaunchSettings(selection = NuXmvCheckSelection.single(generated.kind, generated.key))
        )
        assertEmpty(plan.checks)
        assertTrue(plan.problems.any { it.message.contains("#FOR macro") })
    }

    fun testAlgorithmOptionsExplainRejection() {
        val profile = profile("MixedSpecs.smv")
        val options = NuXmvApplicability.algorithmsFor(
            NuXmvDomainType.INFINITE_DOMAIN,
            NuXmvSpecKind.LTL,
            profile.specsOf(NuXmvSpecKind.LTL)
        )
        assertEquals(listOf(false, true), options.map { it.isAvailable })
        assertEquals(
            NuXmvCheckAlgorithm.MSAT_LTL_IC3, NuXmvApplicability.defaultFor(
                NuXmvDomainType.INFINITE_DOMAIN, NuXmvSpecKind.LTL, profile.specsOf(NuXmvSpecKind.LTL)
            )
        )
    }

    fun testSelectionSurvivesSerialization() {
        val selection = NuXmvCheckSelection(
            mapOf(NuXmvSpecKind.LTL to NuXmvKindSelection(NuXmvKindMode.SOME, NuXmvCheckAlgorithm.LTL_BMC)),
            mapOf("LTL:main:#Gs<3" to null, "INVAR:main:p1" to NuXmvCheckAlgorithm.INVAR_IC3)
        )
        assertEquals(selection, NuXmvCheckSelection.deserialize(selection.serialize()))
        assertNull(NuXmvCheckSelection.deserialize("not json"))
    }
}
