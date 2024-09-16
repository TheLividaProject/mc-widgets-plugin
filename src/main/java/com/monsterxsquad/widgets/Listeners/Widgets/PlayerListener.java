package com.monsterxsquad.widgets.Listeners.Widgets;

import com.monsterxsquad.widgets.Events.PlayerWidgetProcessedEvent;
import com.monsterxsquad.widgets.Managers.Widgets.PlayerWidgetData;
import com.monsterxsquad.widgets.Utils.ItemUtils;
import com.monsterxsquad.widgets.Widgets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;

public class PlayerListener implements Listener {

    private final Widgets plugin;

    private final ItemUtils itemUtils = new ItemUtils();

    public PlayerListener(Widgets plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfigManager().getConfig().getBoolean("blindness-during-prompt")) {
            player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(999999, 1));
        }
    }

    @EventHandler
    public void onProcess(PlayerWidgetProcessedEvent event) {
        Player player = event.getPlayer();
        plugin.getWidgetsManager().getPlayersActiveWidgets().remove(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());
        if (playerWidgetData == null) return;

        try {
            player.getInventory().setContents(itemUtils.itemStackArrayFromBase64(playerWidgetData.getInventory()));
            plugin.getWidgetsManager().getPlayersActiveWidgets().remove(player.getUniqueId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;
        event.setCancelled(true);
    }
}
