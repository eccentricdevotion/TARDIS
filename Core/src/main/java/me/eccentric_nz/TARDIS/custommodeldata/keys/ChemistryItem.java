package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ChemistryItem {

    COMPOUND(new NamespacedKey(TARDIS.plugin, "block/chemistry/compound")),
    CONSTRUCTOR(new NamespacedKey(TARDIS.plugin, "block/chemistry/constructor")),
    CREATIVE(new NamespacedKey(TARDIS.plugin, "block/chemistry/creative")),
    HEAT_BLOCK(new NamespacedKey(TARDIS.plugin, "block/chemistry/heat_block")),
    LAB(new NamespacedKey(TARDIS.plugin, "block/chemistry/lab")),
    PRODUCT(new NamespacedKey(TARDIS.plugin, "block/chemistry/product")),
    REDUCER(new NamespacedKey(TARDIS.plugin, "block/chemistry/reducer")),
    XRAY(new NamespacedKey(TARDIS.plugin, "tardis/xray")),
    MICROSCOPE(new NamespacedKey(TARDIS.plugin, "equipment/microscope")),
    SLIDE_RACK(new NamespacedKey(TARDIS.plugin, "equipment/slide_rack")),
    ELECTRON_MICROSCOPE(new NamespacedKey(TARDIS.plugin, "equipment/electron_microscope")),
    COMPUTER_DISK(new NamespacedKey(TARDIS.plugin, "equipment/computer_disk")),
    COMPUTER_MONITOR(new NamespacedKey(TARDIS.plugin, "equipment/screen")),
    FILING_CABINET_OPEN(new NamespacedKey(TARDIS.plugin, "equipment/filing_cabinet_open")),
    TELESCOPE(new NamespacedKey(TARDIS.plugin, "equipment/telescope")),
    BLEACH(new NamespacedKey(TARDIS.plugin, "products/bleach")),
    ICE_BOMB(new NamespacedKey(TARDIS.plugin, "products/ice_bomb")),
    SUPER_FERTILISER(new NamespacedKey(TARDIS.plugin, "products/super_fertiliser"));

    private final NamespacedKey key;

    ChemistryItem(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

