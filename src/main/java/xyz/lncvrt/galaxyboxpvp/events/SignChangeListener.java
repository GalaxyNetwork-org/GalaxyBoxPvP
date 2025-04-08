package xyz.lncvrt.galaxyboxpvp.events;

import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    private final GalaxyBoxPvP plugin;

    public SignChangeListener(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onSignPlace(SignChangeEvent event) {
        if (plugin.isMuted(event.getPlayer())) event.setCancelled(true);
    }
}
