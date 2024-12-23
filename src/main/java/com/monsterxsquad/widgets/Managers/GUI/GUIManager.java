package com.monsterxsquad.widgets.Managers.GUI;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GUIManager {

    private final HashMap<UUID, MenuInventoryHolder> playerGUICache = new HashMap<>();

    public void setGUI(Player player, MenuInventoryHolder menuInventoryHolder) {
        if (playerGUICache.get(player.getUniqueId()) != null) {
            MenuInventoryHolder oldInventoryHolder = playerGUICache.get(player.getUniqueId());

            if (oldInventoryHolder.getSize() == menuInventoryHolder.getSize()) {
                player.openInventory(menuInventoryHolder.getInventory());
                oldInventoryHolder.getInventory().setContents(menuInventoryHolder.getInventory().getContents());
                playerGUICache.put(player.getUniqueId(), menuInventoryHolder);
                return;
            }
        }

        player.closeInventory();
        player.openInventory(menuInventoryHolder.getInventory());
        playerGUICache.put(player.getUniqueId(), menuInventoryHolder);
    }

    public HashMap<UUID, MenuInventoryHolder> getPlayerGUICache() {
        return playerGUICache;
    }
}
