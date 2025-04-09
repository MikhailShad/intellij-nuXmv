package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

object NuXmvElementFactory {
    fun createFile(project: Project, text: String): NuXmvFile {
        val name = "dummy.nuXmv"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(
                name, NuXmvFileType, text
            ) as NuXmvFile
    }

    fun createModuleName(project: Project, name: String): NuXmvModuleName {
        val file = createFile(project, "MODULE $name\nVAR\n")
        return file.firstChild.firstChild as NuXmvModuleName
    }

    fun createIdentifier(project: Project, name: String): PsiElement {
        val file = createFile(project, "MODULE main\nVAR\n$name : boolean;")
        val varDeclaration = file.findChildByClass(NuXmvModule::class.java)
            ?.moduleBody?.varDeclarationList?.first()
        val varName = varDeclaration?.singleVarDeclarationList?.first()?.varName
        return varName?.firstChild
            ?: throw IllegalStateException("Could not create identifier")
    }
}
