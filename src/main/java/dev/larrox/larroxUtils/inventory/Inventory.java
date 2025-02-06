package dev.larrox.larroxUtils.inventory;

import dev.larrox.larroxUtils.LarroxUtils;
import dev.larrox.larroxUtils.Message;
import dev.larrox.larroxUtils.User;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Inventory implements Listener {
   private final org.bukkit.inventory.Inventory inventory;
   private Consumer<InventoryClickEvent> clickEventConsumer;
   private Consumer<InventoryCloseEvent> closeEventConsumer;

   public Inventory(String title, int rows) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.INVENTORY)) {
         if (rows > 54) {
            rows /= 9;
         }

         this.inventory = Bukkit.createInventory((InventoryHolder)null, 9 * rows, (new Message()).translateIntoColor(title));
         Bukkit.getPluginManager().registerEvents(this, LarroxUtils.getInstance().getPlugin());
      } else {
         this.inventory = null;
         Bukkit.getLogger().info("[LarroxUtils] The Inventory System was disabled.");
      }

   }

   public Inventory(int rows, String title) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.INVENTORY)) {
         if (rows > 54) {
            rows /= 9;
         }

         this.inventory = Bukkit.createInventory((InventoryHolder)null, 9 * rows, (new Message()).translateIntoColor(title));
         Bukkit.getPluginManager().registerEvents(this, LarroxUtils.getInstance().getPlugin());
      } else {
         this.inventory = null;
         Bukkit.getLogger().info("[LarroxUtils] The Inventory System was disabled.");
      }

   }

   public void addItem(Item item) {
      this.inventory.addItem(new ItemStack[]{item.getItemStack()});
   }

   public void addItems(Item... items) {
      Item[] var2 = items;
      int var3 = items.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Item item = var2[var4];
         this.inventory.addItem(new ItemStack[]{item.getItemStack()});
      }

   }

   public void addItems(ItemStack... items) {
      this.inventory.addItem(items);
   }

   public void addItem(ItemStack item) {
      this.inventory.addItem(new ItemStack[]{item});
   }

   public void setItem(short slot, Item item) {
      this.inventory.setItem(slot, item.getItemStack());
   }

   public void setItem(short slot, ItemStack item) {
      this.inventory.setItem(slot, item);
   }

   public void addBorder(Material material) {
      Item border = new Item(material);
      border.setTooltipVisibility(false);
      border.setName("<red> </red>");
      short size = (short)this.inventory.getSize();
      short rows = (short)(size / 9);

      short i;
      for(i = 0; i < 9; ++i) {
         this.setItem(i, border);
         this.setItem((short)((rows - 1) * 9 + i), border);
      }

      for(i = 1; i < rows - 1; ++i) {
         this.setItem((short)(i * 9), border);
         this.setItem((short)(i * 9 + 8), border);
      }

   }

   public void setPattern(InventoryPattern patern, Material... materials) {
      int i;
      switch (patern.ordinal()) {
         case 0:
            if (materials.length == 2) {
               for (i = 0; i < this.inventory.getSize(); ++i) {
                  this.inventory.setItem(i, ItemStack.of(i % 2 == 0 ? materials[0] : materials[1]));
               }
               return;
            } else {
               Bukkit.getLogger().log(Level.SEVERE, "Checked Inventory pattern required two materials. Given Materials: " + materials.length);
               break;
            }
         case 1:
            if (materials.length >= 1) {
               for (i = 0; i < this.inventory.getSize(); ++i) {
                  this.inventory.setItem(i, ItemStack.of(materials[materials.length == 1 ? 0 : (new Random()).nextInt(0, materials.length)]));
               }
               return;
            } else {
               Bukkit.getLogger().log(Level.SEVERE, "Checked Inventory pattern requires one or more materials. Given Materials: " + materials.length);
               break;
            }
         case 2:
            if (materials.length == 2) {
               Item border = new Item(materials[0]);
               border.setTooltipVisibility(false);
               border.setName("<red> </red>");
               Item border2 = new Item(materials[0]);
               border2.setTooltipVisibility(false);
               border2.setName("<red> </red>");
               short size = (short) this.inventory.getSize();
               short rows = (short) (size / 9);

               for (i = 0; i < 9; ++i) {
                  border2.setMaterial((i + (rows - 1)) % 2 == 0 ? materials[0] : materials[1]);
                  border.setMaterial(i % 2 == 0 ? materials[0] : materials[1]);
                  this.setItem((short) i, border2);
                  this.setItem((short) ((rows - 1) * 9 + i), border);
               }

               for (i = 1; i < rows - 1; ++i) {
                  border2.setMaterial((i + (rows - 1)) % 2 == 0 ? materials[0] : materials[1]);
                  this.setItem((short) (i * 9), border2);
                  this.setItem((short) (i * 9 + 8), border2);
               }
            } else {
               Bukkit.getLogger().log(Level.SEVERE, "Checked Inventory pattern required two materials. Given Materials: " + materials.length);
            }
            break;
      }
   }


   public void setClickEvent(Consumer<InventoryClickEvent> clickEvent) {
      this.clickEventConsumer = clickEvent;
   }

   @EventHandler
   private void onInventoryClick(InventoryClickEvent event) {
      if (event.getInventory().equals(this.inventory) && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && this.clickEventConsumer != null) {
         this.clickEventConsumer.accept(event);
      }

   }

   public void clear() {
      this.getInventory().clear();
   }

   public void setCloseEvent(Consumer<InventoryCloseEvent> closeEvent) {
      this.closeEventConsumer = closeEvent;
   }

   @EventHandler
   private void onInventoryClose(InventoryCloseEvent event) {
      if (event.getInventory().equals(this.inventory) && this.closeEventConsumer != null) {
         this.closeEventConsumer.accept(event);
      }

   }

   public void setItems(short[] slots, Item item) {
      short[] var3 = slots;
      int var4 = slots.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         short slot = var3[var5];
         this.setItem(slot, item);
      }

   }

   public Item getItem(short slot) {
      return this.inventory.getItem(slot) == null ? null : new Item(this.inventory.getItem(slot));
   }

   public void setItemCentered(Item item) {
      int middleSlot = this.inventory.getSize() / 2 - 5;
      this.setItem((short)middleSlot, item);
   }

   public void open(Player player) {
      player.openInventory(this.inventory);
   }

   public void open(User user) {
      Bukkit.getScheduler().runTask(LarroxUtils.getInstance().getPlugin(), () -> {
         user.getPlayer().openInventory(this.inventory);
      });
   }

   public void close(Player player) {
      player.closeInventory(Reason.PLUGIN);
   }

   public void close(User user) {
      user.getPlayer().closeInventory(Reason.PLUGIN);
   }

   public org.bukkit.inventory.Inventory getInventory() {
      return this.inventory;
   }

   public int getSize() {
      return this.getInventory().getSize();
   }

   public static enum InventoryPattern {
      CHECKED,
      RANDOM,
      DoubleBorder;

      private static InventoryPattern[] $values() {
         return new InventoryPattern[]{CHECKED, RANDOM, DoubleBorder};
      }
   }
}
