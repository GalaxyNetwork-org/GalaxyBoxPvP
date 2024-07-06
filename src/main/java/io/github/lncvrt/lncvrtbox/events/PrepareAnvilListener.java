package io.github.lncvrt.lncvrtbox.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareAnvilListener implements Listener {
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);

        if (left != null && (left.getType() == Material.DIAMOND_PICKAXE || left.getType() == Material.NETHERITE_PICKAXE)) {
            event.setResult(null);
        }
        if (right != null && (right.getType() == Material.DIAMOND_PICKAXE || right.getType() == Material.NETHERITE_PICKAXE)) {
            event.setResult(null);
        }
    }
}
