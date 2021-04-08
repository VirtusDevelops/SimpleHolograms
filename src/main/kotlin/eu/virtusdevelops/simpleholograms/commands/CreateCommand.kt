package eu.virtusdevelops.simpleholograms.commands

import eu.virtusdevelops.simpleholograms.SimpleHolograms
import eu.virtusdevelops.simpleholograms.hologram.HologramRegistry
import eu.virtusdevelops.simpleholograms.hologram.HologramTemplate
import eu.virtusdevelops.simplehologram.hologram.Hologram
import eu.virtusdevelops.simpleholograms.hologram.Location
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.managers.FileManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateCommand(private var plugin: SimpleHolograms, val fileManager: FileManager, private val hologramRegistry: HologramRegistry) :
        AbstractCommand(CommandType.PLAYER_ONLY, "create") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {
        if(args.isNotEmpty() && sender is Player){
            val location = Location(sender.location.x, sender.location.y, sender.location.z, sender.location.world!!.name)

            val hologramTemplate =
                HologramTemplate(mutableListOf("Example line"), args[0].toString(), 10, null, location, emptyList())
            val configuration = fileManager.getConfiguration("holograms")

            if(configuration.contains(hologramTemplate.name)){
                return ReturnType.SUCCESS
            }

            configuration.set("${hologramTemplate.name}.location.x", hologramTemplate.location.x)
            configuration.set("${hologramTemplate.name}.location.y", hologramTemplate.location.y)
            configuration.set("${hologramTemplate.name}.location.z", hologramTemplate.location.z)
            configuration.set("${hologramTemplate.name}.location.world", hologramTemplate.location.world)


            configuration.set("${hologramTemplate.name}.range", hologramTemplate.range)
            configuration.set("${hologramTemplate.name}.permission", hologramTemplate.permission)
            configuration.set("${hologramTemplate.name}.lines", hologramTemplate.lines.toMutableList())
            fileManager.saveFile("holograms.yml")

            hologramRegistry.addHologram(Hologram(plugin,
                hologramTemplate.name, hologramTemplate.lines, hologramTemplate.location, hologramTemplate.range, emptyList()))
            hologramRegistry.register(sender, hologramTemplate.name)
        }


        return ReturnType.SUCCESS
    }

    override fun getPermissionNode(): String {
        return "simpleholograms.command.create"
    }

    override fun onTab(p0: CommandSender?, vararg p1: String?): List<String> {
        if(p1.size == 1) {
            val arg = p1[0]
            if(arg != null) {
                return hologramRegistry.getHolograms().filter { it.name.contains(arg) }.map { it.name }
            }
        }
        return mutableListOf()
    }

    override fun getDescription(): String {
        return "Create the hologram!"
    }

    override fun getSyntax(): String {
       return "/simpleholograms create <name>"
    }
}