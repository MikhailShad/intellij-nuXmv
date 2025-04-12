package dev.mikhailshad.nuxmvplugin.ide.configuration

import com.intellij.openapi.components.*

@State(
    name = "dev.mikhailshad.nuxmvplugin.settings.NuXmvSettingsState",
    storages = [Storage("NuXmvSettings.xml")]
)
@Service
class NuXmvSettingsState : PersistentStateComponent<NuXmvSettingsState.State> {
    data class State(
        var nuXmvExecutablePath: String = ""
    )

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    companion object {
        fun getInstance(): NuXmvSettingsState =
            service<NuXmvSettingsState>()
    }
}
