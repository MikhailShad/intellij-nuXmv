package dev.mikhailshad.nuxmvplugin.language.utils

import dev.mikhailshad.nuxmvplugin.language.psi.type.NuXmvDomainType

data class ModelSpecifications(
    val hasCtlSpecs: Boolean = false,
    val hasLtlSpecs: Boolean = false,
    val hasInvarSpecs: Boolean = false,
    val domainType: NuXmvDomainType = NuXmvDomainType.FINITE_DOMAIN
)
