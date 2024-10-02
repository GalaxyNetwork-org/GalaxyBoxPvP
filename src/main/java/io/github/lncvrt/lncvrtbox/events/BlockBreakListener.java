package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class BlockBreakListener implements Listener {
    private final LncvrtBox plugin;
    private final Random random = new Random();

    public BlockBreakListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        plugin.convertInventoryItemsPrep(player, player.getUniqueId());
        if (event.getBlock().getType() == Material.SPAWNER) {
            int exp = random.nextInt(29) + 15;

            Location blockLocation = event.getBlock().getLocation();
            ExperienceOrb orb = blockLocation.getWorld().spawn(blockLocation, ExperienceOrb.class);
            orb.setExperience(exp);
        }
    }
}
