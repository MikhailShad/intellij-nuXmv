package dev.mikhailshad.nuxmvplugin.language.psi.type

enum class NuXmvDomainType(val stringRepresentation: String) {
    FINITE_DOMAIN("Finite Domain"),
    INFINITE_DOMAIN("Infinite Domain"),
//    TIMED_DOMAIN("Timed Domain") TODO: implement later
    ;

    companion object {
        fun fromString(value: String?): NuXmvDomainType {
            return NuXmvDomainType.entries.firstOrNull { it.name == value || it.stringRepresentation == value }
                ?: FINITE_DOMAIN
        }
    }

    override fun toString(): String = stringRepresentation
}
