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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus;

import org.bukkit.entity.Wolf;

import java.util.HashMap;
import java.util.List;

public class LazarusWolf {

    public static final HashMap<String, Wolf.Variant> VARIANTS = new HashMap<>() {{
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

    public static final List<String> NAMES = List.of("PALE", "ASHEN", "BLACK", "CHESTNUT", "RUSTY", "SNOWY", "SPOTTED", "STRIPED", "WOODS");
}
