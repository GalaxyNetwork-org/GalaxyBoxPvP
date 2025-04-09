package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.Material
import org.bukkit.block.Furnace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceBurnEvent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class FurnaceBurnListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler
    fun onFurnaceBurn(event: FurnaceBurnEvent) {
        val furnace: Furnace = event.getBlock().state as Furnace
        val smelting: Material? = furnace.inventory.smelting?.type
        if (plugin.isRestrictedMaterial(smelting)) event.isCancelled = true
    }
}