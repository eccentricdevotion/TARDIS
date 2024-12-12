package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ScarecrowVariant {

    SCARECROW(new NamespacedKey(TARDIS.plugin, "button_scarecrow")),
    SCARECROW_ARM(new NamespacedKey(TARDIS.plugin, "scarecrow_arm")),
    SCARECROW_HEAD(new NamespacedKey(TARDIS.plugin, "scarecrow_head")),
    SCARECROW_DISGUISE(new NamespacedKey(TARDIS.plugin, "scarecrow_disguise")),
    SCARECROW_EARS(new NamespacedKey(TARDIS.plugin, "scarecrow_ears")),
    SCARECROW_0(new NamespacedKey(TARDIS.plugin, "scarecrow_0")),
    SCARECROW_1(new NamespacedKey(TARDIS.plugin, "scarecrow_1")),
    SCARECROW_2(new NamespacedKey(TARDIS.plugin, "scarecrow_2")),
    SCARECROW_3(new NamespacedKey(TARDIS.plugin, "scarecrow_3")),
    SCARECROW_4(new NamespacedKey(TARDIS.plugin, "scarecrow_4")),
    SCARECROW_STATIC(new NamespacedKey(TARDIS.plugin, "scarecrow_static")),
    SCARECROW_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "scarecrow_attacking_0")),
    SCARECROW_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "scarecrow_attacking_1")),
    SCARECROW_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "scarecrow_attacking_2")),
    SCARECROW_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "scarecrow_attacking_3")),
    SCARECROW_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "scarecrow_attacking_4"));

    private final NamespacedKey key;

    ScarecrowVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
