package com.artyz.minigameapi.Instance;

import org.bukkit.ChatColor;

import java.util.UUID;

public class Game {

    private Arena arena;

    public Game(Arena arena){
        this.arena = arena;
    }

    public void start(){
        arena.setState(GameState.LIVE);
        arena.sendMessage(ChatColor.GREEN + "GAME STARTED!!!");
    }

}
