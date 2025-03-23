package dev.mikhailshad.nuxmvplugin.language.psi.impl

import com.intellij.psi.util.PsiTreeUtil
import dev.mikhailshad.nuxmvplugin.language.psi.*

object NuXmvPsiUtils {
    /**
     * Получает имя модуля из декларации модуля
     */
    fun getModuleName(module: NuXmvModuleDeclaration): String? {
        return module.moduleName?.text
    }

    /**
     * Находит все объявления переменных в модуле
     */
    fun findVariableDeclarations(module: NuXmvModuleDeclaration): List<NuXmvSingleVarDeclaration> {
        return PsiTreeUtil.findChildrenOfType(module, NuXmvSingleVarDeclaration::class.java).toList()
    }

    /**
     * Находит все объявления входных переменных в модуле
     */
    fun findIvarDeclarations(module: NuXmvModuleDeclaration): List<NuXmvSingleIvarDeclaration> {
        return PsiTreeUtil.findChildrenOfType(module, NuXmvSingleIvarDeclaration::class.java).toList()
    }

    /**
     * Находит все объявления Define в модуле
     */
    fun findDefineDeclarations(module: NuXmvModuleDeclaration): List<NuXmvDefineBody> {
        return PsiTreeUtil.findChildrenOfType(module, NuXmvDefineBody::class.java).toList()
    }

    /**
     * Получает тип выражения (для будущего использования в проверке типов)
     */
    fun getExpressionType(expr: NuXmvExpr): String {
        // Здесь была бы логика определения типа выражения
        // Это более сложная задача, которая требует анализа типов
        return "unknown"
    }
}
