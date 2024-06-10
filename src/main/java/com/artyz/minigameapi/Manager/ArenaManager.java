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
    private static Location arenaLocation;
    private static FileConfiguration config;
    private List<Arena> arenas = new ArrayList<>();
    private Game game;

    public ArenaManager(JavaPlugin plugin, ConfigManager configManager) {
        this.configManager = configManager;
        this.config = configManager.getConfig();
        this.game = game;

        loadArenaLocation();
        if (config.contains("arenas")) {
            for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
                try {;
                    arenas.add(new Arena(plugin,key, arenaLocation));
                } catch (NumberFormatException e) {
                    // Ignore non-integer keys
                }
            }
        }
    }

    public void create(Player player,String size) {
        int nextSectionId = getNextSectionId();
        String sectionName = String.valueOf(nextSectionId);

        String path = "arenas." + sectionName;

        // Directly set the values without creating the section as a string
        if (!config.contains(path)) {
            config.createSection(path);
        }
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
                    int id = Integer.parseInt(key);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning("Invalid arena key found (not an integer): " + key);
                }
            }
        }
        return maxId + 1; // Increment maxId by 1 to get the next available section ID
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
            arenaLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
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

    public static Location getArenaSpawn(){return arenaLocation;}
    public void setGame(Game game) {
        this.game = game;
    }


}

