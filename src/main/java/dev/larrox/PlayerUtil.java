package dev.larrox;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public CommandSender isPlayer(CommandSender sender, Player player) {
        if (sender instanceof Player) {
            return player;
        }

        return sender;
    }

}
