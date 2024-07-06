package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class FurnaceSmeltListener implements Listener {
    private final LncvrtBox plugin;

    public FurnaceSmeltListener(LncvrtBox plugin) {
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
