package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public BlockBreakListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        plugin.convertInventoryItemsPrep(player, player.getUniqueId());
    }
}
