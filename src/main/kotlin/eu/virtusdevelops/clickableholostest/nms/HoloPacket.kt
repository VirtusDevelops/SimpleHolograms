package eu.virtusdevelops.clickableholostest.nms

import eu.virtusdevelops.clickableholostest.nms.impl.*
import eu.virtusdevelops.virtuscore.VirtusCore
import eu.virtusdevelops.virtuscore.utils.NMSUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class HoloPacket {
    abstract fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location)
    abstract fun spawnItem(player: Player, entityId: Int, uuid: UUID, location: Location, itemStack: ItemStack)
    abstract fun destroyEntity(player: Player, entityId: Int)
    abstract fun initArmorStandAsHologram(player: Player, entityId: Int)
    abstract fun updateArmorStandDisplayName(player: Player, entityId: Int, name: String)
    abstract fun updateArmorStandLocation(player: Player, entityId: Int, location: Location)
    abstract fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int)
    abstract fun updateArmorStandEquipmentItem(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack)
    abstract fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any)
    abstract fun getMetaEntityItemStack(itemStack: ItemStack): Any
    abstract fun getMetaEntityProperties(onFire: Boolean, crouched: Boolean, sprinting: Boolean, swimming: Boolean, invisible: Boolean, glowing: Boolean, flyingElytra: Boolean): Any
    abstract fun getMetaEntityGravity(noGravity: Boolean): Any
    abstract fun getMetaEntitySilenced(silenced: Boolean): Any
    abstract fun getMetaEntityCustomNameVisible(visible: Boolean): Any
    abstract fun getMetaEntityCustomName(name: String): Any
    abstract fun getMetaArmorStandProperties(isSmall: Boolean, hasArms: Boolean, noBasePlate: Boolean, marker: Boolean): Any


    fun setPacket(packet: Any, sets: Map<String, Any>): Any {
        sets.forEach { (key: String, value: Any) -> packet.updateField(key, value) }
        return packet
    }

    companion object {
        var INSTANCE: HoloPacket? = null

        init {
            // Add all NMS
            VirtusCore.console().sendMessage(NMSUtil.getServerVersion())
            when(NMSUtil.getServerVersion()){
                "v1_16_R2." -> {
                    INSTANCE = HoloPacket_v1_16_R2()
                }
                "v1_16_R1." -> {
                    INSTANCE = HoloPacket_v1_16_R1()
                }
                "v1_15_R1." -> {
                    INSTANCE = HoloPacket_v1_15_R1()
                }
                "v1_14_R1." -> {
                    INSTANCE = HoloPacket_v1_14_R1()
                }
                "v1_13_R2." -> {
                    INSTANCE = HoloPacket_v1_13_R2()
                }
                "v1_13_R1." -> {
                    INSTANCE = HoloPacket_v1_13_R1()
                }
                "v1_12_R1." -> {
                    INSTANCE = HoloPacket_v1_12_R1()
                }
                "v1_11_R1." -> {
                    INSTANCE = HoloPacket_v1_11_R1()
                }
                "v1_10_R1." -> {
                    INSTANCE = HoloPacket_v1_10_R1()
                }
                "v1_9_R2." -> {
                    INSTANCE = HoloPacket_v1_9_R2()
                }
                "v1_9_R1." -> {
                    INSTANCE = HoloPacket_v1_9_R1()
                }
                "v1_8_R3." -> {
                    INSTANCE = HoloPacket_v1_8_R3()
                }



            }
            if(INSTANCE == null){
                VirtusCore.console().sendMessage("Could not find correct NMS disabling plugin (check if plugin is compatible with your server)")
            }
        }

    }

    private fun Any.retrieveField(name: String): Any
            = this::class.java.getDeclaredField(name).apply { isAccessible = true }.get(this)

    private fun Any.updateField(name: String, value: Any)
            = this::class.java.getDeclaredField(name).apply { isAccessible = true }.set(this, value)
}