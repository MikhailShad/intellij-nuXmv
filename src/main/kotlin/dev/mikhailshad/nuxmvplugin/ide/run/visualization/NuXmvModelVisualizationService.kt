package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.Topic

@Service(Service.Level.PROJECT)
class NuXmvModelVisualizationService(private val project: Project) {
    interface ModelVisualizationListener {
        fun onModelFileUpdated(file: VirtualFile)
    }

    companion object {
        val VISUALIZATION_TOPIC = Topic.create("NuXmv Model Visualization", ModelVisualizationListener::class.java)

        fun getInstance(project: Project): NuXmvModelVisualizationService = project.service()
    }

    fun visualizeModel(file: VirtualFile) {
        project.messageBus.syncPublisher(VISUALIZATION_TOPIC).onModelFileUpdated(file)
    }
}
