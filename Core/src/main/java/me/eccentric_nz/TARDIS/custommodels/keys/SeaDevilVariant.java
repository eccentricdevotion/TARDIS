package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SeaDevilVariant {

    SEA_DEVIL(new NamespacedKey(TARDIS.plugin, "button_sea_devil")),
    SEA_DEVIL_HEAD(new NamespacedKey(TARDIS.plugin, "sea_devil_head")),
    SEA_DEVIL_DISGUISE(new NamespacedKey(TARDIS.plugin, "sea_devil_disguise")),
    SEA_DEVIL_EARS(new NamespacedKey(TARDIS.plugin, "sea_devil_ears")),
    SEA_DEVIL_0(new NamespacedKey(TARDIS.plugin, "sea_devil_0")),
    SEA_DEVIL_1(new NamespacedKey(TARDIS.plugin, "sea_devil_1")),
    SEA_DEVIL_2(new NamespacedKey(TARDIS.plugin, "sea_devil_2")),
    SEA_DEVIL_3(new NamespacedKey(TARDIS.plugin, "sea_devil_3")),
    SEA_DEVIL_4(new NamespacedKey(TARDIS.plugin, "sea_devil_4")),
    SEA_DEVIL_STATIC(new NamespacedKey(TARDIS.plugin, "sea_devil_static")),
    SEA_DEVIL_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "sea_devil_attacking_0")),
    SEA_DEVIL_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "sea_devil_attacking_1")),
    SEA_DEVIL_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "sea_devil_attacking_2")),
    SEA_DEVIL_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "sea_devil_attacking_3")),
    SEA_DEVIL_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "sea_devil_attacking_4")),
    SEA_DEVIL_SWIMMING_0(new NamespacedKey(TARDIS.plugin, "sea_devil_swimming_0")),
    SEA_DEVIL_SWIMMING_1(new NamespacedKey(TARDIS.plugin, "sea_devil_swimming_1")),
    SEA_DEVIL_SWIMMING_2(new NamespacedKey(TARDIS.plugin, "sea_devil_swimming_2")),
    SEA_DEVIL_SWIMMING_3(new NamespacedKey(TARDIS.plugin, "sea_devil_swimming_3")),
    SEA_DEVIL_SWIMMING_4(new NamespacedKey(TARDIS.plugin, "sea_devil_swimming_4"));

    private final NamespacedKey key;

    SeaDevilVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

