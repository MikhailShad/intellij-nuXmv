package dev.mikhailshad.nuxmvplugin.language.reference

import NuXmvCodeInsightFixtureTestCase
import com.intellij.usageView.UsageInfo
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvIdentifierUsage

class NuXmvFindUsagesTest : NuXmvCodeInsightFixtureTestCase() {
    override fun getBasePath(): String = "references"

    /**
     * The `busy` identifier collides as a CONSTANTS entry and an enum value of the `state` IVAR.
     * Find Usages on the CONSTANTS declaration must locate the two use sites (`state = busy`,
     * `state != busy`) even though their references also resolve to the enum value.
     */
    fun testFindUsagesOfConstantSharingNameWithEnumValue() {
        val usages: Collection<UsageInfo> = myFixture.testFindUsages("find_usages_constant.smv")
        val texts = usages.mapNotNull { it.element?.text }
        val busyHits = texts.count { it == "busy" }
        assertTrue(
            "Find Usages should locate at least two `busy` use sites; got texts=$texts",
            busyHits >= 2
        )
    }

    /**
     * Sanity check: an enum value (without any name collision) is still findable through Find
     * Usages — references should resolve to the declaration and produce a non-empty result.
     */
    fun testFindUsagesOfEnumValueWithoutCollision() {
        // Caret will be placed programmatically below since the bundled fib_bench fixture is
        // shared with other tests — we don't want to add a <caret> marker there.
        myFixture.configureByFile("fib_bench_safe_v1.smv")
        val text = myFixture.file.text
        // Place caret at the enum value declaration `not_timed_or_stutter` inside IVAR EVENT in T2.
        val declOffset = text.indexOf("EVENT : {stutter, not_timed_or_stutter}")
        require(declOffset >= 0) { "fib_bench fixture changed; expected snippet not found" }
        val caret = text.indexOf("not_timed_or_stutter", declOffset)
        require(caret >= 0)
        myFixture.editor.caretModel.moveToOffset(caret + 1)
        val usages = myFixture.findUsages(myFixture.elementAtCaret)
        assertFalse(
            "Find Usages of `not_timed_or_stutter` declaration should return at least one usage",
            usages.isEmpty()
        )
    }

    fun testFindUsagesOfFlatVariableIgnoresSiblingNames() {
        myFixture.configureByFile("flat_model.smv")
        val text = myFixture.file.text
        myFixture.editor.caretModel.moveToOffset(text.indexOf("con.s :") + 1)
        val usages = myFixture.findUsages(myFixture.elementAtCaret)
        val texts = usages.mapNotNull { it.element as? NuXmvIdentifierUsage }.map { it.text }
        assertEquals("Expected init/next/case/define uses of con.s, got $texts", 5, texts.size)
        assertTrue(texts.all { it == "con.s" })
    }

    fun testRenameFlatVariable() {
        myFixture.configureByFile("flat_model.smv")
        val text = myFixture.file.text
        myFixture.editor.caretModel.moveToOffset(text.indexOf("con.s :") + 1)
        myFixture.renameElementAtCaret("con.t")
        val renamed = myFixture.file.text
        assertFalse("con.s must be fully renamed:\n$renamed", renamed.contains("con.s"))
        assertEquals(6, Regex("""con\.t\b""").findAll(renamed).count())
        assertTrue(renamed.contains("con.x : real;"))
        assertTrue(renamed.contains("next(con.x) = con.x + con.delta"))
    }

    fun testRenameModuleUpdatesIsa() {
        myFixture.configureByFile("isa.smv")
        myFixture.editor.caretModel.moveToOffset(myFixture.file.text.indexOf("MODULE base") + 8)
        myFixture.renameElementAtCaret("core")
        val renamed = myFixture.file.text
        assertTrue(renamed.contains("MODULE core"))
        assertTrue(renamed.contains("ISA core"))
        assertFalse(renamed.contains("ISA base"))
    }
}
