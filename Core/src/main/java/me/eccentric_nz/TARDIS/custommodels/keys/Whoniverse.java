package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Whoniverse {

    ACID_BATTERY(new NamespacedKey(TARDIS.plugin, "tardis/acid_battery")),
    ACID_BUCKET(new NamespacedKey(TARDIS.plugin, "planets/acid_bucket")),
    ARTRON_BATTERY(new NamespacedKey(TARDIS.plugin, "tardis/artron_battery")),
    BLASTER_BATTERY(new NamespacedKey(TARDIS.plugin, "tardis/blaster_battery")),
    ARTRON_CAPACITOR(new NamespacedKey(TARDIS.plugin, "tardis/artron_capacitor")),
    ARTRON_CAPACITOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "tardis/artron_capacitor_damaged")),
    ADVANCED_CONSOLE(new NamespacedKey(TARDIS.plugin, "tardis/advanced_console")),
    ARTRON_FURNACE(new NamespacedKey(TARDIS.plugin, "tardis/artron_furnace")),
    ARTRON_FURNACE_LIT(new NamespacedKey(TARDIS.plugin, "tardis/artron_furnace_lit")),
    BIOME_READER(new NamespacedKey(TARDIS.plugin, "tardis/biome_reader")),
    BLASTER(new NamespacedKey(TARDIS.plugin, "sonic/blaster")),
    DISK_STORAGE(new NamespacedKey(TARDIS.plugin, "tardis/disk_storage")),
    ELIXIR_OF_LIFE(new NamespacedKey(TARDIS.plugin, "regeneration/elixir_of_life")),
    HANDLES_OFF(new NamespacedKey(TARDIS.plugin, "handles/handles_off")),
    HANDLES_ON(new NamespacedKey(TARDIS.plugin, "handles/handles_on")),
    COMMUNICATOR(new NamespacedKey(TARDIS.plugin, "handles/communicator")),
    // TODO make texture file
    COMMUNICATOR_OVERLAY(new NamespacedKey(TARDIS.plugin, "handles/communicator_overlay")),
    LOCATOR(new NamespacedKey(TARDIS.plugin, "locator/locator_16")),
    RUST_BUCKET(new NamespacedKey(TARDIS.plugin, "planets/rust_bucket")),
    RUST_PLAGUE_SWORD(new NamespacedKey(TARDIS.plugin, "planets/rust_plague_sword")),
    STATTENHEIM_REMOTE(new NamespacedKey(TARDIS.plugin, "tardis/stattenheim_remote")),
    ST_JOHNS(new NamespacedKey(TARDIS.plugin, "tardis/st_johns")),
    THREE_D_GLASSES(new NamespacedKey(TARDIS.plugin, "tardis/3d_glasses")),
    // TODO find 3D glasses overlay texture,
    THREE_D_GLASSES_OVERLAY(new NamespacedKey(TARDIS.plugin, "tardis/3d_glasses_overlay")),
    FOB_WATCH(new NamespacedKey(TARDIS.plugin, "tardis/fob_watch")),
    VORTEX_MANIPULATOR(new NamespacedKey(TARDIS.plugin, "tardis/vortex_manipulator")),
    HELMET(new NamespacedKey(TARDIS.plugin, "space_suit/helmet")),
    TV(new NamespacedKey(TARDIS.plugin, "lazarus/tv")),
    EYE_STORAGE(new NamespacedKey(TARDIS.plugin, "tardis/eye_storage")),
    PANDORICA(new NamespacedKey(TARDIS.plugin, "pandorica")),
    SIEGE_CUBE(new NamespacedKey(TARDIS.plugin, "siege_cube")),
    WAND(new NamespacedKey(TARDIS.plugin, "tardis/wand"));

    private final NamespacedKey key;

    Whoniverse(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

