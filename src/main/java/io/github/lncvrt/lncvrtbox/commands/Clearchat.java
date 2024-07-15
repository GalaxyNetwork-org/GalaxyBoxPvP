package io.github.lncvrt.lncvrtbox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.ChatColor.RED;

public class Clearchat implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("lncvrtbox.clearchat")) {
                player.sendMessage("%sYou do not have permission to use this command.".formatted(RED));
                return true;
            }
        }

        for (int i = 0; i < 1000; i++) {
            Bukkit.broadcastMessage("");
        }
        return true;
    }
}
