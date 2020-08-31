package eu.virtusdevelops.clickableholostest.nms.impl

import eu.virtusdevelops.clickableholostest.nms.HoloPacket
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

class HoloPacket_v1_8_R3 : HoloPacket(){

    override fun destroyEntity(player: Player, entityId: Int) {
        val packet = PacketPlayOutEntityDestroy(entityId)
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun getMetaArmorStandProperties(
        isSmall: Boolean,
        hasArms: Boolean,
        noBasePlate: Boolean,
        marker: Boolean
    ): Any {
        var bits = 0
        bits += if (isSmall) 1 else 0
        bits += if (hasArms) 2 else 0
        bits += if (noBasePlate) 8 else 0
        bits += if (marker) 10 else 0
        return DataWatcher.WatchableObject(0, 10, bits.toByte())
    }

    override fun getMetaEntityCustomName(name: String): Any {
        return DataWatcher.WatchableObject(4, 2, name)
    }

    override fun getMetaEntityCustomNameVisible(visible: Boolean): Any {
        return DataWatcher.WatchableObject(0, 3, (if (visible) 1 else 0).toByte())
    }

    override fun getMetaEntityGravity(noGravity: Boolean): Any {
        TODO()
    }

    override fun getMetaEntityItemStack(itemStack: ItemStack): Any {
        return DataWatcher.WatchableObject(5, 6, CraftItemStack.asNMSCopy(itemStack))
    }

    override fun getMetaEntityProperties(
        onFire: Boolean,
        crouched: Boolean,
        sprinting: Boolean,
        swimming: Boolean,
        invisible: Boolean,
        glowing: Boolean,
        flyingElytra: Boolean
    ): Any {
        var bits = 0
        bits += if (onFire) 1 else 0
        bits += if (crouched) 2 else 0
        bits += if (sprinting) 8 else 0
        bits += if (swimming) 10 else 0
        bits += if (invisible) 40 else 0
        return DataWatcher.WatchableObject(0, 0, bits.toByte())
    }

    override fun getMetaEntitySilenced(silenced: Boolean): Any {
        return DataWatcher.WatchableObject(0, 4, (if (silenced) 1 else 0).toByte())
    }

    override fun initArmorStandAsHologram(player: Player, entityId: Int) {
        sendEntityMetadata(player, entityId,
            getMetaEntityGravity(false),
            getMetaEntityCustomNameVisible(true),
            getMetaEntitySilenced(true),
            getMetaEntityProperties(false, false, true, false, true, false, false),
            getMetaArmorStandProperties(true, false, true, false)
        )
    }

    override fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any) {
        val items: MutableList<DataWatcher.WatchableObject> = mutableListOf()
        for (obj in objects) {
            items.add(obj as DataWatcher.WatchableObject)
        }
        val packet = setPacket(PacketPlayOutEntityMetadata(),
            mapOf(
                Pair("a", entityId),
                Pair("b", items))
        ) as PacketPlayOutEntityMetadata
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location) {
        val packet = setPacket(PacketPlayOutSpawnEntity(),
            mapOf(
                Pair("a", entityId),
                Pair("b", uuid),
                Pair("c", location.x),
                Pair("d", location.y),
                Pair("e", location.z),
                Pair("f", 0),
                Pair("g", 0),
                Pair("h", 0),
                Pair("i", 0),
                Pair("j", 0),
                Pair("k", 78),
                Pair("l", 0)
            )
        ) as PacketPlayOutSpawnEntity
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
        initArmorStandAsHologram(player, entityId)
    }

    override fun spawnItem(player: Player, entityId: Int, uuid: UUID, location: Location, itemStack: ItemStack) {
        val packet = setPacket(PacketPlayOutSpawnEntity(),
            mapOf(
                Pair("a", entityId),
                Pair("b", uuid),
                Pair("c", location.x),
                Pair("d", location.y),
                Pair("e", location.z),
                Pair("f", 0),
                Pair("g", 0),
                Pair("h", 0),
                Pair("i", 0),
                Pair("j", 0),
                Pair("k", 2),
                Pair("l", 0)
            )
        ) as PacketPlayOutSpawnEntity
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
        sendEntityMetadata(player, entityId, getMetaEntityGravity(true), getMetaEntityItemStack(itemStack))
    }

    override fun updateArmorStandDisplayName(player: Player, entityId: Int, name: String) {
        sendEntityMetadata(player, entityId, getMetaEntityCustomName(name))
    }

    override fun updateArmorStandEquipmentItem(
        player: Player,
        entityId: Int,
        slot: EquipmentSlot,
        itemStack: ItemStack
    ) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(
            PacketPlayOutEntityEquipment(entityId, 4, CraftItemStack.asNMSCopy(itemStack)))
    }

    override fun updateArmorStandLocation(player: Player, entityId: Int, location: Location) {
        val packet = setPacket(PacketPlayOutEntityTeleport(),
            mapOf(
                Pair("a", entityId),
                Pair("b", location.x),
                Pair("c", location.y),
                Pair("d", location.z),
                Pair("e", 0),
                Pair("f", 0),
                Pair("g", false)
            )
        ) as PacketPlayOutEntityTeleport
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int) {
        val packet = setPacket(PacketPlayOutAttachEntity(),
            mapOf(
                Pair("a", entityId),
                Pair("b", passengers)
            )
        ) as PacketPlayOutAttachEntity

        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

}