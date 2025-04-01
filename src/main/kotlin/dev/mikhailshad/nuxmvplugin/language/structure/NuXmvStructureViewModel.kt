package dev.mikhailshad.nuxmvplugin.language.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvFile
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvNuXmvModule

class NuXmvStructureViewModel(psiFile: NuXmvFile, editor: Editor?) :
    StructureViewModelBase(psiFile, editor, NuXmvStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        return element.value is NuXmvFile || element.value is NuXmvNuXmvModule
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return element.value !is NuXmvFile && element.value !is NuXmvNuXmvModule
    }

    init {
        withSuitableClasses(
            NuXmvFile::class.java,
            NuXmvNuXmvModule::class.java
        )
    }
}
