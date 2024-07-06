package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class PlayerJoinListener implements Listener {
    private final LncvrtBox plugin;

    public PlayerJoinListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!plugin.autoCompressStatus.containsKey(playerId)) {
            plugin.autoCompressStatus.put(playerId, false);
        }

        player.getInventory().remove(Material.WITHER_SKELETON_SKULL);

        player.sendMessage("%s%sWe added %s%s%s/autocompress%s%s%s! Use that to compress diamonds into diamond blocks and more!".formatted(AQUA, BOLD, RESET, AQUA, UNDERLINE, RESET, AQUA, BOLD));
        player.sendMessage("%s%sYou can also do %s%s%s/settag%s%s%s to customize your suffix/tag (issues related to that have been fixed)".formatted(GREEN, BOLD, RESET, GREEN, UNDERLINE, RESET, GREEN, BOLD));
        player.sendMessage("%s%sAlso make sure to join the Discord by doing %s%s%s/discord%s%s%s!".formatted(BLUE, BOLD, RESET, BLUE, UNDERLINE, RESET, BLUE, BOLD));
    }
}
