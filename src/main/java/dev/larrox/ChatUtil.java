package dev.larrox;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    /**
     * Übersetzt Farbcodes mit '&' zu Bukkit-ChatColor.
     *
     * @param text Text mit Farbcodes, z.B. "&aHallo"
     * @return gefärbter Text
     */
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Sendet eine Chat-Nachricht an einen CommandSender (Konsole oder Spieler).
     *
     * @param sender Empfänger (Spieler oder Konsole)
     * @param message Nachricht mit Farbcodes (&)
     */
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    /**
     * Sendet eine Chat-Nachricht direkt an einen Spieler.
     *
     * @param player Spieler, der die Nachricht bekommt
     * @param message Nachricht mit Farbcodes (&)
     */
    public static void send(Player player, String message) {
        player.sendMessage(color(message));
    }

    /**
     * Sendet einen Title + Subtitle an einen Spieler.
     *
     * @param player Spieler, der die Nachricht bekommt
     * @param title Titeltext
     * @param subtitle Untertiteltext
     * @param fadeIn Dauer des Einblendens (Ticks)
     * @param stay Dauer der Anzeige (Ticks)
     * @param fadeOut Dauer des Ausblendens (Ticks)
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(color(title), color(subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * Sendet nur einen Title (ohne Subtitle).
     *
     * @param player Spieler, der die Nachricht bekommt
     * @param title Titeltext
     * @param fadeIn Dauer des Einblendens (Ticks)
     * @param stay Dauer der Anzeige (Ticks)
     * @param fadeOut Dauer des Ausblendens (Ticks)
     */
    public static void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(color(title), null, fadeIn, stay, fadeOut);
    }

    /**
     * Sendet nur einen Subtitle (ohne Title).
     *
     * @param player Spieler, der die Nachricht bekommt
     * @param subtitle Untertiteltext
     * @param fadeIn Dauer des Einblendens (Ticks)
     * @param stay Dauer der Anzeige (Ticks)
     * @param fadeOut Dauer des Ausblendens (Ticks)
     */
    public static void sendSubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(null, color(subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * Sendet eine Nachricht in die Actionbar (über der Hotbar).
     *
     * @param player Spieler, der die Nachricht bekommt
     * @param message Nachricht mit Farbcodes (&)
     */
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                new net.md_5.bungee.api.chat.TextComponent(color(message))
        );
    }

    /**
     * Verarbeitet Hexcode Strings, returnt die "Finale" Nachricht.
     *
     * @param message Nachricht mit Hexcodes (&#)
     */
    public String hexColor(String message) {
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
        return sb.toString().replace("&", "§");
    }
}
