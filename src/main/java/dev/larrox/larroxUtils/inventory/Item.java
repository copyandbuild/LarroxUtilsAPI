package dev.larrox.larroxUtils.inventory;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import dev.larrox.larroxUtils.LarroxUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Item {
   private final ItemStack itemStack;
   private String displayName;

   public Item(Material material) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.ITEM)) {
         this.itemStack = new ItemStack(material);
      } else {
         this.itemStack = null;
         Bukkit.getLogger().info("[larroxUitls] The Item System was disabled.");
      }

   }

   public Item(ItemStack itemStack) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.ITEM)) {
         this.itemStack = itemStack;
      } else {
         this.itemStack = null;
         Bukkit.getLogger().info("[larroxUitls] The Item System was disabled.");
      }

   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public ItemMeta getItemMeta() {
      return this.itemStack.getItemMeta();
   }

   public Item setItemMeta(ItemMeta itemMeta) {
      this.itemStack.setItemMeta(itemMeta);
      return this;
   }

   public String getName() {
      return LarroxUtils.getInstance().getMessage().getText(this.itemStack.displayName());
   }

   public String getCreatedName() {
      return this.displayName;
   }

   public Item setName(String name) {
      this.displayName = name;
      this.itemStack.editMeta((meta) -> {
         meta.displayName(LarroxUtils.getInstance().getMessage().translateIntoColor(name));
      });
      return this;
   }

   public Item setName(Component component) {
      this.displayName = LarroxUtils.getInstance().getMessage().getText(component);
      this.itemStack.editMeta((meta) -> {
         meta.displayName(component);
      });
      return this;
   }

   public static boolean isObtainable(Material material) {
      if (hasCraftingRecipe(material)) {
         return true;
      } else if (isInLootTable(material)) {
         return true;
      } else {
         return isObtainableViaSpecialMeans(material);
      }
   }

   private static boolean hasCraftingRecipe(Material material) {
      Iterator recipes = Bukkit.recipeIterator();

      Recipe recipe;
      do {
         if (!recipes.hasNext()) {
            return false;
         }

         recipe = (Recipe)recipes.next();
      } while(recipe.getResult().getType() != material);

      return true;
   }

   private static boolean isInLootTable(Material material) {
      List<NamespacedKey> lootTableKeys = List.of(NamespacedKey.minecraft("chests/village_blacksmith"), NamespacedKey.minecraft("chests/end_city_treasure"), NamespacedKey.minecraft("chests/shipwreck_treasure"), NamespacedKey.minecraft("entities/zombie"), NamespacedKey.minecraft("entities/skeleton"));
      Iterator var2 = lootTableKeys.iterator();

      LootTable lootTable;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         NamespacedKey key = (NamespacedKey)var2.next();
         lootTable = Bukkit.getLootTable(key);
      } while(lootTable == null || !(lootTable instanceof Lootable));

      return true;
   }

   private static boolean isObtainableViaSpecialMeans(Material material) {
      switch(material) {
      case GRASS_BLOCK:
      case MYCELIUM:
      case PODZOL:
         return true;
      case EMERALD:
      case ENCHANTED_BOOK:
         return true;
      default:
         return false;
      }
   }

   public Item setMaterial(Material material) {
      this.itemStack.setType(material);
      return this;
   }

   public <T, Z> void setCustomTag(String key, PersistentDataType<T, Z> type, Z value) {
      this.itemStack.editMeta((meta) -> {
         meta.getPersistentDataContainer().set(new NamespacedKey(JavaPlugin.getProvidingPlugin(LarroxUtils.getInstance().getPlugin().getClass()), key), type, value);
      });
   }

   public <T, Z> Z getCustomTag(String key, PersistentDataType<T, Z> type) {
      return this.itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(JavaPlugin.getProvidingPlugin(LarroxUtils.getInstance().getPlugin().getClass()), key), type);
   }

   public Item removeCustomTag(String key) {
      this.itemStack.editMeta((meta) -> {
         meta.getPersistentDataContainer().remove(new NamespacedKey(JavaPlugin.getProvidingPlugin(LarroxUtils.getInstance().getPlugin().getClass()), key));
      });
      return this;
   }

   public Item clone() {
      return new Item(this.itemStack.clone());
   }

   public Item addFlag(ItemFlag flag) {
      this.itemStack.editMeta((meta) -> {
         meta.addItemFlags(new ItemFlag[]{flag});
      });
      return this;
   }

   public Item removeFlag(ItemFlag flag) {
      this.itemStack.editMeta((meta) -> {
         meta.removeItemFlags(new ItemFlag[]{flag});
      });
      return this;
   }

   public Item setTooltipVisibility(boolean visibility) {
      if (!visibility) {
         this.addFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
         this.addFlag(ItemFlag.HIDE_ARMOR_TRIM);
         this.addFlag(ItemFlag.HIDE_ATTRIBUTES);
         this.addFlag(ItemFlag.HIDE_DYE);
         this.addFlag(ItemFlag.HIDE_DESTROYS);
         this.addFlag(ItemFlag.HIDE_ENCHANTS);
         this.addFlag(ItemFlag.HIDE_STORED_ENCHANTS);
         this.addFlag(ItemFlag.HIDE_UNBREAKABLE);
      } else {
         this.removeFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
         this.removeFlag(ItemFlag.HIDE_ARMOR_TRIM);
         this.removeFlag(ItemFlag.HIDE_ATTRIBUTES);
         this.removeFlag(ItemFlag.HIDE_DYE);
         this.removeFlag(ItemFlag.HIDE_DESTROYS);
         this.removeFlag(ItemFlag.HIDE_ENCHANTS);
         this.removeFlag(ItemFlag.HIDE_STORED_ENCHANTS);
         this.removeFlag(ItemFlag.HIDE_UNBREAKABLE);
      }

      return this;
   }

   public int getDurability() {
      return this.itemStack.getItemMeta() instanceof Damageable ? ((Damageable)this.itemStack.getItemMeta()).getDamage() : 0;
   }

   public Item setDurability(int durability) {
      if (this.itemStack.getItemMeta() instanceof Damageable) {
         this.itemStack.editMeta((meta) -> {
            Damageable damageable = (Damageable)meta;
            damageable.setDamage(durability);
         });
      }

      return this;
   }

   public Item setMaxDurability(int durability) {
      if (this.itemStack.getItemMeta() instanceof Damageable) {
         this.itemStack.editMeta((meta) -> {
            Damageable damageable = (Damageable)meta;
            damageable.setMaxDamage(durability);
         });
      }

      return this;
   }

   public Item addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
      this.itemStack.editMeta((meta) -> {
         meta.addAttributeModifier(attribute, modifier);
      });
      return this;
   }

   public boolean isEqual(Item other) {
      return this.itemStack.isSimilar(other.getItemStack());
   }

   public boolean isEqual(ItemStack other) {
      return this.itemStack.isSimilar(other);
   }

   public Item setColoredWool(DyeColor color) {
      this.setMaterial(Material.valueOf(color.name() + "_WOOL"));
      return this;
   }

   public Item setColoredDye(DyeColor color) {
      this.setMaterial(Material.valueOf(color.name() + "_DYE"));
      return this;
   }

   public Item setPotion(PotionType type, boolean extended, boolean upgraded) {
      new Item(Material.POTION);
      this.itemStack.editMeta((meta) -> {
         PotionMeta potionMeta = (PotionMeta)meta;
         potionMeta.setBasePotionData(new PotionData(type, extended, upgraded));
      });
      return this;
   }

   public Item removeAttributeModifiers() {
      this.itemStack.editMeta((meta) -> {
         ((Multimap)Objects.requireNonNull(meta.getAttributeModifiers())).clear();
      });
      return this;
   }

   public Item addEnchantment(Enchantment enchantment, int level, boolean ignoreRestrictions) {
      this.itemStack.addUnsafeEnchantment(enchantment, level);
      return this;
   }

   public Item removeEnchantment(Enchantment enchantment) {
      this.itemStack.removeEnchantment(enchantment);
      return this;
   }

   public Item setAmount(short amount) {
      this.itemStack.setAmount(amount);
      return this;
   }

   public Item setGlowing(boolean glow) {
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      this.itemStack.editMeta((meta) -> {
         meta.setEnchantmentGlintOverride(glow);
      });
      return this;
   }

   public Item setLore(String... lore) {
      List<Component> lores = new ArrayList();
      String[] var3 = lore;
      int var4 = lore.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String curLore = var3[var5];
         lores.add(LarroxUtils.getInstance().getMessage().translateIntoColor(curLore));
      }

      this.setLore((List)lores);
      return this;
   }

   public Item setLore(List<Component> lore) {
      this.itemStack.editMeta((meta) -> {
         meta.lore(lore);
      });
      return this;
   }

   public Item setLore(ArrayList<String> lore) {
      List<Component> lores = new ArrayList();
      Iterator var3 = lore.iterator();

      while(var3.hasNext()) {
         String curLore = (String)var3.next();
         lores.add(LarroxUtils.getInstance().getMessage().translateIntoColor(curLore));
      }

      this.setLore((List)lores);
      return this;
   }

   public Item addLore(String... lore) {
      List<Component> lores = new ArrayList();
      if (this.itemStack.getItemMeta().lore() != null) {
         lores.addAll(Objects.requireNonNull(this.itemStack.getItemMeta().lore()));
      }

      String[] var3 = lore;
      int var4 = lore.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String curLore = var3[var5];
         lores.add(LarroxUtils.getInstance().getMessage().translateIntoColor(curLore));
      }

      this.addLore(lores);
      return this;
   }

   public Item addLore(List<Component> lore) {
      this.itemStack.editMeta((meta) -> {
         meta.lore(lore);
      });
      return this;
   }

   public Item addLore(ArrayList<String> lore) {
      List<Component> lores = new ArrayList();
      if (this.itemStack.getItemMeta().lore() != null) {
         lores.addAll(Objects.requireNonNull(this.itemStack.getItemMeta().lore()));
      }

      Iterator var3 = lore.iterator();

      while(var3.hasNext()) {
         String curLore = (String)var3.next();
         lores.add(LarroxUtils.getInstance().getMessage().translateIntoColor(curLore));
      }

      this.addLore(lores);
      return this;
   }

   public Item removeLore(short index) {
      this.itemStack.editMeta((meta) -> {
         (Objects.requireNonNull(meta.lore())).remove(index);
      });
      return this;
   }

   public Item setPlayerOwner(OfflinePlayer owner) {
      this.setMaterial(Material.PLAYER_HEAD);
      this.itemStack.editMeta((meta) -> {
         ((SkullMeta)meta).setOwningPlayer(owner);
      });
      return this;
   }

   public Item setHeadTexture(String texture) {
      this.setMaterial(Material.PLAYER_HEAD);
      SkullMeta meta = (SkullMeta)this.itemStack.getItemMeta();
      if (meta == null) {
         return this;
      } else {
         PlayerProfile profile = meta.getPlayerProfile();
         if (profile == null) {
            profile = Bukkit.createProfile(UUID.randomUUID());
         }

         profile.setProperty(new ProfileProperty("textures", texture));
         meta.setPlayerProfile(profile);
         this.itemStack.setItemMeta(meta);
         return this;
      }
   }
}
