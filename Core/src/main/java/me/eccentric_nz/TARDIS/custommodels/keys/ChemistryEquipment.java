package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ChemistryEquipment {

    COMPOUND(new NamespacedKey(TARDIS.plugin, "compound")),
    CONSTRUCTOR(new NamespacedKey(TARDIS.plugin, "constructor")),
    CREATIVE(new NamespacedKey(TARDIS.plugin, "creative")),
    HEAT_BLOCK(new NamespacedKey(TARDIS.plugin, "heat_block")),
    LAB(new NamespacedKey(TARDIS.plugin, "lab")),
    PRODUCT(new NamespacedKey(TARDIS.plugin, "product")),
    REDUCER(new NamespacedKey(TARDIS.plugin, "reducer")),
    XRAY(new NamespacedKey(TARDIS.plugin, "xray")),
    MICROSCOPE(new NamespacedKey(TARDIS.plugin, "microscope")),
    GLASS_SLIDE(new NamespacedKey(TARDIS.plugin, "glass_slide")),
    SLIDE_RACK(new NamespacedKey(TARDIS.plugin, "slide_rack")),
    ELECTRON_MICROSCOPE(new NamespacedKey(TARDIS.plugin, "electron_microscope")),
    COMPUTER_MONITOR(new NamespacedKey(TARDIS.plugin, "screen")),
    COMPUTER_DISK(new NamespacedKey(TARDIS.plugin, "computer_disk")),
    FILING_CABINET_OPEN(new NamespacedKey(TARDIS.plugin, "filing_cabinet_open")),
    FOLDER(new NamespacedKey(TARDIS.plugin, "folder")),
    TELESCOPE(new NamespacedKey(TARDIS.plugin, "telescope")),
    SCREEN(new NamespacedKey(TARDIS.plugin, "screen")),
    BLEACH(new NamespacedKey(TARDIS.plugin, "products_bleach")),
    ICE_BOMB(new NamespacedKey(TARDIS.plugin, "products_ice_bomb")),
    SUPER_FERTILISER(new NamespacedKey(TARDIS.plugin, "products_super_fertiliser"));

    private final NamespacedKey key;

    ChemistryEquipment(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

