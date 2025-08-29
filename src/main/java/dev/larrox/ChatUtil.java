package dev.larrox;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    /**
     * Übersetzt Farbcodes (&) und ersetzt ggf. PlaceholderAPI-Placeholders.
     *
     * @param sender  Empfänger (wichtig für PlaceholderAPI)
     * @param text    Text mit Farbcodes (&) und ggf. Placeholders
     * @return        formatierter Text
     */
    public static String format(CommandSender sender, String text) {
        if (text == null) return "";
        String msg = text;

        // Wenn Sender ein Spieler ist -> PlaceholderAPI anwenden
        if (sender instanceof Player) {
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
        }

        // Farbcodes (&) und Hexcodes verarbeiten
        msg = new ChatUtil().hexColor(msg);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Übersetzt Farbcodes (&)
     *
     * @param text    Text mit Farbcodes (&)
     * @return        farbiger Text
     */
    public static String color(String text) {
        if (text == null) return "";
        String msg = text;

        msg = new ChatUtil().hexColor(msg);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Sendet eine Chat-Nachricht an CommandSender (Konsole oder Spieler).
     */
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(format(sender, message));
    }

    /**
     * Sendet eine Chat-Nachricht an einen Spieler.
     */
    public static void send(Player player, String message) {
        player.sendMessage(format(player, message));
    }

    /**
     * Sendet einen Title + Subtitle an einen Spieler.
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(format(player, title), format(player, subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * Sendet nur einen Title (ohne Subtitle).
     */
    public static void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(format(player, title), null, fadeIn, stay, fadeOut);
    }

    /**
     * Sendet nur einen Subtitle (ohne Title).
     */
    public static void sendSubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(null, format(player, subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * Sendet eine Nachricht in die Actionbar.
     */
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                new net.md_5.bungee.api.chat.TextComponent(format(player, message))
        );
    }

    /**
     * Verarbeitet Hex-Farbcodes (&#RRGGBB).
     */
    public String hexColor(String message) {
        if (message == null) return "";
        Pattern pattern = Pattern.compile("(?i)&\\#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append("§").append(c);
            }
            matcher.appendReplacement(sb, replacement.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
