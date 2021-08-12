package eu.virtusdevelops.simpleholograms.nms.impl

import eu.virtusdevelops.simpleholograms.nms.HoloPacket
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import eu.virtusdevelops.simpleholograms.hologram.Location
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketDataSerializer
import net.minecraft.network.chat.ChatComponentText
import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.*
import net.minecraft.network.syncher.DataWatcher
import net.minecraft.network.syncher.DataWatcherObject
import net.minecraft.network.syncher.DataWatcherRegistry
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.EnumItemSlot
import net.minecraft.world.phys.Vec3D
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList

class HoloPacket_v1_17_R1 : HoloPacket() {

    override fun destroyEntity(player: Player, entityId: Int) {

        val packet = PacketPlayOutEntityDestroy(entityId, entityId)

        (player as CraftPlayer).handle.b.a.sendPacket(packet)
    }

    override fun spawnItem(player: Player, entityId: Int, uuid: UUID, location: Location, itemStack: ItemStack) {

        val packet = PacketPlayOutSpawnEntity(entityId,
            uuid,
            location.x,
            location.y,
            location.z,
            0.0f,
            0.0f,
            EntityTypes.Q,
            0,
            Vec3D(0.0,0.0,0.0))

        val packet2 = PacketPlayOutEntityVelocity(entityId, Vec3D(0.0,0.0, 0.0))

        (player as CraftPlayer).handle.b.a.sendPacket(packet)
        player.handle.b.a.sendPacket(packet2)
        sendEntityMetadata(player, entityId, getMetaEntityGravity(true), getMetaEntityItemStack(itemStack))
    }



    override fun updateArmorStandLocation(player: Player, entityId: Int, location: Location) {

        val bytes = byteArrayOf(entityId.toByte(),
            location.x.toInt().toByte(),
            location.y.toInt().toByte(),
            location.z.toInt().toByte(),
            0,
            0,
            1)
        val byteBuf = Unpooled.copiedBuffer(bytes)
        val packet = PacketPlayOutEntityTeleport(PacketDataSerializer(byteBuf))


        (player as CraftPlayer).handle.b.a.sendPacket(packet)
    }

    override fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int) {

        val bytes = byteArrayOf(entityId.toByte(), 0)
        val byteBuf = Unpooled.copiedBuffer(bytes)

        val packet = PacketPlayOutMount(PacketDataSerializer(byteBuf))
        (player as CraftPlayer).handle.b.a.sendPacket(packet)
    }

    override fun updateArmorStandDisplayName(player: Player, entityId: Int, name: String) {
        sendEntityMetadata(player, entityId, getMetaEntityCustomName(name))
    }

    override fun updateArmorStandEquipmentItem(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack) {
        (player as CraftPlayer).handle.b.a.sendPacket(
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
        return DataWatcher.Item<Optional<IChatBaseComponent>>(DataWatcherObject(2, DataWatcherRegistry.f), Optional.of(
            ChatComponentText(name)
        ))

    }

    override fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any) {
        val items: MutableList<DataWatcher.Item<*>> = ArrayList()
        for (obj in objects) {
            items.add(obj as DataWatcher.Item<*>)
        }



        val packet = setPacket(PacketPlayOutEntityMetadata(entityId, DataWatcher(null), false), mapOf(
                Pair("a", entityId),
                Pair("b", items)
        )) as PacketPlayOutEntityMetadata

        (player as CraftPlayer).handle.b.a.sendPacket(packet)

    }

    override fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location) {

        val packet = PacketPlayOutSpawnEntity(
            entityId,
            uuid,
            location.x,
            location.y,
            location.z,
            0.0f,
            0.0f,
            EntityTypes.c,
            0,
            Vec3D(0.0,0.0,0.0))

        (player as CraftPlayer).handle.b.a.sendPacket(packet)
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

    override fun sendPacket(players: List<Player>, packet: Any) {
        packet as Packet<*>?
        for( pl in players){
            (pl as CraftPlayer).handle.b.a.sendPacket(packet)
        }
    }

    override fun sendPacket(players: List<Player>, packets: List<Any>) {
        for( pl in players){
            for(packet in packets){
                (pl as CraftPlayer).handle.b.a.sendPacket(packet as Packet<*>?)
            }
        }
    }


}