package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.LobbyManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private int id;
    private Location spawn;

    private List<UUID> players;
    private GameState state;

    public Arena(int id,Location spawn){
        this.id = id;
        this.spawn = spawn;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        player.teleport(spawn);
    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(LobbyManager.getLobbySpawn());
    }

    public int getId(){return id;}

    public List<UUID> getPlayers(){return players;}

    public GameState getState(){return state;}

}
