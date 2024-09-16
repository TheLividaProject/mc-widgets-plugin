package com.monsterxsquad.widgets.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerWidgetProcessedEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final String widgetID;
    private final FileConfiguration widgetFile;

    public PlayerWidgetProcessedEvent(Player player, String widgetID, FileConfiguration widgetFile) {
        super(player);
        this.widgetID = widgetID;
        this.widgetFile = widgetFile;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getWidgetID() {
        return widgetID;
    }

    public FileConfiguration getWidgetFile() {
        return widgetFile;
    }
}
