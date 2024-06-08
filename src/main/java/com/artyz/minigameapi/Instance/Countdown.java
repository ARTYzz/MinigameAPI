package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.ConfigManager;
import com.artyz.minigameapi.Manager.LobbyManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown  extends BukkitRunnable {

    private JavaPlugin main;
    private Lobby lobby;
    private Game game;
    private int countdownSeconds;

    public Countdown(JavaPlugin main,Game game,Lobby lobby){
        this.main = main;
        this.lobby = lobby;
        this.game = game;
        this.countdownSeconds = LobbyManager.getCountdownSeconds();
    }

    public void start(){
        lobby.setState(GameState.COUNTDOWN);
        runTaskTimer(main,0,20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0){
            cancel();
            lobby.startGame(game);
            return;
        }

        if (countdownSeconds <= 10 || countdownSeconds % 15 == 0){
            lobby.sendMessage(ChatColor.GREEN + "Game will start in " + countdownSeconds + " second" + (countdownSeconds == 1 ? "" : "s") + ".");
        }

        lobby.sendTitle(ChatColor.GREEN.toString() + countdownSeconds + " second" + (countdownSeconds == 1 ? "" : "s"),ChatColor.GRAY + "until game starts");

        countdownSeconds--;
    }
}
