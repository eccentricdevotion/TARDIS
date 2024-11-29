package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ChemistryEquipment {

    COMPOUND(new NamespacedKey(TARDIS.plugin, "chemistry/compound")),
    CONSTRUCTOR(new NamespacedKey(TARDIS.plugin, "chemistry/constructor")),
    CREATIVE(new NamespacedKey(TARDIS.plugin, "chemistry/creative")),
    HEAT_BLOCK(new NamespacedKey(TARDIS.plugin, "chemistry/heat_block")),
    LAB(new NamespacedKey(TARDIS.plugin, "chemistry/lab")),
    PRODUCT(new NamespacedKey(TARDIS.plugin, "chemistry/product")),
    REDUCER(new NamespacedKey(TARDIS.plugin, "chemistry/reducer")),
    XRAY(new NamespacedKey(TARDIS.plugin, "tardis/xray")),
    MICROSCOPE(new NamespacedKey(TARDIS.plugin, "equipment/microscope")),
    GLASS_SLIDE(new NamespacedKey(TARDIS.plugin, "equipment/glass_slide")),
    SLIDE_RACK(new NamespacedKey(TARDIS.plugin, "equipment/slide_rack")),
    ELECTRON_MICROSCOPE(new NamespacedKey(TARDIS.plugin, "equipment/electron_microscope")),
    COMPUTER_MONITOR(new NamespacedKey(TARDIS.plugin, "equipment/screen")),
    COMPUTER_DISK(new NamespacedKey(TARDIS.plugin, "equipment/computer_disk")),
    FILING_CABINET_OPEN(new NamespacedKey(TARDIS.plugin, "equipment/filing_cabinet_open")),
    FOLDER(new NamespacedKey(TARDIS.plugin, "equipment/folder")),
    TELESCOPE(new NamespacedKey(TARDIS.plugin, "equipment/telescope")),
    SCREEN(new NamespacedKey(TARDIS.plugin, "equipment/screen")),
    BLEACH(new NamespacedKey(TARDIS.plugin, "products/bleach")),
    ICE_BOMB(new NamespacedKey(TARDIS.plugin, "products/ice_bomb")),
    SUPER_FERTILISER(new NamespacedKey(TARDIS.plugin, "products/super_fertiliser"));

    private final NamespacedKey key;

    ChemistryEquipment(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

