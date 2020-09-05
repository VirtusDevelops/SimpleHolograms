package eu.virtusdevelops.simpleholograms.nms.impl

import eu.virtusdevelops.simpleholograms.nms.HoloPacket
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_16_R2.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

class HoloPacket_v1_16_R2 : HoloPacket() {

    override fun destroyEntity(player: Player, entityId: Int) {
        val packet = PacketPlayOutEntityDestroy(entityId)
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
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
                        Pair("k", EntityTypes.ITEM),
                        Pair("l", 0)
                )
        ) as PacketPlayOutSpawnEntity
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
        sendEntityMetadata(player, entityId, getMetaEntityGravity(true), getMetaEntityItemStack(itemStack))
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
        )as PacketPlayOutEntityTeleport
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int) {
        val packet = setPacket(PacketPlayOutMount(),
            mapOf(
                Pair("a", entityId),
                Pair("b", passengers)
            )
        ) as PacketPlayOutMount
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun updateArmorStandDisplayName(player: Player, entityId: Int, name: String) {
        sendEntityMetadata(player, entityId, getMetaEntityCustomName(name))
    }

    override fun updateArmorStandEquipmentItem(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(
            PacketPlayOutEntityEquipment(entityId, listOf(
                com.mojang.datafixers.util.Pair(EnumItemSlot.valueOf(slot.name), CraftItemStack.asNMSCopy(itemStack))
            )

            ))
    }


    override fun getMetaArmorStandProperties(isSmall: Boolean, hasArms: Boolean, noBasePlate: Boolean, marker: Boolean): Any {
        var bits = 0
        bits += if (isSmall) 1 else 0
        bits += if (hasArms) 4 else 0
        bits += if (noBasePlate) 8 else 0
        bits += if (marker) 10 else 0
        return DataWatcher.Item(DataWatcherObject(14, DataWatcherRegistry.a), bits.toByte())
    }



    override fun getMetaEntityGravity(noGravity: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(5, DataWatcherRegistry.i), noGravity)
    }

    override fun getMetaEntityItemStack(itemStack: ItemStack): Any {
        return DataWatcher.Item(DataWatcherObject(7, DataWatcherRegistry.g), CraftItemStack.asNMSCopy(itemStack))
    }

    override fun getMetaEntityProperties(onFire: Boolean, crouched: Boolean, sprinting: Boolean, swimming: Boolean, invisible: Boolean, glowing: Boolean, flyingElytra: Boolean): Any {
        var bits = 0
        bits += if (onFire) 1 else 0
        bits += if (crouched) 2 else 0
        bits += if (sprinting) 8 else 0
        bits += if (swimming) 10 else 0
        bits += if (glowing) 20 else 0
        bits += if (invisible) 40 else 0
        bits += if (flyingElytra) 80 else 0
        return DataWatcher.Item(DataWatcherObject(0, DataWatcherRegistry.a), bits.toByte())
    }

    override fun getMetaEntitySilenced(silenced: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(4, DataWatcherRegistry.i), silenced)
    }

    override fun getMetaEntityCustomNameVisible(visible: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(3, DataWatcherRegistry.i), visible)
    }

    override fun getMetaEntityCustomName(name: String): Any {

        val name2 = IChatBaseComponent.ChatSerializer.a(
            ComponentSerializer.toString(
                TextComponent.fromLegacyText(name)))
        if(name2 != null) {
            return DataWatcher.Item<Optional<IChatBaseComponent>>(
                DataWatcherObject(2, DataWatcherRegistry.f),
                Optional.of(name2.mutableCopy())
            )
        }

        return DataWatcher.Item<Optional<IChatBaseComponent>>(DataWatcherObject(2, DataWatcherRegistry.f), Optional.of(ChatComponentText(name)))

    }

    override fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any) {
        val items: MutableList<DataWatcher.Item<*>> = mutableListOf()
        for (obj in objects) {
            items.add(obj as DataWatcher.Item<*>)
        }


        val packet = setPacket(PacketPlayOutEntityMetadata(), mapOf(
                Pair("a", entityId),
                Pair("b", items)
        )) as PacketPlayOutEntityMetadata

        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)

    }

    override fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location) {

        val packet = setPacket(PacketPlayOutSpawnEntity(), mapOf(
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
                Pair("k", EntityTypes.ARMOR_STAND),
                Pair("l", 0)
        )) as PacketPlayOutSpawnEntity
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
        initArmorStandAsHologram(player, entityId)
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




}