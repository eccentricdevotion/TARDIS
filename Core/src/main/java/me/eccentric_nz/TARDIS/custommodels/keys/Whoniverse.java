package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Whoniverse {

    ACID_BATTERY(new NamespacedKey(TARDIS.plugin, "acid_battery")),
    ACID_BUCKET(new NamespacedKey(TARDIS.plugin, "acid_bucket")),
    ARTRON_BATTERY(new NamespacedKey(TARDIS.plugin, "artron_battery")),
    BLASTER_BATTERY(new NamespacedKey(TARDIS.plugin, "blaster_battery")),
    ARTRON_CAPACITOR(new NamespacedKey(TARDIS.plugin, "artron_capacitor")),
    ARTRON_CAPACITOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "artron_capacitor_damaged")),
    ADVANCED_CONSOLE(new NamespacedKey(TARDIS.plugin, "advanced_console")),
    ARTRON_FURNACE(new NamespacedKey(TARDIS.plugin, "artron_furnace")),
    ARTRON_FURNACE_LIT(new NamespacedKey(TARDIS.plugin, "artron_furnace_lit")),
    BIOME_READER(new NamespacedKey(TARDIS.plugin, "biome_reader")),
    BLASTER(new NamespacedKey(TARDIS.plugin, "sonic_blaster")),
    DISK_STORAGE(new NamespacedKey(TARDIS.plugin, "disk_storage")),
    ELIXIR_OF_LIFE(new NamespacedKey(TARDIS.plugin, "elixir_of_life")),
    HANDLES_OFF(new NamespacedKey(TARDIS.plugin, "handles_off")),
    HANDLES_ON(new NamespacedKey(TARDIS.plugin, "handles_on")),
    COMMUNICATOR(new NamespacedKey(TARDIS.plugin, "handles_communicator")),
    COMMUNICATOR_OVERLAY(new NamespacedKey(TARDIS.plugin, "item/handles/handles_communicator_overlay")),
    LOCATOR(new NamespacedKey(TARDIS.plugin, "locator_16")),
    RUST_BUCKET(new NamespacedKey(TARDIS.plugin, "rust_bucket")),
    RUST_PLAGUE_SWORD(new NamespacedKey(TARDIS.plugin, "rust_plague_sword")),
    STATTENHEIM_REMOTE(new NamespacedKey(TARDIS.plugin, "stattenheim_remote")),
    ST_JOHNS(new NamespacedKey(TARDIS.plugin, "st_johns")),
    THREE_D_GLASSES(new NamespacedKey(TARDIS.plugin, "3d_glasses")),
    THREE_D_GLASSES_OVERLAY(new NamespacedKey(TARDIS.plugin, "item/tardis/3d_glasses_overlay")),
    FOB_WATCH(new NamespacedKey(TARDIS.plugin, "fob_watch")),
    VORTEX_MANIPULATOR(new NamespacedKey(TARDIS.plugin, "vortex_manipulator")),
    HELMET(new NamespacedKey(TARDIS.plugin, "space_suit_helmet")),
    TV(new NamespacedKey(TARDIS.plugin, "tv")),
    EYE_STORAGE(new NamespacedKey(TARDIS.plugin, "eye_storage")),
    PANDORICA(new NamespacedKey(TARDIS.plugin, "pandorica")),
    SIEGE_CUBE(new NamespacedKey(TARDIS.plugin, "siege_cube")),
    WAND(new NamespacedKey(TARDIS.plugin, "wand"));

    private final NamespacedKey key;

    Whoniverse(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

