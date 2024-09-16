package com.monsterxsquad.widgets.Listeners.Widgets;

import com.monsterxsquad.widgets.Widgets;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

    private final Widgets plugin;

    public EntityDamageListener(Widgets plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent event) {
        Entity entity = event.getEntity();

        if (!plugin.getConfigManager().getConfig().getBoolean("player-invulnerable-during-load")) return;

        if (!(entity instanceof Player player)) return;
        if (!plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (!plugin.getConfigManager().getConfig().getBoolean("player-invulnerable-during-load")) return;

        if (!(entity instanceof Player player)) return;
        if (!plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }
}
