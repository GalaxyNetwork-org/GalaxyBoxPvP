package io.github.lncvrt.lncvrtbox.events;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.deathMessage(MiniMessage.miniMessage().deserialize("<b><gradient:#FFAA00:#FFFF55>LncvrtBox</gradient></b> <gray>»</gray> " + event.getDeathMessage()));
    }
}
