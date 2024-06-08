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

public class LobbyManager {
    private JavaPlugin plugin;
    private final ConfigManager configManager;
    private static Location lobbyLocation;
    private static FileConfiguration config;
    private List<Lobby> lobbys = new ArrayList<>();
    private Game game;

    public LobbyManager(JavaPlugin plugin, ConfigManager configManager,Game game) {
        this.configManager = configManager;
        this.config = configManager.getConfig();
        this.game = game;

        loadLobbyLocation();
        if (config.contains("Lobby")) {
            for (String key : config.getConfigurationSection("Lobby").getKeys(false)) {
                try {;
                    lobbys.add(new Lobby(plugin,key, lobbyLocation,game));
                } catch (NumberFormatException e) {
                    // Ignore non-integer keys
                }
            }
        }
    }

    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
        saveLobbyLocation();
    }

    private void saveLobbyLocation() {
        config.set("Lobby.0.world", lobbyLocation.getWorld().getName());
        config.set("Lobby.0.x", lobbyLocation.getX());
        config.set("Lobby.0.y", lobbyLocation.getY());
        config.set("Lobby.0.z", lobbyLocation.getZ());
        config.set("Lobby.0.yaw", lobbyLocation.getYaw());
        config.set("Lobby.0.pitch", lobbyLocation.getPitch());
        configManager.save();
    }

    private void loadLobbyLocation() {
        for (String key : config.getConfigurationSection("Lobby").getKeys(false)) {
            String path = "Lobby." + key;

            String worldName = config.getString(path + ".world");
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble( path + ".z");
            float yaw = (float) config.getDouble(path + ".yaw");
            float pitch = (float) config.getDouble(path + ".pitch");
            lobbyLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }

    }

    public List<Lobby> getLobbys(){return lobbys;}

    public Lobby getLobbys(Player player){
        for (Lobby lobby : lobbys){
            if (lobby.getPlayers().contains(player.getUniqueId())){
                return lobby;
            }
        }
        return null;
    }

    public Lobby getLobby(String id){
        for (Lobby lobby : lobbys){
            if (lobby.getId().equals(id)){
                return lobby;
            }
        }
        return null;
    }

    public Lobby getLobbys(String id, Game game){
        for (Lobby lobby : lobbys){
            if (lobby.getId().equals(id)){
                lobby.setGame(game);
                return lobby;
            }
        }
        return null;
    }


    public static int getRequiredPlayers(){return config.getInt("required-players");}

    public static int getCountdownSeconds(){return config.getInt("countdown-seconds");}

    public static Location getLobbySpawn(){return lobbyLocation;}

}

