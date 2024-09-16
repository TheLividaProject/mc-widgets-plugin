package com.monsterxsquad.widgets.Managers.Widgets;

import java.util.ArrayList;

public class PlayerWidgetData {

    private final String id;

    private String inventory;
    private final ArrayList<PlayerPotionData> potionEffects = new ArrayList<>();

    public PlayerWidgetData(String id) {
        this.id = id;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getId() {
        return id;
    }

    public String getInventory() {
        return inventory;
    }

    public ArrayList<PlayerPotionData> getPotionEffects() {
        return potionEffects;
    }
}
