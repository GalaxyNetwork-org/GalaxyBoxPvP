package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent

class PrepareAnvilListener : Listener {
    @EventHandler
    fun onPrepareAnvil(event: PrepareAnvilEvent) {
        val left = event.inventory.getItem(0)
        val right = event.inventory.getItem(1)

        if (left != null && (left.type == Material.DIAMOND_PICKAXE || left.type == Material.NETHERITE_PICKAXE)) {
            event.result = null
        }
        if (right != null && (right.type == Material.DIAMOND_PICKAXE || right.type == Material.NETHERITE_PICKAXE)) {
            event.result = null
        }
    }
}