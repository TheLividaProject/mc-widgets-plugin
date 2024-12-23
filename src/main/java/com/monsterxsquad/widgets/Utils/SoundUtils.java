package com.monsterxsquad.widgets.Utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public void sendSoundToPlayer(Player player, String soundID, Float soundVolume, Float soundPitch) {
        try {
            Sound sound = Sound.valueOf(soundID.toUpperCase());
            player.playSound(player.getLocation(), sound, soundVolume, soundPitch);
        } catch (IllegalArgumentException err) {
            player.playSound(player.getLocation(), soundID, soundVolume, soundPitch);
        }
    }
}
