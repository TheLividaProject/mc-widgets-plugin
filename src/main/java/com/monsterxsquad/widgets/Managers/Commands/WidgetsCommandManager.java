package com.monsterxsquad.widgets.Managers.Commands;

import com.monsterxsquad.widgets.Utils.ColourUtils;
import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Commands.WidgetsFadeCommand;
import com.monsterxsquad.widgets.Commands.WidgetsHelpCommand;
import com.monsterxsquad.widgets.Commands.WidgetsReloadCommand;
import com.monsterxsquad.widgets.Commands.WidgetsShowCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetsCommandManager implements CommandExecutor, TabCompleter {

    //TODO move to brig

    private final Widgets plugin;

    private final ArrayList<SubCommands> commands = new ArrayList<>();

    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetsCommandManager(Widgets plugin) {
        this.plugin = plugin;

        plugin.getCommand("widgets").setExecutor(this);
        plugin.getCommand("widgets").setTabCompleter(this);

        commands.add(new WidgetsShowCommand(plugin));
        commands.add(new WidgetsFadeCommand(plugin));

        commands.add(new WidgetsReloadCommand(plugin));
        commands.add(new WidgetsHelpCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, @NotNull String[] args) {
        if (args.length == 0) {
            Bukkit.dispatchCommand(sender, "widgets help");
            return true;
        }

        SubCommands command = getCommand(args[0]);
        if (command == null) return true;

        if (sender instanceof Player player) {
            if (!player.hasPermission(command.permission())) {
                player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + plugin.getConfigManager().getLang().getString("commands.no-permission")));
                return true;
            }
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        command.onCommand(sender, newArgs);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return commands.stream()
                    .map(SubCommands::name)
                    .collect(Collectors.toList());
        }

        //TODO setup more tab complete holders
        return new ArrayList<>();
    }

    private SubCommands getCommand(String name) {
        return commands.stream()
                .filter(cmd -> cmd.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}