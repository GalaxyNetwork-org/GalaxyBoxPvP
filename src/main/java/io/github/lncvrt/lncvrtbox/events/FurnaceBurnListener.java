package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

import java.util.Objects;

public class FurnaceBurnListener implements Listener {
    private final LncvrtBox plugin;

    public FurnaceBurnListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        Material smelting = Objects.requireNonNull(furnace.getInventory().getSmelting()).getType();

        if (plugin.isRestrictedMaterial(smelting)) {
            event.setCancelled(true);
        }
    }
}
