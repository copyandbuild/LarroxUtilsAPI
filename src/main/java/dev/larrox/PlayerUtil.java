package dev.larrox;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public CommandSender isPlayer(CommandSender sender) {
        if (sender instanceof Player player) {
            return player;
        }

        return sender;
    }

}
