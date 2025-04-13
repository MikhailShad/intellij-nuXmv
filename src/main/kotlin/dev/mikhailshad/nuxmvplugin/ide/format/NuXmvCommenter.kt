package dev.mikhailshad.nuxmvplugin.ide.format

import com.intellij.lang.Commenter


class NuXmvCommenter : Commenter {
    override fun getLineCommentPrefix() = "--"
    override fun getBlockCommentPrefix() = "/--"
    override fun getBlockCommentSuffix() = "--/"
    override fun getCommentedBlockCommentPrefix() = "/--"
    override fun getCommentedBlockCommentSuffix() = "--/"
}
