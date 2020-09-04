package eu.virtusdevelops.clickableholotest.hologram;

import eu.virtusdevelops.clickableholostest.ClickableHolosTest;
import eu.virtusdevelops.clickableholostest.hologram.DynamicHologramLine;
import eu.virtusdevelops.clickableholostest.nms.HoloPacket;
import eu.virtusdevelops.clickableholostest.placeholder.Placeholder;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry;
import eu.virtusdevelops.clickableholostest.utils.LineUtil;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Hologram {
    private String name;
    private List<String> lines;
    private Location location;
    private int distance;
    private int range = 10;
    private ClickableHolosTest plugin;
    private BukkitTask task;


    private List<Integer> ids = new ArrayList<>();
    private List<DynamicHologramLine> dynamicHologramLines = new ArrayList<>();
    private Map<Player, Boolean> viewers = new HashMap<>();

    private HoloPacket holoPacket = HoloPacket.Companion.getINSTANCE();
    private Long currentTicks = 0L;



    private Random random = new Random();

    public Hologram(ClickableHolosTest plugin, String name, List<String> lines, Location location, int range){
        this.name = name;
        this.lines = lines;
        this.location = location;
        this.distance = range;
        this.plugin = plugin;


        for(String line: lines){
            int id = random.nextInt();
            int refresh = LineUtil.Companion.containsPlaceholders(line);
            if(refresh != -1){
                dynamicHologramLines.add(new DynamicHologramLine(line, id, refresh));
            }
            ids.add(id);
        }
        startTask();
    }

    public void refresh(){

        viewers.forEach((player, aBoolean) -> {
            destroy(player);
        });

        task.cancel();
        ids.clear();
        dynamicHologramLines.clear();

        for(String line: lines){
            int id = random.nextInt();
            int refresh = LineUtil.Companion.containsPlaceholders(line);
            if(refresh != -1){
                dynamicHologramLines.add(new DynamicHologramLine(line, id, refresh));
            }
            ids.add(id);
        }

        viewers.forEach((player, aBoolean) -> {
            construct(player);
        });

        startTask();

    }


    public Hologram updateRange(int range){
        this.range = range;
        return this;
    }


    public void startTask(){

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {


            for (Map.Entry<Player, Boolean> playerBooleanEntry : viewers.entrySet()) {

                Map.Entry pair = playerBooleanEntry;
                Player player = (Player) pair.getKey();
                Boolean isAlive = (Boolean) pair.getValue();

                if (player == null || !player.isOnline()) {
                    viewers.remove(player);
                } else if (player.getWorld() == location.getWorld()) {
                    double distance = player.getLocation().distanceSquared(location);
                    if (distance <= range * range) {
                        if (!isAlive) {
                            construct(player);
                            viewers.replace(player, true);
                        }
                    } else {
                        if (isAlive) {
                            destroy(player);
                            viewers.replace(player, false);
                        }
                    }
                } else if (isAlive) {
                    destroy(player);
                    viewers.replace(player, false);
                }
            }
            for(DynamicHologramLine line : dynamicHologramLines){
                line.update(viewers, currentTicks);
            }

            currentTicks++;
        }, 10L, 2L);
    }





    public void construct(Player player){
        double y = location.getY();
        int x = 0;
        for(int id : ids){
            Location newLoc = location.clone();
            newLoc.setY(y);
            String line = lines.get(x);

            holoPacket.spawnArmorStand(player, id, UUID.randomUUID(), newLoc);
            holoPacket.updateArmorStandDisplayName(player, id, HexUtil.colorify(parsePlaceholders(line, player)));
            x++;
            y-=0.25;
        }
        viewers.put(player, true);

    }

    public String parsePlaceholders(String line, Player player){
        line = PlaceholderAPI.setPlaceholders(player, line);

        for(Placeholder placeholder : PlaceholderRegistry.Companion.getPlaceholders()){
            line = line.replace(placeholder.getTextPlaceholder(), placeholder.getCurrentReplacement());
        }

        return line;
    }



    public void destroy(Player player){
        for(int id : ids){
            holoPacket.destroyEntity(player, id);
        }
        viewers.put(player, false);
    }

    public void destroyLine(Player player, int lineId){
        holoPacket.destroyEntity(player, lineId);
    }

    public void register(Player player){
        viewers.put(player, false);
        construct(player);
    }

    public void unregister(Player player){

        viewers.remove(player);
        destroy(player);
    }


    public String getName() {
        return name;
    }

    public void destroyClass(){

        this.task.cancel();
        this.task = null;
        viewers.forEach((player, aBoolean) -> {
            destroy(player);
        });
        viewers.clear();
        holoPacket = null;
        dynamicHologramLines.clear();

    }

    public List<String> getLines(){
        return lines;
    }

    public Location getLocation() {
        return location;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void setLine(String line, int position){
        task.cancel();
        lines.set(position, line);
        startTask();
    }

    public void addLine(String line){
        lines.add(line);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void removeLine(int position){
        task.cancel();
        lines.remove(position);
        viewers.forEach((player, aBoolean) -> {
            destroyLine(player, ids.get(position));
        });
        startTask();
    }
}
