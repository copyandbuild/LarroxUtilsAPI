package dev.larrox;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }
}
