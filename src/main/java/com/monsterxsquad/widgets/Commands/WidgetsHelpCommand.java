package com.monsterxsquad.widgets.Commands;

import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Managers.Commands.SubCommands;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WidgetsHelpCommand implements SubCommands {

    private final Widgets plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetsHelpCommand(Widgets plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("widgets.help")) {
                player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.no-permission")));
                return;
            }
        }

        plugin.getConfigManager().getLang().getStringList("commands.help.usage").forEach(string -> {
            sender.sendMessage(colourUtils.miniFormat(string));
        });
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}