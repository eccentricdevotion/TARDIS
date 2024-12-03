package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Button {

    ADAPT(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_adapt")),
    AGE(new NamespacedKey(TARDIS.plugin, "genetic/button_age")),
    AREAS(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_areas")),
    ARS(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_ars")),
    ARTRON(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_artron")),
    AUTONOMOUS_PREFERENCES(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_auto_prefs")),
    BACK(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_back")),
    CANCEL(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_cancel")),
    CHAMELEON(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_chameleon")),
    CONSTRUCT(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_construct")),
    DEACTIVATE(new NamespacedKey(TARDIS.plugin, "gui/bucket/deactivate")),
    DEATH(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_death")),
    DELETE(new NamespacedKey(TARDIS.plugin, "gui/bucket/delete")),
    DIRECTION(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_direction")),
    DNA(new NamespacedKey(TARDIS.plugin, "genetic/button_dna")),
    FARMING_PREFERENCES(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_farm_prefs")),
    HIDE(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_hide")),
    INFO(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_info")),
    INVISIBLE(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_invisible")),
    LOCK(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_lock")),
    MASTER_OFF(new NamespacedKey(TARDIS.plugin, "genetic/button_master_off")),
    MASTER_ON(new NamespacedKey(TARDIS.plugin, "genetic/button_master_on")),
    OPTIONS(new NamespacedKey(TARDIS.plugin, "genetic/button_opts")),
    PARTICLES(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_particles")),
    PREFERENCES(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_prefs")),
    RANDOM(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_random")),
    REBUILD(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_rebuild")),
    REMOVE(new NamespacedKey(TARDIS.plugin, "gui/bucket/remove")),
    RESTORE(new NamespacedKey(TARDIS.plugin, "genetic/button_restore")),
    ROOMS_WORLD(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_rooms_world")),
    SAVES(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_saves")),
    SCANNER(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_scanner")),
    SHORT(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_short")),
    SKINS(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_skins")),
    SONIC(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_sonic")),
    SYSTEM_UPGRADES(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_system_upgrades")),
    TARDIS_MAP(new NamespacedKey(TARDIS.plugin, "gui/map/button_tardis_map")),
    TEMPORAL(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_temp")),
    TERMINAL(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_term")),
    THEME(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_theme")),
    THROTTLE(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_throttle")),
    TRANSMAT(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_transmat")),
    TWA(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_twa")),
    TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    ZERO(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_zero"));

    private final NamespacedKey key;

    Button(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
