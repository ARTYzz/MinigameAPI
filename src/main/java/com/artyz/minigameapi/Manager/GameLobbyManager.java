package com.artyz.minigameapi.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class GameLobbyManager {

    private final ConfigManager configManager;
    private static Location lobbyLocation;
    private static FileConfiguration config;

    public GameLobbyManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.config = configManager.getConfig();

        loadLobbyLocation();
    }

    private void loadLobbyLocation() {
        String worldName = config.getString("game-spawn.world");
        double x = config.getDouble("game-spawn.x");
        double y = config.getDouble("game-spawn.y");
        double z = config.getDouble("game-spawn.z");
        float yaw = (float) config.getDouble("game-spawn.yaw");
        float pitch = (float) config.getDouble("game-spawn.pitch");
        lobbyLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
        saveLobbyLocation();
    }

    private void saveLobbyLocation() {
        config.set("game-spawn.world", lobbyLocation.getWorld().getName());
        config.set("game-spawn.x", lobbyLocation.getX());
        config.set("game-spawn.y", lobbyLocation.getY());
        config.set("game-spawn.z", lobbyLocation.getZ());
        config.set("game-spawn.yaw", lobbyLocation.getYaw());
        config.set("game-spawn.pitch", lobbyLocation.getPitch());
        configManager.save();
    }

    public static Location getLobbySpawn(){return lobbyLocation;}

}
