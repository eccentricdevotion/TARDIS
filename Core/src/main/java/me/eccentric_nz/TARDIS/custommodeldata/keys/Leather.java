package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Leather {


    BRIGADIER_LETHBRIDGE_STEWART_HAT(new NamespacedKey(TARDIS.plugin, "item/lazarus/brigadier_lethbridge_stewart_hat")),

    ACE_PONYTAIL(new NamespacedKey(TARDIS.plugin, "item/lazarus/ace_ponytail")),

    JO_GRANT_HAIR(new NamespacedKey(TARDIS.plugin, "item/lazarus/jo_grant_hair")),

    MARTHA_JONES_HAIR(new NamespacedKey(TARDIS.plugin, "item/lazarus/martha_jones_hair")),

    TEGAN_HAT(new NamespacedKey(TARDIS.plugin, "item/lazarus/tegan_hat")),

    SYCORAX_CAPE(new NamespacedKey(TARDIS.plugin, "item/lazarus/sycorax_cape")),

    SUTEKH_FEATURES(new NamespacedKey(TARDIS.plugin, "item/lazarus/sutekh_features")),

    THE_BEAST_HORNS(new NamespacedKey(TARDIS.plugin, "item/lazarus/the_beast_horns")),

    CYBERSHADE_EARS(new NamespacedKey(TARDIS.plugin, "item/lazarus/cybershade_ears")),

    OMEGA_FRILL(new NamespacedKey(TARDIS.plugin, "item/lazarus/omega_frill")),

    ANGEL_OF_LIBERTY_CROWN(new NamespacedKey(TARDIS.plugin, "item/lazarus/angel_of_liberty_crown")),

    RACNOSS_FEATURES(new NamespacedKey(TARDIS.plugin, "item/lazarus/racnoss_features")),

    JENNY_FLINT_KATANA(new NamespacedKey(TARDIS.plugin, "item/lazarus/jenny_flint_katana"));

    private final NamespacedKey key;

    Leather(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
