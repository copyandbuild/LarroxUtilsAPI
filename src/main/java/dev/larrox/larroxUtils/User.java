package dev.larrox.larroxUtils;

import dev.larrox.larroxUtils.config.FileWriter;
import dev.larrox.larroxUtils.inventory.Inventory;
import dev.larrox.larroxUtils.inventory.Item;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.BanList.Type;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class User implements Listener {
   private final Player player;
   private final Map<String, Object> temporaryVariables = new HashMap();
   private final Map<String, Object> permanentVariables = new HashMap();
   private final MiniMessage miniMessage = MiniMessage.miniMessage();
   private Consumer<AsyncChatEvent> chatEventConsumer;
   private Consumer<PlayerMoveEvent> moveEventConsumer;

   public User(Player player) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.USER)) {
         this.player = player.getPlayer();
         Bukkit.getPluginManager().registerEvents(this, LarroxUtils.getInstance().getPlugin());
      } else {
         this.player = null;
      }

   }

   public static List<User> getOnlineUsers() {
      List<User> users = new ArrayList();
      Iterator var1 = LarroxUtils.getInstance().getUsers().entrySet().iterator();

      while(var1.hasNext()) {
         Entry<String, User> userEntry = (Entry)var1.next();
         users.add((User)userEntry.getValue());
      }

      return users;
   }

   public static User getUser(String uuid) {
      return (User)LarroxUtils.getInstance().getUsers().get(uuid);
   }

   public static User getUser(UUID uuid) {
      return (User)LarroxUtils.getInstance().getUsers().get(uuid.toString());
   }

   public static User getUser(Player player) {
      return (User)LarroxUtils.getInstance().getUsers().get(player.getPlayer().getUniqueId().toString());
   }

   public Location getLocation() {
      return this.player.getLocation();
   }

   public Location getDeathLocation() {
      return this.player.getLastDeathLocation();
   }

   public DamageCause getDamageCause() {
      return ((EntityDamageEvent)Objects.requireNonNull(this.player.getLastDamageCause())).getCause();
   }

   public String getFirstPlayedDate() {
      return LarroxUtils.getInstance().getDateByMillis(this.player.getFirstPlayed());
   }

   public String getLastPlayedDate() {
      return LarroxUtils.getInstance().getDateByMillis(this.player.getLastSeen());
   }

   public String getFirstPlayedTime() {
      return LarroxUtils.getInstance().translateMinutes(this.player.getFirstPlayed());
   }

   public String getLastPlayedTime() {
      return LarroxUtils.getInstance().translateMinutes(this.player.getLastSeen());
   }

   public World getWorld() {
      return this.player.getWorld();
   }

   public boolean isOp() {
      return this.player.isOp();
   }

   public boolean isOnline() {
      return true;
   }

   public String getUniqueId() {
      return this.player.getUniqueId().toString();
   }

   public int getBlockX() {
      return this.getLocation().getBlockX();
   }

   public int getBlockY() {
      return this.getLocation().getBlockY();
   }

   public int getBlockZ() {
      return this.getLocation().getBlockZ();
   }

   public double getX() {
      return this.getLocation().getX();
   }

   public float getYaw() {
      return this.getLocation().getYaw();
   }

   public float getPitch() {
      return this.getLocation().getPitch();
   }

   public double getY() {
      return this.getLocation().getY();
   }

   public double getZ() {
      return this.getLocation().getZ();
   }

   public Item getItemInMainHand() {
      return this.player.getInventory().getItemInMainHand().getType() != Material.AIR ? new Item(this.player.getInventory().getItemInMainHand()) : null;
   }

   public Item getItemInOffHand() {
      return this.player.getInventory().getItemInOffHand().getType() != Material.AIR ? new Item(this.player.getInventory().getItemInOffHand()) : null;
   }

   public boolean toggleFlight() {
      this.player.setAllowFlight(!this.player.getAllowFlight());
      return this.player.getAllowFlight();
   }

   public void setFlySpeed(double speed) {
      this.player.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(speed);
   }

   public void resetFlySpeed() {
      this.setWalkSpeed(this.player.getAttribute(Attribute.GENERIC_FLYING_SPEED).getDefaultValue());
   }

   public void setWalkSpeed(double speed) {
      this.player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
   }

   public void resetWalkSpeed() {
      this.setWalkSpeed(this.player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getDefaultValue());
   }

   public void setScale(double scale) {
      this.player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(scale);
   }

   public void resetScale() {
      this.setScale(this.player.getAttribute(Attribute.GENERIC_SCALE).getDefaultValue());
   }

   public boolean compareLocations(Location location, Location location2) {
      return location.equals(location2);
   }

   public boolean compareLocations(int x, int y, int z, int x2, int y2, int z2) {
      return x == x2 && y == y2 && z == z2;
   }

   public boolean compareLocations(double x, double y, double z, double x2, double y2, double z2) {
      return Double.compare(x, x2) == 0 && Double.compare(y, y2) == 0 && Double.compare(z, z2) == 0;
   }

   public void giveCosmetic(final Cosmetic.CosmeticType type) {
      if (!this.hasTemporaryVariable("hasCosmetic")) {
         this.setTemporaryVariable("hasCosmetic", true);
         this.setTemporaryVariable("currentCosmetic", type.name());
         (new BukkitRunnable() {
            public void run() {
               if (User.this.player != null && User.this.player.isOnline() && User.this.hasTemporaryVariable("hasCosmetic")) {
                  new Cosmetic(type, User.getUser(User.this.player));
               } else {
                  this.cancel();
               }

            }
         }).runTaskTimerAsynchronously(LarroxUtils.getInstance().getPlugin(), 0L, 1L);
      }

   }

   public void removeCosmetic() {
      this.removeTemporaryVariable("hasCosmetic");
      this.removeTemporaryVariable("currentCosmetic");
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   private void chatEvent(AsyncChatEvent event) {
      if (event.getPlayer().equals(this.player) && this.isTimeouted()) {
         event.setCancelled(true);
      }

   }

   public void setChatEvent(Consumer<AsyncChatEvent> chatEvent) {
      this.chatEventConsumer = chatEvent;
   }

   @EventHandler
   private void onChatEvent(AsyncChatEvent event) {
      if (event.getPlayer().equals(this.player) && this.chatEventConsumer != null) {
         this.chatEventConsumer.accept(event);
      }

   }

   @EventHandler
   private void onMoveEvent(PlayerMoveEvent event) {
      if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase(this.getUniqueId()) && this.hasTemporaryVariable("freezed") && event.getFrom().getBlockY() != event.getTo().getBlockY()) {
         event.setCancelled(true);
      }

   }

   public void setMoveEvent(Consumer<PlayerMoveEvent> moveEvent) {
      this.moveEventConsumer = moveEvent;
   }

   @EventHandler
   private void onPlayerMove(PlayerMoveEvent event) {
      if (event.getPlayer().equals(this.player) && this.moveEventConsumer != null) {
         this.moveEventConsumer.accept(event);
      }

   }

   public void showPlayer(Player target) {
      this.player.showPlayer(LarroxUtils.getInstance().getPlugin(), target);
   }

   public void hidePlayer(Player target) {
      this.player.hidePlayer(LarroxUtils.getInstance().getPlugin(), target);
   }

   public void showPlayer(User target) {
      this.player.showPlayer(LarroxUtils.getInstance().getPlugin(), target.getPlayer());
   }

   public void hidePlayer(User target) {
      this.player.hidePlayer(LarroxUtils.getInstance().getPlugin(), target.getPlayer());
   }

   public Block getHighestBlock() {
      return this.getLocation().getWorld().getHighestBlockAt(this.getLocation());
   }

   public Player getPlayer() {
      return this.player.getPlayer();
   }

   public String getName() {
      return LarroxUtils.getInstance().getMessage().getText(this.player.getPlayer().displayName());
   }

   public UUID getUUID() {
      return this.player.getPlayer().getUniqueId();
   }

   public void healPlayer(double amount) {
      double newHealth = Math.min(this.player.getPlayer().getHealth() + amount, ((AttributeInstance)Objects.requireNonNull(this.player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH))).getValue());
      this.player.getPlayer().setHealth(newHealth);
   }

   public boolean hasPermission(Permission permission) {
      return this.player.hasPermission(permission);
   }

   public boolean hasPermission(String permission) {
      return this.player.hasPermission(permission);
   }

   public String getAddress() {
      return this.getData().getString("lastIP");
   }

   public FileWriter getData() {
      return new FileWriter("data/LarroxUtils/users/", this.getUniqueId() + ".yml");
   }

   public void setMaxHealth(double health) {
      ((AttributeInstance)Objects.requireNonNull(this.player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH))).setBaseValue(health);
   }

   public void setPlayerHealth(double health) {
      this.player.getPlayer().setHealth(Math.min(health, ((AttributeInstance)Objects.requireNonNull(this.player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH))).getValue()));
   }

   public void resetPlayerHealth(double health) {
      this.player.getPlayer().setHealth(((AttributeInstance)Objects.requireNonNull(this.player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH))).getValue());
   }

   public void vanish() {
      this.setTemporaryVariable("vanished", true);
      Iterator var1 = getOnlineUsers().iterator();

      User user;
      while(var1.hasNext()) {
         user = (User)var1.next();
         user.hidePlayer(this.player);
      }

      var1 = getOnlineUsers().iterator();

      while(var1.hasNext()) {
         user = (User)var1.next();
         if (!user.getUniqueId().equalsIgnoreCase(this.player.getUniqueId().toString()) && user.hasTemporaryVariable("vanished")) {
            this.player.hidePlayer(LarroxUtils.getInstance().getPlugin(), user.getPlayer());
         }
      }

   }

   public void sendPlayerToServer(String server) {
      try {
         ByteArrayOutputStream b = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(b);
         out.writeUTF("Connect");
         out.writeUTF(server);
         this.player.sendPluginMessage(LarroxUtils.getInstance().getPlugin(), "BungeeCord", b.toByteArray());
         b.close();
         out.close();
      } catch (Exception var4) {
         System.err.println("[LarroxUtils] " + var4.getMessage());
      }

   }

   public void unvanish() {
      this.removeTemporaryVariable("vanished");
      Iterator var1 = getOnlineUsers().iterator();

      User user;
      while(var1.hasNext()) {
         user = (User)var1.next();
         user.showPlayer(this.player);
      }

      var1 = getOnlineUsers().iterator();

      while(var1.hasNext()) {
         user = (User)var1.next();
         if (!user.getUniqueId().equalsIgnoreCase(this.player.getUniqueId().toString()) && user.hasTemporaryVariable("vanished")) {
            this.player.hidePlayer(LarroxUtils.getInstance().getPlugin(), user.getPlayer());
         }
      }

   }

   public void setFoodLevel(int level) {
      this.player.getPlayer().setFoodLevel(Math.min(level, 20));
   }

   public void resetHunger() {
      this.player.getPlayer().setFoodLevel(20);
      this.player.getPlayer().setSaturation(5.0F);
   }

   public void setGameMode(GameMode gameMode) {
      this.player.getPlayer().setGameMode(gameMode);
   }

   public boolean isInGameMode(GameMode gameMode) {
      return this.player.getPlayer().getGameMode() == gameMode;
   }

   public GameMode lastGameMode() {
      return this.player.getPlayer().getPreviousGameMode();
   }

   public void freeze() {
      this.player.getPlayer().setWalkSpeed(0.0F);
      this.player.getPlayer().setFlySpeed(0.0F);
      this.setTemporaryVariable("freezed", true);
   }

   public void unfreeze() {
      this.player.getPlayer().setWalkSpeed(0.2F);
      this.player.getPlayer().setFlySpeed(0.1F);
      this.removeTemporaryVariable("freezed");
   }

   public void sendMessage(String message, Object... objects) {
      this.player.sendMessage((new Message()).translateIntoColor(String.format(message, objects)));
   }

   public boolean hasCompletedAdvancement(String advancementKey) {
      Iterator advancements = Bukkit.advancementIterator();

      Advancement advancement;
      do {
         if (!advancements.hasNext()) {
            return false;
         }

         advancement = (Advancement)advancements.next();
      } while(!advancement.getKey().toString().equalsIgnoreCase(advancementKey));

      AdvancementProgress progress = this.player.getAdvancementProgress(advancement);
      return progress.isDone();
   }

   public void sendMessage(String message) {
      this.player.sendMessage((new Message()).translateIntoColor(message));
   }

   public void sendActionBar(String message, Object... objects) {
      this.player.sendActionBar(LarroxUtils.getInstance().getMessage().translateIntoColor(String.format(message, objects)));
   }

   public void sendActionBar(String message) {
      this.player.sendActionBar(LarroxUtils.getInstance().getMessage().translateIntoColor(message));
   }

   public void sendTitle(String title, String subtitle) {
      this.player.getPlayer().sendTitlePart(TitlePart.TIMES, Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(3L), Duration.ofSeconds(1L)));
      this.player.getPlayer().sendTitlePart(TitlePart.TITLE, (new Message()).translateIntoColor(title));
      this.player.getPlayer().sendTitlePart(TitlePart.SUBTITLE, (new Message()).translateIntoColor(subtitle));
   }

   public org.bukkit.scoreboard.Scoreboard getScoreboard() {
      return this.player.getScoreboard();
   }

   public void setScoreboard(org.bukkit.scoreboard.Scoreboard scoreboard) {
      this.player.setScoreboard(scoreboard);
   }

   public void playSound(Sound sound) {
      this.player.playSound(this.player, sound, 1.0F, 1.0F);
   }

   public void playSound(Sound sound, float volume, float pitch) {
      this.player.playSound(this.player, sound, volume, pitch);
   }

   public void teleport(Player target) {
      this.player.getPlayer().teleport(target);
   }

   public void teleport(Location target) {
      this.player.getPlayer().teleport(target);
   }

   public void teleport(Player target, Particle effect) {
      this.player.getPlayer().teleport(target);
   }

   public void teleportWithCooldown(final Location location, int time) {
      final int[] tmpTime = new int[]{time};
      (new BukkitRunnable() {
         final int x = User.this.getBlockX();
         final int y = User.this.getBlockY();
         final int z = User.this.getBlockZ();

         public void run() {
            int x2 = User.this.getBlockX();
            int y2 = User.this.getBlockY();
            int z2 = User.this.getBlockZ();
            User.this.sendActionBar("&6&l" + tmpTime[0]);
            if (!User.this.compareLocations(this.x, this.y, this.z, x2, y2, z2)) {
               User.this.playSound(Sound.ITEM_WOLF_ARMOR_CRACK);
               User.this.sendActionBar("&cTeleportvorgang abgebrochen");
               this.cancel();
            } else if (tmpTime[0] == 0) {
               User.this.playSound(Sound.ENTITY_PLAYER_TELEPORT);
               User.this.teleport(location);
               User.this.sendMessage("&aDu wurdest teleportiert");
               User.this.addEffect(PotionEffectType.BLINDNESS, (int)30, (int)0);
               this.cancel();
            } else {
               User.this.playSound(Sound.ENTITY_PLAYER_LEVELUP);
            }

            int var10002 = tmpTime[0]--;
         }
      }).runTaskTimer(LarroxUtils.getInstance().getPlugin(), 0L, 20L);
   }

   public void teleportWithCooldown(final Player target, int time) {
      final int[] tmpTime = new int[]{time};
      (new BukkitRunnable() {
         final int x = User.this.getBlockX();
         final int y = User.this.getBlockY();
         final int z = User.this.getBlockZ();

         public void run() {
            int x2 = User.this.getBlockX();
            int y2 = User.this.getBlockY();
            int z2 = User.this.getBlockZ();
            User.this.sendActionBar("&6&l" + tmpTime[0]);
            if (!User.this.compareLocations(this.x, this.y, this.z, x2, y2, z2)) {
               User.this.playSound(Sound.ITEM_WOLF_ARMOR_CRACK);
               User.this.sendActionBar("&cTeleportvorgang abgebrochen");
               this.cancel();
            } else if (tmpTime[0] == 0) {
               User.this.playSound(Sound.ENTITY_PLAYER_TELEPORT);
               User.this.teleport(target);
               User.this.sendMessage("&aDu wurdest teleportiert");
               User.this.addEffect(PotionEffectType.BLINDNESS, (int)30, (int)0);
               this.cancel();
            } else {
               User.this.playSound(Sound.ENTITY_PLAYER_LEVELUP);
            }

            int var10002 = tmpTime[0]--;
         }
      }).runTaskTimer(LarroxUtils.getInstance().getPlugin(), 0L, 20L);
   }

   public void teleport(Location target, Particle effect) {
      this.player.getPlayer().teleport(target);
   }

   public void setTemporaryVariable(String key, Object value) {
      this.temporaryVariables.put(key, value);
   }

   public Object getTemporaryVariable(String key) {
      return this.temporaryVariables.get(key);
   }

   public boolean hasTemporaryVariable(String key) {
      return this.temporaryVariables.containsKey(key);
   }

   public void removeTemporaryVariable(String key) {
      this.temporaryVariables.remove(key);
   }

   public Map<String, Object> getPermanentVariables() {
      return this.permanentVariables;
   }

   public void setPermanentVariable(String key, Object value) {
      this.permanentVariables.put(key, value);
   }

   public Object getPermanentVariable(String key) {
      return this.permanentVariables.get(key);
   }

   public boolean hasPermanentVariable(String key) {
      return this.permanentVariables.containsKey(key);
   }

   public void removePermanentVariable(String key) {
      this.permanentVariables.remove(key);
   }

   public void addExperience(short amount) {
      this.player.getPlayer().giveExp(amount);
   }

   public void setLevel(short level) {
      this.player.getPlayer().setLevel(level);
   }

   public int getLevel() {
      return this.player.getPlayer().getLevel();
   }

   public void resetExperience() {
      this.player.getPlayer().setExp(0.0F);
      this.player.getPlayer().setLevel(0);
   }

   public void addEffect(PotionEffectType potionEffect, short duration, short amplifier) {
      this.player.getPlayer().addPotionEffect(new PotionEffect(potionEffect, duration, amplifier));
   }

   public void addEffect(PotionEffectType potionEffect, int duration, int amplifier) {
      this.player.getPlayer().addPotionEffect(new PotionEffect(potionEffect, duration, amplifier));
   }

   public void removeEffect(PotionEffectType type) {
      this.player.getPlayer().removePotionEffect(type);
   }

   public void clearEffects() {
      this.player.getPlayer().getActivePotionEffects().clear();
   }

   public void kick(String reason) {
      this.player.getPlayer().kick(LarroxUtils.getInstance().getMessage().translateIntoColor(reason));
   }

   public void ban(String reason) {
      this.player.getPlayer().getServer().getBanList(Type.NAME).addBan(this.player.getPlayer().getName(), reason, (Date)null, (String)null);
      this.kick(reason);
   }

   public boolean isTimeouted() {
      return this.hasTemporaryVariable("timeout");
   }

   public int getTimeoutDuration() {
      return (Integer)this.getTemporaryVariable("timeout");
   }

   public void timeout(int duration) {
      this.setTemporaryVariable("timeout", duration);
      (new BukkitRunnable() {
         public void run() {
            if ((Integer)User.this.getTemporaryVariable("timeout") >= 1) {
               User.this.setTemporaryVariable("timeout", (Integer)User.this.getTemporaryVariable("timeout") - 1);
            } else {
               User.this.removeTemporaryVariable("timeout");
               this.cancel();
            }

         }
      }).runTaskTimerAsynchronously(LarroxUtils.getInstance().getPlugin(), 0L, 20L);
   }

   public void giveItem(Item item) {
      this.giveItem(item.getItemStack());
   }

   public void giveItem(ItemStack item) {
      this.player.getInventory().addItem(new ItemStack[]{item});
   }

   public void openInventory(Inventory inventory) {
      inventory.open(this);
   }

   public void closeInventory() {
      this.player.closeInventory(Reason.PLUGIN);
   }

   public void openInventory(org.bukkit.inventory.Inventory inventory) {
      this.player.openInventory(inventory);
   }
}
