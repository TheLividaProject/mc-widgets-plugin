package com.monsterxsquad.widgets.Managers.Widgets;

import org.bukkit.potion.PotionEffectType;

public class PlayerPotionData {

    private final PotionEffectType type;

    private final int amplifier;
    private final int duration;

    public PlayerPotionData(PotionEffectType type, int amplifier, int duration) {
        this.type = type;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public PotionEffectType getType() {
        return type;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getDuration() {
        return duration;
    }
}
