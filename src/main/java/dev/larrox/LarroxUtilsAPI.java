package dev.larrox;

import org.bukkit.event.EventHandler;

public class LarroxUtilsAPI {

    private final LarroxUtilsAPI instance;
    @EventHandler
    public void onLoad() {
        Logger.info("[LarroxUtilsAPI] Beep Boop, Enabled!");
    }

    public LarroxUtilsAPI(LarroxUtilsAPI instance) {
        this.instance = instance;
    }
}
