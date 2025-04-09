package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.sayandev.stickynote.bukkit.extension.sendComponent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class PlayerJoinListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.getPlayer()
        val playerId = player.uniqueId

        if (!plugin.autoCompressStatus.containsKey(playerId)) plugin.autoCompressStatus.put(playerId, false)
        player.sendComponent("<green>Welcome to LncvrtBox, ${player.name}! This is a PvP arena gamemode that was originally called LncvrtBox, and is almost a year old! We reset the server for GalaxyNetwork and a fresh start</green>")
    }
}