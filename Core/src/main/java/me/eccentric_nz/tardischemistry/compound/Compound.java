/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardischemistry.compound;

import me.eccentric_nz.TARDIS.custommodeldata.keys.ChemistryBottle;
import org.bukkit.NamespacedKey;

public enum Compound {

    Aluminium_Oxide("Aluminium:2-Oxygen:3),", "Al2O3", ChemistryBottle.ALUMINIUM_OXIDE.getKey()),
    Ammonia("Nitrogen:1-Hydrogen:3", "NH3", ChemistryBottle.AMMONIA.getKey()),
    Barium_Sulfate("Barium:1-Sulfur:1-Oxygen:4", "BaSO4", ChemistryBottle.JAR.getKey()),
    Benzene("Carbon:6-Hydrogen:6", "C6H6", ChemistryBottle.BENZENE.getKey()),
    Boron_Trioxide("Boron:2-Oxygen:3", "B2O3", ChemistryBottle.JAR.getKey()),
    Calcium_Bromide("Calcium:1-Bromine:2", "CaBr2", ChemistryBottle.JAR.getKey()),
    Calcium_Chloride("Calcium:1-Chlorine:2", "CaCl2", ChemistryBottle.JAR.getKey()),
    Cerium_Chloride("Cerium:1-Chlorine:3", "CeCl3", ChemistryBottle.JAR.getKey()),
    Charcoal("Carbon:7-Hydrogen:4-Oxygen:1", "C7H4O", null),
    Crude_Oil("Carbon:9-Hydrogen:20", "C9H20", ChemistryBottle.CRUDE_OIL.getKey()),
    Glue("Carbon:5-Hydrogen:5-Nitrogen:1-Oxygen:2", "C5H5NO2", ChemistryBottle.ALUMINIUM_OXIDE.getKey()),
    Hydrogen_Peroxide("Hydrogen:2-Oxygen:2", "H2O2", ChemistryBottle.HYDROGEN_PEROXIDE.getKey()),
    Ink("Iron:1-Sulfur:1-Oxygen:4", "FeSO4", ChemistryBottle.JAR.getKey()),
    Iron_Sulfide("Iron:1-Sulfur:1", "FeS", ChemistryBottle.IRON_SULFIDE.getKey()),
    Latex("Carbon:5-Hydrogen:8", "C5H8", ChemistryBottle.ALUMINIUM_OXIDE.getKey()),
    Lithium_Hydride("Lithium:1-Hydrogen:1", "LiH", ChemistryBottle.ALUMINIUM_OXIDE.getKey()),
    Luminol("Carbon:8-Hydrogen:7-Nitrogen:3-Oxygen:2", "C8H7N3O2", ChemistryBottle.LUMINOL.getKey()),
    Lye("Sodium:1-Oxygen:1-Hydrogen:1", "NaOH", ChemistryBottle.ALUMINIUM_OXIDE.getKey()),
    Magnesium_Nitrate("Magnesium:2-Nitrogen:2-Oxygen:6", "Mg(NO3)2", ChemistryBottle.MAGNESIUM_NITRATE.getKey()),
    Magnesium_Oxide("Magnesium:1-Oxygen:1", "MgO", ChemistryBottle.JAR.getKey()),
    Mercuric_Chloride("Mercury:1-Chlorine:2", "HgCl2", ChemistryBottle.JAR.getKey()),
    Polyethylene("Carbon:10-Hydrogen:20", "(C2H4)5", ChemistryBottle.JAR.getKey()),
    Potassium_Chloride("Potassium:1-Chlorine:1", "KCl", ChemistryBottle.JAR.getKey()),
    Potassium_Iodide("Potassium:1-Iodine:1", "KI", ChemistryBottle.POTASSIUM_IODIDE.getKey()),
    Rust("Iron:2-Oxygen:3-Hydrogen:2-Oxygen:1", "Fe2O3(H2O)", ChemistryBottle.JAR.getKey()),
    Salt("Sodium:1-Chlorine:1", "NaCl", ChemistryBottle.ALUMINIUM_OXIDE.getKey()),
    Soap("Carbon:18-Hydrogen:35-Sodium:1-Oxygen:2", "C18H35NaO2", ChemistryBottle.SOAP.getKey()),
    Sodium_Acetate("Carbon:2-Hydrogen:3-Sodium:1-Oxygen:2", "C2H3NaO2", ChemistryBottle.JAR.getKey()),
    Sodium_Fluoride("Sodium:1-Fluorine:1", "NaF", ChemistryBottle.SODIUM_FLUORIDE.getKey()),
    Sodium_Hydride("Sodium:1-Hydrogen:1", "NaH", ChemistryBottle.SODIUM_HYDRIDE.getKey()),
    Sodium_Hypochlorite("Sodium:1-Chlorine:1-Oxygen:1", "NaClO", ChemistryBottle.SODIUM_HYPOCHLORITE.getKey()),
    Sodium_Oxide("Sodium:1-Oxygen:20", "Na2O", ChemistryBottle.JAR.getKey()),
    Sugar("Carbon:6-Hydrogen:12-Oxygen:6", "C6H12O6", null),
    Sulfate("Sulfur:1-Oxygen:4", "SO4", ChemistryBottle.JAR.getKey()),
    Sulphuric_Acid("Hydrogen:2-Sulfur:1-Oxygen:4", "H2SO4", ChemistryBottle.JAR.getKey()),
    Tungsten_Chloride("Tungsten:1-Chlorine:6", "WCl6", ChemistryBottle.JAR.getKey()),
    Water("Hydrogen:2-Oxygen:1", "H2O", null);

    private final String formula;
    private final String symbol;
    private final NamespacedKey model;

    Compound(String formula, String symbol, NamespacedKey model) {
        this.formula = formula;
        this.symbol = symbol;
        this.model = model;
    }

    public String getFormula() {
        return formula;
    }

    public String getSymbol() {
        return symbol;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return this.toString().replace("_", " ");
    }
}
