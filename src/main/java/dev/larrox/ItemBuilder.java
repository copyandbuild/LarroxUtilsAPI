package dev.larrox;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Erstellt einen neuen ItemBuilder für das angegebene Material.
     *
     * @param material Das Material des Items (z. B. Material.DIAMOND_SWORD)
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    /**
     * Setzt den Anzeigenamen des Items.
     *
     * @param name Name mit Farbcodes (&)
     * @return der aktuelle ItemBuilder
     */
    public ItemBuilder setName(String name) {
        meta.setDisplayName(name.replace("&", "§")); // damit Farbcodes funktionieren
        return this;
    }

    /**
     * Setzt die Lore (Beschreibung) des Items.
     *
     * @param lore Zeilen der Lore
     * @return der aktuelle ItemBuilder
     */
    public ItemBuilder setLore(String... lore) {
        List<String> loreList = new ArrayList<>();
        for (String line : lore) {
            loreList.add(line.replace("&", "§"));
        }
        meta.setLore(loreList);
        return this;
    }

    /**
     * Fügt dem Item eine Verzauberung hinzu.
     *
     * @param ench  die Verzauberung (z. B. Enchantment.DAMAGE_ALL)
     * @param level das Level der Verzauberung
     * @return der aktuelle ItemBuilder
     */
    public ItemBuilder addEnchant(Enchantment ench, int level) {
        meta.addEnchant(ench, level, true);
        return this;
    }

    /**
     * Fügt Flags hinzu, um z. B. Verzauberungen zu verstecken.
     *
     * @param flags Flags wie {@link ItemFlag#HIDE_ENCHANTS}
     * @return der aktuelle ItemBuilder
     */
    public ItemBuilder addFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * Setzt den Besitzer eines Spieler-Kopfes.
     * Funktioniert nur, wenn das Item ein PLAYER_HEAD ist.
     *
     * @param owner Spieler, dessen Skin verwendet werden soll
     * @return der aktuelle ItemBuilder
     */
    public ItemBuilder setOwner(OfflinePlayer owner) {
        if (meta instanceof SkullMeta) {
            ((SkullMeta) meta).setOwningPlayer(owner);
        }
        return this;
    }

    /**
     * Baut das Item und gibt den fertigen ItemStack zurück.
     *
     * @return fertiger ItemStack
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
