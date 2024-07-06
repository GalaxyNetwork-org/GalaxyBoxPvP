package io.github.lncvrt.lncvrtbox.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftItemListener implements Listener {
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.DIAMOND_PICKAXE) {
            ItemStack pickaxe = event.getCurrentItem();
            ItemMeta meta = pickaxe.getItemMeta();

            if (meta != null) {
                meta.setUnbreakable(true);
                pickaxe.setItemMeta(meta);
                event.setCurrentItem(pickaxe);
            }
        }
    }
}
