package com.monsterxsquad.widgets.Managers;

import com.monsterxsquad.widgets.Widgets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {

    private final Widgets plugin;

    private FileConfiguration config, lang;

    private final HashMap<String, FileConfiguration> widgets = new HashMap<>();

    public ConfigManager(Widgets plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
            plugin.saveResource("config.yml", false);
            plugin.saveResource("Widgets/welcome.yml", false);
            plugin.saveResource("Lang/messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "Lang/messages.yml"));

        loadWidgets();
    }

    private void loadWidgets() {
        for (File file : new File(plugin.getDataFolder(), "Widgets").listFiles()) {
            widgets.put(file.getName().replace(".yml", ""), YamlConfiguration.loadConfiguration(file));
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public HashMap<String, FileConfiguration> getWidgets() {
        return widgets;
    }

    public FileConfiguration getLang() {
        return lang;
    }
}
