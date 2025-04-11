package dev.mikhailshad.nuxmvplugin.language

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object NuXmvIcons {
    val FILE: Icon = IconLoader.getIcon("/icons/jar-gray.png", NuXmvIcons::class.java)
    val MODULE: Icon = IconLoader.getIcon("/icons/module.svg", NuXmvIcons::class.java)
    val VAR: Icon = IconLoader.getIcon("/icons/var.svg", NuXmvIcons::class.java)
    val IVAR: Icon = IconLoader.getIcon("/icons/ivar.svg", NuXmvIcons::class.java)
    val SPECIFICATION: Icon = IconLoader.getIcon("/icons/specification.svg", NuXmvIcons::class.java)
}
