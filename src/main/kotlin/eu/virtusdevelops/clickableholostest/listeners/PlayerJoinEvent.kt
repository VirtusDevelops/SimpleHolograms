package eu.virtusdevelops.clickableholostest.listeners

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.clickableholostest.handlers.ClickHandler
import eu.virtusdevelops.clickableholostest.handlers.HologramRegistry
import eu.virtusdevelops.clickableholotest.hologram.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerJoinEvent(private val plugin: ClickableHolosTest, private val hologramRegistry: HologramRegistry) : Listener {

    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent){


        for(hologram in hologramRegistry.getTemplates()){
            hologramRegistry.addHologram(Hologram(
                hologram.name, hologram.lines, hologram.location, 1, event.player).updateRange(hologram.range)
                , event.player)
        }
    }

    @EventHandler
    fun onPlayerDisconect(event: PlayerQuitEvent){
        hologramRegistry.unregister(event.player)
    }
}