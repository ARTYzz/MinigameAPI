package com.artyz.minigameapi;

import com.artyz.minigameapi.Manager.ArenaManager;
import com.artyz.minigameapi.Manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinigameAPI extends JavaPlugin {

    private static MinigameAPI instance;
    private ArenaManager arenaManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("MinigameAPI enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().warning("MinigameAPI disabled!");
    }

    public static MinigameAPI getInstance() {
        return instance;
    }

}
