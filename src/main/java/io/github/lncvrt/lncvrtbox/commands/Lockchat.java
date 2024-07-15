package io.github.lncvrt.lncvrtbox.commands;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public record Lockchat(LncvrtBox plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("chatlock.lock")) {
            plugin.chatLocked = !plugin.chatLocked;
            sender.sendMessage("Chat " + (plugin.chatLocked ? "locked." : "unlocked."));
        } else {
            sender.sendMessage("You do not have permission to use this command.");
        }
        return true;
    }
}
