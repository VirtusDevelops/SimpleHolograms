package eu.virtusdevelops.simpleholograms.hologram.requirements

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

class ValueRequirement(private val value: String, private val operation: String, private val secondaryValue: String):Requirement{


    override fun meetsRequirement(player: Player): Boolean {

        val first = PlaceholderAPI.setPlaceholders(player, value).toDouble()
        val second = PlaceholderAPI.setPlaceholders(player, secondaryValue).toDouble()

        when(operation) {
            ">" -> {
                return first > second
            }
            ">=" -> {
                return first >= second
            }
            "<" -> {
                return first < second
            }
            "<=" -> {
                return first <= second
            }
            "==" -> {
                return first == second
            }
            "!=" -> {
                return first != second
            }
        }
        return false

    }
}