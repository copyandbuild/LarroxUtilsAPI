package dev.larrox;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class LarroxUtilsAPI {

    private final LarroxUtilsAPI instance;
    public static boolean isDevBuild = true;
    public static boolean isLarroxBuild = false;

    @EventHandler
    public void onLoad() {
        Bukkit.getLogger().info("[LarroxUtilsAPI] Beep Boop, Enabled!");
        Bukkit.getLogger().info("[LarroxUtilsAPI] ");
    }

    public LarroxUtilsAPI(LarroxUtilsAPI instance) {
        this.instance = instance;
    }
}
