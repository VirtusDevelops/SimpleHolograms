package eu.virtusdevelops.clickableholostest.handlers

import eu.virtusdevelops.clickableholostest.hologram.HologramTemplate
import eu.virtusdevelops.clickableholotest.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class HologramRegistry {



    private val hologramsMap: MutableMap<UUID, MutableList<Hologram>> = mutableMapOf()
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

    fun addHologram(hologram: Hologram, player: Player){


        if(hologramsMap[player.uniqueId] == null){
            hologramsMap[player.uniqueId] = mutableListOf(hologram)
        }else {
            hologramsMap[player.uniqueId]?.add(hologram)
        }
    }

    fun unregister(player: Player){
        val holograms = hologramsMap[player.uniqueId]
        if(holograms != null) {
            val iter = holograms.iterator()
            while(iter.hasNext()){
                iter.next().destroy()
                iter.remove()
            }
        }
        hologramsMap.remove(player.uniqueId)
    }

    fun update(){
        for(user in hologramsMap.keys){
            val player = Bukkit.getPlayer(user)
            if(player != null){
                val holograms = hologramsMap[user]
                if(holograms != null) {
                    for (hologram in holograms){
                        if(!hologram.tick() ){
                            holograms.remove(hologram)
                        }
                    }
                }

            }
        }
    }

    fun updatePlaceholders(){
        for(user in hologramsMap.keys){
            val player = Bukkit.getPlayer(user)
            if(player != null){
                val holograms = hologramsMap[user]
                if(holograms != null) {
                    for (hologram in holograms){
                        if(hologram.containsPlaceholders()){
                            hologram.tickPlaceholderLines()
                        }
                    }
                }

            }
        }
    }

    fun refresh(){

    }
}