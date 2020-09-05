package eu.virtusdevelops.simpleholograms.hologram

import org.bukkit.Location

data class HologramTemplate(
    val lines : MutableList<String>,
    val name: String,
    val range: Int,
    val permission: String?,
    val location: Location
)