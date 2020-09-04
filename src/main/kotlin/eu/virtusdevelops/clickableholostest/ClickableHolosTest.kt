package eu.virtusdevelops.clickableholostest

import eu.virtusdevelops.clickableholostest.commands.*
import eu.virtusdevelops.clickableholostest.hologram.HologramRegistry
import eu.virtusdevelops.clickableholostest.hologram.HologramStorage
import eu.virtusdevelops.clickableholostest.listeners.PlayerJoinEvent
import eu.virtusdevelops.clickableholostest.nms.HoloPacket
import eu.virtusdevelops.clickableholostest.placeholder.NeededPlaceholders
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderManager
import eu.virtusdevelops.virtuscore.VirtusCore
import eu.virtusdevelops.virtuscore.command.CommandManager
import eu.virtusdevelops.virtuscore.managers.FileManager
import eu.virtusdevelops.virtuscore.utils.FileLocation
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class ClickableHolosTest: JavaPlugin() {

    private lateinit var hologramRegistry: HologramRegistry
    private lateinit var hologramStorage: HologramStorage
    private lateinit var commandManager: CommandManager
    private lateinit var fileManager: FileManager

    override fun onEnable() {

        HoloPacket.INSTANCE // just init stuff so it doesnt lag on player join

        val pm = VirtusCore.plugins()
        fileManager = FileManager(this, linkedSetOf<FileLocation>(
            FileLocation.of("holograms.yml", true)
        ))
        fileManager.loadFiles()
        commandManager = CommandManager(this)


        PlaceholderManager.load(this)

        NeededPlaceholders(this)

        this.hologramRegistry = HologramRegistry(this)
        hologramRegistryAPI = this.hologramRegistry

        this.hologramStorage = HologramStorage(this, fileManager, hologramRegistry)

        hologramStorage.loadHolograms() // loads templates
        hologramRegistry.loadHolograms() // loads holograms from templates

        pm.registerEvents(PlayerJoinEvent(this, hologramRegistry), this)


        registerCommands()
    }

    override fun onDisable() {
        super.onDisable()
    }



    fun registerCommands(){
        commandManager.addMainCommand("simpleholograms").addSubCommands(
                CreateCommand(this, fileManager, hologramRegistry ),
                ReloadCommand(fileManager, hologramRegistry, hologramStorage),
                RemoveLineCommand(this, fileManager, hologramRegistry),
                SetLineCommand(this, fileManager, hologramRegistry),
                AddLineCommand(this, fileManager, hologramRegistry),
                MoveHereCommand(this, fileManager, hologramRegistry)
        ).setHeader("${ChatColor.DARK_RED}Coded with love by VirtusDevelops.")
    }

    companion object {
        @JvmStatic lateinit var hologramRegistryAPI: HologramRegistry

    }

}