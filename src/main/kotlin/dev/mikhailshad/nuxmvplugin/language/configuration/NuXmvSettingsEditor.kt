package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class NuXmvSettingsEditor(private val project: Project) : SettingsEditor<NuXmvRunConfiguration>() {
    private lateinit var panel: JPanel
    private val modelFileChooser = TextFieldWithBrowseButton()
    private val commandLineOptionsField = JBTextField()

    init {
        modelFileChooser.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("smv")
                .withTitle("Select NuXmv Model File")
                .withDescription("Choose the .smv file to run")
        )
    }

    override fun resetEditorFrom(configuration: NuXmvRunConfiguration) {
        modelFileChooser.text = configuration.modelFilePath
        commandLineOptionsField.text = configuration.commandLineOptions
    }

    override fun applyEditorTo(configuration: NuXmvRunConfiguration) {
        configuration.modelFilePath = modelFileChooser.text
        configuration.commandLineOptions = commandLineOptionsField.text
    }

    override fun createEditor(): JComponent {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Model file:"), modelFileChooser)
            .addLabeledComponent(JBLabel("Options:"), commandLineOptionsField)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        return panel
    }
}
