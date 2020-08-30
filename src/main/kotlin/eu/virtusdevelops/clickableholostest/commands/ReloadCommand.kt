package eu.virtusdevelops.clickableholostest.commands

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.clickableholostest.handlers.HologramRegistry
import eu.virtusdevelops.clickableholostest.hologram.HologramStorage
import eu.virtusdevelops.clickableholostest.hologram.HologramTemplate
import eu.virtusdevelops.clickableholotest.hologram.Hologram
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.managers.FileManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCommand(private val fileManager: FileManager, private val hologramRegistry: HologramRegistry, private val hologramStorage: HologramStorage)
    : AbstractCommand(CommandType.BOTH, "reload") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {

        for(player in Bukkit.getOnlinePlayers()){
            hologramRegistry.unregister(player)
        }

        fileManager.clear()
        fileManager.loadFiles()

        hologramRegistry.clearTemplates()
        hologramStorage.loadHolograms()



        for(player in Bukkit.getOnlinePlayers()) {
            for (hologram in hologramRegistry.getTemplates()) {
                hologramRegistry.addHologram(
                    Hologram(
                        hologram.name, hologram.lines, hologram.location, 1, player
                    ).updateRange(hologram.range)
                    , player
                )
            }
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