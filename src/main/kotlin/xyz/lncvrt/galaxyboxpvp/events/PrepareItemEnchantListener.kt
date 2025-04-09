package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.PrepareItemEnchantEvent

class PrepareItemEnchantListener : Listener {
    @EventHandler
    fun onPrepareItemEnchant(event: PrepareItemEnchantEvent) {
        val item = event.item
        if (item.type == Material.DIAMOND_PICKAXE || item.type == Material.NETHERITE_PICKAXE) event.isCancelled = true
    }
}