package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.ConfigManager;
import com.artyz.minigameapi.Manager.LobbyManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown  extends BukkitRunnable {

    private JavaPlugin main;
    private Arena arena;
    private int countdownSeconds;

    public Countdown(JavaPlugin main,Arena arena){
        this.main = main;
        this.arena = arena;
        this.countdownSeconds = LobbyManager.getCountdownSeconds();
    }

    public void start(){
        arena.setState(GameState.COUNTDOWN);
        runTaskTimer(main,0,20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0){
            cancel();
            arena.start();
            return;
        }

        if (countdownSeconds <= 10 || countdownSeconds % 15 == 0){
            arena.sendMessage(ChatColor.GREEN + "Game will start in " + countdownSeconds + "second" + (countdownSeconds == 1 ? "" : "s") + ".");
        }

        countdownSeconds--;
    }
}
