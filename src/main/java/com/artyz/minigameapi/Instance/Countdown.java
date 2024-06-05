package com.artyz.minigameapi.Instance;

import com.artyz.minigameapi.Manager.ConfigManager;
import com.artyz.minigameapi.Manager.LobbyManager;
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

    }

}
