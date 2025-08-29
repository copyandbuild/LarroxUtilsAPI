package dev.larrox;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name + ".yml");

        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}