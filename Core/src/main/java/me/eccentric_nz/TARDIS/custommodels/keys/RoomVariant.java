package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RoomVariant {

    ALLAY(new NamespacedKey(TARDIS.plugin, "gui/room/allay")),
    ANTIGRAVITY(new NamespacedKey(TARDIS.plugin, "gui/room/antigravity")),
    APIARY(new NamespacedKey(TARDIS.plugin, "gui/room/apiary")),
    AQUARIUM(new NamespacedKey(TARDIS.plugin, "gui/room/aquarium")),
    ARBORETUM(new NamespacedKey(TARDIS.plugin, "gui/room/arboretum")),
    BAKER(new NamespacedKey(TARDIS.plugin, "gui/room/baker")),
    BAMBOO(new NamespacedKey(TARDIS.plugin, "gui/room/bamboo")),
    BEDROOM(new NamespacedKey(TARDIS.plugin, "gui/room/bedroom")),
    BIRDCAGE(new NamespacedKey(TARDIS.plugin, "gui/room/birdcage")),
    CHEMISTRY(new NamespacedKey(TARDIS.plugin, "gui/room/chemistry")),
    EMPTY(new NamespacedKey(TARDIS.plugin, "gui/room/empty")),
    EYE(new NamespacedKey(TARDIS.plugin, "gui/room/eye")),
    FARM(new NamespacedKey(TARDIS.plugin, "gui/room/farm")),
    GARDEN(new NamespacedKey(TARDIS.plugin, "gui/room/garden")),
    GEODE(new NamespacedKey(TARDIS.plugin, "gui/room/geode")),
    GRAVITY(new NamespacedKey(TARDIS.plugin, "gui/room/gravity")),
    GREENHOUSE(new NamespacedKey(TARDIS.plugin, "gui/room/greenhouse")),
    HARMONY(new NamespacedKey(TARDIS.plugin, "gui/room/harmony")),
    HUTCH(new NamespacedKey(TARDIS.plugin, "gui/room/hutch")),
    IGLOO(new NamespacedKey(TARDIS.plugin, "gui/room/igloo")),
    IISTUBIL(new NamespacedKey(TARDIS.plugin, "gui/room/iistubil")),
    JETTISON(new NamespacedKey(TARDIS.plugin, "gui/room/jettison")),
    KITCHEN(new NamespacedKey(TARDIS.plugin, "gui/room/kitchen")),
    LAVA(new NamespacedKey(TARDIS.plugin, "gui/room/lava")),
    LAZARUS(new NamespacedKey(TARDIS.plugin, "gui/room/lazarus")),
    LIBRARY(new NamespacedKey(TARDIS.plugin, "gui/room/library")),
    MANGROVE(new NamespacedKey(TARDIS.plugin, "gui/room/mangrove")),
    MAZE(new NamespacedKey(TARDIS.plugin, "gui/room/maze")),
    MUSHROOM(new NamespacedKey(TARDIS.plugin, "gui/room/mushroom")),
    NETHER(new NamespacedKey(TARDIS.plugin, "gui/room/nether")),
    OBSERVATORY(new NamespacedKey(TARDIS.plugin, "gui/room/observatory")),
    PASSAGE(new NamespacedKey(TARDIS.plugin, "gui/room/passage")),
    PEN(new NamespacedKey(TARDIS.plugin, "gui/room/pen")),
    POOL(new NamespacedKey(TARDIS.plugin, "gui/room/pool")),
    RAIL(new NamespacedKey(TARDIS.plugin, "gui/room/rail")),
    RENDERER(new NamespacedKey(TARDIS.plugin, "gui/room/renderer")),
    SHELL(new NamespacedKey(TARDIS.plugin, "gui/room/shell")),
    SLOT(new NamespacedKey(TARDIS.plugin, "gui/room/slot")),
    SMELTER(new NamespacedKey(TARDIS.plugin, "gui/room/smelter")),
    STABLE(new NamespacedKey(TARDIS.plugin, "gui/room/stable")),
    STALL(new NamespacedKey(TARDIS.plugin, "gui/room/stall")),
    SURGERY(new NamespacedKey(TARDIS.plugin, "gui/room/surgery")),
    TRENZALORE(new NamespacedKey(TARDIS.plugin, "gui/room/trenzalore")),
    VAULT(new NamespacedKey(TARDIS.plugin, "gui/room/vault")),
    VILLAGE(new NamespacedKey(TARDIS.plugin, "gui/room/village")),
    WOOD(new NamespacedKey(TARDIS.plugin, "gui/room/wood")),
    WORKSHOP(new NamespacedKey(TARDIS.plugin, "gui/room/workshop")),
    ZERO(new NamespacedKey(TARDIS.plugin, "gui/room/zero"));

    private final NamespacedKey key;

    RoomVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
