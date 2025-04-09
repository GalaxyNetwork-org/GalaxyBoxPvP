package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent

class EntitySpawnListener : Listener {
    @EventHandler
    fun onEntitySpawnEvent(event: EntitySpawnEvent) {
        if (event.entityType == EntityType.WITHER) event.isCancelled = true
    }
}