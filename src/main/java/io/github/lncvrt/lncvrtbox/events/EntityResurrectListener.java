package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

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
                plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize("<b><gradient:#FFAA00:#FFFF55>LncvrtBox</gradient></b> <gray>»</gray> " + player.getName() + " has popped their totem"));
            }
        }
    }
}
