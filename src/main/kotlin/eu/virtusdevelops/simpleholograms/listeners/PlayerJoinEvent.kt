package eu.virtusdevelops.simpleholograms.listeners

import eu.virtusdevelops.simpleholograms.SimpleHolograms
import eu.virtusdevelops.simpleholograms.hologram.HologramRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.system.measureTimeMillis

class PlayerJoinEvent(private val plugin: SimpleHolograms, private val hologramRegistry: HologramRegistry) : Listener {

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