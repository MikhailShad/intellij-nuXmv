package dev.mikhailshad.nuxmvplugin.ide.run.command

object NuXmvBddCommands : NuXmvRunCommands {
    override val buildCmd = "go"
    override val checkCtlCmd: String = "check_ctlspec"
    override val checkLtlCmd: String = "check_ltlspec"
    override val checkInvarCmd: String = "check_invar"
}
