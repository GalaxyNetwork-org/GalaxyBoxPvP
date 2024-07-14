package io.github.lncvrt.lncvrtbox.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.EntityType;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.END_CRYSTAL || event.getDamager().getType() == EntityType.TNT_MINECART || event.getDamager().getType() == EntityType.TNT) {
            double damage = event.getDamage();
            event.setDamage(damage * 0.4);
        }
    }
}
