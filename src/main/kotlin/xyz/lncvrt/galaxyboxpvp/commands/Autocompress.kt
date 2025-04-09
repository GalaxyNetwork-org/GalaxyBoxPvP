package xyz.lncvrt.galaxyboxpvp.commands

import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitCommand
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.extension.sendComponent
import xyz.lncvrt.galaxyboxpvp.GalaxyBoxPvPPlugin

object Autocompress : BukkitCommand("autocompress") {
    override fun rootBuilder(builder: MutableCommandBuilder<BukkitSender>) {
        builder.handler { context ->
            val player = context.sender().player() ?: return@handler
            val playerId = player.uniqueId
            val instance = GalaxyBoxPvPPlugin.getInstance()

            val currentStatus = instance.autoCompressStatus.getOrDefault(playerId, false) ?: return@handler
            instance.autoCompressStatus.put(playerId, !currentStatus)

            if (!currentStatus) {
                player.sendComponent("<green>Auto-compress enabled.")
            } else {
                player.sendComponent("<red>Auto-compress disabled.")
            }
        }
    }
}
