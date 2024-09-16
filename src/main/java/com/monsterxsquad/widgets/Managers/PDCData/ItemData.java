package com.monsterxsquad.widgets.Managers.PDCData;

public class ItemData {

    private final int slot;
    private final String itemOption;

    public ItemData(int slot, String itemOption) {
        this.slot = slot;
        this.itemOption = itemOption;
    }

    public int getSlot() {
        return slot;
    }

    public String getItemOption() {
        return itemOption;
    }
}
