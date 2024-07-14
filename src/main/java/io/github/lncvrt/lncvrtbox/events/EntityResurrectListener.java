package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import static org.bukkit.ChatColor.*;

public class EntityResurrectListener implements Listener {
    private final LncvrtBox plugin;

    public EntityResurrectListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player player && !event.isCancelled()) {
            if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                if (player.getCooldown(Material.TOTEM_OF_UNDYING) != 0) {
                    event.setCancelled(true);
                    return;
                }
                player.setCooldown(Material.TOTEM_OF_UNDYING, 20 * 5);
                plugin.getServer().broadcastMessage("%s%sLncvrtBox %s%s» %s%s has popped their totem".formatted(GOLD, BOLD, GRAY, BOLD, RESET, player.getName()));
            }
        }
    }
}
