package xyz.lncvrt.galaxyboxpvp.commands

import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitCommand
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.extension.sendComponent

object Sky : BukkitCommand("sky") {
    override fun rootBuilder(builder: MutableCommandBuilder<BukkitSender>) {
        builder.handler { context ->
            val player = context.sender().player() ?: return@handler

            player.performCommand("warp sky")
            player.sendComponent("<green>You can do <b><u><click:suggest_command:'/skyrtp'>/skyrtp</click></u></b> (click the bold text) to randomly teleport around here!")
        }
    }
}
