package eu.virtusdevelops.simplehologram.hologram;

import eu.virtusdevelops.simpleholograms.SimpleHolograms;
import eu.virtusdevelops.simpleholograms.hologram.DynamicHologramLine;
import eu.virtusdevelops.simpleholograms.hologram.ItemHologramLine;
import eu.virtusdevelops.simpleholograms.hologram.Location;
import eu.virtusdevelops.simpleholograms.hologram.NormalHologramLine;
import eu.virtusdevelops.simpleholograms.hologram.requirements.Requirement;
import eu.virtusdevelops.simpleholograms.nms.HoloPacket;
import eu.virtusdevelops.simpleholograms.utils.LineUtil;
import eu.virtusdevelops.virtuscore.utils.ItemUtils;
import net.minecraft.server.v1_16_R1.ItemLiquidUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Hologram {
    private String name;
    private List<String> lines;
    private Location location;
    private int distance;
    private int range;
    private SimpleHolograms plugin;
    private BukkitTask task;


    private List<Integer> ids = new ArrayList<>();
    private List<DynamicHologramLine> dynamicHologramLines = new ArrayList<>();
    private List<NormalHologramLine> normalHologramLines = new ArrayList<>();
    private List<ItemHologramLine> itemLines = new ArrayList<>();
    private List<Requirement> requirements;
    private Map<Player, Boolean> viewers = new HashMap<>();


    private HoloPacket holoPacket = HoloPacket.Companion.getINSTANCE();
    private Long currentTicks = 0L;



    private final Random random = new Random();

    public Hologram(SimpleHolograms plugin, String name, List<String> lines, Location location, int range,
                    List<Requirement> requirements){
        this.name = name;
        this.lines = lines;
        this.location = location;
        this.distance = range;
        this.plugin = plugin;
        this.range = range;
        this.requirements = requirements;



        double y = 0;
        for(String line: lines){
            int id = 35000 + random.nextInt(2000000000);
            int refresh = LineUtil.containsPlaceholders(line);
            if(refresh != -1){
                dynamicHologramLines.add(new DynamicHologramLine(line, id, refresh, location, y));
            }else if(line.startsWith("item:")){
                itemLines.add(new ItemHologramLine(ItemUtils.decodeItem(line.substring(5)), id, location, y+1.05));
                y = y - 0.2;
            } else{
                normalHologramLines.add(new NormalHologramLine(line, id, location, y));
            }
            ids.add(id);
            y = y - 0.25;
        }
        startTask();
    }

    public void refresh(){

        viewers.forEach((player, aBoolean) -> destroy(player));

        task.cancel();
        ids.clear();
        dynamicHologramLines.clear();
        normalHologramLines.clear();
        itemLines.clear();

        double y = 0;
        for(String line: lines){
            int id = 35000 + random.nextInt(2000000000);
            int refresh = LineUtil.Companion.containsPlaceholders(line);
            if(refresh != -1){
                dynamicHologramLines.add(new DynamicHologramLine(line, id, refresh, location, y));
            }else if(line.startsWith("item:")){
                itemLines.add(new ItemHologramLine(ItemUtils.decodeItem(line.substring(5)), id, location, y+1.05));
                y = y - 0.2;
            } else{
                normalHologramLines.add(new NormalHologramLine(line, id, location, y));
            }
            y = y - 0.25;
            ids.add(id);
        }

        viewers.forEach((player, aBoolean) -> {
            boolean doShow = true;
            for (Requirement req : requirements) {
                if(!req.meetsRequirement(player)) doShow = false;
            }
            if(doShow) construct(player);
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

                Player player = playerBooleanEntry.getKey();
                Boolean isAlive = playerBooleanEntry.getValue();

                boolean doShow = true;


                if (player == null || !player.isOnline()) {
                    viewers.remove(player);
                } else if (player.getWorld().getName().equals(location.getWorld())) {
                    double distance = location.getDistance(player.getLocation());
                    if (distance <= (range * range)) {

//                        if(currentTicks % 5L == 0L) {
                            for (Requirement req : requirements) {
                                if (!req.meetsRequirement(player)){ doShow = false; break;}
                            }
//                        }

                        if (!isAlive && doShow) {
                            construct(player);
                            viewers.replace(player, true);
                        }else if(!doShow){
                            destroy(player);
                            viewers.replace(player, false);
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
        boolean doShow = true;
        for (Requirement req : requirements) {
            if(!req.meetsRequirement(player)) doShow = false;
        }
        if(doShow) {

            for (NormalHologramLine line : normalHologramLines) {
                line.construct(player);
            }
            for (DynamicHologramLine line : dynamicHologramLines) {
                line.construct(player);
            }
            for(ItemHologramLine line: itemLines){
                line.construct(player);
            }
            viewers.put(player, true);
        }

    }




    public void destroy(Player player){
        for(int id : ids){
            holoPacket.destroyEntity(player, id);
        }
        viewers.put(player, false);
    }

    public void forceDestroy(Player player){
        for(int id : ids){
            holoPacket.destroyEntity(player, id);
        }
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
        forceDestroy(player);
    }


    public String getName() {
        return name;
    }

    public void destroyClass(){

        this.task.cancel();
        this.task = null;
        viewers.forEach((player, aBoolean) -> {
            forceDestroy(player);
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

    public List<Integer> getIds() {
        return ids;
    }
}
