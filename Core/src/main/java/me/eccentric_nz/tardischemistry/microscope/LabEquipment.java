package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.HashMap;

public enum LabEquipment {

    MICROSCOPE(Material.LIGHT_GRAY_DYE),
    SLIDE_RACK(Material.BROWN_DYE),
    ELECTRON_MICROSCOPE(Material.GRAY_DYE),
    COMPUTER_MONITOR(Material.LIGHT_BLUE_DYE),
    TELESCOPE(Material.WHITE_DYE),
    FILING_CABINET(Material.ORANGE_DYE);

    private static final HashMap<Material, LabEquipment> BY_MATERIAL = new HashMap<>();

    static {
        for (LabEquipment equipment : values()) {
            BY_MATERIAL.put(equipment.material, equipment);
        }
    }

    public final Material material;

    LabEquipment(Material material) {
        this.material = material;
    }

    public static HashMap<Material, LabEquipment> getByMaterial() {
        return BY_MATERIAL;
    }

    public String getName() {
        return TARDISStringUtils.capitalise(toString());
    }
}
