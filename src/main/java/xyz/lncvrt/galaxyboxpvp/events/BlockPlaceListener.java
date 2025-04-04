package xyz.lncvrt.galaxyboxpvp.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.POLISHED_BLACKSTONE_BUTTON) {
            if (event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasDisplayName()) {
                String displayName = event.getItemInHand().getItemMeta().getDisplayName();
                if (displayName.equalsIgnoreCase("Coal Fragment")) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getBlockPlaced().getType() == Material.FURNACE ||
                event.getBlockPlaced().getType() == Material.FURNACE_MINECART ||
                event.getBlockPlaced().getType() == Material.BLAST_FURNACE) {
            Player player = event.getPlayer();

            Component message = Component.text("[TIP] ", NamedTextColor.GREEN)
                    .decorate(TextDecoration.BOLD)
                    .append(Component.text("If you are trying to smelt iron, gold, etc you can use the Smelter Shop. Click ", NamedTextColor.GREEN))
                    .append(Component.text("[HERE]", NamedTextColor.GREEN)
                            .decorate(TextDecoration.UNDERLINED)
                            .clickEvent(ClickEvent.runCommand("/warp smelter")))
                    .append(Component.text(" to teleport to the smelter shop!", NamedTextColor.GREEN));

            player.sendMessage(message);
        }
    }
}
