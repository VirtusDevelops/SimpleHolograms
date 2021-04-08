package eu.virtusdevelops.simpleholograms.hologram.requirements

import org.bukkit.entity.Player

class PermissionRequirement(private val permission: String) : Requirement{

    override fun meetsRequirement(player: Player): Boolean{
        return player.hasPermission(permission)
    }
}