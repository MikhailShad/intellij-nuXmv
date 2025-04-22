package dev.mikhailshad.nuxmvplugin.language

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object NuXmvIcons {
    val FILE: Icon = IconLoader.getIcon("/icons/file.svg", NuXmvIcons::class.java)
    val MODULE: Icon = IconLoader.getIcon("/icons/module.svg", NuXmvIcons::class.java)
    val MODULE_LINK_TO: Icon = IconLoader.getIcon("/icons/module_link_to.svg", NuXmvIcons::class.java)
    val MODULE_LINK_FROM: Icon = IconLoader.getIcon("/icons/module_link_from.svg", NuXmvIcons::class.java)
    val VAR: Icon = IconLoader.getIcon("/icons/var.svg", NuXmvIcons::class.java)
    val IVAR: Icon = IconLoader.getIcon("/icons/ivar.svg", NuXmvIcons::class.java)
    val FROZEN_VAR: Icon = IconLoader.getIcon("/icons/frozen_var.svg", NuXmvIcons::class.java)
    val CONSTANT: Icon = IconLoader.getIcon("/icons/constant.svg", NuXmvIcons::class.java)
    val DEFINE: Icon = IconLoader.getIcon("/icons/define.svg", NuXmvIcons::class.java)
    val CTL_SPEC: Icon = IconLoader.getIcon("/icons/spec_ctl.svg", NuXmvIcons::class.java)
    val LTL_SPEC: Icon = IconLoader.getIcon("/icons/spec_ltl.svg", NuXmvIcons::class.java)
    val INVAR_SPEC: Icon = IconLoader.getIcon("/icons/spec_invar.svg", NuXmvIcons::class.java)
    val OTHER_SPEC: Icon = IconLoader.getIcon("/icons/other.svg", NuXmvIcons::class.java)
}
