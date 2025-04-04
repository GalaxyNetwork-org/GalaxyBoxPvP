package xyz.lncvrt.galaxyboxpvp.commands;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public record Autocompress(GalaxyBoxPvP plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(RED + "This command can only be executed by a player.");
            return true;
        }

        UUID playerId = player.getUniqueId();

        boolean currentStatus = plugin.autoCompressStatus.getOrDefault(playerId, false);
        plugin.autoCompressStatus.put(playerId, !currentStatus);

        if (!currentStatus) {
            player.sendMessage(GREEN + "Auto-compress enabled.");
        } else {
            player.sendMessage(RED + "Auto-compress disabled.");
        }
        return true;
    }
}
