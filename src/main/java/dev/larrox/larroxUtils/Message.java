package dev.larrox.larroxUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class Message {
   private final MiniMessage miniMessage;

   public Message() {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE)) {
         this.miniMessage = MiniMessage.miniMessage();
      } else {
         this.miniMessage = null;
         Bukkit.getLogger().info("[LarroxUtils] The Message System was disabled.");
      }

   }

   public MiniMessage getMiniMessage() {
      return this.miniMessage;
   }

   public Component translateIntoColor(String text) {
      if (LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE)) {
         return Component.text("Disabled").color(TextColor.color(255, 0, 0));
      } else {
         text = text.replaceAll("<dark_red>", "<#AA00001>");
         text = text.replaceAll("</dark_red>", "</#AA00001>");
         if (text != null && !text.isBlank()) {
            if (text.contains("§")) {
               text = text.replaceAll("§", "&");
            }

            if (text.startsWith("[") && text.endsWith("]")) {
               text = text.substring(1, text.length() - 1);
            }

            Component chatMessage = null;

            try {
               chatMessage = this.getMiniMessage().deserialize(this.convertLegacyToAdventure(text));
               return chatMessage;
            } catch (ParsingExceptionImpl var4) {
               var4.printStackTrace();
               return Component.text("Error").color(TextColor.color(255, 0, 0));
            }
         } else {
            return Component.text("");
         }
      }
   }

   public String removeColorCoding(String input) {
      return LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE) ? "" : ChatColor.stripColor(this.getMiniMessage().stripTags(input));
   }

   public String translateIntoColorLegacy(String text) {
      return LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE) ? "" : ChatColor.translateAlternateColorCodes('&', text);
   }

   private String convertLegacyToAdventure(String input) {
      return LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE) ? "" : input.replaceAll("&a", "<green>").replaceAll("&b", "<aqua>").replaceAll("&c", "<red>").replaceAll("&d", "<light_purple>").replaceAll("&e", "<yellow>").replaceAll("&f", "<white>").replaceAll("&0", "<black>").replaceAll("&1", "<dark_blue>").replaceAll("&2", "<dark_green>").replaceAll("&3", "<dark_aqua>").replaceAll("&4", "<#AA00001>").replaceAll("&5", "<dark_purple>").replaceAll("&6", "<gold>").replaceAll("&7", "<gray>").replaceAll("&8", "<dark_gray>").replaceAll("&9", "<blue>").replaceAll("&l", "<bold>").replaceAll("&r", "<reset>");
   }

   public String getText(Component component) {
      if (LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE)) {
         return "";
      } else {
         String text = PlainTextComponentSerializer.plainText().serialize(component);
         if (text.startsWith("[") && text.endsWith("]")) {
            text = text.substring(1, text.length() - 1);
         }

         return text;
      }
   }

   public String getLegacyText(Component component) {
      return LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.MESSAGE) ? "" : LegacyComponentSerializer.legacySection().serialize(component);
   }
}
