package me.eccentric_nz.TARDIS.geyser;

import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;

import java.util.HashMap;

public class GeyserItems {

    // TODO add items with upcoming Custom item API v2 - https://github.com/GeyserMC/Geyser/pull/5189
    public HashMap<String, CustomItemData> init() {
        HashMap<String, CustomItemData> items = new HashMap<>();
        CustomItemOptions itemOptions = CustomItemOptions.builder()
                .customModelData(101)
                .build();
        CustomItemData tardisKey = CustomItemData.builder()
                .name("tardis_key")
                .customItemOptions(itemOptions)
                .displayName("TARDIS Key")
                .build();
        items.put("minecraft:trial_key", tardisKey);
        return items;
    }
}
