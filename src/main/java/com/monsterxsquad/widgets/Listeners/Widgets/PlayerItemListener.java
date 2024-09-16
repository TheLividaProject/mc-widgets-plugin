package com.monsterxsquad.widgets.Listeners.Widgets;

import com.monsterxsquad.widgets.Widgets;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class PlayerItemListener implements Listener {

    private final Widgets plugin;

    public PlayerItemListener(Widgets plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (!plugin.getConfigManager().getConfig().getBoolean("player-invulnerable-during-load")) return;

        Player player = event.getPlayer();
        if (!plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;
        if (!plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }
}
