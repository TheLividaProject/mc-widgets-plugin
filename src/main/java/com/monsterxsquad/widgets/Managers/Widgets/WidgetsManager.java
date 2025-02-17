package com.monsterxsquad.widgets.Managers.Widgets;

import com.monsterxsquad.widgets.Events.PlayerWidgetDisplayEvent;
import com.monsterxsquad.widgets.Events.PlayerWidgetProcessedEvent;
import com.monsterxsquad.widgets.GUI.WidgetMenu;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class WidgetsManager {

    private final Widgets plugin;

    private final HashMap<UUID, PlayerWidgetData> widgetsDataCache = new HashMap<>();

    private final HashSet<UUID> playersActiveWidgets = new HashSet<>();

    private final HashSet<UUID> processingPlayers = new HashSet<>();

    private final ItemUtils itemUtils = new ItemUtils();
    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetsManager(Widgets plugin) {
        this.plugin = plugin;
    }

    public void displayWidget(Player player) {
        PlayerWidgetData playerWidgetData = widgetsDataCache.get(player.getUniqueId());

        if (!plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getBoolean("options.enabled")) {
            player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("widgets.disabled")));
            widgetsDataCache.remove(player.getUniqueId());
            return;
        }

        playersActiveWidgets.add(player.getUniqueId());
        playerWidgetData.setInventory(itemUtils.itemStackArrayToBase64(player.getInventory().getContents()));

        player.getInventory().clear();
        plugin.getGUIManager().setGUI(player, new WidgetMenu(plugin, player));

        Bukkit.getServer().getPluginManager().callEvent(new PlayerWidgetDisplayEvent(player, playerWidgetData.getId(), plugin.getConfigManager().getWidgets().get(playerWidgetData.getId())));
    }

    public void giveAllItemsBack() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerWidgetData playerWidgetData = widgetsDataCache.get(player.getUniqueId());
            if (playerWidgetData == null) continue;

            try {
                player.getInventory().setContents(itemUtils.itemStackArrayFromBase64(playerWidgetData.getInventory()));
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
        }
    }

    public String shift1013(Player player) {
        PlayerWidgetData playerWidgetData = widgetsDataCache.get(player.getUniqueId());
        return plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("unicodes.shift-1013");
    }

    public String shift1536(Player player) {
        PlayerWidgetData playerWidgetData = widgetsDataCache.get(player.getUniqueId());
        return plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("unicodes.shift-1536");
    }

    public String widgetTitle(Player player) {
        PlayerWidgetData playerWidgetData = widgetsDataCache.get(player.getUniqueId());
        return plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("options.background.colour") + shift1013(player) + plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("unicodes.background") + shift1536(player) + "<#FFFFFF>" + plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("unicodes.main-screen");
    }

    public HashMap<UUID, PlayerWidgetData> getWidgetsDataCache() {
        return widgetsDataCache;
    }

    public HashSet<UUID> getPlayersActiveWidgets() {
        return playersActiveWidgets;
    }

    public HashSet<UUID> getProcessingPlayers() {
        return processingPlayers;
    }
}
