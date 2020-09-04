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

class RemoveLineCommand(private var plugin: ClickableHolosTest, val fileManager: FileManager, private val hologramRegistry: HologramRegistry) :
        AbstractCommand(CommandType.PLAYER_ONLY, "removeline") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {
        if(args.isNotEmpty() && sender is Player){
            val hologram = hologramRegistry.getHologram(args[0].toString())
            if(hologram != null && args.size > 1){
                val line = args[1]?.toInt()
                if(line != null) {
                    val configuration = fileManager.getConfiguration("holograms")
                    hologram.removeLine(line)
                    hologram.refresh()
                    configuration.set("${hologram.name}.lines", hologram.lines.toList())
                    fileManager.saveFile("holograms.yml")
                    return ReturnType.SUCCESS
                }
            }else{
                sender.sendMessage(HexUtil.colorify("&4Unknown hologram or missing arguments. Check syntax"))
            }
        }
        return ReturnType.SUCCESS
    }

    override fun getPermissionNode(): String {
        return "simpleholograms.command.addline"
    }

    override fun onTab(p0: CommandSender?, vararg p1: String?): MutableList<String> {
        return mutableListOf("")
    }

    override fun getDescription(): String {
        return "Remove line from the hologram."
    }

    override fun getSyntax(): String {
       return "/simpleholograms removeline <hologram:text> <line:number>"
    }
}