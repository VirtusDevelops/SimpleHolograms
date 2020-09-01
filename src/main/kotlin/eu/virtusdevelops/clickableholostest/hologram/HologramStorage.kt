package eu.virtusdevelops.clickableholostest.hologram

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.virtuscore.managers.FileManager
import org.bukkit.Location

class HologramStorage(private val plugin: ClickableHolosTest, private val fileManager: FileManager,
                        private val hologramRegistry: HologramRegistry){

    fun loadHolograms(){
        val section = fileManager.getConfiguration("holograms").getConfigurationSection("") ?: return

        for(data in section.getKeys(false)){
            val location = section.get("$data.location") as Location

            if(location != null) {
                val template = HologramTemplate(
                    section.getStringList("$data.lines"),
                    data,
                    section.getInt("$data.range"),
                    section.getString("$data.permission"),
                    location
                )
                hologramRegistry.registerTemplate(template)
            }
        }


    }
}