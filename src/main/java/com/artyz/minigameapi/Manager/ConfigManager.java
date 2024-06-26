package com.artyz.minigameapi.Manager;

import com.artyz.minigameapi.MinigameAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JavaPlugin plugin;
    private static File file;
    private static FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupConfig(String name){

        file = new File(plugin.getDataFolder(), name);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    public void setupDefaultConfig() {
        if (!config.contains("required-players")){
            config.set("required-players",2);
            save();
        }


        if (!config.contains("countdown-seconds")){
            config.set("countdown-seconds",5);
            save();
        }

        if (!config.contains("game-spawn.world")){
            config.set("game-spawn.world", "world");
            config.set("game-spawn.x", 0.0);
            config.set("game-spawn.y", 0.0);
            config.set("game-spawn.z", 0.0);
            config.set("game-spawn.yaw", 0.0);
            config.set("game-spawn.pitch", 0.0);
            save();
        }

        if (!config.contains("Lobby.0")){
            config.set("Lobby.0.world", "world");
            config.set("Lobby.0.x", 0.0);
            config.set("Lobby.0.y", 0.0);
            config.set("Lobby.0.z", 0.0);
            config.set("Lobby.0.yaw", 0.0);
            config.set("Lobby.0.pitch", 0.0);
            save();
        }

        if (!config.contains("arenas")){
            config.createSection("arenas.0");
            config.set("arenas.0.world", "world");
            config.set("arenas.0.x", 0.0);
            config.set("arenas.0.y", 0.0);
            config.set("arenas.0.z", 0.0);
            config.set("arenas.0.yaw", 0.0);
            config.set("arenas.0.pitch", 0.0);
            save();
        }
        save();
    }

    public static FileConfiguration getConfig(){
        return config;
    }


    public static void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        if (file != null) {
            config = YamlConfiguration.loadConfiguration(file);
        }
    }

}
