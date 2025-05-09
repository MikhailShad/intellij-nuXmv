package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class NuXmvSplitConsoleBuilder(
    private val project: Project,
    private val modelFile: VirtualFile?
) : TextConsoleBuilder() {
    private val consoleInstance: NuXmvSplitConsole by lazy {
        NuXmvSplitConsole(modelFile, project)
    }

    override fun getConsole(): ConsoleView {
        return consoleInstance
    }

    override fun addFilter(filter: com.intellij.execution.filters.Filter) {}

    override fun setViewer(isViewer: Boolean) {}
}
