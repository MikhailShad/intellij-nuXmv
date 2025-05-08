package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.messages.MessageBusConnection
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout

/**
 * Panel that displays the model visualization and manages file selection/updates.
 */
class NuXmvModelGraphPanel(private val project: Project) : JBPanel<NuXmvModelGraphPanel>(BorderLayout()),
    Disposable, NuXmvModelVisualizationService.ModelVisualizationListener {
    private val logger = Logger.getInstance(NuXmvModelGraphPanel::class.java)
    private val visualizer = NuXmvModelVisualizer(project)
    private var currentFile: VirtualFile? = null
    private val connection: MessageBusConnection = project.messageBus.connect(this)
    private val fileLabel = JBLabel("No model file selected")

    init {
        Disposer.register(this, visualizer)

        // Add status bar
        val statusPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            add(fileLabel, BorderLayout.CENTER)
        }

        // Add components to main panel
        add(visualizer.getComponent(), BorderLayout.CENTER)
        add(statusPanel, BorderLayout.SOUTH)

        // Subscribe to model visualization events
        connection.subscribe(
            NuXmvModelVisualizationService.VISUALIZATION_TOPIC,
            this
        )

        // Listen for file changes
        connection.subscribe(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object : FileEditorManagerListener {
                override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
                    checkAndUpdateFile(file)
                }

                override fun selectionChanged(event: FileEditorManagerEvent) {
                    val file = event.newFile
                    checkAndUpdateFile(file)
                }

                override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
                    if (file == currentFile) {
                        // If current visualization file is closed, try to show next available nuXmv file
                        val openFiles = source.openFiles
                        val nextNuXmvFile = openFiles.firstOrNull {
                            it.extension?.lowercase() in SUPPORTED_EXTENSIONS
                        }

                        if (nextNuXmvFile != null) {
                            checkAndUpdateFile(nextNuXmvFile)
                        } else {
                            // No nuXmv files open, show empty visualization
                            currentFile = null
                            fileLabel.text = "No model file selected"
                            visualizer.clearVisualization()
                        }
                    }
                }
            }
        )

        // Check if there's an already open nuXmv file when tool window is first shown
        ApplicationManager.getApplication().invokeLater {
            val openFiles = FileEditorManager.getInstance(project).openFiles
            val nuXmvFile = openFiles.firstOrNull {
                it.extension?.lowercase() in SUPPORTED_EXTENSIONS
            }

            if (nuXmvFile != null) {
                checkAndUpdateFile(nuXmvFile)
            }
        }
    }

    private fun checkAndUpdateFile(file: VirtualFile?) {
        if (file != null && (file.extension?.lowercase() in SUPPORTED_EXTENSIONS)) {
            updateFile(file)
        }
    }

    override fun onModelFileUpdated(file: VirtualFile) {
        updateFile(file)
    }

    private fun updateFile(file: VirtualFile) {
        logger.info("Updating visualization for file: ${file.path}")
        currentFile = file
        fileLabel.text = "Model: ${file.name}"
        visualizer.visualizeModelFile(file)
    }

    override fun dispose() {
        // Resources will be disposed through registration
    }

    companion object {
        private val SUPPORTED_EXTENSIONS = setOf("smv", "nuxmv", "xmv")
    }
}
