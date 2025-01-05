package com.monsterxsquad.widgets.Utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public void sendSoundToPlayer(Player player, String soundID, String source, Float soundVolume, Float soundPitch) {
        if (soundID == null) return;
        Sound sound = Sound.sound(Key.key(soundID), Sound.Source.valueOf(source), soundVolume, soundPitch);
        player.playSound(sound, Sound.Emitter.self());
    }
}
