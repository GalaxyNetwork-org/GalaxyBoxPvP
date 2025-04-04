package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public PlayerDropItemListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        plugin.convertInventoryItemsPrep(event.getPlayer(), event.getPlayer().getUniqueId());
    }
}
