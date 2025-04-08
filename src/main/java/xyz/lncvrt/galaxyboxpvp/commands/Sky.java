package xyz.lncvrt.galaxyboxpvp.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Sky implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (sender instanceof Player player) {
            player.performCommand("warp sky");
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You can do <b><u><click:suggest_command:'/skyrtp'>/skyrtp</click></u></b> (click the bold text) to randomly teleport around here!"));
        }
        return true;
    }
}
