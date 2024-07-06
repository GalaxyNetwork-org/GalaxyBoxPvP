package io.github.lncvrt.lncvrtbox.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static org.bukkit.ChatColor.RED;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().isGliding()) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(event.getPlayer().getWorld()));
            if (regionManager != null) {
                BlockVector3 location = BlockVector3.at(event.getPlayer().getLocation().getX(), event.getPlayer().getLocation().getY(), event.getPlayer().getLocation().getZ());
                ApplicableRegionSet applicableRegions = regionManager.getApplicableRegions(location);
                for (ProtectedRegion region : applicableRegions) {
                    if (region.getId().equalsIgnoreCase("event")) {
                        event.setCancelled(true);
                        event.getPlayer().setGliding(false);
                        event.getPlayer().sendMessage(RED + "Elytras are disabled in events.");
                        break;
                    }
                }
            }
        }
    }
}
