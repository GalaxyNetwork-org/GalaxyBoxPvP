package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class BlockBreakListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        plugin.convertInventoryItemsPrep(player, player.uniqueId)
        if (event.getBlock().location.y == 100.0 && event.getBlock().world.name == "world") {
            event.isCancelled = true
        }
    }
}