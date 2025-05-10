package me.eccentric_nz.TARDIS.geyser;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.CustomItemData;

import java.util.Map;

public class GeyserRegister {

    @Subscribe
    public void onGeyserPreInitializeEvent(GeyserDefineCustomItemsEvent event) {
        for (Map.Entry<String, CustomItemData> data : new GeyserItems().init().entrySet()) {
            event.register(data.getKey(), data.getValue());
        }
    }
}
