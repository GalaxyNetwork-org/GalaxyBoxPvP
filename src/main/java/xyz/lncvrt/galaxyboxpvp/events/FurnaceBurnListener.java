package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

import java.util.Objects;

public class FurnaceBurnListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public FurnaceBurnListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        Material smelting = Objects.requireNonNull(furnace.getInventory().getSmelting()).getType();
        if (plugin.isRestrictedMaterial(smelting)) event.setCancelled(true);
    }
}
