package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    private final LncvrtBox plugin;

    public PlayerDropItemListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        plugin.convertInventoryItemsPrep(event.getPlayer(), event.getPlayer().getUniqueId());
    }
}
