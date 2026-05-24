package dev.mikhailshad.nuxmvplugin.perf

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import java.util.concurrent.atomic.AtomicInteger

/**
 * Test-only counter for module-scope builds. Production code in `NuXmvScopes` is not aware of
 * this — tests opt in by calling `enable(project)` before exercising the code path, then read
 * the counter to assert that subsequent invocations hit the cache rather than rebuilding.
 *
 * The counter is stored as a per-project `UserData` value; in production deployments the
 * `getUserData` lookup misses and the increment site is a no-op, so there is no measurable
 * overhead outside tests.
 */
object PsiWalkCounter {
    /** Public so production code paths can increment via the same Key without exporting helpers. */
    val KEY: Key<AtomicInteger> = Key.create("nuxmv.test.walkCounter")

    fun enable(project: Project) {
        project.putUserData(KEY, AtomicInteger())
    }

    fun disable(project: Project) {
        project.putUserData(KEY, null)
    }

    fun reset(project: Project) {
        project.getUserData(KEY)?.set(0)
    }

    fun count(project: Project): Int = project.getUserData(KEY)?.get() ?: 0

    fun increment(project: Project) {
        project.getUserData(KEY)?.incrementAndGet()
    }
}
