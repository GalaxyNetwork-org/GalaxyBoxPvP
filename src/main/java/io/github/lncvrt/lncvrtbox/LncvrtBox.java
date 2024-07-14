package io.github.lncvrt.lncvrtbox;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.world.block.BlockState;
import io.github.lncvrt.lncvrtbox.events.*;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bukkit.inventory.ItemStack;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public final class LncvrtBox extends JavaPlugin implements Listener, TabExecutor {

    private LuckPerms luckPerms;
    public final Map<UUID, Boolean> autoCompressStatus = new HashMap<>();
    private String serverRules;
    public boolean chatLocked = false;
    private Essentials essentials;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPermsProvider.get();
            getLogger().info("LuckPerms detected and hooked.");
        } else {
            getLogger().warning("LuckPerms not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
            getLogger().warning("Essentials not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Plugin essentialsPlugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        if (essentialsPlugin == null) {
            getLogger().warning("Essentials not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            getLogger().info("Essentials detected. Attempting to hook...");
            if (essentialsPlugin instanceof Essentials) {
                essentials = (Essentials) essentialsPlugin;
                getLogger().info("Essentials hooked successfully.");
            }
            else {
                getLogger().info("Essentials failed to hook.");
            }
        }

        if (new PlaceholderAPIExpansion().register()) {
            getLogger().info("Successfully registered PlaceholderAPIExpansion!");
        } else {
            getLogger().warning("Failed to register PlaceholderAPIExpansion. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityPickupItemListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityResurrectListener(this), this);
        getServer().getPluginManager().registerEvents(new FurnaceBurnListener(this), this);
        getServer().getPluginManager().registerEvents(new FurnaceSmeltListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerElytraBoostListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerItemConsumeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PortalCreateListener(), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new PrepareItemEnchantListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);

        getServer().getPluginManager().registerEvents(this, this);

        loadAutoCompressStatus();
        loadServerRules();
        Objects.requireNonNull(getCommand("autocompress")).setExecutor(this);
        Objects.requireNonNull(getCommand("sky")).setExecutor(this);
        Objects.requireNonNull(getCommand("rules")).setExecutor(this);
        Objects.requireNonNull(getCommand("link")).setExecutor(this);
        Objects.requireNonNull(getCommand("unlink")).setExecutor(this);
        Objects.requireNonNull(getCommand("clearchat")).setExecutor(this);
        Objects.requireNonNull(getCommand("settag")).setExecutor(this);
    }

    @Override
    public void onDisable() {
        saveAutoCompressStatus();
    }

    public boolean isRestrictedMaterial(Material material) {
        return material == Material.IRON_ORE || material == Material.RAW_IRON || material == Material.GOLD_ORE || material == Material.RAW_GOLD || material == Material.ANCIENT_DEBRIS || material == Material.SAND;
    }

    public void convertInventoryItemsPrep(Player player, UUID playerId) {
        if (autoCompressStatus.getOrDefault(playerId, false)) {
            convertInventoryItems(player, Material.DIAMOND, Material.DIAMOND_BLOCK, 9, 1);
            convertInventoryItems(player, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, 9, 1);
            convertInventoryItems(player, Material.REDSTONE, Material.REDSTONE_BLOCK, 9, 1);
            convertInventoryItems(player, Material.EMERALD, Material.EMERALD_BLOCK, 9, 1);
            convertInventoryItems(player, Material.RAW_GOLD, Material.RAW_GOLD_BLOCK, 9, 1);
            convertInventoryItems(player, Material.GOLD_INGOT, Material.GOLD_BLOCK, 9, 1);
            convertInventoryItems(player, Material.GOLD_NUGGET, Material.GOLD_INGOT, 9, 1);
            convertInventoryItems(player, Material.IRON_ORE, Material.IRON_BLOCK, 9, 1);
            convertInventoryItems(player, Material.RAW_IRON, Material.RAW_IRON_BLOCK, 9, 1);
            convertInventoryItems(player, Material.COAL, Material.COAL_BLOCK, 9, 1);
            convertInventoryItems(player, Material.RAW_COPPER, Material.COPPER_BLOCK, 9, 1);
            convertInventoryItems(player, Material.COPPER_INGOT, Material.COPPER_BLOCK, 9, 1);
            convertInventoryItems(player, Material.QUARTZ, Material.QUARTZ_BLOCK, 4, 1);
        }
    }

    private void convertInventoryItems(Player player, Material fromMaterial, Material toMaterial, int fromCount, int toCount) {
        ItemStack[] inventoryContents = player.getInventory().getContents();

        Bukkit.getScheduler().runTask(this, () -> {
            for (int i = 0; i < inventoryContents.length; i++) {
                ItemStack item = inventoryContents[i];

                if (item != null && item.getType() == fromMaterial && item.getAmount() >= fromCount) {
                    int stacksToConvert = item.getAmount() / fromCount;
                    int remainingItems = item.getAmount() % fromCount;

                    if (stacksToConvert > 0) {
                        item.setAmount(remainingItems);
                        player.getInventory().setItem(i, item);

                        ItemStack toMaterialStack = new ItemStack(toMaterial, stacksToConvert * toCount);
                        HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(toMaterialStack);

                        if (!remaining.isEmpty()) {
                            for (ItemStack remainingItem : remaining.values()) {
                                player.getWorld().dropItem(player.getLocation(), remainingItem);
                            }
                        }
                    }
                }
            }
            mergeInventoryStacks(player.getInventory(), fromMaterial);
        });
    }

    private void mergeInventoryStacks(Inventory inventory, Material material) {
        int totalAmount = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                totalAmount += item.getAmount();
                item.setAmount(0);
            }
        }

        int fullStacks = totalAmount / material.getMaxStackSize();
        int remainingItems = totalAmount % material.getMaxStackSize();

        for (int i = 0; i < fullStacks; i++) {
            inventory.addItem(new ItemStack(material, material.getMaxStackSize()));
        }
        if (remainingItems > 0) {
            inventory.addItem(new ItemStack(material, remainingItems));
        }
    }

    public void loadServerRules() {
        File rulesFile = new File(getDataFolder(), "rules.txt");

        if (!rulesFile.exists()) {
            getLogger().log(Level.WARNING, "rules.txt file not found in plugin folder.");
            return;
        }

        StringBuilder rulesContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rulesContent.append(line).append("\n");
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error reading rules.txt file.", e);
        }

        serverRules = rulesContent.toString();
    }

    @SuppressWarnings("unchecked")
    private void saveAutoCompressStatus() {
        File file = new File(getDataFolder(), "autocompress.json");
        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<UUID, Boolean> entry : autoCompressStatus.entrySet()) {
            jsonObject.put(entry.getKey().toString(), entry.getValue());
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonObject.toJSONString());
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error saving statuses to autocompress.json", e);
        }
    }

    private void loadAutoCompressStatus() {
        File file = new File(getDataFolder(), "autocompress.json");
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            for (Object key : jsonObject.keySet()) {
                UUID playerId = UUID.fromString((String) key);
                Boolean status = (Boolean) jsonObject.get(key);
                autoCompressStatus.put(playerId, status);
            }
        } catch (IOException | ParseException e) {
            getLogger().log(Level.SEVERE, "Error loading statuses from autocompress.json", e);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("autocompress")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(RED + "This command can only be executed by a player.");
                return true;
            }

            UUID playerId = player.getUniqueId();

            boolean currentStatus = autoCompressStatus.getOrDefault(playerId, false);
            autoCompressStatus.put(playerId, !currentStatus);

            if (!currentStatus) {
                player.sendMessage(GREEN + "Auto-compress enabled.");
            } else {
                player.sendMessage(RED + "Auto-compress disabled.");
            }
            return true;
        } else if (label.equalsIgnoreCase("sky")) {
            if (sender instanceof Player player) {
                player.performCommand("warp sky");
            }
            return true;
        } else if (label.equalsIgnoreCase("rules")) {
            if (serverRules != null && !serverRules.isEmpty()) {
                sender.sendMessage(translateAlternateColorCodes('&', serverRules));
            } else {
                sender.sendMessage("%sServer rules are currently unavailable.".formatted(RED));
            }
            return true;
        } else if (label.equalsIgnoreCase("link")) {
            if (sender instanceof Player player) {
                player.performCommand("discord link");
            }
            return true;
        } else if (label.equalsIgnoreCase("unlink")) {
            if (sender instanceof Player player) {
                player.performCommand("discord unlink");
            }
            return true;
        } else if (label.equalsIgnoreCase("clearchat")) {
            if (sender instanceof Player player) {
                if (!player.hasPermission("lncvrtbox.clearchat")) {
                    player.sendMessage("%sYou do not have permission to use this command.".formatted(RED));
                    return true;
                }
            }

            for (int i = 0; i < 1000; i++) {
                Bukkit.broadcastMessage("");
            }
            return true;
        } else if (label.equalsIgnoreCase("settag")) {
            sender.sendMessage(RED + "Setting tags has been removed. Sorry!");
            return true;
        } else if (label.equalsIgnoreCase("lockchat")) {
            if (sender.hasPermission("chatlock.lock")) {
                chatLocked = !chatLocked;
                sender.sendMessage("Chat " + (chatLocked ? "locked." : "unlocked."));
            } else {
                sender.sendMessage("You do not have permission to use this command.");
            }
            return true;
        } else if (label.equalsIgnoreCase("fix")) {
            //TODO: make selectionwall4 (front wall)
            com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Bukkit.getWorld("world"));
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
        return false;
    }

    public boolean isAfk(Player player) {
        com.earth2me.essentials.User user = essentials.getUser(player);
        return user.isAfk();
    }

    public boolean isMuted(Player player) {
        com.earth2me.essentials.User user = essentials.getUser(player);
        return user.isMuted();
    }

    private void setSuffix(Player player, String suffix) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            Node suffixNode = SuffixNode.builder(" " + suffix, 1).build();
            user.data().clear(NodeType.SUFFIX::matches);
            user.data().add(suffixNode);
            luckPerms.getUserManager().saveUser(user);
        }
    }

    private void removeSuffix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            user.data().clear(NodeType.SUFFIX::matches);
            luckPerms.getUserManager().saveUser(user);
        }
    }
}