package eu.virtusdevelops.clickableholostest.hologram

import eu.virtusdevelops.clickableholostest.nms.HoloPacket
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry.Companion.placeholders
import eu.virtusdevelops.virtuscore.utils.HexUtil
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import java.util.*

open class DynamicHologramLine(private var line: String, private var id: Int, private var refresh: Int) {

    var uuid = UUID.randomUUID()




    fun update(players: MutableMap<Player, Boolean>, tenthsPassed: Long){
        if(tenthsPassed % refresh == 0L) {
            for (data in players) {
                if (data.value) {
                    getPackets().updateArmorStandDisplayName(data.key, id, HexUtil.colorify(parsePlaceholders(line, data.key)))
                }
            }
        }
    }

    fun update(player: Player, tenthsPassed: Long){
        if(tenthsPassed % refresh == 0L) {
            getPackets().updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)))
        }
    }

    fun getPackets(): HoloPacket{
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