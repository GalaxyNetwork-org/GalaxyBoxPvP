package xyz.lncvrt.galaxyboxpvp.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {
    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.WITHER) event.setCancelled(true);
    }
}
