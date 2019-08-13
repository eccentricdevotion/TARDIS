package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISLazarusDisguise {

    private final TARDIS plugin;
    private final Player player;
    private final EntityType entityType;
    private final Object[] options;

    public TARDISLazarusDisguise(TARDIS plugin, Player player, EntityType entityType, Object[] options) {
        this.plugin = plugin;
        this.player = player;
        this.entityType = entityType;
        this.options = options;
    }

    public void createDisguise() {
        if (entityType.equals(EntityType.PLAYER)) {
            plugin.getTardisHelper().disguise(player, UUID.fromString("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2")); // or 91f25eb5-2b0e-46bc-8437-401c6017f369
        } else {
            plugin.getTardisHelper().disguise(entityType, player, options);
        }
    }
}
