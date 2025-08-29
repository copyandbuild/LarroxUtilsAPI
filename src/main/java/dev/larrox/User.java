package dev.larrox;

import dev.larrox.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {

    private final CommandSender sender;

    public User(CommandSender sender) {
        this.sender = sender;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    public String getName() {
        return sender.getName();
    }

    public UUID getUniqueId() {
        return isPlayer() ? getPlayer().getUniqueId() : null;
    }

    public Player getPlayer() {
        return isPlayer() ? (Player) sender : null;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void sendMessage(String message) {
        ChatUtil.send(sender, message);
    }

    public void sendMessages(String... messages) {
        for (String msg : messages) {
            ChatUtil.send(sender, msg);
        }
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (isPlayer()) {
            ChatUtil.sendTitle(getPlayer(), title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public void sendTitle(String title, int fadeIn, int stay, int fadeOut) {
        if (isPlayer()) {
            ChatUtil.sendTitle(getPlayer(), title, fadeIn, stay, fadeOut);
        }
    }

    public void sendSubtitle(String subtitle, int fadeIn, int stay, int fadeOut) {
        if (isPlayer()) {
            ChatUtil.sendSubtitle(getPlayer(), subtitle, fadeIn, stay, fadeOut);
        }
    }

    public void sendActionBar(String message) {
        if (isPlayer()) {
            ChatUtil.sendActionBar(getPlayer(), message);
        }
    }

    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    public void kick(String reason) {
        if (isPlayer()) {
            getPlayer().kickPlayer(ChatUtil.color(reason));
        }
    }

    public void teleport(Location location) {
        if (isPlayer()) {
            getPlayer().teleport(location);
        }
    }

    public Location getLocation() {
        return isPlayer() ? getPlayer().getLocation() : null;
    }

    public void setHealth(double health) {
        if (isPlayer()) {
            getPlayer().setHealth(health);
        }
    }

    public double getHealth() {
        return isPlayer() ? getPlayer().getHealth() : -1;
    }

    public void setFoodLevel(int food) {
        if (isPlayer()) {
            getPlayer().setFoodLevel(food);
        }
    }

    public int getFoodLevel() {
        return isPlayer() ? getPlayer().getFoodLevel() : -1;
    }

    public void executeCommand(String command) {
        Bukkit.dispatchCommand(sender, command);
    }

    public boolean isOnline() {
        if (isPlayer()) {
            return getPlayer().isOnline();
        }
        return true;
    }
}
