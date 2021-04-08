package eu.virtusdevelops.simpleholograms.hologram.requirements

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

class TextRequirement(private val value: String, private val operation: String, private val secondaryValue: String):Requirement{


    override fun meetsRequirement(player: Player): Boolean {

        val first = PlaceholderAPI.setPlaceholders(player, value)
        val second = PlaceholderAPI.setPlaceholders(player, secondaryValue)

        when(operation) {
            "==" -> {
                return first == second
            }
            "!=" -> {
                return first != second
            }
            "contains" -> {
                return first.contains(second)
            }
            "!contains" -> {
                return !first.contains(second)
            }
        }
        return false

    }
}