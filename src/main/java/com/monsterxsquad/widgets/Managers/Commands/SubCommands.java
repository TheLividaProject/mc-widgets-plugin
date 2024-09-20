package com.monsterxsquad.widgets.Managers.Commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommands {
    void onCommand(CommandSender sender, String[] args);
    String name();
    String permission();
    List<String> tabComplete(CommandSender sender, String[] args);
}
