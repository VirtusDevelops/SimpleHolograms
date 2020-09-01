package eu.virtusdevelops.clickableholostest.commands

import eu.virtusdevelops.clickableholostest.hologram.HologramRegistry
import eu.virtusdevelops.clickableholostest.hologram.HologramStorage
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.managers.FileManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class ReloadCommand(private val fileManager: FileManager, private val hologramRegistry: HologramRegistry, private val hologramStorage: HologramStorage)
    : AbstractCommand(CommandType.BOTH, "reload") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {

        for(player in Bukkit.getOnlinePlayers()){
            hologramRegistry.unregister(player)
        }

        fileManager.clear()
        fileManager.loadFiles()

        hologramRegistry.clearTemplates()
        hologramRegistry.clearHolograms()

        hologramStorage.loadHolograms()
        hologramRegistry.loadHolograms()



        for(player in Bukkit.getOnlinePlayers()) {
            hologramRegistry.register(player)
        }


        return ReturnType.SUCCESS
    }

    override fun getPermissionNode(): String {
        return "simpleholograms.command.reload"
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