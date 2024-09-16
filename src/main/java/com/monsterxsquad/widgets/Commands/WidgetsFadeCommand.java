package com.monsterxsquad.widgets.Commands;

import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Managers.Commands.SubCommands;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

public class WidgetsFadeCommand implements SubCommands {

    private final Widgets plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetsFadeCommand(Widgets plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("widgets.fade")) {
                player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.no-permission")));
                return;
            }
        }

        if (args.length <= 1) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.fade.usage")));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.fade.invalid-player")));
            return;
        }

        String widgetName = args[1];

        FileConfiguration widgetFile = plugin.getConfigManager().getWidgets().get(widgetName);
        if (widgetFile == null) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.fade.invalid-widget")));
            return;
        }

        if (args.length < 5) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.fade.invalid-numbers-amount")));
            return;
        }

        if (!args[2].matches("[0-9]+") && !args[3].matches("[0-9]+") && !args[4].matches("[0-9]+")) {
            sender.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.fade.invalid-numbers")));
            return;
        }

        target.showTitle(Title.title(colourUtils.miniFormat(widgetFile.getString("unicodes.background")), colourUtils.miniFormat(""), Title.Times.times(Duration.ofMillis(Long.parseLong(args[2])), Duration.ofMillis(Long.parseLong(args[3])), Duration.ofMillis(Long.parseLong(args[4])))));
    }

    @Override
    public String name() {
        return "fade";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
