package dev.mikhailshad.nuxmvplugin.ide.run.command

interface NuXmvRunCommands {
    val buildCmd: String
    val checkCtlCmd: String
    val checkLtlCmd: String
    val checkInvarCmd: String
    val showTracesCmd: String
        get() = "show_traces -a -v"
    val quitCmd: String
        get() = "quit"
}
