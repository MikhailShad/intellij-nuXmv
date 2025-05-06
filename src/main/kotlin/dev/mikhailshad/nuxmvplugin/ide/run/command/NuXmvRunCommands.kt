package dev.mikhailshad.nuxmvplugin.ide.run.command

interface NuXmvRunCommands {
    val buildCmd: String
    val checkCtlCmd: String
    val checkLtlCmd: String
    val checkInvarCmd: String
    val quitCmd: String
        get() = "quit"
}
