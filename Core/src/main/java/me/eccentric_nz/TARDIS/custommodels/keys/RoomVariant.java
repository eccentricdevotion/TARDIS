package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RoomVariant {

    ALLAY(new NamespacedKey(TARDIS.plugin, "button_allay")),
    ANTIGRAVITY(new NamespacedKey(TARDIS.plugin, "button_antigravity")),
    APIARY(new NamespacedKey(TARDIS.plugin, "button_apiary")),
    AQUARIUM(new NamespacedKey(TARDIS.plugin, "button_aquarium")),
    ARBORETUM(new NamespacedKey(TARDIS.plugin, "button_arboretum")),
    BAKER(new NamespacedKey(TARDIS.plugin, "button_baker")),
    BAMBOO(new NamespacedKey(TARDIS.plugin, "button_bamboo")),
    BEDROOM(new NamespacedKey(TARDIS.plugin, "button_bedroom")),
    BIRDCAGE(new NamespacedKey(TARDIS.plugin, "button_birdcage")),
    CHEMISTRY(new NamespacedKey(TARDIS.plugin, "button_chemistry")),
    EMPTY(new NamespacedKey(TARDIS.plugin, "button_empty")),
    EYE(new NamespacedKey(TARDIS.plugin, "button_eye")),
    FARM(new NamespacedKey(TARDIS.plugin, "button_farm")),
    GARDEN(new NamespacedKey(TARDIS.plugin, "button_garden")),
    GEODE(new NamespacedKey(TARDIS.plugin, "button_geode")),
    GRAVITY(new NamespacedKey(TARDIS.plugin, "button_gravity")),
    GREENHOUSE(new NamespacedKey(TARDIS.plugin, "button_greenhouse")),
    HARMONY(new NamespacedKey(TARDIS.plugin, "button_harmony")),
    HUTCH(new NamespacedKey(TARDIS.plugin, "button_hutch")),
    IGLOO(new NamespacedKey(TARDIS.plugin, "button_igloo")),
    IISTUBIL(new NamespacedKey(TARDIS.plugin, "button_iistubil")),
    JETTISON(new NamespacedKey(TARDIS.plugin, "button_jettison")),
    KITCHEN(new NamespacedKey(TARDIS.plugin, "button_kitchen")),
    LAVA(new NamespacedKey(TARDIS.plugin, "button_lava")),
    LAZARUS(new NamespacedKey(TARDIS.plugin, "button_lazarus")),
    LIBRARY(new NamespacedKey(TARDIS.plugin, "button_library")),
    MANGROVE(new NamespacedKey(TARDIS.plugin, "button_mangrove")),
    MAZE(new NamespacedKey(TARDIS.plugin, "button_maze")),
    MUSHROOM(new NamespacedKey(TARDIS.plugin, "button_mushroom")),
    NETHER(new NamespacedKey(TARDIS.plugin, "button_nether")),
    OBSERVATORY(new NamespacedKey(TARDIS.plugin, "button_observatory")),
    PASSAGE(new NamespacedKey(TARDIS.plugin, "button_passage")),
    PEN(new NamespacedKey(TARDIS.plugin, "button_pen")),
    POOL(new NamespacedKey(TARDIS.plugin, "button_pool")),
    RAIL(new NamespacedKey(TARDIS.plugin, "button_rail")),
    RENDERER(new NamespacedKey(TARDIS.plugin, "button_renderer")),
    SHELL(new NamespacedKey(TARDIS.plugin, "button_shell")),
    SLOT(new NamespacedKey(TARDIS.plugin, "button_slot")),
    SMELTER(new NamespacedKey(TARDIS.plugin, "button_smelter")),
    STABLE(new NamespacedKey(TARDIS.plugin, "button_stable")),
    STALL(new NamespacedKey(TARDIS.plugin, "button_stall")),
    SURGERY(new NamespacedKey(TARDIS.plugin, "button_surgery")),
    TRENZALORE(new NamespacedKey(TARDIS.plugin, "button_trenzalore")),
    VAULT(new NamespacedKey(TARDIS.plugin, "button_vault")),
    VILLAGE(new NamespacedKey(TARDIS.plugin, "button_village")),
    WOOD(new NamespacedKey(TARDIS.plugin, "button_wood")),
    WORKSHOP(new NamespacedKey(TARDIS.plugin, "button_workshop")),
    ZERO(new NamespacedKey(TARDIS.plugin, "button_zero"));

    private final NamespacedKey key;

    RoomVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
