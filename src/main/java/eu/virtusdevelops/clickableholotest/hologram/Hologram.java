package eu.virtusdevelops.clickableholotest.hologram;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import eu.virtusdevelops.clickableholostest.hologram.PlaceholderLine;
import eu.virtusdevelops.clickableholostest.placeholder.Placeholder;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderManager;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry;
import eu.virtusdevelops.clickableholostest.utils.LineUtil;
import eu.virtusdevelops.clickableholotest.packet.AbstractPacket;
import eu.virtusdevelops.clickableholotest.packet.WrapperPlayServerEntityDestroy;
import eu.virtusdevelops.clickableholotest.packet.WrapperPlayServerEntityMetadata;
import eu.virtusdevelops.clickableholotest.packet.WrapperPlayServerSpawnEntity;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class Hologram {
    private String name;
    private List<String> lines;
    private Location location;
    private int distance;
    private int range = 10;
    private Player player;

    private List<Integer> ids = new ArrayList<>();

    private List<PlaceholderLine> placeholderLines = new ArrayList<>();
    //private List<Integer> clickables = new ArrayList<>();

    private boolean isAlive = false;
    private boolean containsPlaceholders = false;


    private Random random = new Random();

    public Hologram(String name, List<String> lines, Location location, int distance, Player player){
        this.name = name;
        this.lines = lines;
        this.location = location;
        this.distance = distance;
        this.player = player;

        for(String line : lines) {
            if (LineUtil.Companion.containsPlaceholders(line)){
                containsPlaceholders = true;
                break;
            }
        }



        construct();
    }

    public Hologram updateRange(int range){
        this.range = range;
        return this;
    }

    public void construct(){
        double y = location.getY();
        List<AbstractPacket> packets = new ArrayList<>();

        for(String line: lines){
            // Constructs line
            int id = random.nextInt();
            WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity();
            packet.setEntityID(id);
            packet.setType(EntityType.ARMOR_STAND);
            packet.setX(location.getX());
            packet.setY(y);
            packet.setZ(location.getZ());
            packet.setUniqueId(UUID.randomUUID());
            y-=0.25;
            packets.add(packet);
            ids.add(id);


            WrapperPlayServerEntityMetadata metaPacket = new WrapperPlayServerEntityMetadata();
            metaPacket.setEntityID(id);
            WrappedDataWatcher dataWatcher = new WrappedDataWatcher(metaPacket.getMetadata());
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);

            if(LineUtil.Companion.containsPlaceholders(line)){
                placeholderLines.add(new PlaceholderLine(line, id));
            }

            Optional<?> opt = Optional
                    .of(WrappedChatComponent
                            .fromChatMessage(HexUtil.colorify(parsePlaceholders(line)))[0].getHandle());
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);

            metaPacket.setMetadata(dataWatcher.getWatchableObjects());
            packets.add(metaPacket);
        }

        for(AbstractPacket packet : packets){
            packet.sendPacket(player);
        }
        isAlive = true;
        packets.clear();

    }

    public String parsePlaceholders(String line){
        line = PlaceholderAPI.setPlaceholders(player, line);

        for(Placeholder placeholder : PlaceholderRegistry.Companion.getPlaceholders()){
            line = line.replace(placeholder.getTextPlaceholder(), placeholder.getCurrentReplacement());
        }

        return line;
    }

    private void update(){
        int line = 0;
        for(int id : ids){
            WrapperPlayServerEntityMetadata metaPacket = new WrapperPlayServerEntityMetadata();
            metaPacket.setEntityID(id);
            WrappedDataWatcher dataWatcher = new WrappedDataWatcher(metaPacket.getMetadata());
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
            Optional<?> opt = Optional
                    .of(WrappedChatComponent
                            .fromChatMessage(HexUtil.colorify(parsePlaceholders(lines.get(line))))[0].getHandle());
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);

            metaPacket.setMetadata(dataWatcher.getWatchableObjects());
            metaPacket.sendPacket(player);
            line++;
        }
    }

    public void tickPlaceholderLines(){
        if(isAlive) {
            for (PlaceholderLine line : placeholderLines) {
                WrapperPlayServerEntityMetadata metaPacket = new WrapperPlayServerEntityMetadata();
                metaPacket.setEntityID(line.getId());
                WrappedDataWatcher dataWatcher = new WrappedDataWatcher(metaPacket.getMetadata());
                dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
                Optional<?> opt = Optional
                        .of(WrappedChatComponent
                                .fromChatMessage(HexUtil.colorify(parsePlaceholders(line.getLine())))[0].getHandle());
                dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
                dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);

                metaPacket.setMetadata(dataWatcher.getWatchableObjects());
                metaPacket.sendPacket(player);

            }
        }
    }


    public void destroy(){
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntityIds(ids.stream().mapToInt(i->i).toArray());
        packet.sendPacket(player);

        ids.clear();
        isAlive = false;
    }

    public boolean tick(){
        if(player == null){
            destroy();
            return false;
        }
        if(player.getWorld() == location.getWorld()){
            double distance = player.getLocation().distanceSquared(location);
            if(distance <= range*range){
                if(isAlive){
                    update();
                }else{
                    construct();
                }
            }else{
                destroy();
            }
        }else{
            isAlive = false;
        }
        return true;
    }


    public boolean containsPlaceholders(){
        return containsPlaceholders;
    }
}
