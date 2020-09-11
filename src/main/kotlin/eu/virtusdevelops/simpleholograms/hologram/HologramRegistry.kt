package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.SimpleHolograms
import eu.virtusdevelops.simplehologram.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class HologramRegistry(private var plugin: SimpleHolograms) {


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

    fun addHologram(hologram: Hologram): Hologram{
        holograms.add(hologram)
        return hologram
    }

    fun addHologram(template: HologramTemplate): Hologram{
        val hologram = Hologram(plugin,template.name, template.lines, template.location, template.range)
        holograms.add(hologram)
        return hologram
    }

    fun getHologram(hologram: String): Hologram?{
        holograms.asSequence().forEach {
            if(it.name == hologram){
                return it
            }
        }
        return null
    }

    fun getHolograms(): MutableList<Hologram>{
        return holograms
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            holograms.asSequence().forEach {
                it.register(player)
            }
        })
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            for (hologram in holograms) {
                hologram.unregister(player)
            }
        })
    }

    fun getHologramByID(id: Int): Hologram?{
        holograms.asSequence().forEach {
            if(it.ids.contains(id)){
                return it
            }
        }
        return null
    }

}