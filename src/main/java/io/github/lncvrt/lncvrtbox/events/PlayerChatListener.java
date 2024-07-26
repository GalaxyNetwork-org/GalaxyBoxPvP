package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Base64;

import static org.bukkit.ChatColor.*;

public class PlayerChatListener implements Listener {
    private final LncvrtBox plugin;

    public PlayerChatListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().replaceAll("[^a-zA-Z0-9\\p{Punct}\\s]", "").replaceAll("\\s+", " ").trim();
        Player player = event.getPlayer();

        String[] slurs = {"bmlnZ2Vy", "bmlnZ2E="};

        event.setMessage(message);

        if (plugin.chatLocked && !player.hasPermission("chatlock.bypass")) {
            event.setCancelled(true);
            player.sendMessage("Chat is currently locked. Admins can still view your message.");
            for (Player admin : Bukkit.getServer().getOnlinePlayers()) {
                if (admin.hasPermission("chatlock.admin")) {
                    admin.sendMessage("%s%s%s tried to say: %s".formatted(GRAY, ITALIC, player.getName(), event.getMessage()));
                }
            }
        }

        for (String slur : slurs) {
            if (message.toLowerCase().contains(new String(Base64.getDecoder().decode(slur)))) {
                player.chat("Oops! I'm now banned because I said a slur.");

                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tempbanip %s 1w Slurs".formatted(player.getName()));
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tempban %s 1w Slurs".formatted(player.getName()));
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mute %s 1mo Slurs".formatted(player.getName()));

                event.setCancelled(true);
                break;
            }
        }

        if (message.toLowerCase().contains("minehut.gg")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempbanip %s 1m Advertising".formatted(player.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban %s 1m Advertising".formatted(player.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute %s 3m Advertising".formatted(player.getName()));
            event.setCancelled(true);
        }
    }
}
