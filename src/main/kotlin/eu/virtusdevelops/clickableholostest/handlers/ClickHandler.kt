package eu.virtusdevelops.clickableholostest.handlers


import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class ClickHandler(private val player: Player, private val plugin: ClickableHolosTest) {
    private val mobs = mutableListOf<Int>()
    private val distance = 1
    private var center = player.location

    private lateinit var task : BukkitTask

    init {
        player.sendMessage("Started clicking handler.")


        // set location of holo
        center.x = -154.0
        center.y = 67.0
        center.z = 113.0

        //spawnMobs()
        //startTask()

    }
    /*
    private fun spawnMobs(){

        val id = (0 .. Int.MAX_VALUE).random()


        val packet = WrapperPlayServerSpawnEntityLiving()
        packet.entityID = id
        packet.setType(75)
        packet.x = center.x
        packet.y = center.y
        packet.z = center.z
        packet.uniqueId = UUID.randomUUID()
        mobs.add(id)
        packet.sendPacket(player)

        val id2 = (0 .. Int.MAX_VALUE).random()



        val packet2 = WrapperPlayServerSpawnEntityLiving()

        packet2.entityID = id2
        packet2.setType(75)
        packet2.x = center.x
        packet2.y = center.y
        packet2.z = center.z
        packet2.uniqueId = UUID.randomUUID()
        mobs.add(id2)
        packet2.sendPacket(player)





    }

    private fun startTask(){
        task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable{

            if(!player.isOnline){
                delete()
            }

            val plloc = player.location


            val x: Double = center.x + distance * cos(Math.toRadians(0.0 + plloc.yaw))
            val z: Double = center.z + distance * sin(Math.toRadians(0.0 + plloc.yaw))

            val x2: Double = center.x + distance * cos(Math.toRadians(180.0 + plloc.yaw))
            val z2: Double = center.z + distance * sin(Math.toRadians(180.0 + plloc.yaw))

            val packet = WrapperPlayServerEntityTeleport()
            packet.entityID = mobs[0]
            packet.x = x
            packet.y = center.y
            packet.z = z


            val packet2 = WrapperPlayServerEntityTeleport()
            packet2.entityID = mobs[1]
            packet2.x = x2
            packet2.y = center.y
            packet2.z = z2



            packet.sendPacket(player)
            packet2.sendPacket(player)

        }, 0L, 1L)

    }

    private fun delete(){
        task.cancel()
        mobs.clear()
    }

     */
}