package dev.mikhailshad.nuxmvplugin.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import dev.mikhailshad.nuxmvplugin.language.NuXmvFileType

object NuXmvElementFactory {
    fun createFile(project: Project, text: String): NuXmvFile {
        val name = "dummy.nuXmv"
        return PsiFileFactory.getInstance(project).createFileFromText(
            name, NuXmvFileType, text
        ) as NuXmvFile
    }

    fun createModuleName(project: Project, name: String): NuXmvModuleName {
        val file = createFile(project, "MODULE $name\n")
        return file.firstChild.children[1] as NuXmvModuleName
    }

    fun createVarName(project: Project, name: String): NuXmvVarName {
        val file = createFile(project, "MODULE dummy\nVAR $name : boolean;\n")
        val moduleBody = (file.firstChild as NuXmvNuXmvModule).moduleBody!!
        val varDecl = moduleBody.children.first { it is NuXmvVarDeclaration }
        val singleVarDecl = varDecl.children.first { it is NuXmvSingleVarDeclaration }
        return (singleVarDecl as NuXmvSingleVarDeclaration).varName
    }

    fun createIdentifier(project: Project, name: String): NuXmvSimpleIdentifier {
        val file = createFile(project, "MODULE $name\n")
        return file.firstChild.children[1].children.first { it is NuXmvSimpleIdentifier } as NuXmvSimpleIdentifier
    }

    fun createDefineIdentifier(project: Project, name: String): NuXmvComplexIdentifier {
        val file = createFile(project, "MODULE dummy\nDEFINE $name := TRUE;\n")
        val moduleBody = (file.firstChild as NuXmvNuXmvModule).moduleBody!!
        val defineDecl = moduleBody.children.first { it is NuXmvDefineDeclaration }
        val defineBody = defineDecl.children.first { it is NuXmvDefineBody }
        return (defineBody as NuXmvDefineBody).complexIdentifier
    }
}
