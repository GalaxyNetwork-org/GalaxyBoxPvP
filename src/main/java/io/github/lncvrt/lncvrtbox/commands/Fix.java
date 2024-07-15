package io.github.lncvrt.lncvrtbox.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import io.github.lncvrt.lncvrtbox.LncvrtBox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Fix implements CommandExecutor {
    private final LncvrtBox plugin;

    public Fix(LncvrtBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //TODO: make selectionwall4 (front wall)
        if (plugin.fixRanTooOften) {
            sender.sendMessage("/fix was ran within the past 5 seconds!");
            return true;
        }
        plugin.fixRanTooOften = true;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.fixRanTooOften = false, 20 * 5);

        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld("world")));
        CuboidRegion selectionground1 = new CuboidRegion(world, BlockVector3.at(131, 100, 105), BlockVector3.at(-67, 100, -123));
        CuboidRegion selectionwall1 = new CuboidRegion(world, BlockVector3.at(-66, 139, 105), BlockVector3.at(130, 101, 105));
        CuboidRegion selectionwall2 = new CuboidRegion(world, BlockVector3.at(-67, 139, 104), BlockVector3.at(-67, 101, -122));
        CuboidRegion selectionwall3 = new CuboidRegion(world, BlockVector3.at(-67, 139, -123), BlockVector3.at(130, 101, -123));

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            RandomPattern pat = new RandomPattern();
            BlockState deepslate_tiles = BukkitAdapter.adapt(Material.DEEPSLATE_TILES.createBlockData());
            BlockState deepslate_bricks = BukkitAdapter.adapt(Material.DEEPSLATE_BRICKS.createBlockData());
            pat.add(deepslate_tiles, 0.5);
            pat.add(deepslate_bricks, 0.5);

            editSession.setBlocks(selectionground1, pat);
        } catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            RandomPattern pat = new RandomPattern();
            BlockState gold_block = BukkitAdapter.adapt(Material.GOLD_BLOCK.createBlockData());
            BlockState raw_gold_block = BukkitAdapter.adapt(Material.RAW_GOLD_BLOCK.createBlockData());
            BlockState yellow_concrete = BukkitAdapter.adapt(Material.YELLOW_CONCRETE.createBlockData());
            pat.add(gold_block, 0.33333333333);
            pat.add(raw_gold_block, 0.33333333333);
            pat.add(yellow_concrete, 0.33333333333);

            editSession.setBlocks(selectionwall1, pat);
        } catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            RandomPattern pat = new RandomPattern();
            BlockState lapis_block = BukkitAdapter.adapt(Material.LAPIS_BLOCK.createBlockData());
            BlockState blue_concrete_powder = BukkitAdapter.adapt(Material.BLUE_CONCRETE_POWDER.createBlockData());
            BlockState blue_concrete = BukkitAdapter.adapt(Material.BLUE_CONCRETE.createBlockData());
            pat.add(lapis_block, 0.33333333333);
            pat.add(blue_concrete_powder, 0.33333333333);
            pat.add(blue_concrete, 0.33333333333);

            editSession.setBlocks(selectionwall2, pat);
        } catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            RandomPattern pat = new RandomPattern();
            BlockState lime_concrete = BukkitAdapter.adapt(Material.LIME_CONCRETE.createBlockData());
            BlockState melon = BukkitAdapter.adapt(Material.MELON.createBlockData());
            BlockState green_glazed_terracotta = BukkitAdapter.adapt(Material.GREEN_GLAZED_TERRACOTTA.createBlockData());
            pat.add(lime_concrete, 0.33333333333);
            pat.add(melon, 0.33333333333);
            pat.add(green_glazed_terracotta, 0.33333333333);

            editSession.setBlocks(selectionwall3, pat);
        } catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }
        sender.sendMessage("fixed!");
        return true;
    }
}
