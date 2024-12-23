package com.monsterxsquad.widgets.Listeners.Widgets;

import com.monsterxsquad.widgets.Managers.Widgets.PlayerPotionData;
import com.monsterxsquad.widgets.Managers.Widgets.PlayerWidgetData;
import com.monsterxsquad.widgets.Utils.ColourUtils;
import com.monsterxsquad.widgets.Utils.SoundUtils;
import com.monsterxsquad.widgets.Widgets;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.HashSet;
import java.util.UUID;

public class ResourcePackListener implements Listener {

    private final Widgets plugin;

    private final HashSet<UUID> playerTracker = new HashSet<>();

    private final SoundUtils soundUtils = new SoundUtils();
    private final ColourUtils colourUtils = new ColourUtils();

    public ResourcePackListener(Widgets plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void loadPacks(PlayerJoinEvent event) {
        if (!plugin.getConfigManager().getConfig().getBoolean("resource-packs.enabled")) return;
        Player player = event.getPlayer();
        plugin.getResourcePackManager().load(player);
    }

    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfigManager().getConfig().getBoolean("resource-pack.kick-on-decline")) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(player);
            } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(player);
                player.kick(colourUtils.miniFormat(plugin.getConfigManager().getConfig().getString("resource-pack.kick-on-decline")));
            }
        } else if (!plugin.getConfigManager().getConfig().getBoolean("resource-pack.kick-on-decline")) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(player);
            } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(player);
                for (String msg : plugin.getConfigManager().getConfig().getStringList("resource-pack.no-pack-loaded")) {
                    player.sendMessage(colourUtils.miniFormat(msg));
                }
            }
        }
    }

    @EventHandler
    public void onPackAccept(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        if (plugin.getWidgetsManager().getPlayersActiveWidgets().contains(player.getUniqueId())) return;

        if (plugin.getConfigManager().getConfig().getBoolean("widgets-onjoin.enabled")) {
            String widgetDisplayID = getWidget(player);
            PlayerWidgetData newPlayerWidgetData = new PlayerWidgetData(widgetDisplayID);
            plugin.getWidgetsManager().getWidgetsDataCache().put(player.getUniqueId(), newPlayerWidgetData);
        }

        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());
        if (playerWidgetData == null) return;

        switch (event.getStatus()) {
            case ACCEPTED -> {
                if (!plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getBoolean("options.background.delay")) return;
                player.showTitle(Title.title(colourUtils.miniFormat(plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("options.background.colour") + plugin.getConfigManager().getWidgets().get(playerWidgetData.getId()).getString("unicodes.background")), colourUtils.miniFormat(""), Title.Times.times(Duration.ofMillis(0), Duration.ofMillis(999999), Duration.ofMillis(0))));
            }
            case SUCCESSFULLY_LOADED -> {
                try {
                    if (plugin.getConfigManager().getConfig().getBoolean("sound-on-pack-load.enable")) {
                        String soundID = plugin.getConfigManager().getConfig().getString("sound-on-pack-load.sound");
                        float soundVolume = Float.parseFloat(plugin.getConfigManager().getConfig().getString("sound-on-pack-load.volume"));
                        float soundPitch = Float.parseFloat(plugin.getConfigManager().getConfig().getString("sound-on-pack-load.pitch"));

                        soundUtils.sendSoundToPlayer(player, soundID, soundVolume, soundPitch);
                    }
                } catch (NullPointerException err) {
                    plugin.getLogger().warning("sound-on-pack-load (config.yml) is not configured correctly.");
                }

                if (!player.hasPlayedBefore()) {
                    if (plugin.getConfigManager().getConfig().getBoolean("widgets-onjoin." + playerWidgetData.getId() + ".first-join")) {
                        plugin.getWidgetsManager().displayWidget(player);
                        return;
                    }
                }

                if (plugin.getConfigManager().getConfig().getBoolean("widgets-onjoin." + playerWidgetData.getId() + ".once-per-restart")) {
                    if (!playerTracker.contains(player.getUniqueId())) {
                        playerTracker.add(player.getUniqueId());
                        plugin.getWidgetsManager().displayWidget(player);
                    }

                } else if (!plugin.getConfigManager().getConfig().getBoolean("widgets-onjoin." + playerWidgetData.getId() + ".once-per-restart")) {
                    plugin.getWidgetsManager().displayWidget(player);
                }
            }
            case FAILED_DOWNLOAD -> {
                if (plugin.getConfigManager().getConfig().getBoolean("debug-mode")) {
                    player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + "<#FF5555>No server resource pack detected and/or debug mode is enabled."));
                    player.sendMessage(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("prefix") + "<#FF5555>Sending widget"));
                    plugin.getWidgetsManager().displayWidget(player);
                } else {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);

                    if (plugin.getConfigManager().getLang().getString("resource-pack.no-pack-loaded") != null) {
                        for (String msg : plugin.getConfigManager().getLang().getStringList("resource-pack.no-pack-loaded")) {
                            player.sendMessage(colourUtils.miniFormat(msg));
                        }
                    }
                }
            }
        }
    }

    private void disableEffects(Player player) {
        PlayerWidgetData playerWidgetData = plugin.getWidgetsManager().getWidgetsDataCache().get(player.getUniqueId());

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            PlayerPotionData playerPotionData = new PlayerPotionData(potionEffect.getType(), potionEffect.getAmplifier(), potionEffect.getDuration());
            playerWidgetData.getPotionEffects().add(playerPotionData);
            player.removePotionEffect(potionEffect.getType());
        }
    }

    private String getWidget(Player player) {
        String highestPriorityWidget = null;
        int highestPriority = Integer.MIN_VALUE;

        for (String key : plugin.getConfigManager().getConfig().getConfigurationSection("widgets-onjoin.widgets").getKeys(false)) {
            ConfigurationSection widgetConfigID = plugin.getConfigManager().getConfig().getConfigurationSection("widgets-onjoin.widgets." + key);
            int priority = widgetConfigID.getInt("priority");

            if (widgetConfigID.getString("permission") != null) {
                if (!player.hasPermission(widgetConfigID.getString("permission"))) continue;
            }

            if (priority > highestPriority) {
                highestPriority = priority;
                highestPriorityWidget = key;
            }
        }

        return highestPriorityWidget;
    }
}
