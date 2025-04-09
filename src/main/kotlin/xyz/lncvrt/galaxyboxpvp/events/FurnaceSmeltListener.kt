package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceSmeltEvent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class FurnaceSmeltListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler
    fun onFurnaceSmelt(event: FurnaceSmeltEvent) {
        val smelted = event.source.type
        if (plugin.isRestrictedMaterial(smelted)) event.isCancelled = true
    }
}