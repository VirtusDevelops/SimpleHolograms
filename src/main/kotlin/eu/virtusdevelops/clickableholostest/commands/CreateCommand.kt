package eu.virtusdevelops.clickableholostest.commands

import eu.virtusdevelops.clickableholostest.hologram.HologramRegistry
import eu.virtusdevelops.clickableholostest.hologram.HologramTemplate
import eu.virtusdevelops.clickableholotest.hologram.Hologram
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.managers.FileManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateCommand(private val fileManager: FileManager, private val hologramRegistry: HologramRegistry) : AbstractCommand(CommandType.PLAYER_ONLY, "create") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {
        if(args.isNotEmpty() && sender is Player){


            val hologramTemplate =
                HologramTemplate(mutableListOf("Example line"), args[0].toString(), 10, null, sender.location)
            val configuration = fileManager.getConfiguration("holograms")

            if(configuration.contains(hologramTemplate.name)){
                return ReturnType.SUCCESS
            }

            configuration.set("${hologramTemplate.name}.location", hologramTemplate.location)
            configuration.set("${hologramTemplate.name}.range", hologramTemplate.range)
            configuration.set("${hologramTemplate.name}.permission", hologramTemplate.permission)
            configuration.set("${hologramTemplate.name}.lines", hologramTemplate.lines.toMutableList())
            fileManager.saveFile("holograms.yml")

            hologramRegistry.addHologram(Hologram(
                hologramTemplate.name, hologramTemplate.lines, hologramTemplate.location, 1).updateRange(hologramTemplate.range))
            hologramRegistry.register(sender, hologramTemplate.name)
        }


        return ReturnType.SUCCESS
    }

    override fun getPermissionNode(): String {
        return "simpleholograms.command.create"
    }

    override fun onTab(p0: CommandSender?, vararg p1: String?): MutableList<String> {
        return mutableListOf("")
    }

    override fun getDescription(): String {
        return "Create the hologram!"
    }

    override fun getSyntax(): String {
       return "/simpleholograms create <name>"
    }
}