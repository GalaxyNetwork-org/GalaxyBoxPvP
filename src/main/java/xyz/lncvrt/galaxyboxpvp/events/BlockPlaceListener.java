package xyz.lncvrt.galaxyboxpvp.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;

public class BlockPlaceListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public BlockPlaceListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.POLISHED_BLACKSTONE_BUTTON) {
            if (event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasDisplayName()) {
                event.setCancelled(true); //temp fix
            }
        } else if (event.getBlockPlaced().getType() == Material.FURNACE || event.getBlockPlaced().getType() == Material.FURNACE_MINECART || event.getBlockPlaced().getType() == Material.BLAST_FURNACE) {
            event.getPlayer().sendMessage(plugin.miniMessage.deserialize("<bold><green>[TIP] </green></bold><green>If you are trying to smelt iron, gold, etc you can use the Smelter Shop. Click <underlined><click:run_command:'/warp smelter'>[HERE]</click></underlined> to teleport to the smelter shop!</green>"));
        }
    }
}
