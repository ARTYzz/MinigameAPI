package com.artyz.minigameapi.Manager;

import com.artyz.minigameapi.Instance.Arena;
import com.artyz.minigameapi.Instance.Game;
import com.artyz.minigameapi.Instance.Lobby;
import com.artyz.minigameapi.Manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArenaManager {
    private JavaPlugin plugin;
    private final ConfigManager configManager;
    private static Location lobbyLocation;
    private static FileConfiguration config;
    private List<Arena> arenas = new ArrayList<>();

    public ArenaManager(JavaPlugin plugin, ConfigManager configManager) {
        this.configManager = configManager;
        this.config = configManager.getConfig();

        loadArenaLocation();
        if (config.contains("arenas")) {
            for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
                try {;
                    arenas.add(new Arena(plugin,key, lobbyLocation,null));
                } catch (NumberFormatException e) {
                    // Ignore non-integer keys
                }
            }
        }
    }

    public void setArenaLocation(Location location) {
        this.lobbyLocation = location;
        saveArenaLocation();
    }

    private void saveArenaLocation() {
        config.set("arenas.0.world", lobbyLocation.getWorld().getName());
        config.set("arenas.0.x", lobbyLocation.getX());
        config.set("arenas.0.y", lobbyLocation.getY());
        config.set("arenas.0.z", lobbyLocation.getZ());
        config.set("arenas.0.yaw", lobbyLocation.getYaw());
        config.set("arenas.0.pitch", lobbyLocation.getPitch());
        configManager.save();
    }

    private void loadArenaLocation() {
        for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + key;

            String worldName = config.getString(path + ".world");
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble( path + ".z");
            float yaw = (float) config.getDouble(path + ".yaw");
            float pitch = (float) config.getDouble(path + ".pitch");
            lobbyLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }

    }

    public List<Arena> getArenas(){return arenas;}

    public Arena getArenas(Player player){
        for (Arena arena : arenas){
            if (arena.getPlayers().contains(player.getUniqueId())){
                return arena;
            }
        }
        return null;
    }

    public Arena getArenas(String id){
        for (Arena arena : arenas){
            if (arena.getId().equals(id)){
                return arena;
            }
        }
        return null;
    }

    public static Location getArenaSpawn(){return lobbyLocation;}

}

