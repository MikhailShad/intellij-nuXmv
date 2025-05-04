package dev.mikhailshad.nuxmvplugin.ide.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvUtils
import java.io.File
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class NuXmvSettingsEditor(private val project: Project) : SettingsEditor<NuXmvRunConfiguration>() {
    private lateinit var panel: JPanel
    private val modelFileChooser = TextFieldWithBrowseButton()
    private val commandLineOptionsField = JBTextField()
    private val checkCtlSpecificationsCheckBox = JBCheckBox("Check CTL specifications")
    private val checkLtlSpecificationsCheckBox = JBCheckBox("Check LTL specifications")
    private val checkInvarSpecificationsCheckBox = JBCheckBox("Check INVAR specifications")
    private val domainTypeComboBox = ComboBox(
        DefaultComboBoxModel(NuXmvDomainType.entries.toTypedArray())
    )

    init {
        modelFileChooser.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("smv")
                .withTitle("Select NuXmv Model File")
                .withDescription("Choose the .smv file to run")
        )

        modelFileChooser.textField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updateCheckboxes()
            override fun removeUpdate(e: DocumentEvent) = updateCheckboxes()
            override fun changedUpdate(e: DocumentEvent) = updateCheckboxes()
        })
    }

    private fun updateCheckboxes() {
        val filePath = modelFileChooser.text
        if (filePath.isBlank()) {
            setCheckBoxesEnabled()
            return
        }

        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            setCheckBoxesEnabled()
            return
        }

        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)
        if (virtualFile == null) {
            setCheckBoxesEnabled()
            return
        }

        val modelSpecifications = NuXmvUtils.analyzeModelSpecifications(project, virtualFile)
        setCheckBoxesEnabled(modelSpecifications)

        domainTypeComboBox.selectedItem = modelSpecifications.domainType
    }

    private fun setCheckBoxesEnabled(modelSpecifications: NuXmvUtils.ModelSpecifications? = null) {
        if (modelSpecifications == null) {
            setCheckBoxValue(checkCtlSpecificationsCheckBox, false)
            setCheckBoxValue(checkLtlSpecificationsCheckBox, false)
            setCheckBoxValue(checkInvarSpecificationsCheckBox, false)
        } else {
            setCheckBoxValue(checkCtlSpecificationsCheckBox, modelSpecifications.hasCtlSpecs)
            setCheckBoxValue(checkLtlSpecificationsCheckBox, modelSpecifications.hasLtlSpecs)
            setCheckBoxValue(checkInvarSpecificationsCheckBox, modelSpecifications.hasInvarSpecs)
        }
    }

    private fun setCheckBoxValue(checkBox: JBCheckBox, isEnabled: Boolean, isSelected: Boolean = isEnabled) {
        checkBox.isEnabled = isEnabled
        checkBox.isSelected = isSelected
    }

    override fun resetEditorFrom(configuration: NuXmvRunConfiguration) {
        modelFileChooser.text = configuration.modelFilePath
        commandLineOptionsField.text = configuration.commandLineOptions
        checkCtlSpecificationsCheckBox.isSelected = configuration.checkCtlSpecifications
        checkLtlSpecificationsCheckBox.isSelected = configuration.checkLtlSpecifications
        checkInvarSpecificationsCheckBox.isSelected = configuration.checkInvarSpecifications
        domainTypeComboBox.selectedItem = configuration.domainType
        updateCheckboxes()
    }

    override fun applyEditorTo(configuration: NuXmvRunConfiguration) {
        configuration.modelFilePath = modelFileChooser.text
        configuration.commandLineOptions = commandLineOptionsField.text
        configuration.checkCtlSpecifications = checkCtlSpecificationsCheckBox.isSelected
        configuration.checkLtlSpecifications = checkLtlSpecificationsCheckBox.isSelected
        configuration.checkInvarSpecifications = checkInvarSpecificationsCheckBox.isSelected
        configuration.domainType = domainTypeComboBox.selectedItem as NuXmvDomainType
    }

    override fun createEditor(): JComponent {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Model file:"), modelFileChooser)
            .addLabeledComponent(JBLabel("Domain type:"), domainTypeComboBox)
            .addLabeledComponent(JBLabel("Command Line options:"), commandLineOptionsField)
            .addSeparator(3)
            .addComponent(checkCtlSpecificationsCheckBox)
            .addComponent(checkLtlSpecificationsCheckBox)
            .addComponent(checkInvarSpecificationsCheckBox)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        updateCheckboxes()
        return panel
    }
}
