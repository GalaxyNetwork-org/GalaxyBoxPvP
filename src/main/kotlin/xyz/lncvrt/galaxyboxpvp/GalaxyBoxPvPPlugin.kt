package xyz.lncvrt.galaxyboxpvp

import me.confuser.banmanager.common.BanManagerPlugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.sayandev.stickynote.loader.bukkit.StickyNoteBukkitLoader
import xyz.lncvrt.galaxyboxpvp.commands.Autocompress
import xyz.lncvrt.galaxyboxpvp.commands.Sky
import xyz.lncvrt.galaxyboxpvp.commands.Skyrtp
import xyz.lncvrt.galaxyboxpvp.events.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.ParseException
import java.util.*


class GalaxyBoxPvPPlugin : JavaPlugin(), TabExecutor {
    internal val autoCompressStatus: MutableMap<UUID?, Boolean?> = HashMap<UUID?, Boolean?>()
    internal val skyRTPDelays: MutableMap<UUID?, Long?> = HashMap<UUID?, Long?>()
    private val placeholderAPIExpansion: PlaceholderAPIExpansion = PlaceholderAPIExpansion()

    override fun onEnable() {
        StickyNoteBukkitLoader(this)
        instance = this

        if (placeholderAPIExpansion.register()) {
            logger.info("Successfully registered PlaceholderAPIExpansion!")
        } else {
            logger.warning("Failed to register PlaceholderAPIExpansion. Disabling plugin.")
            server.pluginManager.disablePlugin(this)
            return
        }

        registerEvents()
        registerCommands()

        loadAutoCompressStatus()
    }

    override fun onDisable() {
        placeholderAPIExpansion.unregister()
        saveAutoCompressStatus()
    }

    private fun registerEvents() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(BlockBreakListener(this), this)
        pluginManager.registerEvents(BlockPlaceListener(), this)
        pluginManager.registerEvents(CraftItemListener(), this)
        pluginManager.registerEvents(EntityPickupItemListener(this), this)
        pluginManager.registerEvents(EntitySpawnListener(), this)
        pluginManager.registerEvents(FurnaceBurnListener(this), this)
        pluginManager.registerEvents(FurnaceSmeltListener(this), this)
        pluginManager.registerEvents(PlayerDropItemListener(this), this)
        pluginManager.registerEvents(PlayerJoinListener(this), this)
        pluginManager.registerEvents(PlayerMoveListener(), this)
        pluginManager.registerEvents(PrepareAnvilListener(), this)
        pluginManager.registerEvents(PrepareItemEnchantListener(), this)
        pluginManager.registerEvents(SignChangeListener(this), this)
    }

    private fun registerCommands() {
        Autocompress
        Sky
        Skyrtp
    }

    internal fun isRestrictedMaterial(material: Material?): Boolean {
        return material == Material.IRON_ORE || material == Material.RAW_IRON || material == Material.GOLD_ORE || material == Material.RAW_GOLD || material == Material.ANCIENT_DEBRIS || material == Material.SAND
    }

    internal fun convertInventoryItemsPrep(player: Player, playerId: UUID?) {
        if (autoCompressStatus[playerId] == true) {
            convertInventoryItems(player, Material.DIAMOND, Material.DIAMOND_BLOCK, 9, 1)
            convertInventoryItems(player, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, 9, 1)
            convertInventoryItems(player, Material.REDSTONE, Material.REDSTONE_BLOCK, 9, 1)
            convertInventoryItems(player, Material.EMERALD, Material.EMERALD_BLOCK, 9, 1)
            convertInventoryItems(player, Material.RAW_GOLD, Material.RAW_GOLD_BLOCK, 9, 1)
            convertInventoryItems(player, Material.GOLD_INGOT, Material.GOLD_BLOCK, 9, 1)
            convertInventoryItems(player, Material.GOLD_NUGGET, Material.GOLD_INGOT, 9, 1)
            convertInventoryItems(player, Material.IRON_ORE, Material.IRON_BLOCK, 9, 1)
            convertInventoryItems(player, Material.RAW_IRON, Material.RAW_IRON_BLOCK, 9, 1)
            convertInventoryItems(player, Material.COAL, Material.COAL_BLOCK, 9, 1)
            convertInventoryItems(player, Material.RAW_COPPER, Material.RAW_COPPER_BLOCK, 9, 1)
            convertInventoryItems(player, Material.COPPER_INGOT, Material.COPPER_BLOCK, 9, 1)
            convertInventoryItems(player, Material.QUARTZ, Material.QUARTZ_BLOCK, 4, 1)
        }
    }

    private fun convertInventoryItems(
        player: Player,
        fromMaterial: Material,
        toMaterial: Material,
        fromCount: Int,
        toCount: Int
    ) {
        val inventoryContents = player.inventory.contents

        Bukkit.getScheduler().runTask(this, Runnable {
            for (i in inventoryContents.indices) {
                val item = inventoryContents[i]

                if (item != null && item.type == fromMaterial && item.amount >= fromCount) {
                    val stacksToConvert = item.amount / fromCount
                    val remainingItems = item.amount % fromCount

                    if (stacksToConvert > 0) {
                        item.amount = remainingItems
                        player.inventory.setItem(i, item)

                        val toMaterialStack = ItemStack(toMaterial, stacksToConvert * toCount)
                        val remaining = player.inventory.addItem(toMaterialStack)

                        if (!remaining.isEmpty()) {
                            for (remainingItem in remaining.values) {
                                player.world.dropItem(player.location, remainingItem)
                            }
                        }
                    }
                }
            }
            mergeInventoryStacks(player.inventory, fromMaterial)
        })
    }

    private fun mergeInventoryStacks(inventory: Inventory, material: Material) {
        var totalAmount = 0

        for (item in inventory.contents) {
            if (item != null && item.type == material) {
                totalAmount += item.amount
                item.amount = 0
            }
        }

        val fullStacks = totalAmount / material.maxStackSize
        val remainingItems = totalAmount % material.maxStackSize

        for (i in 0..<fullStacks) {
            inventory.addItem(ItemStack(material, material.maxStackSize))
        }
        if (remainingItems > 0) {
            inventory.addItem(ItemStack(material, remainingItems))
        }
    }

    private fun saveAutoCompressStatus() {
        val file = File(dataFolder, "autocompress.json")
        val jsonObject = JSONObject()

        for (entry in autoCompressStatus.entries) {
            jsonObject.put(entry.key.toString(), entry.value)
        }

        try {
            FileWriter(file).use { writer ->
                writer.write(jsonObject.toJSONString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadAutoCompressStatus() {
        val file = File(dataFolder, "autocompress.json")
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        if (!file.exists()) {
            file.writeText("[]")
        }

        try {
            FileReader(file).use { reader ->
                val parser = JSONParser()
                val jsonObject = parser.parse(reader) as JSONObject
                for (key in jsonObject.keys) {
                    val playerId = UUID.fromString(key as String?)
                    val status = jsonObject.get(key) as Boolean?
                    autoCompressStatus.put(playerId, status)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    internal fun isMuted(player: Player): Boolean {
        return BanManagerPlugin.getInstance().playerMuteStorage.isMuted(player.uniqueId)
    }

    companion object {
        private lateinit var instance: GalaxyBoxPvPPlugin

        fun getInstance(): GalaxyBoxPvPPlugin {
            return instance
        }
    }
}