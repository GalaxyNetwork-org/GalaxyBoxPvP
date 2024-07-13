package io.github.lncvrt.lncvrtbox.events;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static org.bukkit.ChatColor.*;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.POLISHED_BLACKSTONE_BUTTON) {
            if (event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasDisplayName()) {
                String displayName = ChatColor.stripColor(event.getItemInHand().getItemMeta().getDisplayName());
                if (displayName.equalsIgnoreCase("Coal Fragment")) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getBlockPlaced().getType() == Material.FURNACE || event.getBlockPlaced().getType() == Material.FURNACE_MINECART || event.getBlockPlaced().getType() == Material.BLAST_FURNACE) {
            Player player = event.getPlayer();
            String message = String.format("%s%s[TIP]%s%s If you are trying to smelt iron, gold, etc you can use the Smelter Shop. Click %s[HERE]%s to teleport to the smelter shop! (if you are on bedrock, instead of clicking there run %s/warp smelter%s.", GREEN, BOLD, RESET, GREEN, UNDERLINE, GREEN, UNDERLINE, GREEN);

            TextComponent fullMessage;
            int hereIndex = message.indexOf("[HERE]");

            TextComponent clickableHere = new TextComponent("[HERE]");
            clickableHere.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            clickableHere.setUnderlined(true);
            clickableHere.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp smelter"));

            TextComponent[] components = new TextComponent[] {
                    new TextComponent(message.substring(0, hereIndex)),
                    clickableHere,
                    new TextComponent(message.substring(hereIndex + "[HERE]".length()))
            };
            fullMessage = new TextComponent(components);

            player.spigot().sendMessage(fullMessage);
        }
    }
}
