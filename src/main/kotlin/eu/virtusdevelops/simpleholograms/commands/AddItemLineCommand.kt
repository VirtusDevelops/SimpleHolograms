package eu.virtusdevelops.simpleholograms.commands

import com.sun.xml.internal.ws.util.VersionUtil
import eu.virtusdevelops.simpleholograms.SimpleHolograms
import eu.virtusdevelops.simpleholograms.hologram.HologramRegistry
import eu.virtusdevelops.virtuscore.command.AbstractCommand
import eu.virtusdevelops.virtuscore.compatibility.ServerVersion
import eu.virtusdevelops.virtuscore.managers.FileManager
import eu.virtusdevelops.virtuscore.utils.HexUtil
import eu.virtusdevelops.virtuscore.utils.ItemUtils
import eu.virtusdevelops.virtuscore.utils.NMSUtils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AddItemLineCommand(private var plugin: SimpleHolograms, val fileManager: FileManager, private val hologramRegistry: HologramRegistry) :
        AbstractCommand(CommandType.PLAYER_ONLY, "additem") {

    override fun runCommand(sender: CommandSender?, vararg args: String?): ReturnType {
        if(args.isNotEmpty() && sender is Player){
            val hologram = hologramRegistry.getHologram(args[0].toString())
            if(hologram != null && args.isNotEmpty()){
                val configuration = fileManager.getConfiguration("holograms")
                var item: ItemStack
                if(ServerVersion.getServerVersion() == ServerVersion.V1_8){
                    item = sender.inventory.itemInHand
                }else{
                    item = sender.inventory.itemInMainHand
                }
                if(item.type != Material.AIR){
                    val encrypted = ItemUtils.encodeItem(item)
                    hologram.addLine("item:$encrypted")
                    sender.sendMessage("item:$encrypted".substring(5))
                    hologram.refresh()
                    configuration.set("${hologram.name}.lines", hologram.lines.toList())
                    fileManager.saveFile("holograms.yml")
                    return ReturnType.SUCCESS

                }else{
                    sender.sendMessage(HexUtil.colorify("&4Invalid item type: ${item.type}"))
                }
                return ReturnType.FAILURE
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
        return "Add item line to hologram"
    }

    override fun getSyntax(): String {
       return "/simpleholograms additem <hologram:text>"
    }
}