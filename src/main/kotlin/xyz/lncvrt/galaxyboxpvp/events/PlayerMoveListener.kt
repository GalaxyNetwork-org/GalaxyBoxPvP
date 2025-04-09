package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener : Listener {
    @EventHandler
    private fun onSignPlace(event: PlayerMoveEvent) {
        val player = event.getPlayer()
        val maxY = player.world.maxHeight
        if (player.location.y >= maxY) {
            val newLocation = player.location.clone()
            newLocation.y = (maxY - 1).toDouble()
            player.teleport(newLocation)
        }
    }
}