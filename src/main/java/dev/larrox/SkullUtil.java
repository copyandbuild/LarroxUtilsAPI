package dev.larrox;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtil {

    /**
     * Setzt den Besitzer eines Schädels.
     *
     * @param skull  Der Skull-Block (z. B. ein Spieler-Kopf)
     * @param owner  Der Spieler, dessen Skin verwendet werden soll
     * @return SkullMeta mit dem gesetzten Owner
     */
    public static SkullMeta setOwner(SkullMeta skull, OfflinePlayer owner) {
        skull.setOwningPlayer(owner);
        return skull;
    }
}
