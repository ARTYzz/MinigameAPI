package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.GameLobbyManager;
import com.artyz.minigameapi.Manager.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private String id;
    private Location spawn;

    private List<UUID> players;
    private GameState state;
    private Countdown countdown;
    private Game game;
    private JavaPlugin main;

    public Arena(JavaPlugin main, String id, Location spawn){
        this.main = main;

        this.id = id;
        this.spawn = spawn;
        this.game = game;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();

    }

    /* GAMES */

    public void start() {
        if (game != null) {
            game.start();
        } else {
            sendMessage(ChatColor.RED + "No game instance set!");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void reset(boolean kickPlayers){
        if (kickPlayers){
            Location loc = GameLobbyManager.getLobbySpawn();
            for (UUID uuid : players){
                Bukkit.getPlayer(uuid).teleport(loc);
            }
            players.clear();
        }

        sendTitle("","");
        state = GameState.RECRUITING;
        countdown.cancel();
        game.reset();

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

    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(GameLobbyManager.getLobbySpawn());
        player.sendTitle("","");

        if (state == GameState.COUNTDOWN && players.size() < LobbyManager.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "There is not enough player. Countdown stopped!");
            reset(false);
            return;
        }

        if (state == GameState.LIVE && players.size() < LobbyManager.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "The Game has ended as too many players have left");
            reset(true);
        }

    }

    /*INFO*/

    public String getId(){return id;}

    public GameState getState(){return state;}
    public List<UUID> getPlayers(){return players;}

    public void setState(GameState state){this.state = state;}


}
