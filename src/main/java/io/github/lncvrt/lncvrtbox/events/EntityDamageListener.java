package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static org.bukkit.ChatColor.*;

public class EntityDamageListener implements Listener {
    private final LncvrtBox plugin;

    public EntityDamageListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (plugin.isAfk(player)) {
                event.setCancelled(true);
                if (event instanceof EntityDamageByEntityEvent damageEvent) {
                    if (damageEvent.getDamager() instanceof Player attacker) {
                        attacker.sendMessage("%s%sLncvrtBox %s%s» %sYou can't attack AFK players!".formatted(GOLD, BOLD, GRAY, BOLD, RESET));
                        player.sendMessage("%s%sLncvrtBox %s%s» %s%s tried to attack you while you were AFK!".formatted(GOLD, BOLD, GRAY, BOLD, RESET, attacker.getName()));
                        if (attacker.getNoDamageTicks() == 0) {
                            attacker.damage(0.0001);
                            attacker.setVelocity(attacker.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().setY(0.5).multiply(0.5));
                        }
                    }
                }
            }
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            double damage = event.getDamage();
            event.setDamage(damage * 0.4);
        }
    }
}
