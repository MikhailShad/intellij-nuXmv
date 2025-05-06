package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class NuXmvModelGraphToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val modelGraphPanel = NuXmvModelGraphPanel(project)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(modelGraphPanel, "Model Graph", false)
        toolWindow.contentManager.addContent(content)
    }
}
