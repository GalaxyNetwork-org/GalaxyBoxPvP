package io.github.lncvrt.lncvrtbox.events;

import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    private final LncvrtBox plugin;

    public SignChangeListener(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onSignPlace(SignChangeEvent event) {
        if (plugin.isMuted(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
