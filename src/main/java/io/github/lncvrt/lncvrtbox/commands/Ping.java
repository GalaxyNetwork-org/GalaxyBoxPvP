package io.github.lncvrt.lncvrtbox.commands;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.ChatColor.*;

public class Ping implements CommandExecutor {
    private final LncvrtBox plugin;

    public Ping(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            String targetName = (args.length > 0 && args[0] != null && !args[0].isEmpty() && !args[0].equals("!")) ? args[0].replaceAll("\\.[^a-zA-Z0-9_]", "").substring(0, Math.min(args[0].replaceAll("[^a-zA-Z0-9_]", "").length(), 16)) : player.getName();
            Player target = player;

            if (!targetName.equalsIgnoreCase(player.getName())) {
                target = plugin.getServer().getPlayer(targetName);
                if (target == null) {
                    player.sendMessage(String.format("%s%s%s is not online!", RED, targetName, GRAY));
                    return true;
                }
            }

            targetName = target.getName();
            int ping = target.getPing();
            ChatColor color;

            if (ping < 79) {
                color = GREEN;
            } else if (ping < 150) {
                color = YELLOW;
            } else {
                color = RED;
            }

            player.sendMessage(String.format("%s%s%s's ping is: %s%s%sms", RED, targetName, GRAY, color, ping, GRAY));
        }
        return true;
    }
}
