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
            holograms.add(Hologram(plugin,hologramTemplate.name, hologramTemplate.lines, hologramTemplate.location, hologramTemplate.range))
        }
    }

    fun addHologram(hologram: Hologram){
        holograms.add(hologram)
    }

    fun addHologram(template: HologramTemplate){
        holograms.add(Hologram(plugin,template.name, template.lines, template.location, template.range))
    }

    fun getHologram(hologram: String): Hologram?{
        holograms.asSequence().forEach {
            if(it.name == hologram){
                return it
            }
        }
        return null
    }

    fun clearHolograms(){
        holograms.asSequence().forEach {
            it.destroyClass()
        }
        holograms.clear()
    }

    fun removeHologram(name: String){
        holograms.asSequence().forEach {
            if(it.name == name){
                it.destroyClass()
                holograms.remove(it)
                return
            }
        }
    }



    fun register(player: Player){
        holograms.asSequence().forEach {
            it.register(player)
        }
    }


    fun register(player: Player, hologramName: String){
        holograms.asSequence().forEach {
            if(it.name == hologramName){
                it.register(player)
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


}