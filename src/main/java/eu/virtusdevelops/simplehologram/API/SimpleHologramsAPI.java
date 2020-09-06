package eu.virtusdevelops.simplehologram.API;

import eu.virtusdevelops.simpleholograms.SimpleHolograms;
import eu.virtusdevelops.simpleholograms.hologram.HologramTemplate;
import eu.virtusdevelops.simpleholograms.placeholder.PlaceholderRegistry;
import eu.virtusdevelops.simpleholograms.API.PlaceholderReplacer;
import eu.virtusdevelops.simplehologram.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SimpleHologramsAPI {


    /**
     * Get any hologram by its name, keep in mind if hologram doesn't exist its gonna return null!
     *
     * @param name Name of the hologram you created
     * @return Returns the Hologram.class
     */
    public static Hologram getHologram(String name){
        return SimpleHolograms.getHologramRegistryAPI().getHologram(name);
    }

    /**
     * Get any hologram by its id, , keep in mind if there's no hologram with that id its gonna return null!
     *
     * @param id id of hologram
     * @return returns null or Hologram if exists
     */
    public static Hologram getHologramById(int id){
        return SimpleHolograms.getHologramRegistryAPI().getHologramByID(id);
    }

    /**
     *  Remove any hologram by its name.
     *
     * @param name Hologram name you wish to remove
     */
    public static void removeHologram(String name){
        SimpleHolograms.getHologramRegistryAPI().removeHologram(name);
    }

    /**
     * Create hologram, usefull for any plugin (Crates, informations...)
     * If using this method you need to register viewers using seperate function
     *
     * @param name Name of hologram you're creating
     * @param range How far away can viewers be to see it (in blocks)
     * @param location Where should hologram be
     * @param lines Lines of hologram
     */
    public static void createHologram(String name, int range, Location location, List<String> lines){
        SimpleHolograms.getHologramRegistryAPI().addHologram(new HologramTemplate(lines, name, range, "", location));
    }

    /**
     * Create hologram, usefull for any plugin (Crates, informations...)
     * No need to register viewers in seperate class.
     *
     * @param name Name of hologram you're creating
     * @param range How far away can viewers be to see it (in blocks)
     * @param location Where should hologram be
     * @param lines Lines of hologram
     * @param viewers List of players that can see hologram.
     */
    public static void createHologram(String name, int range, Location location, List<String> lines, List<Player> viewers){
        SimpleHolograms.getHologramRegistryAPI().addHologram(new HologramTemplate(lines, name, range, "", location));
        registerViewers(name, viewers);
    }

    /**
     *  Allow specific players to see specific hologram
     *
     * @param hologramName Hologram you wish to add viewers to
     * @param viewers Viewers (players) to add to be able to see hologram
     */
    public static void registerViewers(String hologramName, List<Player> viewers){
        Hologram hologram = SimpleHolograms.getHologramRegistryAPI().getHologram(hologramName);
        if(hologram != null) {
            for (Player viewer : viewers) {
                hologram.register(viewer);
            }
        }
    }

    /**
     * Allow specific players to see specific hologram
     *
     * @param hologramName Hologram you wish to add viewers to
     * @param viewers Viewers (players) to add to be able to see hologram
     */
    public static void registerViewers(String hologramName, Player...viewers){
        Hologram hologram = SimpleHolograms.getHologramRegistryAPI().getHologram(hologramName);
        if(hologram != null) {
            for (Player viewer : viewers) {
                hologram.register(viewer);
            }
        }
    }

    /**
     * Remove player from specific hologram
     *
     * @param hologramName - Name of hologram to remove from
     * @param viewer - Viewer/Player you wish not to see hologram anymore
     */
    public static void removeViewer(String hologramName, Player viewer){
        SimpleHolograms.getHologramRegistryAPI().unregister(viewer, hologramName);
    }

    /**
     *  Remove player from all holograms.
     *
     * @param viewer - Viewer/Player you wish not to see holograms anymore
     */
    public static void removeViewer(Player viewer){
        SimpleHolograms.getHologramRegistryAPI().unregister(viewer);
    }

    /**
     * Add new placeholders to holograms (they change text upon what you choose them to be)
     *
     * @param plugin Plugin registering the placeholder
     * @param refreshrate How often is placeholder refreshed
     * @param replacer Replacer that replaces text placeholder represents
     * @param placeholder Placeholder text used on holograms
     */
    public static void registerPlaceholder(JavaPlugin plugin, double refreshrate, PlaceholderReplacer replacer, String placeholder){
        PlaceholderRegistry.Companion.registerPlaceholder(plugin, refreshrate, replacer, placeholder);
    }

    /**
     * Remove placeholders.
     *
     * @param plugin Plugin that registered the placeholder
     * @param placeholder Placeholder text used on holograms
     */
    public static void unregisterPlaceholder(JavaPlugin plugin, String placeholder){
        PlaceholderRegistry.Companion.unregister(plugin, placeholder);
    }

}
