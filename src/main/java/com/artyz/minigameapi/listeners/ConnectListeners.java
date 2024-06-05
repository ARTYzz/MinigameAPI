package com.artyz.minigameapi.listeners;

import com.artyz.minigameapi.Instance.Arena;
import com.artyz.minigameapi.Manager.ArenaManager;
import com.artyz.minigameapi.Manager.LobbyManager;
import com.artyz.minigameapi.MinigameAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListeners implements Listener {

    private MinigameAPI minigameAPI;

    public ConnectListeners(MinigameAPI minigameAPI){
        this.minigameAPI = minigameAPI;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        e.getPlayer().teleport(LobbyManager.getLobbySpawn());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Arena arena = minigameAPI.getArenaManager().getArena(e.getPlayer());
        if (arena != null){
            arena.removePlayer(e.getPlayer());
        }
    }

}
