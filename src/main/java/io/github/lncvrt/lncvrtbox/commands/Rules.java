package io.github.lncvrt.lncvrtbox.commands;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public record Rules(LncvrtBox plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (plugin.serverRules != null && !plugin.serverRules.isEmpty()) {
            sender.sendMessage(translateAlternateColorCodes('&', plugin.serverRules));
        } else {
            sender.sendMessage("%sServer rules are currently unavailable.".formatted(RED));
        }
        return true;
    }
}
