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
package me.eccentric_nz.tardischemistry.element;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public enum Element {
    Hydrogen(1, "H", 0, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Helium(2, "He", 2, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Lithium(3, "Li", 4, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Beryllium(4, "Be", 5, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Boron(5, "B", 6, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Carbon(6, "C", 6, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Nitrogen(7, "N", 7, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Oxygen(8, "O", 8, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Fluorine(9, "F", 10, new NamespacedKey(TARDIS.plugin, "elements/1")),
    Neon(10, "Ne", 10, new NamespacedKey(TARDIS.plugin, "elements/10")),
    Sodium(11, "Na", 12, new NamespacedKey(TARDIS.plugin, "elements/11")),
    Magnesium(12, "Mg", 12, new NamespacedKey(TARDIS.plugin, "elements/12")),
    Aluminium(13, "Al", 14, new NamespacedKey(TARDIS.plugin, "elements/13")),
    Silicon(14, "Si", 14, new NamespacedKey(TARDIS.plugin, "elements/14")),
    Phosphorus(15, "P", 16, new NamespacedKey(TARDIS.plugin, "elements/15")),
    Sulfur(16, "S", 16, new NamespacedKey(TARDIS.plugin, "elements/16")),
    Chlorine(17, "Cl", 18, new NamespacedKey(TARDIS.plugin, "elements/17")),
    Argon(18, "Ar", 22, new NamespacedKey(TARDIS.plugin, "elements/18")),
    Potassium(19, "K", 20, new NamespacedKey(TARDIS.plugin, "elements/19")),
    Calcium(20, "Ca", 20, new NamespacedKey(TARDIS.plugin, "elements/20")),
    Scandium(21, "Sc", 24, new NamespacedKey(TARDIS.plugin, "elements/21")),
    Titanium(22, "Ti", 26, new NamespacedKey(TARDIS.plugin, "elements/22")),
    Vanadium(23, "V", 28, new NamespacedKey(TARDIS.plugin, "elements/23")),
    Chromium(24, "Cr", 28, new NamespacedKey(TARDIS.plugin, "elements/24")),
    Manganese(25, "Mn", 30, new NamespacedKey(TARDIS.plugin, "elements/25")),
    Iron(26, "Fe", 30, new NamespacedKey(TARDIS.plugin, "elements/26")),
    Cobalt(27, "Co", 32, new NamespacedKey(TARDIS.plugin, "elements/27")),
    Nickel(28, "Ni", 31, new NamespacedKey(TARDIS.plugin, "elements/28")),
    Copper(29, "Cu", 35, new NamespacedKey(TARDIS.plugin, "elements/29")),
    Zinc(30, "Zn", 30, new NamespacedKey(TARDIS.plugin, "elements/30")),
    Gallium(31, "Ga", 39, new NamespacedKey(TARDIS.plugin, "elements/31")),
    Germanium(32, "Ge", 41, new NamespacedKey(TARDIS.plugin, "elements/32")),
    Arsenic(33, "As", 42, new NamespacedKey(TARDIS.plugin, "elements/33")),
    Selenium(34, "Se", 45, new NamespacedKey(TARDIS.plugin, "elements/34")),
    Bromine(35, "Br", 45, new NamespacedKey(TARDIS.plugin, "elements/35")),
    Krypton(36, "Kr", 48, new NamespacedKey(TARDIS.plugin, "elements/36")),
    Rubidium(37, "Rb", 48, new NamespacedKey(TARDIS.plugin, "elements/37")),
    Strontium(38, "Sr", 50, new NamespacedKey(TARDIS.plugin, "elements/38")),
    Yttrium(39, "Y", 50, new NamespacedKey(TARDIS.plugin, "elements/39")),
    Zirconium(40, "Zr", 51, new NamespacedKey(TARDIS.plugin, "elements/40")),
    Niobium(41, "Nb", 52, new NamespacedKey(TARDIS.plugin, "elements/41")),
    Molybdenum(42, "Mo", 54, new NamespacedKey(TARDIS.plugin, "elements/42")),
    Technetium(43, "Tc", 55, new NamespacedKey(TARDIS.plugin, "elements/43")),
    Ruthenium(44, "Ru", 57, new NamespacedKey(TARDIS.plugin, "elements/44")),
    Rhodium(45, "Rh", 58, new NamespacedKey(TARDIS.plugin, "elements/45")),
    Palladium(46, "Pd", 60, new NamespacedKey(TARDIS.plugin, "elements/46")),
    Silver(47, "Ag", 61, new NamespacedKey(TARDIS.plugin, "elements/47")),
    Cadmium(48, "Cd", 64, new NamespacedKey(TARDIS.plugin, "elements/48")),
    Indium(49, "In", 66, new NamespacedKey(TARDIS.plugin, "elements/49")),
    Tin(50, "Sn", 69, new NamespacedKey(TARDIS.plugin, "elements/50")),
    Antimony(51, "Sb", 71, new NamespacedKey(TARDIS.plugin, "elements/51")),
    Tellurium(52, "Te", 76, new NamespacedKey(TARDIS.plugin, "elements/52")),
    Iodine(53, "I", 74, new NamespacedKey(TARDIS.plugin, "elements/53")),
    Xenon(54, "Xe", 77, new NamespacedKey(TARDIS.plugin, "elements/54")),
    Caesium(55, "Cs", 78, new NamespacedKey(TARDIS.plugin, "elements/55")),
    Barium(56, "Ba", 81, new NamespacedKey(TARDIS.plugin, "elements/56")),
    Lanthanum(57, "La", 82, new NamespacedKey(TARDIS.plugin, "elements/57")),
    Cerium(58, "Ce", 82, new NamespacedKey(TARDIS.plugin, "elements/58")),
    Praseodymium(59, "Pr", 82, new NamespacedKey(TARDIS.plugin, "elements/59")),
    Neodymium(60, "Nd", 84, new NamespacedKey(TARDIS.plugin, "elements/60")),
    Promethium(61, "Pm", 84, new NamespacedKey(TARDIS.plugin, "elements/61")),
    Samarium(62, "Sm", 88, new NamespacedKey(TARDIS.plugin, "elements/62")),
    Europium(63, "Eu", 89, new NamespacedKey(TARDIS.plugin, "elements/63")),
    Gadolinium(64, "Gd", 93, new NamespacedKey(TARDIS.plugin, "elements/64")),
    Terbium(65, "Tb", 94, new NamespacedKey(TARDIS.plugin, "elements/65")),
    Dysprosium(66, "Dy", 97, new NamespacedKey(TARDIS.plugin, "elements/66")),
    Holmium(67, "Ho", 98, new NamespacedKey(TARDIS.plugin, "elements/67")),
    Erbium(68, "Er", 99, new NamespacedKey(TARDIS.plugin, "elements/68")),
    Thulium(69, "Tm", 100, new NamespacedKey(TARDIS.plugin, "elements/69")),
    Ytterbium(70, "Yb", 103, new NamespacedKey(TARDIS.plugin, "elements/70")),
    Lutetium(71, "Lu", 104, new NamespacedKey(TARDIS.plugin, "elements/71")),
    Hafnium(72, "Hf", 106, new NamespacedKey(TARDIS.plugin, "elements/72")),
    Tantalum(73, "Ta", 108, new NamespacedKey(TARDIS.plugin, "elements/73")),
    Tungsten(74, "W", 110, new NamespacedKey(TARDIS.plugin, "elements/74")),
    Rhenium(75, "Re", 111, new NamespacedKey(TARDIS.plugin, "elements/75")),
    Osmium(76, "Os", 114, new NamespacedKey(TARDIS.plugin, "elements/76")),
    Iridium(77, "Ir", 115, new NamespacedKey(TARDIS.plugin, "elements/77")),
    Platinum(78, "Pt", 117, new NamespacedKey(TARDIS.plugin, "elements/78")),
    Gold(79, "Au", 118, new NamespacedKey(TARDIS.plugin, "elements/79")),
    Mercury(80, "Hg", 121, new NamespacedKey(TARDIS.plugin, "elements/80")),
    Thallium(81, "Tl", 123, new NamespacedKey(TARDIS.plugin, "elements/81")),
    Lead(82, "Pb", 125, new NamespacedKey(TARDIS.plugin, "elements/82")),
    Bismuth(83, "Bi", 126, new NamespacedKey(TARDIS.plugin, "elements/83")),
    Polonium(84, "Po", 125, new NamespacedKey(TARDIS.plugin, "elements/84")),
    Astatine(85, "At", 125, new NamespacedKey(TARDIS.plugin, "elements/85")),
    Radon(86, "Rn", 136, new NamespacedKey(TARDIS.plugin, "elements/86")),
    Francium(87, "Fr", 136, new NamespacedKey(TARDIS.plugin, "elements/87")),
    Radium(88, "Ra", 138, new NamespacedKey(TARDIS.plugin, "elements/88")),
    Actinium(89, "Ac", 138, new NamespacedKey(TARDIS.plugin, "elements/89")),
    Thorium(90, "Th", 142, new NamespacedKey(TARDIS.plugin, "elements/90")),
    Protactinium(91, "Pa", 148, new NamespacedKey(TARDIS.plugin, "elements/91")),
    Uranium(92, "U", 146, new NamespacedKey(TARDIS.plugin, "elements/92")),
    Neptunium(93, "Np", 144, new NamespacedKey(TARDIS.plugin, "elements/93")),
    Plutonium(94, "Pu", 150, new NamespacedKey(TARDIS.plugin, "elements/94")),
    Americium(95, "Am", 148, new NamespacedKey(TARDIS.plugin, "elements/95")),
    Curium(96, "Cm", 151, new NamespacedKey(TARDIS.plugin, "elements/96")),
    Berkelium(97, "Bk", 150, new NamespacedKey(TARDIS.plugin, "elements/97")),
    Californium(98, "Cf", 153, new NamespacedKey(TARDIS.plugin, "elements/98")),
    Einsteinium(99, "Es", 153, new NamespacedKey(TARDIS.plugin, "elements/99")),
    Fermium(100, "Fm", 157, new NamespacedKey(TARDIS.plugin, "elements/100")),
    Mendelevium(101, "Md", 157, new NamespacedKey(TARDIS.plugin, "elements/101")),
    Nobelium(102, "No", 157, new NamespacedKey(TARDIS.plugin, "elements/102")),
    Lawrencium(103, "Lr", 159, new NamespacedKey(TARDIS.plugin, "elements/103")),
    Rutherfordium(104, "Rf", 157, new NamespacedKey(TARDIS.plugin, "elements/104")),
    Dubnium(105, "Db", 157, new NamespacedKey(TARDIS.plugin, "elements/105")),
    Seaborgium(106, "Sg", 157, new NamespacedKey(TARDIS.plugin, "elements/106")),
    Bohrium(107, "Bh", 155, new NamespacedKey(TARDIS.plugin, "elements/107")),
    Hassium(108, "Hs", 157, new NamespacedKey(TARDIS.plugin, "elements/108")),
    Meitnerium(109, "Mt", 157, new NamespacedKey(TARDIS.plugin, "elements/109")),
    Darmstadtium(110, "Ds", 151, new NamespacedKey(TARDIS.plugin, "elements/110")),
    Roentgenium(111, "Rg", 161, new NamespacedKey(TARDIS.plugin, "elements/111")),
    Copernicium(112, "Cn", 165, new NamespacedKey(TARDIS.plugin, "elements/112")),
    Nihonium(113, "Nh", 173, new NamespacedKey(TARDIS.plugin, "elements/113")),
    Flerovium(114, "Fl", 174, new NamespacedKey(TARDIS.plugin, "elements/114")),
    Moscovium(115, "Mc", 174, new NamespacedKey(TARDIS.plugin, "elements/115")),
    Livermorium(116, "Lv", 177, new NamespacedKey(TARDIS.plugin, "elements/116")),
    Tennessine(117, "Ts", 177, new NamespacedKey(TARDIS.plugin, "elements/117")),
    Oganesson(118, "Og", 176, new NamespacedKey(TARDIS.plugin, "elements/118")),
    Unknown(119, "Un", 999, new NamespacedKey(TARDIS.plugin, "elements/unknown"));

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
