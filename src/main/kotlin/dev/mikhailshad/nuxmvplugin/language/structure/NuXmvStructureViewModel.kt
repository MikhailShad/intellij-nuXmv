package dev.mikhailshad.nuxmvplugin.language.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import dev.mikhailshad.nuxmvplugin.language.psi.*

class NuXmvStructureViewModel(psiFile: NuXmvFile, editor: Editor?) :
    StructureViewModelBase(psiFile, editor, NuXmvStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        return suitableClasses.contains(element.value::class.java)
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return element.value is NuXmvPresentableElement
    }

    init {
        withSuitableClasses(
            NuXmvFile::class.java,
            NuXmvModule::class.java,
            NuXmvVarDeclaration::class.java,
            NuXmvIvarDeclaration::class.java,
            NuXmvFrozenVarDeclaration::class.java,
            NuXmvDefineDeclaration::class.java,
            NuXmvConstantsDeclaration::class.java,
            NuXmvCtlSpecification::class.java,
            NuXmvLtlSpecification::class.java,
            NuXmvInvarSpecification::class.java
        )
    }
}
