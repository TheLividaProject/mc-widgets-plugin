package com.monsterxsquad.widgets.GUI;

import com.monsterxsquad.widgets.Events.PlayerWidgetProcessedEvent;
import com.monsterxsquad.widgets.Managers.Widgets.PlayerWidgetData;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import com.monsterxsquad.widgets.Utils.ItemUtils;
import com.monsterxsquad.widgets.Widgets;
import com.monsterxsquad.widgets.Managers.PDCData.ItemData;
import com.monsterxsquad.widgets.Managers.PDCData.PDC.ItemDataPDC;
import com.monsterxsquad.widgets.Managers.GUI.MenuInventoryHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WidgetMenu implements MenuInventoryHolder {

    private final Widgets plugin;

    private final Inventory inventory;

    private final NamespacedKey itemDataKey;

    private final Player player;

    private final ItemUtils itemUtils = new ItemUtils();
    private final ColourUtils colourUtils = new ColourUtils();

    public WidgetMenu(Widgets plugin, Player player) {
        this.plugin = plugin;
        this.itemDataKey = new NamespacedKey(plugin, "ItemData");
        this.player = player;

        this.inventory = Bukkit.createInventory(this, getSize(), getName());

        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());

        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> {
            if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getConfigurationSection("menu") != null) {
                for (String key : plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getConfigurationSection("menu").getKeys(false)) {

                    int slot = plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getInt("menu." + key + ".slot");
                    ItemStack item = new ItemStack(Material.valueOf(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("menu." + key + ".material")));

                    item.editMeta(meta -> {
                        if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).get("menu." + key + ".lore") != null) {
                            List<Component> loreSetter = new ArrayList<>();

                            for (String string : plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getStringList("menu." + key + ".lore")) {
                                loreSetter.add(colourUtils.placeHolderMiniFormat(player, string));
                            }

                            meta.lore(loreSetter);
                        }

                        if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getBoolean("menu." + key + ".hide-flags")) {
                            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        }

                        meta.setCustomModelData(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getInt("menu." + key + ".custom-model"));
                        meta.displayName(colourUtils.miniFormat(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("menu." + key + ".name")));

                        meta.getPersistentDataContainer().set(itemDataKey, new ItemDataPDC(), new ItemData(slot, key));
                    });

                    player.getOpenInventory().setItem(slot, item);
                }
            }
        }, plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getInt("options.delay"));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return plugin.getWidgetsManager().widgetTitle(player);
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public void handleClick(Player player, ItemStack item, InventoryClickEvent event) {
        ItemMeta meta = item.getItemMeta();

        ItemData itemData = meta.getPersistentDataContainer().get(itemDataKey, new ItemDataPDC());
        if (itemData == null) return;

        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());

        String itemName = itemData.getItemOption();

        if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getBoolean("menu." + itemName + ".close-on-click")) player.closeInventory();
        if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getStringList("menu." + itemName + ".commands").isEmpty()) return;

        for (String command : plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getStringList("menu." + itemName + ".commands")) {
            if (command.contains("[console]")) {
                command = command.replace("[console] ", "");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            } else if (command.contains("[message]")) {
                command = command.replace("[message] ", "");
                player.sendMessage(colourUtils.placeHolderMiniFormat(player, command));
            } else if (command.contains("[player]")) {
                command = command.replace("[player] ", "");
                player.performCommand(command);
            } else {
                player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + "<#FF5555>An error occurred. Please review the console for more information."));
                plugin.getLogger().warning("\"" + itemName + "\"" + "(" + playerWidgetData.getId() + ") has a command with an invalid format.");
            }
        }
    }

    @Override
    public void handleClose(Player player, InventoryCloseEvent event) {
        if (!plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;
        if (plugin.getWidgetsManager().getProcessingPlayers().contains(player.getUniqueId())) return;

        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());

        if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getBoolean("options.fade.enabled")) {
            player.showTitle(Title.title(colourUtils.miniFormat(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("unicodes.background")), colourUtils.miniFormat(""), Title.Times.times(Duration.ofMillis(0), Duration.ofMillis(0), Duration.ofMillis(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getInt("options.fade.time")))));
        }

        plugin.getWidgetsManager().getProcessingPlayers().add(player.getUniqueId());

        try {
            if (plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getBoolean("sound-on-advance.enable")) {
                String soundID = plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("sound-on-advance.sound");
                float soundVolume = Float.parseFloat(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("sound-on-advance.volume"));
                float soundPitch = Float.parseFloat(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("sound-on-advance.pitch"));

                try {
                    Sound sound = Sound.valueOf(soundID.toUpperCase());
                    player.playSound(player.getLocation(), sound, soundVolume, soundPitch);
                } catch (IllegalArgumentException err) {
                    player.playSound(player.getLocation(), soundID, soundVolume, soundPitch);
                }
            }
        } catch (NullPointerException ex) {
            plugin.getLogger().warning("sound-on-advance (" + playerWidgetData.getId() + ") is not configured correctly.");
        }

        try {
            player.getInventory().setContents(itemUtils.itemStackArrayFromBase64(playerWidgetData.getInventory()));

            plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getStringList("commands-on-advance").forEach(command -> {
                if (command.contains("[console]")) {
                    command = command.replace("[console] ", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (command.contains("[message]")) {
                    command = command.replace("[message] ", "");
                    player.sendMessage(colourUtils.placeHolderMiniFormat(player, command));
                } else if (command.contains("[player]")) {
                    command = command.replace("[player] ", "");
                    player.performCommand(command);
                } else {
                    player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + "<#FF5555>An error occurred. Please review the console for more information."));
                    plugin.getLogger().warning("commands-on-advance (" + playerWidgetData.getId() + ") has a command with an invalid format.");
                }
            });
        } catch (IOException err) {
            throw new RuntimeException(err);
        } finally {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerWidgetProcessedEvent(player, playerWidgetData.getId(), plugin.getConfigManager().getWidgets().get(playerWidgetData.getId())));
            plugin.getWidgetsManager().getProcessingPlayers().remove(player.getUniqueId());
        }

        playerWidgetData.getPotionEffects().forEach(playerPotionsData -> {
            PotionEffect potionEffect = new PotionEffect(playerPotionsData.getType(), playerPotionsData.getAmplifier(), playerPotionsData.getDuration());
            player.addPotionEffect(potionEffect);
        });

        welcomeMessage(player);
        plugin.getWidgetsManager().getWidgetsDataCache().remove(player.getUniqueId());
    }

    @Override
    public boolean cancelEvent() {
        return true;
    }

    private void welcomeMessage(Player player) {
        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());
        List<String > welcomeMessage = plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getStringList("welcome-message-on-advance");
        if (welcomeMessage.isEmpty()) return;

        for (String message : welcomeMessage) {
            player.sendMessage(colourUtils.placeHolderMiniFormat(player, message));
        }
    }
}
