package dev.mikhailshad.nuxmvplugin.ide.run

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.Computable
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.intellij.ui.*
import com.intellij.ui.components.*
import com.intellij.util.PsiNavigateUtil
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.tree.TreeUtil
import dev.mikhailshad.nuxmvplugin.ide.run.command.*
import dev.mikhailshad.nuxmvplugin.ide.run.configuration.NuXmvRunConfiguration
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvModelProfile
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecEntry
import dev.mikhailshad.nuxmvplugin.language.utils.NuXmvSpecKind
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.tree.DefaultTreeModel

class NuXmvSettingsEditor(private val project: Project) : SettingsEditor<NuXmvRunConfiguration>() {
    private val modelFileChooser = TextFieldWithBrowseButton()
    private val commandLineOptionsField = JBTextField()
    private val runModeComboBox = ComboBox(DefaultComboBoxModel(NuXmvRunMode.entries.toTypedArray()))
    private val domainComboBox = ComboBox<DomainItem>()
    private val interactiveSessionCheckBox = JBCheckBox("Keep nuXmv shell open after the run")
    private val bmcBoundSpinner = JSpinner(SpinnerNumberModel(NuXmvLaunchSettings.DEFAULT_BMC_BOUND, 0, 100_000, 1))
    private val bmcBoundLabel = JBLabel("BMC bound:")
    private val simulationStepsSpinner =
        JSpinner(SpinnerNumberModel(NuXmvLaunchSettings.DEFAULT_SIMULATION_STEPS, 1, 100_000, 1))
    private val randomSimulationCheckBox = JBCheckBox("Pick states randomly")
    private val algorithmLabel = JBLabel()
    private val algorithmComboBox = ComboBox<AlgorithmItem>()
    private val previewArea = JBTextArea().apply {
        isEditable = false
        font = EditorColorsManager.getInstance().globalScheme.getFont(null)
    }
    private val modeCards = JPanel(CardLayout())

    private val treeRoot = CheckedTreeNode(null)
    private val specTree =
        CheckboxTree(SpecTreeRenderer(), treeRoot, CheckboxTreeBase.CheckPolicy(true, true, true, true))

    private var profile: NuXmvModelProfile? = null
    private var selection = NuXmvCheckSelection.ALL
    private var updating = false

    init {
        modelFileChooser.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("smv")
                .withTitle("Select NuXmv Model File")
                .withDescription("Choose the .smv file to run")
        )
        modelFileChooser.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) = reloadProfile()
        })
        runModeComboBox.addActionListener { refresh() }
        domainComboBox.addActionListener { if (!updating) rebuildTree() }
        listOf(bmcBoundSpinner, simulationStepsSpinner).forEach { spinner -> spinner.addChangeListener { refresh() } }
        listOf(
            randomSimulationCheckBox,
            interactiveSessionCheckBox
        ).forEach { box -> box.addActionListener { refresh() } }

        specTree.isRootVisible = false
        specTree.addCheckboxTreeListener(object : CheckboxTreeListener {
            override fun nodeStateChanged(node: CheckedTreeNode) {
                if (!updating) {
                    selection = selectionFromTree()
                    refresh()
                }
            }
        })
        specTree.addTreeSelectionListener { updateAlgorithmChooser() }
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                val element = (selectedNode()?.userObject as? SpecNode)?.spec?.pointer?.element ?: return false
                PsiNavigateUtil.navigate(element)
                return true
            }
        }.installOn(specTree)

        algorithmComboBox.renderer = AlgorithmRenderer()
        algorithmComboBox.addActionListener { if (!updating) applyAlgorithmChoice() }
    }

    override fun resetEditorFrom(configuration: NuXmvRunConfiguration) {
        updating = true
        try {
            modelFileChooser.text = configuration.modelFilePath
            commandLineOptionsField.text = configuration.commandLineOptions
            runModeComboBox.selectedItem = configuration.runMode
            interactiveSessionCheckBox.isSelected = configuration.interactiveSession
            bmcBoundSpinner.value = configuration.bmcBound
            simulationStepsSpinner.value = configuration.simulationSteps
            randomSimulationCheckBox.isSelected = configuration.randomSimulation
            selection = configuration.checkSelection
        } finally {
            updating = false
        }
        reloadProfile(configuration.domainOverride)
    }

    override fun applyEditorTo(configuration: NuXmvRunConfiguration) {
        configuration.modelFilePath = modelFileChooser.text
        configuration.commandLineOptions = commandLineOptionsField.text
        configuration.runMode = runModeComboBox.item ?: NuXmvRunMode.VERIFICATION
        configuration.checkSelection = selection
        configuration.domainOverride = domainComboBox.item?.domain
        configuration.bmcBound = bmcBoundSpinner.value as Int
        configuration.simulationSteps = simulationStepsSpinner.value as Int
        configuration.randomSimulation = randomSimulationCheckBox.isSelected
        configuration.interactiveSession = interactiveSessionCheckBox.isSelected
    }

    override fun createEditor(): JComponent {
        val algorithmRow = JPanel(BorderLayout(JBUI.scale(8), 0)).apply {
            add(algorithmLabel, BorderLayout.WEST)
            add(algorithmComboBox, BorderLayout.CENTER)
        }
        val verificationPanel = JPanel(BorderLayout(0, JBUI.scale(4))).apply {
            add(JBScrollPane(specTree).apply { preferredSize = Dimension(JBUI.scale(480), JBUI.scale(200)) })
            add(algorithmRow, BorderLayout.SOUTH)
        }
        val simulationPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Steps:", simulationStepsSpinner)
            .addComponent(randomSimulationCheckBox)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        modeCards.add(verificationPanel, NuXmvRunMode.VERIFICATION.name)
        modeCards.add(simulationPanel, NuXmvRunMode.SIMULATION.name)

        return FormBuilder.createFormBuilder()
            .addLabeledComponent("Model file:", modelFileChooser)
            .addLabeledComponent("Run mode:", runModeComboBox)
            .addLabeledComponent("Domain:", domainComboBox)
            .addComponent(interactiveSessionCheckBox)
            .addSeparator(3)
            .addComponent(modeCards)
            .addLabeledComponent(bmcBoundLabel, bmcBoundSpinner)
            .addLabeledComponent("Command line options:", commandLineOptionsField)
            .addSeparator(3)
            .addLabeledComponentFillVertically("nuXmv commands:", JBScrollPane(previewArea).apply {
                preferredSize = Dimension(JBUI.scale(480), JBUI.scale(120))
            })
            .panel
    }

    private val domain: NuXmvDomainType
        get() = domainComboBox.item?.domain ?: profile?.domain ?: NuXmvDomainType.FINITE_DOMAIN

    private fun reloadProfile(domainOverride: NuXmvDomainType? = domainComboBox.item?.domain) {
        profile = loadProfile(modelFileChooser.text)
        updating = true
        try {
            val detected =
                profile?.let { "Auto: ${it.domain}" + (it.domainReason?.let { reason -> " ($reason)" } ?: "") }
                    ?: "Auto"
            val items = listOf(DomainItem(null, detected)) +
                    NuXmvDomainType.entries.map { DomainItem(it, it.stringRepresentation) }
            domainComboBox.model = DefaultComboBoxModel(items.toTypedArray())
            domainComboBox.selectedItem = items.first { it.domain == domainOverride }
        } finally {
            updating = false
        }
        rebuildTree()
    }

    private fun loadProfile(path: String): NuXmvModelProfile? {
        if (path.isBlank()) return null
        val file = File(path).takeIf { it.isFile } ?: return null
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file) ?: return null
        return ApplicationManager.getApplication().runReadAction(Computable {
            (PsiManager.getInstance(project).findFile(virtualFile) as? NuXmvFile)?.let { NuXmvModelProfile.of(it) }
        })
    }

    private fun rebuildTree() {
        updating = true
        try {
            treeRoot.removeAllChildren()
            val currentProfile = profile ?: NuXmvModelProfile.EMPTY
            for (kind in NuXmvSpecKind.entries) {
                val specs = currentProfile.specsOf(kind)
                if (specs.isEmpty()) continue
                val kindSelection = selection.kind(kind)
                val unavailable = NuXmvApplicability.kindUnavailableReason(domain, kind)
                val kindNode = CheckedTreeNode(KindNode(kind, specs.size, unavailable))
                kindNode.isEnabled = unavailable == null
                for (spec in specs) {
                    val individualReason = NuXmvApplicability.individualSelectionUnavailableReason(spec)
                    val specNode = CheckedTreeNode(SpecNode(spec, individualReason))
                    specNode.isEnabled = unavailable == null && individualReason == null
                    specNode.isChecked = unavailable == null && when (kindSelection.mode) {
                        NuXmvKindMode.ALL -> true
                        NuXmvKindMode.SOME -> spec.key in selection.specs
                        NuXmvKindMode.NONE -> false
                    }
                    kindNode.add(specNode)
                }
                kindNode.isChecked = kindNode.children().asSequence().all { (it as CheckedTreeNode).isChecked }
                treeRoot.add(kindNode)
            }
            (specTree.model as DefaultTreeModel).reload()
            TreeUtil.expandAll(specTree)
        } finally {
            updating = false
        }
        updateAlgorithmChooser()
        refresh()
    }

    private fun selectionFromTree(): NuXmvCheckSelection {
        val kinds = selection.kinds.toMutableMap()
        val specs = selection.specs.toMutableMap()
        for (kindNode in treeRoot.children().asSequence().map { it as CheckedTreeNode }) {
            if (!kindNode.isEnabled) continue
            val kind = (kindNode.userObject as KindNode).kind
            val specNodes = kindNode.children().asSequence().map { it as CheckedTreeNode }.toList()
            val checked = specNodes.filter { it.isChecked }.map { (it.userObject as SpecNode).spec.key }.toSet()
            val mode = when {
                checked.isEmpty() -> NuXmvKindMode.NONE
                checked.size == specNodes.size -> NuXmvKindMode.ALL
                else -> NuXmvKindMode.SOME
            }
            kinds[kind] = NuXmvKindSelection(mode, selection.kind(kind).algorithm)
            specNodes.map { (it.userObject as SpecNode).spec.key }.forEach { key ->
                if (key in checked) specs.putIfAbsent(key, null) else specs.remove(key)
            }
        }
        return NuXmvCheckSelection(kinds, specs)
    }

    private fun selectedNode(): CheckedTreeNode? = specTree.selectionPath?.lastPathComponent as? CheckedTreeNode

    private fun updateAlgorithmChooser() {
        val node = selectedNode()?.userObject
        val currentProfile = profile ?: NuXmvModelProfile.EMPTY
        updating = true
        try {
            when (node) {
                is KindNode -> {
                    val specs = currentProfile.specsOf(node.kind)
                    val default = NuXmvApplicability.defaultFor(domain, node.kind, specs)
                    val items = listOf(AlgorithmItem(null, "Default (${default?.displayName ?: "none"})", null)) +
                            NuXmvApplicability.algorithmsFor(domain, node.kind, specs).map { it.toItem() }
                    showAlgorithms("${node.kind} algorithm:", items, selection.kind(node.kind).algorithm)
                }

                is SpecNode -> {
                    val inherited = selection.kind(node.spec.kind).algorithm
                        ?: NuXmvApplicability.defaultFor(domain, node.spec.kind, listOf(node.spec))
                    val items = listOf(
                        AlgorithmItem(
                            null,
                            "Same as ${node.spec.kind} (${inherited?.displayName ?: "none"})",
                            null
                        )
                    ) +
                            NuXmvApplicability.algorithmsFor(domain, node.spec.kind, listOf(node.spec))
                                .map { it.toItem() }
                    showAlgorithms("Algorithm:", items, selection.specs[node.spec.key])
                }

                else -> showAlgorithms("Select a specification kind to choose its algorithm", emptyList(), null)
            }
        } finally {
            updating = false
        }
    }

    private fun showAlgorithms(label: String, items: List<AlgorithmItem>, selected: NuXmvCheckAlgorithm?) {
        algorithmLabel.text = label
        algorithmComboBox.model = DefaultComboBoxModel(items.toTypedArray())
        algorithmComboBox.selectedItem = items.firstOrNull { it.algorithm == selected } ?: items.firstOrNull()
        algorithmComboBox.isVisible = items.isNotEmpty()
    }

    private fun applyAlgorithmChoice() {
        val item = algorithmComboBox.item ?: return
        if (item.disabledReason != null) {
            updateAlgorithmChooser()
            return
        }
        selection = when (val node = selectedNode()?.userObject) {
            is KindNode -> selection.withKind(node.kind, selection.kind(node.kind).copy(algorithm = item.algorithm))
            is SpecNode -> if (selectedNode()?.isChecked == true) {
                selection.copy(specs = selection.specs + (node.spec.key to item.algorithm))
            } else {
                selection
            }

            else -> selection
        }
        refresh()
    }

    private fun refresh() {
        if (updating) return
        val mode = runModeComboBox.item ?: NuXmvRunMode.VERIFICATION
        (modeCards.layout as CardLayout).show(modeCards, mode.name)
        randomSimulationCheckBox.isEnabled = NuXmvEngine.of(domain).supportsRandomSimulation

        val currentProfile = profile
        if (currentProfile == null) {
            previewArea.text = "Select a nuXmv model file"
            setBoundVisible(false)
            return
        }
        val plan = NuXmvLaunchPlanner.plan(currentProfile, currentSettings(mode))
        setBoundVisible(mode == NuXmvRunMode.VERIFICATION && plan.checks.any { it.algorithm.usesBound })
        val problems = plan.problems.joinToString("") { "# ${it.message}\n" }
        val command = (listOf("nuXmv", "-int") + plan.cliParams).joinToString(" ")
        previewArea.text = "$problems# $command <model>\n" + plan.preview().joinToString("\n")
    }

    private fun setBoundVisible(visible: Boolean) {
        bmcBoundLabel.isVisible = visible
        bmcBoundSpinner.isVisible = visible
    }

    private fun currentSettings(mode: NuXmvRunMode) = NuXmvLaunchSettings(
        runMode = mode,
        selection = selection,
        domainOverride = domainComboBox.item?.domain,
        bmcBound = bmcBoundSpinner.value as Int,
        simulationSteps = simulationStepsSpinner.value as Int,
        randomSimulation = randomSimulationCheckBox.isSelected,
        interactiveSession = interactiveSessionCheckBox.isSelected
    )

    private data class DomainItem(val domain: NuXmvDomainType?, val label: String) {
        override fun toString(): String = label
    }

    private data class AlgorithmItem(
        val algorithm: NuXmvCheckAlgorithm?,
        val label: String,
        val disabledReason: String?
    ) {
        override fun toString(): String = label
    }

    private fun NuXmvAlgorithmOption.toItem() = AlgorithmItem(algorithm, algorithm.displayName, disabledReason)

    private data class KindNode(val kind: NuXmvSpecKind, val count: Int, val unavailableReason: String?)

    private data class SpecNode(val spec: NuXmvSpecEntry, val unavailableReason: String?)

    private class SpecTreeRenderer : CheckboxTree.CheckboxTreeCellRenderer() {
        override fun customizeRenderer(
            tree: JTree, value: Any, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean
        ) {
            val node = (value as? CheckedTreeNode)?.userObject ?: return
            val reason = when (node) {
                is KindNode -> {
                    textRenderer.append("${node.kind}  ")
                    textRenderer.append("${node.count}", SimpleTextAttributes.GRAYED_ATTRIBUTES)
                    node.unavailableReason
                }

                is SpecNode -> {
                    val spec = node.spec
                    textRenderer.append(spec.label)
                    if (spec.name != null) textRenderer.append("  ${spec.text}", SimpleTextAttributes.GRAYED_ATTRIBUTES)
                    textRenderer.append("  ${spec.module}:${spec.line}", SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES)
                    val instances = spec.instances.filter { it.isNotEmpty() }
                    if (instances.isNotEmpty()) {
                        textRenderer.append(
                            "  IN ${instances.joinToString()}",
                            SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES
                        )
                    }
                    node.unavailableReason
                }

                else -> null
            }
            if (reason != null) {
                textRenderer.append("  — $reason", SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES)
            }
            toolTipText = reason
        }
    }

    private class AlgorithmRenderer : ColoredListCellRenderer<AlgorithmItem>() {
        override fun customizeCellRenderer(
            list: JList<out AlgorithmItem>, value: AlgorithmItem?, index: Int, selected: Boolean, hasFocus: Boolean
        ) {
            value ?: return
            if (value.disabledReason == null) {
                append(value.label)
            } else {
                append(value.label, SimpleTextAttributes.GRAYED_ATTRIBUTES)
                append("  ${value.disabledReason}", SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES)
            }
            toolTipText = value.disabledReason
        }
    }
}
