package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.nms.HoloPacket
import eu.virtusdevelops.simpleholograms.placeholder.PlaceholderRegistry.Companion.placeholders
import eu.virtusdevelops.virtuscore.utils.HexUtil
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import java.util.*

open class NormalHologramLine(private var line: String, private var id: Int) {

    var uuid = UUID.randomUUID()
    var isVisible = true

    init {
        if(line.contains("{CLEAR}")){
            isVisible = false
        }
    }

    fun update(players: MutableMap<Player, Boolean>) {
        players.forEach { player, isAlive ->
            if(isAlive)
                if(!isVisible) getPackets().destroyEntity(player, id)
                else getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))
        }
    }

    fun update(player: Player) {
        if(!isVisible) getPackets().destroyEntity(player, id)
        else getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))
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