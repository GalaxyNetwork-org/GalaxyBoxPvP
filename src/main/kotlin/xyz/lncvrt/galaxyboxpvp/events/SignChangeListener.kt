package xyz.lncvrt.galaxyboxpvp.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

class SignChangeListener(private val plugin: GalaxyBoxPvPPlugin) : Listener {
    @EventHandler
    private fun onSignPlace(event: SignChangeEvent) {
        if (plugin.isMuted(event.player)) event.isCancelled = true
    }
}