package eu.virtusdevelops.simpleholograms.commands

import eu.virtusdevelops.simpleholograms.SimpleHolograms
import eu.virtusdevelops.simpleholograms.hologram.HologramRegistry
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.managers.FileManager
import eu.virtusdevelops.virtuscore.utils.HexUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddLineCommand(private var plugin: SimpleHolograms, val fileManager: FileManager, private val hologramRegistry: HologramRegistry) :
        AbstractCommand(CommandType.PLAYER_ONLY, "addline") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {
        if(args.isNotEmpty() && sender is Player){
            val hologram = hologramRegistry.getHologram(args[0].toString())
            if(hologram != null && args.size > 1){
                val configuration = fileManager.getConfiguration("holograms")
                hologram.addLine(args.copyOfRange(1, args.size).joinToString(" "))
                hologram.refresh()
                configuration.set("${hologram.name}.lines", hologram.lines.toList())
                fileManager.saveFile("holograms.yml")
                return ReturnType.SUCCESS
            }else{
                sender.sendMessage(HexUtil.colorify("&4Unknown hologram or missing arguments. Check Syntax."))
            }
        }
        return ReturnType.SUCCESS
    }

    override fun getPermissionNode(): String {
        return "simpleholograms.command.addline"
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
        return "Add line to hologram"
    }

    override fun getSyntax(): String {
       return "/simpleholograms addline <hologram:text> <line:text>"
    }
}