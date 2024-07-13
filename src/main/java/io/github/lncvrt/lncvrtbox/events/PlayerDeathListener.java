package io.github.lncvrt.lncvrtbox.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static org.bukkit.ChatColor.*;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String suicideMsg = "";
        if (event.getEntity() == event.getEntity().getKiller()) suicideMsg = "%s (suicide)".formatted(RESET);

        event.setDeathMessage("%s%sLncvrtBox %s%s» %s%s%s".formatted(GOLD, BOLD, GRAY, BOLD, WHITE, event.getDeathMessage(), suicideMsg));
    }
}
