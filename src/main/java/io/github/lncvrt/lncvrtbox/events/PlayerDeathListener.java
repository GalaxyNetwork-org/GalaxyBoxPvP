package io.github.lncvrt.lncvrtbox.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.RESET;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String suicideMsg = "";
        if (event.getEntity() == event.getEntity().getKiller()) suicideMsg = RESET + " (suicide)";

        event.setDeathMessage("%s%sLncvrtBoxFFA %s%s» %s%s%s".formatted(GOLD, BOLD, GRAY, BOLD, RESET, event.getDeathMessage(), suicideMsg));
        //Bukkit.getServer().broadcastMessage(event.getEntity().getKiller().getName());
    }
}
