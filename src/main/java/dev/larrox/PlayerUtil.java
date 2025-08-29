package dev.larrox;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}
