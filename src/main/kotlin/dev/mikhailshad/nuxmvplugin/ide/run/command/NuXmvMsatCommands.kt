package dev.mikhailshad.nuxmvplugin.ide.run.command

object NuXmvMsatCommands : NuXmvRunCommands {
    override val buildCmd = "go_msat"
    override val checkCtlCmd: String get() = throw UnsupportedOperationException("CTL commands are not supported in MSAT engine")
    override val checkLtlCmd: String = "msat_check_ltlspec_bmc"
    override val checkInvarCmd: String = "msat_check_invar_bmc"
}
