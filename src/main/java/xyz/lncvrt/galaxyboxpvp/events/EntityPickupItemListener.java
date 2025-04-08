package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItemListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public EntityPickupItemListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) plugin.convertInventoryItemsPrep(player, player.getUniqueId());
    }
}
