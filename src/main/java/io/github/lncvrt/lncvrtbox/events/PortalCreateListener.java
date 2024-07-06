package io.github.lncvrt.lncvrtbox.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Objects;

public class PortalCreateListener implements Listener {
    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        World.Environment env = event.getWorld().getEnvironment();
        if (env == World.Environment.NORMAL && event.getReason() != PortalCreateEvent.CreateReason.FIRE) {
            event.setCancelled(true);

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (Objects.equals(event.getEntity(), player)) {
                    Location spawnLocation = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getSpawnLocation();
                    player.teleport(spawnLocation);
                    break;
                }
            }
        }
    }
}
