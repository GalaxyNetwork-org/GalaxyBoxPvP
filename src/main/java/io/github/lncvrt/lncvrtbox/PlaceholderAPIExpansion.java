package io.github.lncvrt.lncvrtbox;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static org.bukkit.ChatColor.*;

public final class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private Essentials essentials;

    @Override
    public @NotNull String getIdentifier() {
        return "lncvrtbox";
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
    public boolean register() {
        Plugin essentialsPlugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        if (essentialsPlugin instanceof Essentials) {
            this.essentials = (Essentials) essentialsPlugin;
            return super.register();
        }
        return false;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (essentials == null) {
            return null;
        }

        int decimalPlaces = 1;
        if (identifier.matches("(health|hearts)_\\d+")) {
            try {
                decimalPlaces = Integer.parseInt(identifier.split("_")[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            }
            identifier = identifier.split("_")[0];
        }

        switch (identifier) {
            case "afk" -> {
                int afkCount = 0;
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    User user = essentials.getUser(p);
                    if (user != null && user.isAfk()) {
                        afkCount++;
                    }
                }
                return String.valueOf(afkCount);
            }
            case "afk_staff" -> {
                int staffAfkCount = 0;
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p.hasPermission("lncvrtbox.staff") && isAfk(p)) {
                        staffAfkCount++;
                    }
                }
                return String.valueOf(staffAfkCount);
            }
            case "team" -> {
                String team = PlaceholderAPI.setPlaceholders(player, "%betterteams_displayname%");
                if (team.isEmpty()) {
                    return "";
                } else {
                    return "%s[%s%s%s%s]%s ".formatted(GRAY, RESET, team, RESET, GRAY, RESET);
                }
            }
            case "health" -> {
                double health = player.getHealth();
                String format = decimalPlaces > 0 ? "#." + repeat("0", decimalPlaces) : "#";
                return new DecimalFormat(format).format(health);
            }
            case "hearts" -> {
                double hearts = player.getHealth() / 2.0;
                String format = decimalPlaces > 0 ? "#." + repeat("0", decimalPlaces) : "#";
                return new DecimalFormat(format).format(hearts);
            }
            case "total_joins" -> {
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                return numberFormat.format(Bukkit.getOfflinePlayers().length);
            }
            case "total_joins_formatted" -> {
                return formatCompact(Bukkit.getOfflinePlayers().length);
            }
            case "kdr" -> {
                int kills = getPlayerKills(player);
                int deaths = getPlayerDeaths(player);
                double kdr = (deaths == 0) ? kills : (double) kills / deaths;
                String format = decimalPlaces > 0 ? "0." + repeat("0", decimalPlaces) : "0";
                return new DecimalFormat(format).format(kdr);
            }
            case "playtime" -> {
                long playtimeTicks = player.getStatistic(org.bukkit.Statistic.PLAY_ONE_MINUTE);
                long playtimeSeconds = playtimeTicks / 20;
                return formatTime(playtimeSeconds);
            }
        }

        return null;
    }

    private String repeat(String str, int count) {
        return String.valueOf(str).repeat(Math.max(0, count));
    }

    private boolean isAfk(Player player) {
        User user = essentials.getUser(player);
        return user != null && user.isAfk();
    }

    private String formatCompact(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        if (number >= 1_000_000) {
            return decimalFormat.format(number / 1_000_000.0) + "M";
        } else if (number >= 1_000) {
            return decimalFormat.format(number / 1_000.0) + "k";
        } else {
            return String.valueOf(number);
        }
    }

    private String formatTime(long seconds) {
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder formattedTime = new StringBuilder();
        if (days > 0) formattedTime.append(days).append(" days, ");
        if (hours > 0 || days > 0) formattedTime.append(hours).append(" hours, ");
        if (minutes > 0 || hours > 0 || days > 0) formattedTime.append(minutes).append(" minutes, ");
        formattedTime.append(seconds).append(" seconds");

        return formattedTime.toString().trim();
    }

    public int getPlayerKills(Player player) {
        return player.getStatistic(org.bukkit.Statistic.PLAYER_KILLS);
    }

    public int getPlayerDeaths(Player player) {
        return player.getStatistic(org.bukkit.Statistic.DEATHS);
    }
}
