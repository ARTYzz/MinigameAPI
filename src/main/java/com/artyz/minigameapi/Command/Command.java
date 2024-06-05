package com.artyz.minigameapi.Command;

import com.artyz.minigameapi.MinigameAPI;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public abstract class Command implements CommandExecutor {
    protected JavaPlugin main;

    public Command(JavaPlugin main, String name) {
        this.main = main;

        PluginCommand pluginCommand = main.getCommand(name);
        pluginCommand.setExecutor(this);
    }

    public abstract void execute(Player player, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        if (sender instanceof Player){
            execute((Player) sender, args);
        }

        return true;
    }
}
