package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class EntityPickupItemListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemPickup(event: EntityPickupItemEvent) {
        if (event.getEntity() is Player) {
            val player = event.entity as Player
            plugin.convertInventoryItemsPrep(player, player.uniqueId)
        }
    }
}