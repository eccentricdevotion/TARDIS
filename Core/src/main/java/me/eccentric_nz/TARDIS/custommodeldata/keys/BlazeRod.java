package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlazeRod {

    MARK1(new NamespacedKey(TARDIS.plugin, "item/sonic/mark1")),
    MARK2(new NamespacedKey(TARDIS.plugin, "item/sonic/mark2")),
    MARK3(new NamespacedKey(TARDIS.plugin, "item/sonic/mark3")),
    MARK4(new NamespacedKey(TARDIS.plugin, "item/sonic/mark4")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "item/sonic/eighth")),
    NINTH(new NamespacedKey(TARDIS.plugin, "item/sonic/ninth")),
    TENTH(new NamespacedKey(TARDIS.plugin, "item/sonic/tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "item/sonic/eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "item/sonic/twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "item/sonic/thirteenth")),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "item/sonic/fourteenth")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "item/sonic/fifteenth")),
    RIVER_SONG(new NamespacedKey(TARDIS.plugin, "item/sonic/river_song")),
    MASTER(new NamespacedKey(TARDIS.plugin, "item/sonic/master")),
    SARAH_JANE(new NamespacedKey(TARDIS.plugin, "item/sonic/sarah_jane")),
    SONIC_PROBE(new NamespacedKey(TARDIS.plugin, "item/sonic/sonic_probe")),
    UMBRELLA(new NamespacedKey(TARDIS.plugin, "item/sonic/umbrella")),
    WAR(new NamespacedKey(TARDIS.plugin, "item/sonic/war")),
    MARK1_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/mark1_on")),
    MARK2_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/mark2_on")),
    MARK3_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/mark3_on")),
    MARK4_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/mark4_on")),
    EIGHTH_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/eighth_on")),
    NINTH_OPEN(new NamespacedKey(TARDIS.plugin, "item/sonic/ninth_open")),
    TENTH_OPEN(new NamespacedKey(TARDIS.plugin, "item/sonic/tenth_open")),
    ELEVENTH_OPEN(new NamespacedKey(TARDIS.plugin, "item/sonic/eleventh_open")),
    THIRTEENTH_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/thirteenth_on")),
    FOURTEENTH_OPEN(new NamespacedKey(TARDIS.plugin, "item/sonic/fourteenth_open")),
    FIFTEENTH_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/fifteenth_on")),
    RIVER_SONG_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/river_song_on")),
    MASTER_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/master_on")),
    SARAH_JANE_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/sarah_jane_on")),
    SONIC_PROBE_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/sonic_probe_on")),
    UMBRELLA_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/umbrella_on")),
    WAR_ON(new NamespacedKey(TARDIS.plugin, "item/sonic/war_on"));

    private final NamespacedKey key;

    BlazeRod(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
