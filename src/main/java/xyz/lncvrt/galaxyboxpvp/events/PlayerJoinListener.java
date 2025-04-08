package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static org.bukkit.ChatColor.GREEN;

public class PlayerJoinListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public PlayerJoinListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!plugin.autoCompressStatus.containsKey(playerId)) plugin.autoCompressStatus.put(playerId, false);
        player.sendMessage("%sWelcome to LncvrtBox, %s! This is a PvP arena gamemode that was originally called LncvrtBox, and is almost a year old! We reset the server for GalaxyNetwork and a fresh start".formatted(GREEN, player.getName()));
    }
}
