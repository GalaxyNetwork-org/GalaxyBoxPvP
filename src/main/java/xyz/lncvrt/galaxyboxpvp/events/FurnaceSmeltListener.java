package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class FurnaceSmeltListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public FurnaceSmeltListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Material smelted = event.getSource().getType();

        if (plugin.isRestrictedMaterial(smelted)) {
            event.setCancelled(true);
        }
    }
}
