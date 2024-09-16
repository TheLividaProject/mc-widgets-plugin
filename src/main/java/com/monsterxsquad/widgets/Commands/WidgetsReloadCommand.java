package com.monsterxsquad.widgets.Commands;

import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Managers.Commands.SubCommands;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WidgetsReloadCommand implements SubCommands {

    private final Widgets plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetsReloadCommand(Widgets plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("widgets.reload")) {
                player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.no-permission")));
                return;
            }
        }

        Bukkit.getAsyncScheduler().runNow(plugin, task -> {
            plugin.getConfigManager().load();
        });

        sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.reload.config-reloaded")));
    }


    @Override
    public String name() {
        return "reload";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
