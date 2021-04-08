package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.hologram.requirements.Requirement

data class HologramTemplate(
    val lines : MutableList<String>,
    val name: String,
    val range: Int,
    val permission: String?,
    val location: Location,
    val requirements: List<Requirement>
)