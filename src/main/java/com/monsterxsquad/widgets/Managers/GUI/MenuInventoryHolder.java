package com.monsterxsquad.widgets.Managers.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface MenuInventoryHolder extends InventoryHolder {
    String getName();
    int getSize();
    void handleClick(Player player, ItemStack item, InventoryClickEvent event);
    void handleClose(Player player, InventoryCloseEvent event);
    boolean cancelEvent();
}
