package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent

class CraftItemListener : Listener {
    @EventHandler
    fun onCraftItem(event: CraftItemEvent) {
        if (event.currentItem != null && event.currentItem!!.type == Material.DIAMOND_PICKAXE) {
            val pickaxe = event.currentItem
            val meta = pickaxe!!.itemMeta

            if (meta != null) {
                meta.isUnbreakable = true
                pickaxe.setItemMeta(meta)
                event.currentItem = pickaxe
            }
        }
    }
}