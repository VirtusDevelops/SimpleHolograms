package eu.virtusdevelops.simpleholograms.hologram

import eu.virtusdevelops.simpleholograms.SimpleHolograms
import eu.virtusdevelops.simpleholograms.hologram.requirements.PermissionRequirement
import eu.virtusdevelops.simpleholograms.hologram.requirements.Requirement
import eu.virtusdevelops.simpleholograms.hologram.requirements.TextRequirement
import eu.virtusdevelops.simpleholograms.hologram.requirements.ValueRequirement
import eu.virtusdevelops.virtuscore.managers.FileManager

class HologramStorage(private val plugin: SimpleHolograms, private val fileManager: FileManager,
                      private val hologramRegistry: HologramRegistry){

    fun loadHolograms(){
        val section = fileManager.getConfiguration("holograms").getConfigurationSection("") ?: return

        for(data in section.getKeys(false)){
            val world = section.getString("$data.location.world")
            if(world != null) {
                val location = Location(
                    section.getDouble("$data.location.x"),
                    section.getDouble("$data.location.y"),
                    section.getDouble("$data.location.z"),
                    world
                )
                val requirements = getRequirements(data)

                val template = HologramTemplate(
                    section.getStringList("$data.lines"),
                    data,
                    section.getInt("$data.range"),
                    section.getString("$data.permission"),
                    location,
                    requirements
                )
                hologramRegistry.registerTemplate(template)
            }
        }


    }


    fun getRequirements(holoname: String): List<Requirement>{
        val section = fileManager.getConfiguration("holograms").getConfigurationSection("$holoname.requirements") ?: return emptyList()
        val list : MutableList<Requirement> = mutableListOf()
        for(data in section.getKeys(false)){
            if(section.getString("$data.type").equals("permission", true)){
                val perm = section.getString("$data.permission")
                if(perm != null) {
                    list.add(PermissionRequirement(perm))
                }
            }

            if(section.getString("$data.type").equals("value", true)){
                val operation = section.getString("$data.operation")
                val first = section.getString("$data.first")
                val second = section.getString("$data.second")
                if(operation != null && first != null && second != null) {
                    list.add(ValueRequirement(first, operation, second))
                }
            }

            if(section.getString("$data.type").equals("text", true)){
                val operation = section.getString("$data.operation")
                val first = section.getString("$data.first")
                val second = section.getString("$data.second")
                if(operation != null && first != null && second != null) {
                    list.add(TextRequirement(first, operation, second))
                }
            }
        }
        return list
    }
}