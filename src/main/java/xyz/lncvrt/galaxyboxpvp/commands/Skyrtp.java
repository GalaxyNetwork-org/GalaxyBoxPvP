package xyz.lncvrt.galaxyboxpvp.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvP;

import java.util.concurrent.ThreadLocalRandom;

public class Skyrtp implements CommandExecutor {
    private final GalaxyBoxPvP plugin;

    public Skyrtp(GalaxyBoxPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (sender instanceof Player player) {
            Long last = plugin.skyRTPDelays.get(player.getUniqueId());
            Long time = System.currentTimeMillis();
            if (last != null && time - last < 30000) {
                Long timeLeft = (30000 - (System.currentTimeMillis() - last)) / 1000;
                player.sendMessage(plugin.miniMessage.deserialize("<red>You can't RTP in the sky world so often, please wait %s second(s)!".formatted(timeLeft)));
                return true;
            }
            World world = plugin.getServer().getWorld("sky");
            if (world == null) {
                player.sendMessage(plugin.miniMessage.deserialize("<red>Failed, please try again!"));
                return true;
            }
            int randomX = ThreadLocalRandom.current().nextInt(100, 15000);
            int randomZ = ThreadLocalRandom.current().nextInt(100, 15000);
            Location location = new Location(world, randomX, 100, randomZ);
            Block block = world.getBlockAt(location);
            if (block.getType() == Material.AIR) {
                block.setType(Material.GRASS_BLOCK);
            }
            player.teleport(location.add(0.5f, 1, 0.5f));
            player.sendMessage(plugin.miniMessage.deserialize("<green>You have been randomly teleported to <u>%s 100 %s<u>".formatted(randomX, randomZ)));
            plugin.skyRTPDelays.put(player.getUniqueId(), time);
        }
        return true;
    }
}
