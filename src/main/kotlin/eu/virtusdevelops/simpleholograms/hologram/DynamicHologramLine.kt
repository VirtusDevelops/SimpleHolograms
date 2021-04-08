package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.nms.HoloPacket
import eu.virtusdevelops.simpleholograms.placeholder.PlaceholderRegistry.Companion.placeholders
import eu.virtusdevelops.virtuscore.utils.HexUtil
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import java.util.*

class DynamicHologramLine(private var line: String, private var id: Int, private var refresh: Double, originalLocation: Location,
                            offset: Double) {

    var uuid: UUID = UUID.randomUUID()
    var isVisible = true
    var newLocation: Location

    init {
        if(line.contains("{CLEAR}")){
            isVisible = false
        }
        newLocation = originalLocation.copy()
        newLocation.y = newLocation.y + offset
    }


    fun construct(players: MutableMap<Player, Boolean>){
        players.forEach { (player, isAlive) ->
            if(!isAlive && isVisible){
                // TODO CONSTRUCT LINE

                getPackets().spawnArmorStand(player, id, UUID.randomUUID(), newLocation)
                getPackets().initArmorStandAsHologram(player, id)
                getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player, 0)))

            }
        }
    }

    fun construct(player: Player){

        if(isVisible){
            // TODO CONSTRUCT LINE

            getPackets().spawnArmorStand(player, id, UUID.randomUUID(), newLocation)
            getPackets().initArmorStandAsHologram(player, id)
            getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player, 0)))

        }

    }


    fun update(players: MutableMap<Player, Boolean>, tenthsPassed: Long){
        if(tenthsPassed % refresh == 0.0) {
            players.forEach { player, isAlive ->
                if(isAlive) getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player, tenthsPassed)))
            }

        }
    }

    fun update(player: Player, tenthsPassed: Long){
        if(tenthsPassed % refresh == 0.0) {
            getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player, tenthsPassed)))
        }
    }

    private fun getPackets(): HoloPacket{
        return HoloPacket.INSTANCE!!
    }

    private fun parsePlaceholders(line: String, player: Player?, tenths: Long): String {
        var line = line
        line = PlaceholderAPI.setPlaceholders(player, line)
        for (placeholder in placeholders) {
            line = line.replace(placeholder.textPlaceholder, placeholder.currentReplacement)
        }
        line = line.replace("{CURRENTDATA}", tenths.toString())
        return line
    }

}