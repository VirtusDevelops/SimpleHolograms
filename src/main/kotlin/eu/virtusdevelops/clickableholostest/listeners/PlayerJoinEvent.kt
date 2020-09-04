package eu.virtusdevelops.clickableholostest.listeners

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.clickableholostest.hologram.HologramRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.system.measureTimeMillis

class PlayerJoinEvent(private val plugin: ClickableHolosTest, private val hologramRegistry: HologramRegistry) : Listener {

    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent){
        plugin.logger.info(
                "Registering player - ${
                measureTimeMillis {
                    hologramRegistry.register(event.player)
                }
                }ms"
        )
        hologramRegistry.register(event.player)
    }

    @EventHandler
    fun onPlayerDisconect(event: PlayerQuitEvent){
        hologramRegistry.unregister(event.player)
    }
}