package com.monsterxsquad.widgets.Managers.ResourcePack;

import com.monsterxsquad.widgets.Utils.ColourUtils;
import com.monsterxsquad.widgets.Widgets;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ResourcePackManager {

    private final Widgets plugin;

    private final HashMap<String, ResourcePackInfo> resourcePackCache = new HashMap<>();
    private final HashSet<String> enabledResourcePackList = new HashSet<>();

    private final ColourUtils colourUtils = new ColourUtils();

    public ResourcePackManager(Widgets plugin) {
        this.plugin = plugin;
        loadPack();
    }

    public void loadPack() {
        try {
            plugin.getConfigManager().getConfig().getConfigurationSection("resource-packs.downloads").getKeys(false).forEach(key -> {
                ConfigurationSection packInfoConfig = plugin.getConfigManager().getConfig().getConfigurationSection("resource-pack.downloads." + key);

                try {
                    if (packInfoConfig.getBoolean("download-onjoin")) enabledResourcePackList.add(key);

                    ResourcePackInfo resourcePackInfo = ResourcePackInfo.resourcePackInfo()
                            .id(UUID.fromString(key))
                            .uri(URI.create(packInfoConfig.getString("link")))
                            .computeHashAndBuild().get();

                    resourcePackCache.put(key, resourcePackInfo);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void load(Player player) {
        enabledResourcePackList.forEach(resourcePackUUID -> {
            sendResourcePack(player, resourcePackUUID, true);
        });
    }

    public void sendResourcePack(Player player, String packID, boolean required) {
        ResourcePackRequest resourcePackRequest = ResourcePackRequest.resourcePackRequest()
                .packs(resourcePackCache.get(packID))
                .prompt(colourUtils.miniFormat(plugin.getConfigManager().getLang().getString("resource-pack.prompt")))
                .required(required)
                .build();

        player.sendResourcePacks(resourcePackRequest);
    }

    public HashMap<String, ResourcePackInfo> getResourcePackCache() {
        return resourcePackCache;
    }

    public HashSet<String> getEnabledResourcePackList() {
        return enabledResourcePackList;
    }
}
