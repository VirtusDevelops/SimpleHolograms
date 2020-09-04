package eu.virtusdevelops.clickableholostest.commands

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.clickableholostest.hologram.HologramRegistry
import eu.virtusdevelops.clickableholostest.hologram.HologramTemplate
import eu.virtusdevelops.clickableholotest.hologram.Hologram
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.managers.FileManager
import eu.virtusdevelops.virtuscore.utils.HexUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoveHereCommand(private var plugin: ClickableHolosTest, val fileManager: FileManager, private val hologramRegistry: HologramRegistry) :
        AbstractCommand(CommandType.PLAYER_ONLY, "movehere") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {
        if(args.isNotEmpty() && sender is Player){
            val hologram = hologramRegistry.getHologram(args[0].toString())
            if(hologram != null){
                val position = sender.location
                hologram.location = position;
                hologram.refresh()
                val configuration = fileManager.getConfiguration("holograms")
                configuration.set("${hologram.name}.location", position)
                fileManager.saveFile("holograms.yml")
                return ReturnType.SUCCESS

            }else{
                sender.sendMessage(HexUtil.colorify("&4Unknown hologram: &e${args[0]}"))
            }
        }
        return ReturnType.SUCCESS
    }

    override fun getPermissionNode(): String {
        return "simpleholograms.command.setline"
    }

    override fun onTab(p0: CommandSender?, vararg p1: String?): MutableList<String> {
        return mutableListOf("")
    }

    override fun getDescription(): String {
        return "Teleport hologram to yourself."
    }

    override fun getSyntax(): String {
       return "/simpleholograms movehere <hologram:text>"
    }
}