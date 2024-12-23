package com.monsterxsquad.widgets;

import com.monsterxsquad.widgets.Listeners.Widgets.EntityDamageListener;
import com.monsterxsquad.widgets.Listeners.Widgets.PlayerItemListener;
import com.monsterxsquad.widgets.Listeners.Widgets.PlayerListener;
import com.monsterxsquad.widgets.Listeners.Widgets.ResourcePackListener;
import com.monsterxsquad.widgets.Managers.Commands.WidgetsCommandManager;
import com.monsterxsquad.widgets.Managers.GUI.GUIManager;
import com.monsterxsquad.widgets.Listeners.GUI.GUIClickListener;
import com.monsterxsquad.widgets.Managers.ConfigManager;
import com.monsterxsquad.widgets.Managers.ResourcePack.ResourcePackManager;
import com.monsterxsquad.widgets.Managers.Widgets.WidgetsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Widgets extends JavaPlugin {

    private ConfigManager configManager;

    private WidgetsManager widgetsManager;
    private GUIManager GUIManager;

    private ResourcePackManager resourcePackManager;

    @Override
    public void onEnable() {
        loadManagers();
        loadListeners();

        loadCommands();
    }

    @Override
    public void onDisable() {
        widgetsManager.giveAllItemsBack();
    }

    private void loadManagers() {
        configManager = new ConfigManager(this);

        widgetsManager = new WidgetsManager(this);
        GUIManager = new GUIManager();

        resourcePackManager = new ResourcePackManager(this);
    }

    private void loadCommands() {
        new WidgetsCommandManager(this);
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ResourcePackListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerItemListener(this), this);

        Bukkit.getPluginManager().registerEvents(new GUIClickListener(this), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public WidgetsManager getWidgetsManager() {
        return widgetsManager;
    }

    public GUIManager getGUIManager() {
        return GUIManager;
    }

    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }
}
