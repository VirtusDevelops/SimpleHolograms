package eu.virtusdevelops.clickableholotest.hologram;

import eu.virtusdevelops.clickableholostest.hologram.PlaceholderLine;
import eu.virtusdevelops.clickableholostest.nms.HoloPacket;
import eu.virtusdevelops.clickableholostest.placeholder.Placeholder;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry;
import eu.virtusdevelops.clickableholostest.utils.LineUtil;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Hologram {
    private String name;
    private List<String> lines;
    private Location location;
    private int distance;
    private int range = 10;

    private List<Integer> ids = new ArrayList<>();

    private List<PlaceholderLine> placeholderLines = new ArrayList<>();

    private Map<Player, Boolean> viewers = new HashMap<>();

    private HoloPacket holoPacket = HoloPacket.Companion.getINSTANCE();
    //private List<ViewerData> viewers = new ArrayList<>();


    private boolean containsPlaceholders = false;


    private Random random = new Random();

    public Hologram(String name, List<String> lines, Location location, int distance){
        this.name = name;
        this.lines = lines;
        this.location = location;
        this.distance = distance;
        for(String line : lines) {
            if (LineUtil.Companion.containsPlaceholders(line)){
                containsPlaceholders = true;
                break;
            }
        }

        for(String line: lines){
            int id = random.nextInt();

            if(LineUtil.Companion.containsPlaceholders(line)){
                placeholderLines.add(new PlaceholderLine(line, id));
            }

            ids.add(id);
        }
        //construct();
    }
    public Hologram updateRange(int range){
        this.range = range;
        return this;
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

    public void register(Player player){
        viewers.put(player, false);
        construct(player);
    }

    public void unregister(Player player){

        viewers.remove(player);
        destroy(player);
    }

    public void tick(){
        for(Player data : viewers.keySet()){
            Player player = data.getPlayer();
            if(!player.isOnline()){
                viewers.remove(player);
            }
            if(player.getWorld() == location.getWorld()){
                double distance = player.getLocation().distanceSquared(location);
                if(distance <= range*range){
                    if(!viewers.get(player)){
                        construct(player);
                        viewers.replace(player, true);
                    }
                }else{
                    if(viewers.get(player)){
                        destroy(player);
                        viewers.replace(player, false);
                    }
                }
            }else{
                if(viewers.get(player)){
                    viewers.replace(player, false);
                }
            }
        }
    }

    public void tickPlaceholderLines(){ // still need to add check how often should each line be updated...
        for(Player data : viewers.keySet()){
            if(viewers.get(data)){
                Player player = data.getPlayer();
                for (PlaceholderLine line : placeholderLines) {
                    holoPacket.updateArmorStandDisplayName(player, line.getId(), HexUtil.colorify(parsePlaceholders(line.getLine(), data)));
                }

            }

        }
    }

    public String getName() {
        return name;
    }

    public boolean containsPlaceholders(){
        return containsPlaceholders;
    }
}
