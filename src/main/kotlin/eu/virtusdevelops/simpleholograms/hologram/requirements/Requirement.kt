package eu.virtusdevelops.simpleholograms.hologram.requirements

import org.bukkit.entity.Player

interface Requirement {

    fun meetsRequirement(player: Player): Boolean
}