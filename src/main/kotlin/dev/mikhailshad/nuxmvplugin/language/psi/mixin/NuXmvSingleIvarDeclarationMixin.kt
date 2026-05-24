package dev.mikhailshad.nuxmvplugin.language.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import dev.mikhailshad.nuxmvplugin.language.NuXmvIcons
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvPresentableElement
import dev.mikhailshad.nuxmvplugin.language.psi.NuXmvSingleIvarDeclaration

abstract class NuXmvSingleIvarDeclarationMixin(node: ASTNode) : ASTWrapperPsiElement(node), NuXmvSingleIvarDeclaration,
    NuXmvPresentableElement {
    override fun getPresentation(): ItemPresentation =
        CachedValuesManager.getCachedValue(this, PRESENTATION_KEY) {
            val presentationText = "${varName.name}: ${simpleTypeSpecifier?.text}"
            val presentation = SimplePresentation(presentationText, NuXmvIcons.IVAR)
            CachedValueProvider.Result.create(presentation, PsiModificationTracker.MODIFICATION_COUNT)
        }

    companion object {
        private val PRESENTATION_KEY: Key<CachedValue<ItemPresentation>> =
            Key.create("nuxmv.singleIvarDeclaration.presentation")
    }
}
