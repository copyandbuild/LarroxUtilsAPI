package dev.larrox;

import org.bukkit.Bukkit;

public class Logger {

    public static void info (String message) {
        Bukkit.getLogger().info( "[LarroxUtilsAPI] " + message);
    }

    public static void warn (String message) {
        Bukkit.getLogger().warning( "[LarroxUtilsAPI] " + message);
    }

    public static void error (String message) {
        Bukkit.getLogger().severe( "[LarroxUtilsAPI] " + message);
    }
}
