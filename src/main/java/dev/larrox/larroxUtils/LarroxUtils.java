package dev.larrox.larroxUtils;

import dev.larrox.larroxUtils.config.FileWriter;
import dev.larrox.larroxUtils.inventory.Inventory;
import dev.larrox.larroxUtils.inventory.Item;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LarroxUtils implements Listener {
    private static LarroxUtils instance;
    private final JavaPlugin plugin;
    private final HashMap<String, User> users;
    private Message message;
    private int defaultParticleSpeed = 0;
    private FileWriter config;

    public LarroxUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        this.users = new HashMap();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.onEnable();
    }

    public static LarroxUtils getInstance() {
        return instance;
    }

    public int getDefaultParticleSpeed() {
        return this.defaultParticleSpeed;
    }

    public void setDefaultParticleSpeed(int defaultParticleSpeed) {
        this.defaultParticleSpeed = defaultParticleSpeed;
    }

    public String fixCase(String word) {
        if (word != null && !word.isEmpty()) {
            String var10000 = word.substring(0, 1).toUpperCase();
            return var10000 + word.substring(1).toLowerCase();
        } else {
            return "";
        }
    }

    public void onEnable() {
        instance = this;
        this.message = new Message();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            this.users.put(player.getUniqueId().toString(), new User(player.getPlayer()));
        });
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        this.setupConfig();
        this.loadConfig();
        Bukkit.getOnlinePlayers().forEach(this::joined);
    }

    private void setupConfig() {
        FileWriter config = this.getFileWriter();
        config.setDefaultValue("system.database.enabled", true);
        config.setDefaultValue("system.user.enabled", true);
        config.setDefaultValue("system.inventory.enabled", true);
        config.setDefaultValue("system.item.enabled", true);
        config.setDefaultValue("system.message.enabled", true);
        config.setDefaultValue("system.particle.enabled", true);
        config.setDefaultValue("system.particle.defaultSpeed", 0);
        config.save();
    }
    private FileWriter getFileWriter() {
        if (this.config == null) {
            this.config = new FileWriter("settings/LarroxUtils/", "config.yml");
        }

        return this.config;
    }

    public boolean isSystemDisabled(LarroxUtils.SystemType type) {
        if (!this.getFileWriter().valueExist(String.format("system.%s.enabled", type.name().toLowerCase()))) {
            Bukkit.getLogger().warning("[LarroxUtils] This System doesn't exist. Used System: " + type.name().toLowerCase());
            return false;
        } else {
            boolean disabled = !this.getFileWriter().getBoolean(String.format("system.%s.enabled", type.name().toLowerCase()));
            if (disabled) {
                Bukkit.getLogger().info(type.name().toLowerCase() + " is Disabled");
            }

            return disabled;
        }
    }

    private void loadConfig() {
        FileWriter config = this.getFileWriter();
        this.setDefaultParticleSpeed(config.getInt("system.particle.defaultSpeed"));
    }

    public Message getMessage() {
        return this.message;
    }

    public MathUtils getMathUtils() {
        return new MathUtils();
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    private void onJoin(PlayerJoinEvent event) {
        this.joined(event.getPlayer());
    }

    private void joined(Player player) {
        if (!getInstance().isSystemDisabled(LarroxUtils.SystemType.USER)) {
            this.registerUser(player);
        }

        Iterator var2 = this.users.values().iterator();

        while(var2.hasNext()) {
            User user = (User)var2.next();
            if (!user.getUniqueId().equalsIgnoreCase(player.getUniqueId().toString()) && user.hasTemporaryVariable("vanished")) {
                user.vanish();
            }
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onProcess(PlayerCommandPreprocessEvent event) {
        User player = User.getUser(event.getPlayer());
        String[] args = event.getMessage().split(" ");
        if (args.length >= 1 && args[0].equalsIgnoreCase("/LarroxUtils")) {
            event.setCancelled(true);
            if (args.length == 2 && args[1].equalsIgnoreCase("cosmetics") && player.hasPermission("LarroxUtils.cosmetics.use")) {
                Inventory cosmeticsInventory = new Inventory(3, "<bold><gradient:red:blue>Cosmetics</gradient></bold>");
                cosmeticsInventory.setPattern(Inventory.InventoryPattern.DoubleBorder, Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE);
                Item angel = (new Item(Material.NETHER_STAR)).setName("<white><bold>Angel</bold></white>");
                angel.setLore(String.format("<gray>Equip the %s cosmetic</gray>", angel.getCreatedName()));
                Item devil = (new Item(Material.BLAZE_POWDER)).setName("<red><bold>Devil</bold></red>");
                devil.setLore(String.format("<gray>Equip the %s cosmetic</gray>", devil.getCreatedName()));
                Item alastor = (new Item(Material.CHAIN)).setName("<black><bold>Alastor</bold></black>");
                devil.setLore(String.format("<gray>Equip the %s cosmetic</gray>", devil.getCreatedName()));
                Item stopCosmetic = (new Item(Material.BARRIER)).setName("<bold><red>Stop Cosmetic</red></bold>").setLore("<red>Stop</red> <gray>the current Cosmetic</gray>");
                if (player.hasTemporaryVariable("hasCosmetic")) {
                    Item info = (new Item(Material.ITEM_FRAME)).setName("<gradient:blue:red><bold>Current Cosmetic</bold></gradient>").setLore(String.format("<gray>Info:</gray> <gold>%s</gold>", getInstance().fixCase((String)player.getTemporaryVariable("currentCosmetic"))));
                    cosmeticsInventory.setItem((short)4, (Item)info);
                    cosmeticsInventory.setItem((short)22, (Item)stopCosmetic);
                }

                cosmeticsInventory.addItems(angel, devil, alastor);
                cosmeticsInventory.setClickEvent((event2) -> {
                    event2.setCancelled(true);
                    if (stopCosmetic.isEqual(event2.getCurrentItem())) {
                        player.playSound(Sound.BLOCK_COMPARATOR_CLICK, 1.0F, 2.0F);
                        player.removeCosmetic();
                        cosmeticsInventory.close(player);
                    } else if (angel.isEqual(event2.getCurrentItem()) && player.hasPermission("LarroxUtils.cosmetics.angel")) {
                        player.playSound(Sound.BLOCK_COMPARATOR_CLICK, 1.0F, 2.0F);
                        player.giveCosmetic(Cosmetic.CosmeticType.ANGEL);
                        cosmeticsInventory.close(player);
                    } else if (devil.isEqual(event2.getCurrentItem()) && player.hasPermission("LarroxUtils.cosmetics.devil")) {
                        player.playSound(Sound.BLOCK_COMPARATOR_CLICK, 1.0F, 2.0F);
                        player.giveCosmetic(Cosmetic.CosmeticType.DEVIL);
                        cosmeticsInventory.close(player);
                    } else if (alastor.isEqual(event2.getCurrentItem()) && player.hasPermission("LarroxUtils.cosmetics.alastor")) {
                        player.playSound(Sound.BLOCK_COMPARATOR_CLICK, 1.0F, 2.0F);
                        player.giveCosmetic(Cosmetic.CosmeticType.ALASTOR);
                        cosmeticsInventory.close(player);
                    }

                });
                cosmeticsInventory.open(player);
            }
        }

    }

    public double getNewestVersion(int resourceId) {
        try {
            URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + resourceId);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String line;
            while((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
            System.out.println("API Response: " + String.valueOf(response));
            String jsonResponse = response.toString();
            String searchKey = "\"current_version\":\"";
            int startIndex = jsonResponse.indexOf(searchKey);
            if (startIndex == -1) {
                System.err.println("Error: 'current_version' not found in the response.");
                return 0.0D;
            } else {
                startIndex += searchKey.length();
                int endIndex = jsonResponse.indexOf("\"", startIndex);
                if (endIndex == -1) {
                    System.err.println("Error: Closing quote for 'current_version' not found.");
                    return 0.0D;
                } else {
                    String currentVersion = jsonResponse.substring(startIndex, endIndex);
                    System.out.println("Extracted version: " + currentVersion);
                    String[] parts = currentVersion.split("\\.");
                    if (parts.length < 2) {
                        System.err.println("Error: Insufficient version parts.");
                        return 0.0D;
                    } else {
                        String majorMinor = parts[0] + "." + parts[1];
                        return Double.parseDouble(majorMinor);
                    }
                }
            }
        } catch (IOException var14) {
            var14.printStackTrace();
            return 0.0D;
        } catch (NumberFormatException var15) {
            System.err.println("Error parsing version number: " + var15.getMessage());
            return 0.0D;
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    private void onQuit(PlayerQuitEvent event) {
        if (!getInstance().isSystemDisabled(LarroxUtils.SystemType.USER)) {
            this.unregisterUser(User.getUser(event.getPlayer()));
        }

    }

    private void registerUser(Player player) {
        User user = new User(player);
        FileWriter data = new FileWriter("data/LarroxUtils/users/", user.getUniqueId() + ".yml");
        if (data.valueExist("permanentValue")) {
            Iterator var4 = ((ConfigurationSection)Objects.requireNonNull(data.getYaml().getConfigurationSection("permanentValue"))).getKeys(false).iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                if (data.valueExist("permanentValue." + key)) {
                    user.setPermanentVariable("permanentValue." + key, data.getObject("permanentValue." + key));
                }
            }
        }

        this.users.put(user.getUniqueId(), user);
    }

    private void unregisterUser(User user) {
        FileWriter data = new FileWriter("data/LarroxUtils/users/", user.getUniqueId() + ".yml");
        data.setValue("name", user.getName());
        data.getYaml().setComments("name", Arrays.asList("This File was generated by LarroxUtils.", "Changing this file wont do anything.", "PermanentVariables are changeable but only if the User is not connected to the Server."));
        data.setValue("lastIP", ((InetSocketAddress)Objects.requireNonNull(user.getPlayer().getAddress())).getAddress().toString().startsWith("/") ? user.getPlayer().getAddress().getAddress().toString().substring(1) : user.getPlayer().getAddress().getAddress().toString());
        data.getYaml().setComments("lastIP", List.of("The IP-Address of an User is required to be protected and not shown to outsiders!"));
        data.setValue("firstJoined", user.getFirstPlayedDate());
        data.setValue("lastPlayed", user.getLastPlayedDate());
        data.setValue("lastLocation.world", user.getLocation().getWorld().getName());
        data.setValue("lastLocation.x", user.getBlockX());
        data.setValue("lastLocation.y", user.getBlockY());
        data.setValue("lastLocation.z", user.getBlockZ());
        if (user.getDeathLocation() != null) {
            data.setValue("lastDeath.world", ((Location)Objects.requireNonNull(user.getPlayer().getLastDeathLocation())).getWorld().getName());
            data.setValue("lastDeath.x", user.getPlayer().getLastDeathLocation().getBlockX());
            data.setValue("lastDeath.y", user.getPlayer().getLastDeathLocation().getBlockY());
            data.setValue("lastDeath.z", user.getPlayer().getLastDeathLocation().getBlockZ());
        }

        data.setValue("permanentValue", (Object)null);
        data.save();
        Iterator var3 = user.getPermanentVariables().entrySet().iterator();

        while(var3.hasNext()) {
            Entry<String, Object> entries = (Entry)var3.next();
            data.setValue("permanentValue." + (String)entries.getKey(), entries.getValue());
        }

        data.save();
        this.users.remove(user.getUniqueId());
    }

    public String translateMinutes(long minutes) {
        long years = minutes / 525600L;
        long months = minutes % 525600L / 43200L;
        long days = minutes % 525600L % 43200L / 1440L;
        long l = minutes % 525600L % 43200L % 1440L;
        long hours = l / 60L;
        long remainingMinutes = l % 60L;
        StringBuilder result = new StringBuilder();
        if (years > 0L) {
            result.append(years).append(" Jahr").append(years > 1L ? "e" : "").append(" ");
        }

        if (months > 0L) {
            result.append(months).append(" Monat").append(months > 1L ? "e" : "").append(" ");
        }

        if (days > 0L) {
            result.append(days).append(" Tag").append(days > 1L ? "e" : "").append(" ");
        }

        if (hours > 0L) {
            result.append(hours).append(" Stunde").append(hours > 1L ? "n" : "").append(" ");
        }

        if (remainingMinutes > 0L) {
            result.append(remainingMinutes).append(" Minute").append(remainingMinutes > 1L ? "n" : "").append(" ");
        }

        return result.toString();
    }

    public String getDateByMillis(long milli) {
        Date firstPlayedDate = new Date(milli);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return dateFormat.format(firstPlayedDate);
    }

    public static enum SystemType {
        INVENTORY,
        ITEM,
        MESSAGE,
        PARTICLE,
        USER,
        DATABASE;

        private static LarroxUtils.SystemType[] $values() {
            return new LarroxUtils.SystemType[]{INVENTORY, ITEM, MESSAGE, PARTICLE, USER, DATABASE};
        }
    }
}