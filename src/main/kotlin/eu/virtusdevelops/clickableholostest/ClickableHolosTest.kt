package eu.virtusdevelops.clickableholostest

import eu.virtusdevelops.clickableholostest.commands.CreateCommand
import eu.virtusdevelops.clickableholostest.commands.ReloadCommand
import eu.virtusdevelops.clickableholostest.handlers.HologramRegistry
import eu.virtusdevelops.clickableholostest.hologram.HologramStorage
import eu.virtusdevelops.clickableholostest.listeners.PlayerJoinEvent
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderManager
import eu.virtusdevelops.clickableholotest.example.ExamplePlaceholders
import eu.virtusdevelops.virtuscore.VirtusCore
import eu.virtusdevelops.virtuscore.command.CommandManager
import eu.virtusdevelops.virtuscore.managers.FileManager
import eu.virtusdevelops.virtuscore.utils.FileLocation
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class ClickableHolosTest: JavaPlugin() {

    private lateinit var hologramRegistry: HologramRegistry
    private lateinit var hologramStorage: HologramStorage
    private lateinit var examplePlaceholders: ExamplePlaceholders
    private lateinit var commandManager: CommandManager
    private lateinit var fileManager: FileManager

    override fun onEnable() {

        val pm = VirtusCore.plugins()
        fileManager = FileManager(this, linkedSetOf<FileLocation>(
            FileLocation.of("holograms.yml", true)
        ))
        fileManager.loadFiles()
        commandManager = CommandManager(this)

        PlaceholderManager.load(this)
        examplePlaceholders = ExamplePlaceholders(this)
        this.hologramRegistry = HologramRegistry()

        this.hologramStorage = HologramStorage(this, fileManager, hologramRegistry)
        hologramStorage.loadHolograms()

        pm.registerEvents(PlayerJoinEvent(this, hologramRegistry), this)
        startTask()

        registerCommands()
    }

    override fun onDisable() {
        super.onDisable()
    }

    fun startTask(){
        Bukkit.getScheduler().runTaskTimer(this, Runnable{
            hologramRegistry.update()
        }, 0, 100L)

        /*Bukkit.getScheduler().runTaskTimer(this, Runnable{
            hologramRegistry.updatePlaceholders()
        }, 0, 2L)*/
    }

    fun tick(){
        hologramRegistry.updatePlaceholders();
    }

    fun registerCommands(){
        commandManager.addMainCommand("simpleholograms").addSubCommands(
            CreateCommand(fileManager, hologramRegistry),
            ReloadCommand(fileManager, hologramRegistry, hologramStorage)
        )
    }


    fun Any.retrieveField(name: String): Any
            = this::class.java.getDeclaredField(name).apply { isAccessible = true }.get(this)

    fun Any.updateField(name: String, value: Any)
            = this::class.java.getDeclaredField(name).apply { isAccessible = true }.set(this, value)
}