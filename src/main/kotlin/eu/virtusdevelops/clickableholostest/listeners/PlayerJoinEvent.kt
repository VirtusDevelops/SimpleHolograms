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
        // construct rotation handler class
        //plugin.injectPlayer(event.player)
        //ClickHandler(event.player, plugin)

        for(hologram in hologramRegistry.getTemplates()){

            hologramRegistry.addHologram(Hologram(
                hologram.name, hologram.lines, hologram.location, 1, event.player).updateRange(hologram.range)
                , event.player)

        }

        /*hologramRegistry.addHologram(
                Hologram("Custom", mutableListOf(
                        "This is line 1",
                        "<r>This is line 2"
                ), event.player.location, 1, event.player), event.player)

        hologramRegistry.addHologram(
                Hologram("Custom2", mutableListOf(
                        "&8[ &eStatistics &8]",
                        "&8[&7Biome: &e%player_biome%&8]",
                        "&8[&7Gamemode: &e%player_gamemode%&8]",
                        "&8[&7Health: &e%player_health%&8]",
                        "&8[&7Name: &e%player_name%&8]",
                        "{THISDA}"
                ), event.player.location.apply { x = -162.0; y = 66.0; z = 119.0 }, 1, event.player).updateRange(25), event.player)*/

        // Load all holograms.

    }

    @EventHandler
    fun onPlayerDisconect(event: PlayerQuitEvent){
        hologramRegistry.unregister(event.player)
        //plugin.removePlayer(event.player)
    }
}