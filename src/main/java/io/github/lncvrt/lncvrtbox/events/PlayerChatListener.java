package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Base64;

import static org.bukkit.ChatColor.*;

public class PlayerChatListener implements Listener {
    private final LncvrtBox plugin;

    public PlayerChatListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setMessage(event.getMessage().replace("wlc", "welcome").replace("linqverted", "Lncvrt").replace("linqvert", "Lncvrt"));

        if (plugin.chatLocked && !event.getPlayer().hasPermission("chatlock.bypass")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Chat is currently locked. Admins can still view your message.");
            for (Player admin : Bukkit.getServer().getOnlinePlayers()) {
                if (admin.hasPermission("chatlock.admin")) {
                    admin.sendMessage("%s%s%s tried to say: %s".formatted(GRAY, ITALIC, event.getPlayer().getName(), event.getMessage()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage().toLowerCase();
        Player player = event.getPlayer();

        String[] slurs = {"bmlnZ2Vy", "bmlnZ2E="};

        for (String slur : slurs) {
            if (message.contains(new String(Base64.getDecoder().decode(slur)))) {
                player.chat("Oops! I'm now banned because I said a slur.");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempbanip %s 1w Slurs".formatted(player.getName()));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban %s 1w Slurs".formatted(player.getName()));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute %s 1mo Slurs".formatted(player.getName()));

                event.setCancelled(true);
                break;
            }
        }

        if (message.contains("chillindabox") || (message.contains("minehut.gg") && !(message.contains("lncvrtboxffa.minehut.gg") || message.contains("lncvrtboxes.minehut.gg")))) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempbanip %s 1m Advertising".formatted(player.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban %s 1m Advertising".formatted(player.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute %s 3m Advertising".formatted(player.getName()));
            event.setCancelled(true);
        }
    }
}
