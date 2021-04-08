package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.nms.HoloPacket
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

open class ItemHologramLine(private var item: ItemStack, private var id: Int, originalLocation: Location,
                            offset: Double) {

    var uuid = UUID.randomUUID()
    var isVisible = true
    var newLocation: Location

    init {
        newLocation = originalLocation.copy()
        newLocation.y = newLocation.y + offset

    }

    fun construct(players: MutableMap<Player, Boolean>){
        players.forEach { (player, isAlive) ->
            if(!isAlive && isVisible){
                // TODO CONSTRUCT LINE
                getPackets().spawnItem(player, id, UUID.randomUUID(), newLocation, item)
            }
        }
    }

    fun construct(player: Player){
        if(isVisible){
            // TODO CONSTRUCT LINE
            getPackets().spawnItem(player, id, UUID.randomUUID(), newLocation, item)
        }
    }

    fun update(players: MutableMap<Player, Boolean>) {
        players.forEach { (player, isAlive) ->
            if(isAlive)
                getPackets().spawnItem(player, id, UUID.randomUUID(), newLocation, item)
        }
    }

    fun update(player: Player) {
        if(isVisible)
            getPackets().spawnItem(player, id, UUID.randomUUID(), newLocation, item)
    }

    fun getPackets(): HoloPacket {
        return HoloPacket.INSTANCE!!
    }


}