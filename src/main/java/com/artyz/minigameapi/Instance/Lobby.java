package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.ArenaManager;
import com.artyz.minigameapi.Manager.GameLobbyManager;
import com.artyz.minigameapi.Manager.LobbyManager;
import com.artyz.minigameapi.Team.Team;
import com.google.common.collect.TreeMultimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class Lobby {

    private String id;
    private Location spawn;

    private List<UUID> players;
    private HashMap<UUID, Boolean> playerReadyState = new HashMap<>();
    private HashMap<UUID, Team> teams;
    private GameState state;
    private GameMode gameMode;
    private Countdown countdown;
    private JavaPlugin main;
    private Game game;

    public Lobby(JavaPlugin main, String id, Location spawn,Game game){
        this.main = main;

        this.id = id;
        this.spawn = spawn;
        this.game = game;

        this.setGame(game);
        this.state = GameState.RECRUITING;
        this.gameMode = GameMode.TEAM_SELECT;
        this.players = new ArrayList<>();
        this.playerReadyState = new HashMap<>();
        this.teams = new HashMap<>();
        this.countdown = new Countdown(main,game,this);

        main.getLogger().info("Lobby initialized with game: " + game);
    }

    /* LOBBY ACTIONS */

    public void startGame(Game game){
        // Logic to start the game when countdown ends
        // This should transition to an Arena or actual game start
        sendMessage(ChatColor.GREEN + "Game is starting!");
        // You may want to create a new Arena instance and transfer players
        Arena arena = new Arena(main , id , ArenaManager.getArenaSpawn());
        arena.setGame(game);

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                arena.addPlayer(player);
            }
        }
        arena.setGame(game);
        arena.setState(GameState.LIVE);
        arena.start();
        reset(false); // Reset the lobby but do not kick players
    }

    public void setGame(Game game) {
        this.game = game;
        this.countdown = new Countdown(main,game,this);
    }

    public Game getGame() {
        return game;
    }

    public void reset(boolean kickPlayers){
        if (kickPlayers){
            Location loc = GameLobbyManager.getLobbySpawn();
            for (UUID uuid : players){
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    player.teleport(loc);
                }
            }
            players.clear();
            teams.clear();
        }

        sendTitle("", "");
        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(main, game , this);
    }

    /* TOOLS */

    public void sendMessage(String message){
        for (UUID uuid : players){
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public void sendTitle(String title, String subTitle){
        for (UUID uuid: players){
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendTitle(title, subTitle);
            }
        }
    }

    /* PLAYERS */

    public void addPlayer(Player player){
        if (players.contains(player.getUniqueId())) {
            return;
        }

        players.add(player.getUniqueId());
        setReady(player,false);
        player.teleport(spawn);

        player.sendMessage(ChatColor.GRAY + "You have joined the lobby. Initial ready state: " + isReady(player)); // Debug statement
        if (isReady(player) == false){
            player.sendMessage(ChatColor.GRAY + "You are now unready </ready> to ready");
        }

        if (state.equals(GameState.RECRUITING) || state.equals(GameState.COUNTDOWN) && gameMode.equals(GameMode.RANDOM_TEAM)){
            randomTeam(player);
        }

        if (state.equals(GameState.RECRUITING) && players.size() >= LobbyManager.getRequiredPlayers()){
            countdown.start();
        }

    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(GameLobbyManager.getLobbySpawn());
        player.sendTitle("", "");

        removeTeam(player);

        if (state == GameState.COUNTDOWN && players.size() < LobbyManager.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "There are not enough players. Countdown stopped!");
            reset(false);
            return;
        }
    }

    /* INFO */

    public String getId(){return id;}

    public GameState getState(){return state;}

    public GameMode getGameMode(){return gameMode;}

    public List<UUID> getPlayers(){return players;}

    public void setGameMode(GameMode gameMode){this.gameMode = gameMode;}

    public void setState(GameState state){this.state = state;}

    public void setTeam(Player player,Team team){
        removeTeam(player);
        teams.put(player.getUniqueId(),team);
    }

    public void removeTeam(Player player){
        if(teams.containsKey(player.getUniqueId())){
            teams.remove(player.getUniqueId());
        }
    }

    public int getTeamAmount(Team team){
        int amount = 0;
        for (Team t : teams.values()){
            if (t == team){
                amount++;
            }
        }
        return amount;
    }

    public void randomTeam(Player player){
        TreeMultimap<Integer,Team> count = TreeMultimap.create();
        for (Team team : Team.values()){
            count.put(getTeamAmount(team),team);
        }

        Team lowest = (Team) count.values().toArray()[0];
        setTeam(player,lowest);

        player.sendMessage(ChatColor.AQUA + "You have been automatically placed on " + lowest.getDisplay() + ChatColor.AQUA + " team.");
    }

    public Team getTeam(Player player){return teams.get(player.getUniqueId());}

    /* READY STATE METHODS */

    public void setReady(Player player, boolean isReady) {
        playerReadyState.put(player.getUniqueId(), isReady);
    }

    public boolean isReady(Player player) {
        return playerReadyState.getOrDefault(player.getUniqueId(), false);
    }

    public int getReadyPlayerCount() {
        int readyCount = 0;
        for (boolean isReady : playerReadyState.values()) {
            if (isReady) {
                readyCount++;
            }
        }
        return readyCount;
    }

    public int getUnreadyPlayerCount() {
        return players.size() - getReadyPlayerCount();
    }

}
