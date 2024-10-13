package io.github.lncvrt.lncvrtbox;

import com.earth2me.essentials.Essentials;
import io.github.lncvrt.lncvrtbox.commands.*;
import io.github.lncvrt.lncvrtbox.events.*;
import org.bukkit.*;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public final class LncvrtBox extends JavaPlugin implements Listener, TabExecutor {
    public final Map<UUID, Boolean> autoCompressStatus = new HashMap<>();
    public String serverRules;
    public boolean chatLocked = false;
    private Essentials essentials;
    public boolean fixRanTooOften = false;

    @Override
    public void onEnable() {
        Plugin essentialsPlugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        getLogger().info("Essentials detected. Attempting to hook...");
        if (essentialsPlugin instanceof Essentials) {
            essentials = (Essentials) essentialsPlugin;
            getLogger().info("Essentials hooked successfully.");
        } else {
            getLogger().info("Essentials failed to hook. Disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }

        if (new PlaceholderAPIExpansion().register()) {
            getLogger().info("Successfully registered PlaceholderAPIExpansion!");
        } else {
            getLogger().warning("Failed to register PlaceholderAPIExpansion. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerEvents();
        registerCommands();

        loadAutoCompressStatus();
        loadServerRules();
    }

    @Override
    public void onDisable() {
        saveAutoCompressStatus();
    }

    private void registerEvents() {
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
        getServer().getPluginManager().registerEvents(new PlayerItemConsumeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalCreateListener(), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new PrepareItemEnchantListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("autocompress")).setExecutor(new Autocompress(this));
        Objects.requireNonNull(getCommand("clearchat")).setExecutor(new Clearchat());
        Objects.requireNonNull(getCommand("fix")).setExecutor(new Fix(this));
        Objects.requireNonNull(getCommand("link")).setExecutor(new Link());
        Objects.requireNonNull(getCommand("lockchat")).setExecutor(new Lockchat(this));
        Objects.requireNonNull(getCommand("ping")).setExecutor(new Ping(this));
        Objects.requireNonNull(getCommand("rules")).setExecutor(new Rules(this));
        Objects.requireNonNull(getCommand("sky")).setExecutor(new Sky());
        Objects.requireNonNull(getCommand("unlink")).setExecutor(new Unlink());
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

    public boolean isAfk(Player player) {
        com.earth2me.essentials.User user = essentials.getUser(player);
        return user.isAfk();
    }

    public boolean isMuted(Player player) {
        com.earth2me.essentials.User user = essentials.getUser(player);
        return user.isMuted();
    }
}