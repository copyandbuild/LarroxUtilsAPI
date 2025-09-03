package dev.larrox;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerUtil {

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }

    public static Player getPlayer(CommandSender sender) {
        return isPlayer(sender) ? (Player) sender : null;
    }

    public static UUID getUUID(Player player) {
        return player != null ? player.getUniqueId() : null;
    }

    public static Player getPlayerByName(String name) {
        return Bukkit.getPlayerExact(name);
    }

    public static Player getPlayerByUUID(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public static boolean isOnline(String name) {
        return getPlayerByName(name) != null;
    }

    public static boolean isOnline(UUID uuid) {
        return getPlayerByUUID(uuid) != null;
    }

    public static void sendMessage(CommandSender sender, String message) {
        if (sender != null && message != null) {
           ChatUtil.send(sender, message);
        }
    }

    public static void kick(Player player, String reason) {
        if (player != null) {
            player.kickPlayer(reason != null ? reason : "You have been kicked!");
        }
    }
}
