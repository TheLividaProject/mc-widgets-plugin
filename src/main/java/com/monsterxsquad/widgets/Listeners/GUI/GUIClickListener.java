package com.monsterxsquad.widgets.Listeners.GUI;

import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Managers.GUI.MenuInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class GUIClickListener implements Listener {

    private final Widgets plugin;

    public GUIClickListener(Widgets plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        MenuInventoryHolder menuInventoryHolder = plugin.getGUIManager().getPlayerGUICache().get(player.getUniqueId());
        if (menuInventoryHolder == null) return;
        if (menuInventoryHolder.cancelEvent()) event.setCancelled(true);

        menuInventoryHolder.handleClick(player, event.getCurrentItem(), event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.PLAYER) return;
        if (event.getInventory().getType() == InventoryType.CRAFTING) return;

        Player player = (Player) event.getPlayer();

        MenuInventoryHolder menuInventoryHolder = plugin.getGUIManager().getPlayerGUICache().get(player.getUniqueId());

        if (menuInventoryHolder == null) return;

        menuInventoryHolder.handleClose(player, event);
        plugin.getGUIManager().getPlayerGUICache().remove(player.getUniqueId());
    }
}
