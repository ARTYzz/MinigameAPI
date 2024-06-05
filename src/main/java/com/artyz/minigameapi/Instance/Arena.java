package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private int id;
    private Location spawn;

    private List<UUID> players;
    private GameState state;
    private Countdown countdown;
    private Game game;
    private JavaPlugin main;

    public Arena(JavaPlugin main,  int id, Location spawn){
        this.main = main;

        this.id = id;
        this.spawn = spawn;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.countdown = new Countdown(main,this);
        this.game = new Game(this);
    }

    /* GAMES */

    public void start(){game.start();}

    public void reset(boolean kickPlayers){
        if (kickPlayers){
            Location loc = LobbyManager.getLobbySpawn();
            for (UUID uuid : players){
                Bukkit.getPlayer(uuid).teleport(loc);
            }
            players.clear();
        }

        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(main,this);
        game = new Game(this);

    }

    /*TOOLS*/

    public void sendMessage(String message){
        for (UUID uuid: players){
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title,String subTitle){
        for (UUID uuid: players){
            Bukkit.getPlayer(uuid).sendTitle(title, subTitle);
        }
    }

    /*PLAYERS*/

    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        player.teleport(spawn);

        if (state.equals(GameState.RECRUITING) && players.size() >= LobbyManager.getRequiredPlayers()){
            countdown.start();
        }

    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(LobbyManager.getLobbySpawn());
    }

    /*INFO*/

    public int getId(){return id;}

    public GameState getState(){return state;}
    public List<UUID> getPlayers(){return players;}

    public void setState(GameState state){this.state = state;}


}
