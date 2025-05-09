package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.HyperlinkInfo
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBSplitter
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.NuXmvModelVisualizer
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingUtilities

class NuXmvSplitConsole(
    val modelFile: VirtualFile?,
    project: Project
) : ConsoleView, Disposable {
    val textConsole: ConsoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
    val visualizer = NuXmvModelVisualizer(project)
    private val mainPanel = JPanel(BorderLayout())
    private val splitter = JBSplitter(false, 0.4f, 0.3f, 0.7f).apply {
        firstComponent = textConsole.component
        secondComponent = visualizer.getComponent()
        dividerWidth = 3
        isShowDividerControls = true
        isOpaque = true
        setHonorComponentsMinimumSize(true)
        setDragging(true)
    }

    init {
        mainPanel.add(splitter, BorderLayout.CENTER)
        mainPanel.minimumSize = java.awt.Dimension(200, 200)
        mainPanel.preferredSize = java.awt.Dimension(800, 600)

        SwingUtilities.invokeLater {
            modelFile?.let {
                visualizer.visualizeModelFile(it)
            }
        }
    }

    fun visualizeModel(file: VirtualFile) {
        visualizer.visualizeModelFile(file)
    }

    fun visualizeModuleWithTraces(file: VirtualFile, traces: List<CounterexampleTrace>) {
        visualizer.visualizeCounterexampleTraces(traces)
    }

    override fun print(text: String, contentType: ConsoleViewContentType) {
        textConsole.print(text, contentType)
    }

    override fun clear() {
        textConsole.clear()
    }

    override fun scrollTo(offset: Int) {
        textConsole.scrollTo(offset)
    }

    override fun attachToProcess(processHandler: ProcessHandler) {
        textConsole.attachToProcess(processHandler)
    }

    override fun setOutputPaused(value: Boolean) {
        textConsole.isOutputPaused = value
    }

    override fun isOutputPaused(): Boolean {
        return textConsole.isOutputPaused
    }

    override fun getComponent(): JComponent {
        return mainPanel
    }

    override fun getPreferredFocusableComponent(): JComponent {
        return textConsole.preferredFocusableComponent
    }

    override fun dispose() {
        visualizer.dispose()
        textConsole.dispose()
    }

    override fun hasDeferredOutput(): Boolean {
        return textConsole.hasDeferredOutput()
    }

    override fun performWhenNoDeferredOutput(runnable: Runnable) {
        textConsole.performWhenNoDeferredOutput(runnable)
    }

    override fun setHelpId(helpId: String) {
        textConsole.setHelpId(helpId)
    }

    override fun addMessageFilter(filter: Filter) {
        textConsole.addMessageFilter(filter)
    }

    override fun printHyperlink(hyperlinkText: String, info: HyperlinkInfo?) {
        textConsole.printHyperlink(hyperlinkText, info)
    }

    override fun getContentSize(): Int {
        return textConsole.contentSize
    }

    override fun canPause(): Boolean {
        return textConsole.canPause()
    }

    override fun createConsoleActions(): Array<AnAction> {
        return textConsole.createConsoleActions()
    }

    override fun allowHeavyFilters() {
        textConsole.allowHeavyFilters()
    }
}
