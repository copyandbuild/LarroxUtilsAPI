package dev.larrox;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AdvancementUtil {

    /**
     * Creates and grants a custom advancement to a player.
     *
     * @param plugin           Your plugin instance
     * @param player           Target player
     * @param icon             The icon material (minecraft ID, e.g. "diamond")
     * @param title            Title text (supports PlaceholderAPI & color codes)
     * @param description      Description text (supports PlaceholderAPI & color codes)
     * @param removeAfterTicks How long before removing the advancement (set -1 to keep permanently)
     */
    public static void giveAdvancement(Plugin plugin,
                                       Player player,
                                       String icon,
                                       String title,
                                       String description,
                                       long removeAfterTicks) {

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            title = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, title);
            description = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, description);
        }

        title = ChatUtil.color(title);
        description = ChatUtil.color(description);

        title = escapeJson(title);
        description = escapeJson(description);

        NamespacedKey key = new NamespacedKey(plugin,
                "adv_" + player.getName().toLowerCase() + "_" + System.currentTimeMillis());

        String json = """
        {
          "criteria": {
            "triggered": {
              "trigger": "minecraft:impossible"
            }
          },
          "display": {
            "icon": { "id": "minecraft:%s" },
            "title": "%s",
            "description": "%s",
            "frame": "task",
            "show_toast": true,
            "announce_to_chat": true,
            "hidden": false
          }
        }
        """.formatted(icon.toLowerCase(), title, description);

        Advancement adv;
        try {
            Bukkit.getUnsafe().loadAdvancement(key, json);
            adv = Bukkit.getAdvancement(key);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to create advancement: " + e.getMessage());
            return;
        }

        if (adv != null) {
            AdvancementProgress progress = player.getAdvancementProgress(adv);
            for (String criterion : progress.getRemainingCriteria()) {
                progress.awardCriteria(criterion);
            }

            if (removeAfterTicks > 0) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Bukkit.getUnsafe().removeAdvancement(key);
                }, removeAfterTicks);
            }
        }
    }

    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
