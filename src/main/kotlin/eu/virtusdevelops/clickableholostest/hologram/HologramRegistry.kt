package eu.virtusdevelops.clickableholostest.hologram

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.clickableholotest.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class HologramRegistry(private var plugin: ClickableHolosTest) {


    private val holograms: MutableList<Hologram> = mutableListOf()
    private val hologramTemplates: MutableList<HologramTemplate> = mutableListOf()

    fun getTemplates(): MutableList<HologramTemplate>{
        return this.hologramTemplates
    }

    fun clearTemplates(){
        hologramTemplates.clear()
    }

    fun registerTemplate(hologramTemplate: HologramTemplate){
        hologramTemplates.add(hologramTemplate)
    }

    fun loadHolograms(){
        for(hologramTemplate in hologramTemplates){
            holograms.add(Hologram(plugin,hologramTemplate.name, hologramTemplate.lines, hologramTemplate.location, 1).updateRange(hologramTemplate.range))
        }
    }

    fun addHologram(hologram: Hologram){
        holograms.add(hologram)
    }

    fun clearHolograms(){
        for(hologram in holograms){
            for(player in Bukkit.getOnlinePlayers()){
                hologram.unregister(player)
            }
        }
        holograms.clear()
    }


    fun register(player: Player){
        for(hologram in holograms){
            hologram.register(player)
        }
    }


    fun register(player: Player, hologramName: String){
        for(hologram in holograms){
            if(hologram.name == hologramName){
                hologram.register(player)
            }
        }
    }


    fun unregister(player: Player, hologramName: String){
        for(hologram in holograms){
            if(hologram.name == hologramName){
                hologram.unregister(player)
            }
        }
    }

    fun unregister(player: Player){
        for(hologram in holograms){
            hologram.unregister(player)
        }
    }

    fun update(){
        /*for(hologram in holograms){
            hologram.tick()
        }*/
    }

    fun updatePlaceholders(elapsedTenthsOfSecond: Long){
        /*for(hologram in holograms){
            hologram.tickPlaceholderLines(elapsedTenthsOfSecond)
        }*/
    }

}