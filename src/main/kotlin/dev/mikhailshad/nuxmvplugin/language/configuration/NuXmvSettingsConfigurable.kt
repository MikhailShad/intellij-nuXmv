package dev.mikhailshad.nuxmvplugin.language.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class NuXmvSettingsConfigurable : Configurable {
    private var panel: JPanel? = null
    private var nuXmvExecutablePathField: TextFieldWithBrowseButton? = null

    override fun createComponent(): JComponent {
        nuXmvExecutablePathField = TextFieldWithBrowseButton().apply {
            val descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
                .withTitle("Select nuXmv Executable")
                .withDescription("Choose the nuXmv executable file")
                .withShowHiddenFiles(true)

            addBrowseFolderListener(null, descriptor)
        }

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Path to nuXmv executable:"), nuXmvExecutablePathField!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        reset()
        return panel!!
    }

    override fun isModified(): Boolean {
        return nuXmvExecutablePathField?.text != NuXmvSettingsState.getInstance().state.nuXmvExecutablePath
    }

    override fun apply() {
        NuXmvSettingsState.getInstance().state.nuXmvExecutablePath = nuXmvExecutablePathField?.text ?: ""
    }

    override fun reset() {
        nuXmvExecutablePathField?.text = NuXmvSettingsState.getInstance().state.nuXmvExecutablePath
    }

    override fun getDisplayName(): String = "nuXmv"
}
