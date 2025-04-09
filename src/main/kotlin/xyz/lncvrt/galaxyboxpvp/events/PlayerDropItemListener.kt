package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class PlayerDropItemListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemDrop(event: PlayerDropItemEvent) {
        plugin.convertInventoryItemsPrep(event.getPlayer(), event.getPlayer().uniqueId)
    }
}