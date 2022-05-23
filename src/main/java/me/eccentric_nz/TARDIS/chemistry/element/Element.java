/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.element;

import java.util.HashMap;

public enum Element {
    Hydrogen(1, "H", 0),
    Helium(2, "He", 2),
    Lithium(3, "Li", 4),
    Beryllium(4, "Be", 5),
    Boron(5, "B", 6),
    Carbon(6, "C", 6),
    Nitrogen(7, "N", 7),
    Oxygen(8, "O", 8),
    Fluorine(9, "F", 10),
    Neon(10, "Ne", 10),
    Sodium(11, "Na", 12),
    Magnesium(12, "Mg", 12),
    Aluminium(13, "Al", 14),
    Silicon(14, "Si", 14),
    Phosphorus(15, "P", 16),
    Sulfur(16, "S", 16),
    Chlorine(17, "Cl", 18),
    Argon(18, "Ar", 22),
    Potassium(19, "K", 20),
    Calcium(20, "Ca", 20),
    Scandium(21, "Sc", 24),
    Titanium(22, "Ti", 26),
    Vanadium(23, "V", 28),
    Chromium(24, "Cr", 28),
    Manganese(25, "Mn", 30),
    Iron(26, "Fe", 30),
    Cobalt(27, "Co", 32),
    Nickel(28, "Ni", 31),
    Copper(29, "Cu", 35),
    Zinc(30, "Zn", 30),
    Gallium(31, "Ga", 39),
    Germanium(32, "Ge", 41),
    Arsenic(33, "As", 42),
    Selenium(34, "Se", 45),
    Bromine(35, "Br", 45),
    Krypton(36, "Kr", 48),
    Rubidium(37, "Rb", 48),
    Strontium(38, "Sr", 50),
    Yttrium(39, "Y", 50),
    Zirconium(40, "Zr", 51),
    Niobium(41, "Nb", 52),
    Molybdenum(42, "Mo", 54),
    Technetium(43, "Tc", 55),
    Ruthenium(44, "Ru", 57),
    Rhodium(45, "Rh", 58),
    Palladium(46, "Pd", 60),
    Silver(47, "Ag", 61),
    Cadmium(48, "Cd", 64),
    Indium(49, "In", 66),
    Tin(50, "Sn", 69),
    Antimony(51, "Sb", 71),
    Tellurium(52, "Te", 76),
    Iodine(53, "I", 74),
    Xenon(54, "Xe", 77),
    Caesium(55, "Cs", 78),
    Barium(56, "Ba", 81),
    Lanthanum(57, "La", 82),
    Cerium(58, "Ce", 82),
    Praseodymium(59, "Pr", 82),
    Neodymium(60, "Nd", 84),
    Promethium(61, "Pm", 84),
    Samarium(62, "Sm", 88),
    Europium(63, "Eu", 89),
    Gadolinium(64, "Gd", 93),
    Terbium(65, "Tb", 94),
    Dysprosium(66, "Dy", 97),
    Holmium(67, "Ho", 98),
    Erbium(68, "Er", 99),
    Thulium(69, "Tm", 100),
    Ytterbium(70, "Yb", 103),
    Lutetium(71, "Lu", 104),
    Hafnium(72, "Hf", 106),
    Tantalum(73, "Ta", 108),
    Tungsten(74, "W", 110),
    Rhenium(75, "Re", 111),
    Osmium(76, "Os", 114),
    Iridium(77, "Ir", 115),
    Platinum(78, "Pt", 117),
    Gold(79, "Au", 118),
    Mercury(80, "Hg", 121),
    Thallium(81, "Tl", 123),
    Lead(82, "Pb", 125),
    Bismuth(83, "Bi", 126),
    Polonium(84, "Po", 125),
    Astatine(85, "At", 125),
    Radon(86, "Rn", 136),
    Francium(87, "Fr", 136),
    Radium(88, "Ra", 138),
    Actinium(89, "Ac", 138),
    Thorium(90, "Th", 142),
    Protactinium(91, "Pa", 148),
    Uranium(92, "U", 146),
    Neptunium(93, "Np", 144),
    Plutonium(94, "Pu", 150),
    Americium(95, "Am", 148),
    Curium(96, "Cm", 151),
    Berkelium(97, "Bk", 150),
    Californium(98, "Cf", 153),
    Einsteinium(99, "Es", 153),
    Fermium(100, "Fm", 157),
    Mendelevium(101, "Md", 157),
    Nobelium(102, "No", 157),
    Lawrencium(103, "Lr", 159),
    Rutherfordium(104, "Rf", 157),
    Dubnium(105, "Db", 157),
    Seaborgium(106, "Sg", 157),
    Bohrium(107, "Bh", 155),
    Hassium(108, "Hs", 157),
    Meitnerium(109, "Mt", 157),
    Darmstadtium(110, "Ds", 151),
    Roentgenium(111, "Rg", 161),
    Copernicium(112, "Cn", 165),
    Nihonium(113, "Nh", 173),
    Flerovium(114, "Fl", 174),
    Moscovium(115, "Mc", 174),
    Livermorium(116, "Lv", 177),
    Tennessine(117, "Ts", 177),
    Oganesson(118, "Og", 176),
    Unknown(999, "Un", 999);

    private static final HashMap<String, Element> bySymbol = new HashMap<>();
    private static final HashMap<Integer, Element> byAtomicNumber = new HashMap<>();

    static {
        for (Element e : values()) {
            bySymbol.put(e.symbol, e);
            byAtomicNumber.put(e.atomicNumber, e);
        }
    }

    private final int atomicNumber;
    private final String symbol;
    private final int neutrons;

    Element(int atomicNumber, String symbol, int neutrons) {
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
        this.neutrons = neutrons;
    }

    public static HashMap<String, Element> getBySymbol() {
        return bySymbol;
    }

    public static HashMap<Integer, Element> getByAtomicNumber() {
        return byAtomicNumber;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getNeutrons() {
        return neutrons;
    }
}
