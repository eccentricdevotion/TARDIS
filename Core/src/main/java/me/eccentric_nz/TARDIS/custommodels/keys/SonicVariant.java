package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SonicVariant {

    MARK1(new NamespacedKey(TARDIS.plugin, "sonic_mark1")),
    MARK2(new NamespacedKey(TARDIS.plugin, "sonic_mark2")),
    MARK3(new NamespacedKey(TARDIS.plugin, "sonic_mark3")),
    MARK4(new NamespacedKey(TARDIS.plugin, "sonic_mark4")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "sonic_eighth")),
    NINTH(new NamespacedKey(TARDIS.plugin, "sonic_ninth")),
    TENTH(new NamespacedKey(TARDIS.plugin, "sonic_tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "sonic_eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "sonic_twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "sonic_thirteenth")),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "sonic_fourteenth")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "sonic_fifteenth")),
    RIVER_SONG(new NamespacedKey(TARDIS.plugin, "sonic_river_song")),
    MASTER(new NamespacedKey(TARDIS.plugin, "sonic_master")),
    SARAH_JANE(new NamespacedKey(TARDIS.plugin, "sonic_sarah_jane")),
    SONIC_PROBE(new NamespacedKey(TARDIS.plugin, "sonic_sonic_probe")),
    UMBRELLA(new NamespacedKey(TARDIS.plugin, "sonic_umbrella")),
    WAR(new NamespacedKey(TARDIS.plugin, "sonic_war")),
    MARK1_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark1_on")),
    MARK2_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark2_on")),
    MARK3_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark3_on")),
    MARK4_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark4_on")),
    EIGHTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_eighth_on")),
    NINTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_ninth_open")),
    TENTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_tenth_open")),
    ELEVENTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_eleventh_open")),
    TWELFTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_twelfth_on")),
    THIRTEENTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_thirteenth_on")),
    FOURTEENTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_fourteenth_open")),
    FIFTEENTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_fifteenth_on")),
    RIVER_SONG_ON(new NamespacedKey(TARDIS.plugin, "sonic_river_song_on")),
    MASTER_ON(new NamespacedKey(TARDIS.plugin, "sonic_master_on")),
    SARAH_JANE_ON(new NamespacedKey(TARDIS.plugin, "sonic_sarah_jane_on")),
    SONIC_PROBE_ON(new NamespacedKey(TARDIS.plugin, "sonic_sonic_probe_on")),
    UMBRELLA_ON(new NamespacedKey(TARDIS.plugin, "sonic_umbrella_on")),
    WAR_ON(new NamespacedKey(TARDIS.plugin, "sonic_war_on")),
    STANDARD_SONIC(new NamespacedKey(TARDIS.plugin, "button_standard_sonic"));

    private final NamespacedKey key;

    SonicVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
