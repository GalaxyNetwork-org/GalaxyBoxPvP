package xyz.lncvrt.galaxyboxpvp.commands

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitCommand
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.extension.sendComponent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin
import java.util.concurrent.ThreadLocalRandom

object Skyrtp : BukkitCommand("skyrtp") {
    override fun rootBuilder(builder: MutableCommandBuilder<BukkitSender>) {
        builder.handler { context ->
            val player = context.sender().player() ?: return@handler
            val instance = GalaxyBoxPvPPlugin.getInstance()

            val last: Long? = instance.skyRTPDelays.get(player.uniqueId)
            val time = System.currentTimeMillis()
            if (last != null && time - last < 30000) {
                val timeLeft = (30000 - (System.currentTimeMillis() - last)) / 1000
                player.sendComponent("<red>You can't RTP in the sky world so often, please wait $timeLeft second(s)!")
                return@handler
            }
            val world: World? = instance.server.getWorld("sky")
            if (world == null) {
                player.sendComponent("<red>Failed, please try again!")
                return@handler
            }
            val randomX = ThreadLocalRandom.current().nextInt(100, 15000)
            val randomZ = ThreadLocalRandom.current().nextInt(100, 15000)
            val location = Location(world, randomX.toDouble(), 100.0, randomZ.toDouble())
            val block: Block = world.getBlockAt(location)
            if (block.type === Material.AIR) {
                block.type = Material.GRASS_BLOCK
            }
            player.teleport(location.add(0.5, 1.0, 0.5))
            player.sendComponent("<green>You have been randomly teleported to <u>$randomX 100 $randomZ<u>")
            instance.skyRTPDelays.put(player.uniqueId, time)
        }
    }
}
