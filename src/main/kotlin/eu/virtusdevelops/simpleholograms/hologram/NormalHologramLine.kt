package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.nms.HoloPacket
import eu.virtusdevelops.simpleholograms.placeholder.PlaceholderRegistry.Companion.placeholders
import eu.virtusdevelops.virtuscore.VirtusCore
import eu.virtusdevelops.virtuscore.utils.HexUtil
import eu.virtusdevelops.virtuscore.utils.TextUtil
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

open class NormalHologramLine(private var line: String, private var id: Int, originalLocation: Location,
                                offset: Double) {

    var uuid = UUID.randomUUID()
    var isVisible = true
    var newLocation: Location;

    init {
        if(line.contains("{CLEAR}")){
            isVisible = false
        }
        newLocation = originalLocation.clone()
        newLocation.y = newLocation.y + offset
    }

    fun construct(players: MutableMap<Player, Boolean>){
        players.forEach { (player, isAlive) ->
            if(!isAlive && isVisible){
                // TODO CONSTRUCT LINE

                getPackets().spawnArmorStand(player, id, UUID.randomUUID(), newLocation)
                getPackets().initArmorStandAsHologram(player, id)
                getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))

            }
        }
    }

    fun construct(player: Player){

        if(isVisible){
            // TODO CONSTRUCT LINE

            getPackets().spawnArmorStand(player, id, UUID.randomUUID(), newLocation)
            getPackets().initArmorStandAsHologram(player, id)
            getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))

        }

    }

    fun update(players: MutableMap<Player, Boolean>) {
        players.forEach { (player, isAlive) ->
            if(isAlive && isVisible)
                getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))
        }
    }

    fun update(player: Player) {
        if(isVisible)
            getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))
    }

    fun getPackets(): HoloPacket {
        return HoloPacket.INSTANCE!!
    }

    open fun parsePlaceholders(line: String, player: Player?): String {
        var line = line
        line = PlaceholderAPI.setPlaceholders(player, line)
        for (placeholder in placeholders) {
            line = line.replace(placeholder.textPlaceholder, placeholder.currentReplacement)
        }
        return line
    }

}