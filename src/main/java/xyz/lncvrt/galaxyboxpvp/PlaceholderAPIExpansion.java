package xyz.lncvrt.galaxyboxpvp;

import com.booksaw.betterTeams.Team;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlaceholderAPIExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "galaxyboxpvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lncvrt";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        switch (identifier) {
            case "hearts":
                return String.format("%.1f", player.getHealth() / 2);
            case "kills":
                return String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS));
            case "deaths":
                return String.valueOf(player.getStatistic(Statistic.DEATHS));
            case "kdr":
                int kills = player.getStatistic(Statistic.PLAYER_KILLS);
                int deaths = player.getStatistic(Statistic.DEATHS);
                return deaths == 0 ? String.valueOf(kills) : String.format("%.1f", (double) kills / deaths);
            case "team":
                Team team1 = Team.getTeam(player);
                if (team1 == null) return "";
                return ChatColor.translateAlternateColorCodes('&', "%s[%s%s%s%s]%s ".formatted(ChatColor.GRAY, ChatColor.RESET, team1.getDisplayName(), ChatColor.RESET, ChatColor.GRAY, ChatColor.RESET));
            case "teamname":
                Team team2 = Team.getTeam(player);
                if (team2 == null) return "None";
                return team2.getName();
            default:
                return null;
        }
    }
}
