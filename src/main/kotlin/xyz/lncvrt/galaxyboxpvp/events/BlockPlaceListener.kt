package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.sayandev.stickynote.bukkit.extension.sendComponent

class BlockPlaceListener() : Listener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.blockPlaced.type == Material.POLISHED_BLACKSTONE_BUTTON) {
            if (event.getItemInHand().hasItemMeta() && event.getItemInHand().itemMeta.hasDisplayName()) {
                event.isCancelled = true //temp fix
            }
        } else if (event.blockPlaced.type == Material.FURNACE || event.blockPlaced.type == Material.FURNACE_MINECART || event.blockPlaced.type == Material.BLAST_FURNACE) {
            event.getPlayer().sendComponent("<bold><green>[TIP] </green></bold><green>If you are trying to smelt iron, gold, etc you can use the Smelter Shop. Click <underlined><click:run_command:'/warp smelter'>[HERE]</click></underlined> to teleport to the smelter shop!</green>")
        }
    }
}