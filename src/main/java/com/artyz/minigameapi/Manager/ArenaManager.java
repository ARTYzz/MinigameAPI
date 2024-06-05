package com.artyz.minigameapi.Manager;

import com.artyz.minigameapi.Instance.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();
    private final ConfigManager configManager;
    private Location arenasLocation;
    private static FileConfiguration config;


    public ArenaManager(JavaPlugin plugin,ConfigManager configManager){
        this.configManager = configManager;
        this.config = configManager.getConfig();

        if (config.contains("arenas")) {
            for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
                try {
                    int id = Integer.parseInt(key);
                    arenas.add(new Arena(plugin,id, arenasLocation));
                } catch (NumberFormatException e) {
                    // Ignore non-integer keys
                }
            }
        }

    }

    public void create(Player player) {
        int nextSectionId = getNextSectionId();
        String sectionName = "ID" + nextSectionId;

        String path = "arenas." + sectionName;

        // Directly set the values without creating the section as a string
        config.set(path + ".world", player.getWorld().getName());
        config.set(path + ".x", player.getLocation().getX());
        config.set(path + ".y", player.getLocation().getY());
        config.set(path + ".z", player.getLocation().getZ());
        config.set(path + ".yaw", player.getLocation().getYaw());
        config.set(path + ".pitch", player.getLocation().getPitch());
        configManager.save();
    }

    private int getNextSectionId() {
        int maxId = -1;
        if (config.contains("arenas")) {
            for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
                try {
                    if (key.startsWith("ID")) {
                        int id = Integer.parseInt(key.substring(2)); // Extract the numeric part of the key
                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignore non-integer keys
                }
            }
        }
        return maxId + 1;
    }

    private void loadArenasLocation() {
        for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + key;
            String worldName = config.getString(path + ".world");
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble( path + ".z");
            float yaw = (float) config.getDouble(path + ".yaw");
            float pitch = (float) config.getDouble(path + ".pitch");
            arenasLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }

    }

    public List<Arena> getArenas(){return arenas;}

    public Arena getArenas(Player player){
        for (Arena arena: arenas){
            if (arena.getPlayers().contains(player.getUniqueId())){
                return arena;
            }
        }
        return null;
    }

    public Arena getArenas(int id){
        for (Arena arena: arenas){
            if (arena.getId() == id){
                return arena;
            }
        }
        return null;
    }

//    public void setArenasLocation(Location location) {
//        this.arenasLocation = location;
//        saveArenasLocation();
//    }

//    private void saveArenasLocation() {
//        FileConfiguration config = configManager.getConfig();
//        config.set("arenas.world", arenasLocation.getWorld().getName());
//        config.set("arenas.x", arenasLocation.getX());
//        config.set("arenas.y", arenasLocation.getY());
//        config.set("arenas.z", arenasLocation.getZ());
//        config.set("arenas.yaw", arenasLocation.getYaw());
//        config.set("arenas.pitch", arenasLocation.getPitch());
//        configManager.save();
//    }

}
