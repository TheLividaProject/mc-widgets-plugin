package com.monsterxsquad.widgets.Commands;

import com.monsterxsquad.widgets.Managers.Widgets.PlayerWidgetData;
import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Managers.Commands.SubCommands;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class WidgetsShowCommand implements SubCommands {

    private final Widgets plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetsShowCommand(Widgets plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.show.usage")));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.show.invalid-player")));
            return;
        }

        String widgetName = args[1];
        FileConfiguration widgetFile = plugin.getConfigManager().getWidgets().get(widgetName);
        if (widgetFile == null) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.show.invalid-widget")));
            return;
        }

        if (!widgetFile.getBoolean("options.enabled")) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("widgets.disabled")));
            return;
        }

        PlayerWidgetData playerWidgetData = new PlayerWidgetData(widgetName);
        plugin.getWidgetsManager().getWidgetsDataCache().put(target.getUniqueId(), playerWidgetData);

        plugin.getWidgetsManager().displayWidget(target);
        sender.sendMessage(colourUtils.placeHolderMiniFormat(target, plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.show.showing")));
    }

    @Override
    public String name() {
        return "show";
    }

    @Override
    public String permission() {
        return "widgets.commands.show";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
