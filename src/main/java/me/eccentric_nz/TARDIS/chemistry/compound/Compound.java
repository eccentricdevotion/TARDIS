/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.compound;

public enum Compound {

    Aluminium_Oxide("Aluminium:2-Oxygen:3", "Al2O3"),
    Ammonia("Nitrogen:1-Hydrogen:3", "NH3"),
    Barium_Sulfate("Barium:1-Sulfur:1-Oxygen:4", "BaSO4"),
    Benzene("Carbon:6-Hydrogen:6", "C6H6"),
    Boron_Trioxide("Boron:2-Oxygen:3", "B2O3"),
    Calcium_Bromide("Calcium:1-Bromine:2", "CaBr2"),
    Calcium_Chloride("Calcium:1-Chlorine:2", "CaCl2"),
    Cerium_Chloride("Cerium:1-Chlorine:3", "CeCl3"),
    Charcoal("Carbon:7-Hydrogen:4-Oxygen:1", "C7H4O"),
    Crude_Oil("Carbon:9-Hydrogen:20", "C9H20"),
    Glue("Carbon:5-Hydrogen:5-Nitrogen:1-Oxygen:2", "C5H5NO2"),
    Hydrogen_Peroxide("Hydrogen:2-Oxygen:2", "H2O2"),
    Ink("Iron:1-Sulfur:1-Oxygen:4", "FeSO4"),
    Iron_Sulfide("Iron:1-Sulfur:1", "FeS"),
    Latex("Carbon:5-Hydrogen:8", "C5H8"),
    Lithium_Hydride("Lithium:1-Hydrogen:1", "LiH"),
    Luminol("Carbon:8-Hydrogen:7-Nitrogen:3-Oxygen:2", "C8H7N3O2"),
    Lye("Sodium:1-Oxygen:1-Hydrogen:1", "NaOH"),
    Magnesium_Nitrate("Magnesium:2-Nitrogen:2-Oxygen:6", "Mg(NO3)2"),
    Magnesium_Oxide("Magnesium:1-Oxygen:1", "MgO"),
    Mercuric_Chloride("Mercury:1-Chlorine:2", "HgCl2"),
    Polyethylene("Carbon:10-Hydrogen:20", "(C2H4)5"),
    Potassium_Chloride("Potassium:1-Chlorine:1", "KCl"),
    Potassium_Iodide("Potassium:1-Iodine:1", "KI"),
    Salt("Sodium:1-Chlorine:1", "NaCl"),
    Soap("Carbon:18-Hydrogen:35-Sodium:1-Oxygen:2", "C18H35NaO2"),
    Sodium_Acetate("Carbon:2-Hydrogen:3-Sodium:1-Oxygen:2", "C2H3NaO2"),
    Sodium_Fluoride("Sodium:1-Fluorine:1", "NaF"),
    Sodium_Hydride("Sodium:1-Hydrogen:1", "NaH"),
    Sodium_Hypochlorite("Sodium:1-Chlorine:1-Oxygen:1", "NaClO"),
    Sodium_Oxide("Sodium:1-Oxygen:20", "Na2O"),
    Sugar("Carbon:6-Hydrogen:12-Oxygen:6", "C6H12O6"),
    Sulfate("Sulfur:1-Oxygen:4", "SO4"),
    Tungsten_Chloride("Tungsten:1-Chlorine:6", "WCl6"),
    Water("Hydrogen:2-Oxygen:1", "H2O");

    private final String formula;
    private final String symbol;

    Compound(String formula, String symbol) {
        this.formula = formula;
        this.symbol = symbol;
    }

    public String getFormula() {
        return formula;
    }

    public String getSymbol() {
        return symbol;
    }
}
