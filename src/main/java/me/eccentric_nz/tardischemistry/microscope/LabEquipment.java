/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
