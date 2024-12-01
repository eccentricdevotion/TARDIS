package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Features {

    ACE_PONYTAIL(new NamespacedKey(TARDIS.plugin, "lazarus/ace_ponytail")),
    ANGEL_OF_LIBERTY_CROWN(new NamespacedKey(TARDIS.plugin, "lazarus/angel_of_liberty_crown")),
    ANGEL_OF_LIBERTY_TORCH(new NamespacedKey(TARDIS.plugin, "lazarus/angel_of_liberty_torch")),
    BANNAKAFFALATTA_SPIKES(new NamespacedKey(TARDIS.plugin, "lazarus/bannakaffalatta_spikes")),
    BRIGADIER_LETHBRIDGE_STEWART_HAT(new NamespacedKey(TARDIS.plugin, "lazarus/brigadier_lethbridge_stewart_hat")),
    CYBERSHADE_EARS(new NamespacedKey(TARDIS.plugin, "lazarus/cybershade_ears")),
    IMPOSSIBLE_ASTRONAUT_PACK(new NamespacedKey(TARDIS.plugin, "lazarus/impossible_astronaut_pack")),
    JENNY_FLINT_KATANA(new NamespacedKey(TARDIS.plugin, "lazarus/jenny_flint_katana")),
    JO_GRANT_HAIR(new NamespacedKey(TARDIS.plugin, "lazarus/jo_grant_hair")),
    MARTHA_JONES_HAIR(new NamespacedKey(TARDIS.plugin, "lazarus/martha_jones_hair")),
    OMEGA_FRILL(new NamespacedKey(TARDIS.plugin, "lazarus/omega_frill")),
    RACNOSS_FEATURES(new NamespacedKey(TARDIS.plugin, "lazarus/racnoss_features")),
    SUTEKH_FEATURES(new NamespacedKey(TARDIS.plugin, "lazarus/sutekh_features")),
    SYCORAX_CAPE(new NamespacedKey(TARDIS.plugin, "lazarus/sycorax_cape")),
    TEGAN_HAT(new NamespacedKey(TARDIS.plugin, "lazarus/tegan_hat")),
    THE_BEAST_HORNS(new NamespacedKey(TARDIS.plugin, "lazarus/the_beast_horns")),
    VAMPIRE_OF_VENICE_FAN(new NamespacedKey(TARDIS.plugin, "lazarus/vampire_of_venice_fan"));

    private final NamespacedKey key;

    Features(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
