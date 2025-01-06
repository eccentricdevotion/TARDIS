package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryEquipment;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public enum LabEquipment {

    MICROSCOPE(Material.LIGHT_GRAY_DYE, ChemistryEquipment.MICROSCOPE.getKey()),
    SLIDE_RACK(Material.BROWN_DYE, ChemistryEquipment.SLIDE_RACK.getKey()),
    ELECTRON_MICROSCOPE(Material.GRAY_DYE, ChemistryEquipment.ELECTRON_MICROSCOPE.getKey()),
    COMPUTER_MONITOR(Material.LIGHT_BLUE_DYE, ChemistryEquipment.COMPUTER_MONITOR.getKey()),
    TELESCOPE(Material.WHITE_DYE, ChemistryEquipment.TELESCOPE.getKey()),
    FILING_CABINET(Material.ORANGE_DYE, ChemistryEquipment.FILING_CABINET_OPEN.getKey());

    private static final HashMap<Material, LabEquipment> BY_MATERIAL = new HashMap<>();

    static {
        for (LabEquipment equipment : values()) {
            BY_MATERIAL.put(equipment.material, equipment);
        }
    }

    private final Material material;
    private final NamespacedKey model;

    LabEquipment(Material material, NamespacedKey model) {
        this.material = material;
        this.model = model;
    }

    public static HashMap<Material, LabEquipment> getByMaterial() {
        return BY_MATERIAL;
    }

    public String getName() {
        return TARDISStringUtils.capitalise(toString());
    }

    public Material getMaterial() {
        return material;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
