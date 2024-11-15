package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedCandle {

    HEADLESS_MONK(new NamespacedKey(TARDIS.plugin, "item/genetic/headless_monk")),
    HEADLESS_MONK_HEAD(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/headless_monk_head")),
    HEADLESS_MONK_DISGUISE(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/headless_monk_disguise")),
    HEADLESS_MONK_0(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_0")),
    HEADLESS_MONK_1(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_1")),
    HEADLESS_MONK_2(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_2")),
    HEADLESS_MONK_3(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_3")),
    HEADLESS_MONK_4(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_4")),
    HEADLESS_MONK_STATIC(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/headless_monk_static")),
    HEADLESS_MONK_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_attacking_0")),
    HEADLESS_MONK_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_attacking_1")),
    HEADLESS_MONK_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_attacking_2")),
    HEADLESS_MONK_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_attacking_3")),
    HEADLESS_MONK_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "item/monster/headless_monk/frames/headless_monk_attacking_4"));

    private final NamespacedKey key;

    RedCandle(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

