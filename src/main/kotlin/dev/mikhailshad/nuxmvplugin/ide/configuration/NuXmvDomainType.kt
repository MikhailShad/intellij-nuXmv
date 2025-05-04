package dev.mikhailshad.nuxmvplugin.ide.configuration

enum class NuXmvDomainType(val stringRepresentation: String) {
    FINITE_DOMAIN("Finite Domain"),
    INFINITE_DOMAIN("Infinite Domain"),
    TIMED_DOMAIN("Timed Domain");

    companion object {
        fun fromString(value: String?): NuXmvDomainType {
            return NuXmvDomainType.entries.first { it.name == value || it.stringRepresentation == value }
        }
    }

    override fun toString(): String = stringRepresentation
}
