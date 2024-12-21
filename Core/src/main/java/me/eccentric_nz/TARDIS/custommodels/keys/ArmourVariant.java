package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ArmourVariant {

    ANGEL_OF_LIBERTY(new NamespacedKey(TARDIS.plugin, "angel_of_liberty")),
    CLOCKWORK_DROID(new NamespacedKey(TARDIS.plugin, "clockwork_droid")),
    CLOCKWORK_DROID_FEMALE(new NamespacedKey(TARDIS.plugin, "clockwork_droid_female")),
    CYBERMAN(new NamespacedKey(TARDIS.plugin, "cyberman")),
    CYBERSHADE(new NamespacedKey(TARDIS.plugin, "cybershade")),
    DALEK_SEC(new NamespacedKey(TARDIS.plugin, "dalek_sec")),
    EMPTY_CHILD(new NamespacedKey(TARDIS.plugin, "empty_child")),
    HATH(new NamespacedKey(TARDIS.plugin, "hath")),
    HEADLESS_MONK(new NamespacedKey(TARDIS.plugin, "headless_monk")),
    ICE_WARRIOR(new NamespacedKey(TARDIS.plugin, "ice_warrior")),
    JUDOON(new NamespacedKey(TARDIS.plugin, "judoon")),
    MIRE(new NamespacedKey(TARDIS.plugin, "mire")),
    OMEGA(new NamespacedKey(TARDIS.plugin, "omega")),
    OOD_BLACK(new NamespacedKey(TARDIS.plugin, "ood_black")),
    OOD_BLUE(new NamespacedKey(TARDIS.plugin, "ood_blue")),
    OOD_BROWN(new NamespacedKey(TARDIS.plugin, "ood_brown")),
    OSSIFIED(new NamespacedKey(TARDIS.plugin, "ossified")),
    RACNOSS(new NamespacedKey(TARDIS.plugin, "racnoss")),
    SCARECROW(new NamespacedKey(TARDIS.plugin, "scarecrow")),
    SEA_DEVIL(new NamespacedKey(TARDIS.plugin, "sea_devil")),
    SILENCE(new NamespacedKey(TARDIS.plugin, "silence")),
    SILURIAN(new NamespacedKey(TARDIS.plugin, "silurian")),
    SLITHEEN(new NamespacedKey(TARDIS.plugin, "slitheen")),
    SMILER(new NamespacedKey(TARDIS.plugin, "smiler")),
    SONTARAN(new NamespacedKey(TARDIS.plugin, "sontaran")),
    STRAX(new NamespacedKey(TARDIS.plugin, "strax")),
    SUTEKH(new NamespacedKey(TARDIS.plugin, "sutekh")),
    SYCORAX(new NamespacedKey(TARDIS.plugin, "sycorax")),
    THE_BEAST(new NamespacedKey(TARDIS.plugin, "the_beast")),
    VAMPIRE_OF_VENICE(new NamespacedKey(TARDIS.plugin, "vashta_nerada")),
    VASHTA_NERADA(new NamespacedKey(TARDIS.plugin, "vashta_nerada")),
    WEEPING_ANGEL(new NamespacedKey(TARDIS.plugin, "weeping_angel")),
    ZYGON(new NamespacedKey(TARDIS.plugin, "zygon"));

    private final NamespacedKey key;

    ArmourVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
