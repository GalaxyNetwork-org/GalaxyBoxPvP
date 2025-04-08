package xyz.lncvrt.galaxyboxpvp.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    private void onSignPlace(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int maxY = player.getWorld().getMaxHeight();
        if (player.getLocation().getY() >= maxY) {
            Location newLocation = player.getLocation().clone();
            newLocation.setY(maxY - 1);
            player.teleport(newLocation);
        }
    }
}
