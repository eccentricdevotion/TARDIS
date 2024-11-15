package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Snowball {

    ICE_WARRIOR(new NamespacedKey(TARDIS.plugin, "item/genetic/ice_warrior")),
    ICE_WARRIOR_ARM(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/ice_warrior_arm")),
    ICE_WARRIOR_DAGGER_ARM(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/ice_warrior_dagger_arm")),
    ICE_WARRIOR_HEAD(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/ice_warrior_head")),
    ICE_WARRIOR_DISGUISE(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/ice_warrior_disguise")),
    ICE_WARRIOR_CREST(new NamespacedKey(TARDIS.plugin, "item/lazarus/ice_warrior_crest")),
    ICE_WARRIOR_0(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_0")),
    ICE_WARRIOR_1(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_1")),
    ICE_WARRIOR_2(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_2")),
    ICE_WARRIOR_3(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_3")),
    ICE_WARRIOR_4(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_4")),
    ICE_WARRIOR_STATIC(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/ice_warrior_static")),
    ICE_WARRIOR_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_attacking_0")),
    ICE_WARRIOR_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_attacking_1")),
    ICE_WARRIOR_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_attacking_2")),
    ICE_WARRIOR_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_attacking_3")),
    ICE_WARRIOR_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "item/monster/ice_warrior/frames/ice_warrior_attacking_4")),
    ICE_BOMB(new NamespacedKey(TARDIS.plugin, "item/products/ice_bomb"));

    private final NamespacedKey key;

    Snowball(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
