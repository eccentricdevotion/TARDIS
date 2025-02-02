/*
 * Copyright (C) 2025 eccentric_nz
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
 * along with this program. If not, see <http://www.gnu.org/license_>.
 */
package me.eccentric_nz.tardischemistry.element;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public enum Element {
    Hydrogen(1, "H", 0, new NamespacedKey(TARDIS.plugin, "element_1")),
    Helium(2, "He", 2, new NamespacedKey(TARDIS.plugin, "element_2")),
    Lithium(3, "Li", 4, new NamespacedKey(TARDIS.plugin, "element_3")),
    Beryllium(4, "Be", 5, new NamespacedKey(TARDIS.plugin, "element_4")),
    Boron(5, "B", 6, new NamespacedKey(TARDIS.plugin, "element_5")),
    Carbon(6, "C", 6, new NamespacedKey(TARDIS.plugin, "element_6")),
    Nitrogen(7, "N", 7, new NamespacedKey(TARDIS.plugin, "element_7")),
    Oxygen(8, "O", 8, new NamespacedKey(TARDIS.plugin, "element_8")),
    Fluorine(9, "F", 10, new NamespacedKey(TARDIS.plugin, "element_9")),
    Neon(10, "Ne", 10, new NamespacedKey(TARDIS.plugin, "element_10")),
    Sodium(11, "Na", 12, new NamespacedKey(TARDIS.plugin, "element_11")),
    Magnesium(12, "Mg", 12, new NamespacedKey(TARDIS.plugin, "element_12")),
    Aluminium(13, "Al", 14, new NamespacedKey(TARDIS.plugin, "element_13")),
    Silicon(14, "Si", 14, new NamespacedKey(TARDIS.plugin, "element_14")),
    Phosphorus(15, "P", 16, new NamespacedKey(TARDIS.plugin, "element_15")),
    Sulfur(16, "S", 16, new NamespacedKey(TARDIS.plugin, "element_16")),
    Chlorine(17, "Cl", 18, new NamespacedKey(TARDIS.plugin, "element_17")),
    Argon(18, "Ar", 22, new NamespacedKey(TARDIS.plugin, "element_18")),
    Potassium(19, "K", 20, new NamespacedKey(TARDIS.plugin, "element_19")),
    Calcium(20, "Ca", 20, new NamespacedKey(TARDIS.plugin, "element_20")),
    Scandium(21, "Sc", 24, new NamespacedKey(TARDIS.plugin, "element_21")),
    Titanium(22, "Ti", 26, new NamespacedKey(TARDIS.plugin, "element_22")),
    Vanadium(23, "V", 28, new NamespacedKey(TARDIS.plugin, "element_23")),
    Chromium(24, "Cr", 28, new NamespacedKey(TARDIS.plugin, "element_24")),
    Manganese(25, "Mn", 30, new NamespacedKey(TARDIS.plugin, "element_25")),
    Iron(26, "Fe", 30, new NamespacedKey(TARDIS.plugin, "element_26")),
    Cobalt(27, "Co", 32, new NamespacedKey(TARDIS.plugin, "element_27")),
    Nickel(28, "Ni", 31, new NamespacedKey(TARDIS.plugin, "element_28")),
    Copper(29, "Cu", 35, new NamespacedKey(TARDIS.plugin, "element_29")),
    Zinc(30, "Zn", 30, new NamespacedKey(TARDIS.plugin, "element_30")),
    Gallium(31, "Ga", 39, new NamespacedKey(TARDIS.plugin, "element_31")),
    Germanium(32, "Ge", 41, new NamespacedKey(TARDIS.plugin, "element_32")),
    Arsenic(33, "As", 42, new NamespacedKey(TARDIS.plugin, "element_33")),
    Selenium(34, "Se", 45, new NamespacedKey(TARDIS.plugin, "element_34")),
    Bromine(35, "Br", 45, new NamespacedKey(TARDIS.plugin, "element_35")),
    Krypton(36, "Kr", 48, new NamespacedKey(TARDIS.plugin, "element_36")),
    Rubidium(37, "Rb", 48, new NamespacedKey(TARDIS.plugin, "element_37")),
    Strontium(38, "Sr", 50, new NamespacedKey(TARDIS.plugin, "element_38")),
    Yttrium(39, "Y", 50, new NamespacedKey(TARDIS.plugin, "element_39")),
    Zirconium(40, "Zr", 51, new NamespacedKey(TARDIS.plugin, "element_40")),
    Niobium(41, "Nb", 52, new NamespacedKey(TARDIS.plugin, "element_41")),
    Molybdenum(42, "Mo", 54, new NamespacedKey(TARDIS.plugin, "element_42")),
    Technetium(43, "Tc", 55, new NamespacedKey(TARDIS.plugin, "element_43")),
    Ruthenium(44, "Ru", 57, new NamespacedKey(TARDIS.plugin, "element_44")),
    Rhodium(45, "Rh", 58, new NamespacedKey(TARDIS.plugin, "element_45")),
    Palladium(46, "Pd", 60, new NamespacedKey(TARDIS.plugin, "element_46")),
    Silver(47, "Ag", 61, new NamespacedKey(TARDIS.plugin, "element_47")),
    Cadmium(48, "Cd", 64, new NamespacedKey(TARDIS.plugin, "element_48")),
    Indium(49, "In", 66, new NamespacedKey(TARDIS.plugin, "element_49")),
    Tin(50, "Sn", 69, new NamespacedKey(TARDIS.plugin, "element_50")),
    Antimony(51, "Sb", 71, new NamespacedKey(TARDIS.plugin, "element_51")),
    Tellurium(52, "Te", 76, new NamespacedKey(TARDIS.plugin, "element_52")),
    Iodine(53, "I", 74, new NamespacedKey(TARDIS.plugin, "element_53")),
    Xenon(54, "Xe", 77, new NamespacedKey(TARDIS.plugin, "element_54")),
    Caesium(55, "Cs", 78, new NamespacedKey(TARDIS.plugin, "element_55")),
    Barium(56, "Ba", 81, new NamespacedKey(TARDIS.plugin, "element_56")),
    Lanthanum(57, "La", 82, new NamespacedKey(TARDIS.plugin, "element_57")),
    Cerium(58, "Ce", 82, new NamespacedKey(TARDIS.plugin, "element_58")),
    Praseodymium(59, "Pr", 82, new NamespacedKey(TARDIS.plugin, "element_59")),
    Neodymium(60, "Nd", 84, new NamespacedKey(TARDIS.plugin, "element_60")),
    Promethium(61, "Pm", 84, new NamespacedKey(TARDIS.plugin, "element_61")),
    Samarium(62, "Sm", 88, new NamespacedKey(TARDIS.plugin, "element_62")),
    Europium(63, "Eu", 89, new NamespacedKey(TARDIS.plugin, "element_63")),
    Gadolinium(64, "Gd", 93, new NamespacedKey(TARDIS.plugin, "element_64")),
    Terbium(65, "Tb", 94, new NamespacedKey(TARDIS.plugin, "element_65")),
    Dysprosium(66, "Dy", 97, new NamespacedKey(TARDIS.plugin, "element_66")),
    Holmium(67, "Ho", 98, new NamespacedKey(TARDIS.plugin, "element_67")),
    Erbium(68, "Er", 99, new NamespacedKey(TARDIS.plugin, "element_68")),
    Thulium(69, "Tm", 100, new NamespacedKey(TARDIS.plugin, "element_69")),
    Ytterbium(70, "Yb", 103, new NamespacedKey(TARDIS.plugin, "element_70")),
    Lutetium(71, "Lu", 104, new NamespacedKey(TARDIS.plugin, "element_71")),
    Hafnium(72, "Hf", 106, new NamespacedKey(TARDIS.plugin, "element_72")),
    Tantalum(73, "Ta", 108, new NamespacedKey(TARDIS.plugin, "element_73")),
    Tungsten(74, "W", 110, new NamespacedKey(TARDIS.plugin, "element_74")),
    Rhenium(75, "Re", 111, new NamespacedKey(TARDIS.plugin, "element_75")),
    Osmium(76, "Os", 114, new NamespacedKey(TARDIS.plugin, "element_76")),
    Iridium(77, "Ir", 115, new NamespacedKey(TARDIS.plugin, "element_77")),
    Platinum(78, "Pt", 117, new NamespacedKey(TARDIS.plugin, "element_78")),
    Gold(79, "Au", 118, new NamespacedKey(TARDIS.plugin, "element_79")),
    Mercury(80, "Hg", 121, new NamespacedKey(TARDIS.plugin, "element_80")),
    Thallium(81, "Tl", 123, new NamespacedKey(TARDIS.plugin, "element_81")),
    Lead(82, "Pb", 125, new NamespacedKey(TARDIS.plugin, "element_82")),
    Bismuth(83, "Bi", 126, new NamespacedKey(TARDIS.plugin, "element_83")),
    Polonium(84, "Po", 125, new NamespacedKey(TARDIS.plugin, "element_84")),
    Astatine(85, "At", 125, new NamespacedKey(TARDIS.plugin, "element_85")),
    Radon(86, "Rn", 136, new NamespacedKey(TARDIS.plugin, "element_86")),
    Francium(87, "Fr", 136, new NamespacedKey(TARDIS.plugin, "element_87")),
    Radium(88, "Ra", 138, new NamespacedKey(TARDIS.plugin, "element_88")),
    Actinium(89, "Ac", 138, new NamespacedKey(TARDIS.plugin, "element_89")),
    Thorium(90, "Th", 142, new NamespacedKey(TARDIS.plugin, "element_90")),
    Protactinium(91, "Pa", 148, new NamespacedKey(TARDIS.plugin, "element_91")),
    Uranium(92, "U", 146, new NamespacedKey(TARDIS.plugin, "element_92")),
    Neptunium(93, "Np", 144, new NamespacedKey(TARDIS.plugin, "element_93")),
    Plutonium(94, "Pu", 150, new NamespacedKey(TARDIS.plugin, "element_94")),
    Americium(95, "Am", 148, new NamespacedKey(TARDIS.plugin, "element_95")),
    Curium(96, "Cm", 151, new NamespacedKey(TARDIS.plugin, "element_96")),
    Berkelium(97, "Bk", 150, new NamespacedKey(TARDIS.plugin, "element_97")),
    Californium(98, "Cf", 153, new NamespacedKey(TARDIS.plugin, "element_98")),
    Einsteinium(99, "Es", 153, new NamespacedKey(TARDIS.plugin, "element_99")),
    Fermium(100, "Fm", 157, new NamespacedKey(TARDIS.plugin, "element_100")),
    Mendelevium(101, "Md", 157, new NamespacedKey(TARDIS.plugin, "element_101")),
    Nobelium(102, "No", 157, new NamespacedKey(TARDIS.plugin, "element_102")),
    Lawrencium(103, "Lr", 159, new NamespacedKey(TARDIS.plugin, "element_103")),
    Rutherfordium(104, "Rf", 157, new NamespacedKey(TARDIS.plugin, "element_104")),
    Dubnium(105, "Db", 157, new NamespacedKey(TARDIS.plugin, "element_105")),
    Seaborgium(106, "Sg", 157, new NamespacedKey(TARDIS.plugin, "element_106")),
    Bohrium(107, "Bh", 155, new NamespacedKey(TARDIS.plugin, "element_107")),
    Hassium(108, "Hs", 157, new NamespacedKey(TARDIS.plugin, "element_108")),
    Meitnerium(109, "Mt", 157, new NamespacedKey(TARDIS.plugin, "element_109")),
    Darmstadtium(110, "Ds", 151, new NamespacedKey(TARDIS.plugin, "element_110")),
    Roentgenium(111, "Rg", 161, new NamespacedKey(TARDIS.plugin, "element_111")),
    Copernicium(112, "Cn", 165, new NamespacedKey(TARDIS.plugin, "element_112")),
    Nihonium(113, "Nh", 173, new NamespacedKey(TARDIS.plugin, "element_113")),
    Flerovium(114, "Fl", 174, new NamespacedKey(TARDIS.plugin, "element_114")),
    Moscovium(115, "Mc", 174, new NamespacedKey(TARDIS.plugin, "element_115")),
    Livermorium(116, "Lv", 177, new NamespacedKey(TARDIS.plugin, "element_116")),
    Tennessine(117, "Ts", 177, new NamespacedKey(TARDIS.plugin, "element_117")),
    Oganesson(118, "Og", 176, new NamespacedKey(TARDIS.plugin, "element_118")),
    Unknown(119, "Un", 999, new NamespacedKey(TARDIS.plugin, "unknown"));

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
    private final NamespacedKey model;

    Element(int atomicNumber, String symbol, int neutrons, NamespacedKey model) {
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
        this.neutrons = neutrons;
        this.model = model;
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

    public NamespacedKey getModel() {
        return model;
    }
}
