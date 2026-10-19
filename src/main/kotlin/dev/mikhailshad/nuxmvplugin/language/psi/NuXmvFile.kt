package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType
import dev.mikhailshad.nuxmvplugin.language.NuXmvLanguage

class NuXmvFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, NuXmvLanguage) {
    override fun getFileType(): FileType = NuXmvFileType

    /**
     * The model is a timed transition system: it starts with `@TIME_DOMAIN continuous`.
     * Only then `time`, `clock`, `URGENT`, `X~`, `Y~`, `time_until` and `time_since` are meaningful.
     */
    val isTimed: Boolean
        get() = timeDomain?.identifier?.text == CONTINUOUS_TIME_DOMAIN

    /** The `@TIME_DOMAIN` annotation of the model, if present. */
    val timeDomain: NuXmvTimeDomainAnnotation?
        get() = PsiTreeUtil.getChildOfType(this, NuXmvTimeDomainAnnotation::class.java)

    companion object {
        const val CONTINUOUS_TIME_DOMAIN = "continuous"
        const val NONE_TIME_DOMAIN = "none"
        const val TIME_VARIABLE = "time"
    }
}

/** True when the element belongs to a timed model, see [NuXmvFile.isTimed]. */
val PsiElement.isInTimedModel: Boolean
    get() = (containingFile as? NuXmvFile)?.isTimed == true
