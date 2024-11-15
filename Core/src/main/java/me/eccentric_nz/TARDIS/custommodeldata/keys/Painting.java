package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Painting {

    ZYGON(new NamespacedKey(TARDIS.plugin, "item/genetic/zygon")),
    ZYGON_ARM(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/zygon_arm")),
    ZYGON_HEAD(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/zygon_head")),
    ZYGON_DISGUISE(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/zygon_disguise")),
    ZYGON_CREST(new NamespacedKey(TARDIS.plugin, "item/lazarus/zygon_crest")),
    ZYGON_0(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_0")),
    ZYGON_1(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_1")),
    ZYGON_2(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_2")),
    ZYGON_3(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_3")),
    ZYGON_4(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_4")),
    ZYGON_STATIC(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/zygon_static")),
    ZYGON_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_attacking_0")),
    ZYGON_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_attacking_1")),
    ZYGON_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_attacking_2")),
    ZYGON_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_attacking_3")),
    ZYGON_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "item/monster/zygon/frames/zygon_attacking_4"));

    private final NamespacedKey key;

    Painting(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
