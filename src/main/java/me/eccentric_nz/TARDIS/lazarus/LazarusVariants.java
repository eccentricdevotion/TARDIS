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
package me.eccentric_nz.TARDIS.lazarus;

import org.bukkit.Registry;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.List;

public class LazarusVariants {

    public static final HashMap<String, Wolf.Variant> WOLF_VARIANTS = new HashMap<>() {{
        put("PALE", Wolf.Variant.PALE);
        put("ASHEN", Wolf.Variant.ASHEN);
        put("BLACK", Wolf.Variant.BLACK);
        put("CHESTNUT", Wolf.Variant.CHESTNUT);
        put("RUSTY", Wolf.Variant.RUSTY);
        put("SNOWY", Wolf.Variant.SNOWY);
        put("SPOTTED", Wolf.Variant.SPOTTED);
        put("STRIPED", Wolf.Variant.STRIPED);
        put("WOODS", Wolf.Variant.WOODS);
    }};

    public static final List<String> WOLF_NAMES = List.of("PALE", "ASHEN", "BLACK", "CHESTNUT", "RUSTY", "SNOWY", "SPOTTED", "STRIPED", "WOODS");

    public static final HashMap<String, Chicken.Variant> CHICKEN_VARIANTS = new HashMap<>() {{
        put("COLD", Chicken.Variant.COLD);
        put("TEMPERATE", Chicken.Variant.TEMPERATE);
        put("WARM", Chicken.Variant.WARM);
    }};

    public static final HashMap<String, Cow.Variant> COW_VARIANTS = new HashMap<>() {{
        put("COLD", Cow.Variant.COLD);
        put("TEMPERATE", Cow.Variant.TEMPERATE);
        put("WARM", Cow.Variant.WARM);
    }};

    public static final HashMap<String, Pig.Variant> PIG_VARIANTS = new HashMap<>() {{
        put("COLD", Pig.Variant.COLD);
        put("TEMPERATE", Pig.Variant.TEMPERATE);
        put("WARM", Pig.Variant.WARM);
    }};

    public static final HashMap<String, Frog.Variant> FROG_VARIANTS = new HashMap<>() {{
        put("COLD", Frog.Variant.COLD);
        put("TEMPERATE", Frog.Variant.TEMPERATE);
        put("WARM", Frog.Variant.WARM);
    }};

    public static final List<String> NAMES = List.of("COLD", "TEMPERATE", "WARM");

    public static final HashMap<String, Cat.Type> CAT_VARIANTS = new HashMap<>() {{
        put("ALL_BLACK", Cat.Type.ALL_BLACK);
        put("BLACK", Cat.Type.BLACK);
        put("BRITISH_SHORTHAIR", Cat.Type.BRITISH_SHORTHAIR);
        put("CALICO", Cat.Type.CALICO);
        put("JELLIE", Cat.Type.JELLIE);
        put("PERSIAN", Cat.Type.PERSIAN);
        put("RAGDOLL", Cat.Type.RAGDOLL);
        put("RED", Cat.Type.RED);
        put("SIAMESE", Cat.Type.SIAMESE);
        put("TABBY", Cat.Type.TABBY);
        put("WHITE", Cat.Type.WHITE);
    }};

    public static final List<String> CAT_NAMES = List.of("ALL_BLACK", "BLACK", "BRITISH_SHORTHAIR", "CALICO", "JELLIE", "PERSIAN", "RAGDOLL", "RED", "SIAMESE", "TABBY", "WHITE");

    public static final HashMap<String, Villager.Profession> PROFESSIONS = new HashMap<>() {{
        put("ARMORER", Villager.Profession.ARMORER);
        put("BUTCHER", Villager.Profession.BUTCHER);
        put("CARTOGRAPHER", Villager.Profession.CARTOGRAPHER);
        put("CLERIC", Villager.Profession.CLERIC);
        put("FARMER", Villager.Profession.FARMER);
        put("FISHERMAN", Villager.Profession.FISHERMAN);
        put("FLETCHER", Villager.Profession.FLETCHER);
        put("LEATHERWORKER", Villager.Profession.LEATHERWORKER);
        put("LIBRARIAN", Villager.Profession.LIBRARIAN);
        put("MASON", Villager.Profession.MASON);
        put("NITWIT", Villager.Profession.NITWIT);
        put("NONE", Villager.Profession.NONE);
        put("SHEPHERD", Villager.Profession.SHEPHERD);
        put("TOOLSMITH", Villager.Profession.TOOLSMITH);
        put("WEAPONSMITH", Villager.Profession.WEAPONSMITH);
    }};

    public static final List<String> PROFESSION_NAMES = List.of("ARMORER", "BUTCHER", "CARTOGRAPHER", "CLERIC", "FARMER", "FISHERMAN", "FLETCHER", "LEATHERWORKER", "LIBRARIAN", "MASON", "NITWIT", "NONE", "SHEPHERD", "TOOLSMITH", "WEAPONSMITH");

    public static final List<Villager.Profession> VILLAGER_PROFESSIONS = Registry.VILLAGER_PROFESSION.stream().toList();

    public static final List<Villager.Type> VILLAGER_TYPES = Registry.VILLAGER_TYPE.stream().toList();
}
