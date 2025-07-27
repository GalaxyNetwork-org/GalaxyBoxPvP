@file:Suppress("DEPRECATION")

package xyz.lncvrt.galaxyboxpvp

import com.booksaw.betterTeams.Team
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.Statistic
import org.bukkit.entity.Player
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.legacyColored
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.legacyString
import javax.annotation.Nullable

class PlaceholderAPIExpansion : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "galaxyboxpvp"
    }

    override fun getAuthor(): String {
        return "Lncvrt"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    @Nullable
    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        when (identifier) {
            "hearts" -> return String.format("%.1f", player.health / 2)
            "kills" -> return player.getStatistic(Statistic.PLAYER_KILLS).toString()
            "deaths" -> return player.getStatistic(Statistic.DEATHS).toString()
            "kdr" -> {
                val kills = player.getStatistic(Statistic.PLAYER_KILLS)
                val deaths = player.getStatistic(Statistic.DEATHS)
                return if (deaths == 0) kills.toString() else String.format("%.1f", kills.toDouble() / deaths)
            }
            "team" -> {
                val team1: Team? = Team.getTeam(player)
                if (team1 == null) return ""
                return LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize("<gray>[</gray>%s<gray>]</gray>".format(team1.displayName)))
            }
            "teamname" -> {
                val team2: Team? = Team.getTeam(player)
                if (team2 == null) return "None"
                return team2.name
            }
            else -> return null
        }
    }
}