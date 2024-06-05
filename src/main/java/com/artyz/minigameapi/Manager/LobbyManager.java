package com.artyz.minigameapi.Manager;

import com.artyz.minigameapi.Manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class LobbyManager {
    private final ConfigManager configManager;
    private static Location lobbyLocation;
    private static FileConfiguration config;

    public LobbyManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.config = configManager.getConfig();

        loadLobbyLocation();
    }

    private void loadLobbyLocation() {
        String worldName = config.getString("lobby-spawn.world");
        double x = config.getDouble("lobby-spawn.x");
        double y = config.getDouble("lobby-spawn.y");
        double z = config.getDouble("lobby-spawn.z");
        float yaw = (float) config.getDouble("lobby-spawn.yaw");
        float pitch = (float) config.getDouble("lobby-spawn.pitch");
        lobbyLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
        saveLobbyLocation();
    }

    private void saveLobbyLocation() {
        config.set("lobby-spawn.world", lobbyLocation.getWorld().getName());
        config.set("lobby-spawn.x", lobbyLocation.getX());
        config.set("lobby-spawn.y", lobbyLocation.getY());
        config.set("lobby-spawn.z", lobbyLocation.getZ());
        config.set("lobby-spawn.yaw", lobbyLocation.getYaw());
        config.set("lobby-spawn.pitch", lobbyLocation.getPitch());
        configManager.save();
    }

    public static int getRequiredPlayers(){return config.getInt("required-players");}

    public static int getCountdownSeconds(){return config.getInt("countdown-seconds");}

    public static Location getLobbySpawn(){return lobbyLocation;}

}

