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
package me.eccentric_nz.TARDIS.blueprints;

import java.util.HashMap;

public enum BlueprintRoom {

    ARCHITECTURAL("tardis.architectural"), // needed to grow rooms or use ARS
    JETTISON("tardis.jettison"), // jettison a room

//    ROOM("tardis.room"), // all room perms

//    VAULT("tardis.vault"), // can use vault room
//    FARM("tardis.farm"), // can farm mobs into TARDIS - must have appropriate room
//    LAZARUS("tardis.lazarus"), // can use the genetic manipulator

    ALLAY("tardis.room.allay", BlueprintFeature.FARM),
    ANTIGRAVITY("tardis.room.antigravity"),
    APIARY("tardis.room.apiary", BlueprintFeature.FARM),
    AQUARIUM("tardis.room.aquarium", BlueprintFeature.FARM),
    ARBORETUM("tardis.room.arboretum"),
    ARCHITECTURAL_ROOM("tardis.room.architectural"),
    BAKER("tardis.room.baker"),
    BAMBOO("tardis.room.bamboo", BlueprintFeature.FARM),
    BEDROOM("tardis.room.bedroom"),
    BIRDCAGE("tardis.room.birdcage", BlueprintFeature.FARM),
    CHEMISTRY("tardis.room.chemistry"),
    CLOISTER("tardis.room.cloister"),
    EMPTY("tardis.room.empty"),
    EYE("tardis.room.eye"),
    FARM("tardis.room.farm", BlueprintFeature.FARM),
    GALLERY("tardis.room.gallery"),
    GARDEN("tardis.room.garden"),
    GEODE("tardis.room.geode"),
    GRAVITY("tardis.room.gravity"),
    GREENHOUSE("tardis.room.greenhouse"),
    HAPPY("tardis.room.happy"),
    HARMONY("tardis.room.harmony"),
    HUTCH("tardis.room.hutch", BlueprintFeature.FARM),
    IGLOO("tardis.room.igloo", BlueprintFeature.FARM),
    IISTUBIL("tardis.room.iistubil", BlueprintFeature.FARM),
    KITCHEN("tardis.room.kitchen"),
    LAUNDRY("tardis.room.laundry"),
    LAVA("tardis.room.lava", BlueprintFeature.FARM),
    LAZARUS("tardis.room.lazarus", BlueprintFeature.LAZARUS),
    LIBRARY("tardis.room.library"),
    MAZE("tardis.room.maze"),
    MUSHROOM("tardis.room.mushroom"),
    NAUTILUS("tardis.room.nautilus"),
    NETHER("tardis.room.nether"),
    OBSERVATORY("tardis.room.observatory"),
    PASSAGE("tardis.room.passage"),
    PEN("tardis.room.pen", BlueprintFeature.FARM),
    POOL("tardis.room.pool"),
    RAIL("tardis.room.rail"),
    RENDERER("tardis.room.renderer"),
    SHELL("tardis.room.shell"),
    SMELTER("tardis.room.smelter"),
    STABLE("tardis.room.stable", BlueprintFeature.FARM),
    STAIRCASE("tardis.room.staircase"),
    STALL("tardis.room.stall", BlueprintFeature.FARM),
    TRENZALORE("tardis.room.trenzalore"),
    VAULT("tardis.room.vault", BlueprintFeature.VAULT),
    VILLAGE("tardis.room.village", BlueprintFeature.FARM),
    WARDROBE("tardis.room.wardrobe"),
    WOOD("tardis.room.wood"),
    WORKSHOP("tardis.room.workshop"),
    ZERO("tardis.room.zero");

    public static HashMap<String, BlueprintRoom> PERMS = new HashMap<>();

    static {
        for (BlueprintRoom room : BlueprintRoom.values()) {
            PERMS.put(room.permission, room);
        }
    }

    private final String permission;
    private final BlueprintFeature feature;

    BlueprintRoom(String permission) {
        this.permission = permission;
        feature = BlueprintFeature.NONE;
    }

    BlueprintRoom(String permission, BlueprintFeature feature) {
        this.permission = permission;
        this.feature = feature;
    }

    public String getPermission() {
        return permission;
    }

    public BlueprintFeature getFeature() {
        return feature;
    }
}
