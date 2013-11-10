/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.JSON.JSONArray;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPreset {

    private final String yellowsub_id = "[[35,35,35,0],[35,20,35,0],[35,35,35,0],[35,20,35,0],[35,35,35,0],[35,20,35,0],[35,35,35,0],[71,71,35,0],[0,0,35,89],[0,0,68,0]]";
    private final String yellowsub_data = "[[4,4,4,0],[4,0,4,0],[4,4,4,0],[4,0,4,0],[4,4,4,0],[4,0,4,0],[4,4,4,0],[0,8,4,0],[0,0,4,0],[0,0,4,0]]";
    private final String windmill_id = "[[35,35,0,0],[35,35,0,0],[0,0,35,0],[0,35,5,35],[0,0,35,0],[35,35,0,0],[35,35,0,0],[64,64,35,0],[35,35,35,50],[0,0,68,0]]";
    private final String windmill_data = "[[1,1,0,0],[1,1,0,0],[0,0,0,0],[0,0,1,0],[0,0,0,0],[1,1,0,0],[1,1,0,0],[0,8,1,0],[1,1,1,5],[0,0,4,0]]";
    private final String well_id = "[[98,85,85,98],[98,0,0,98],[98,85,85,98],[98,0,0,98],[98,85,85,98],[98,0,0,98],[98,85,85,98],[98,0,0,98],[96,0,0,98],[0,0,0,106]]";
    private final String well_data = "[[1,0,0,1],[1,0,0,1],[1,0,0,1],[1,0,0,1],[1,0,0,1],[1,0,0,1],[1,0,0,1],[1,0,0,1],[2,0,0,1],[0,0,0,8]]";
    private final String well_ice_id = "[[79,79,79,79],[79,0,0,79],[79,79,79,79],[79,0,0,79],[79,79,79,79],[79,0,0,79],[79,79,79,79],[79,0,0,79],[96,0,0,79],[0,0,0,0]]";
    private final String well_ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[2,0,0,0],[0,0,0,0]]";
    private final String well_glass_id = "[[20,20,20,20],[20,0,0,20],[20,20,20,20],[20,0,0,20],[20,20,20,20],[20,0,0,20],[20,20,20,20],[20,0,0,20],[96,0,0,20],[0,0,0,0]]";
    private final String well_glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[2,0,0,0],[0,0,0,0]]";
    private final String village_id = "[[4,4,4,17],[4,5,4,17],[4,4,4,17],[4,5,4,17],[4,4,4,17],[4,5,4,17],[4,4,4,17],[64,64,4,17],[0,0,4,17],[0,0,68,0]]";
    private final String village_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String tent_id = "[[35,35,0,0],[35,35,35,0],[35,35,0,0],[35,35,35,0],[35,35,0,0],[35,35,35,0],[35,35,0,0],[71,71,35,0],[0,0,35,35],[0,0,68,0]]";
    private final String tent_data = "[[5,5,0,0],[5,5,5,0],[5,5,0,0],[5,5,5,0],[5,5,0,0],[5,5,5,0],[5,5,0,0],[0,8,5,0],[0,0,5,5],[0,0,4,0]]";
    private final String tent_ice_id = "[[79,79,0,0],[79,79,79,0],[79,79,0,0],[79,79,79,0],[79,79,0,0],[79,79,79,0],[79,79,0,0],[71,71,79,0],[0,0,79,79],[0,0,68,0]]";
    private final String tent_ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String tent_glass_id = "[[20,20,0,0],[20,20,20,0],[20,20,0,0],[20,20,20,0],[20,20,0,0],[20,20,20,0],[20,20,0,0],[71,71,20,0],[0,0,20,20],[0,0,68,0]]";
    private final String tent_glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String telephone_id = "[[35,35,35,44],[35,20,35,44],[35,35,35,44],[35,20,35,44],[35,35,35,44],[35,20,35,44],[35,35,35,44],[71,71,35,44],[0,0,152,124],[0,0,68,0]]";
    private final String telephone_data = "[[14,14,14,0],[14,0,14,0],[14,14,14,0],[14,0,14,0],[14,14,14,0],[14,0,14,0],[14,14,14,0],[0,8,14,0],[0,0,14,0],[0,0,4,0]]";
    private final String taller_id = "[[35,35,35,44],[35,35,35,44],[35,35,35,44],[35,35,35,44],[35,35,35,44],[35,35,35,44],[35,35,35,44],[71,71,35,44],[0,0,152,124],[0,0,68,0]]";
    private final String taller_data = "[[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[0,8,11,0],[0,0,11,0],[0,0,4,0]]";
    private final String taller_ice_id = "[[79,79,79,79],[79,79,79,79],[79,79,79,79],[79,79,79,79],[79,79,79,79],[79,79,79,79],[79,79,79,79],[71,71,79,79],[0,0,0,79],[0,0,68,0]]";
    private final String taller_ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String taller_glass_id = "[[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[71,71,20,20],[0,0,0,20],[0,0,68,0]]";
    private final String taller_glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String swamp_id = "[[17,17,17,126],[5,5,5,126],[17,17,17,126],[5,5,5,126],[17,17,17,126],[5,5,5,126],[17,17,17,126],[64,64,5,126],[0,0,5,50],[0,0,0,0]]";
    private final String swamp_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,5],[0,0,0,0]]";
    private final String submerged_id = "[[3,2,0,0],[3,3,2,0],[3,3,2,0],[3,3,2,0],[3,3,2,0],[3,3,2,0],[3,2,0,0],[71,71,0,0],[3,35,2,0],[2,0,0,0]]";
    private final String submerged_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,15,0,0],[0,0,0,0]]";
    private final String submerged_ice_id = "[[2,79,0,0],[2,79,79,0],[2,79,79,0],[2,79,79,0],[2,79,79,0],[2,79,79,0],[2,79,0,0],[71,71,0,0],[2,0,79,0],[2,0,0,0]]";
    private final String submerged_ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,0,0,0],[0,0,0,0]]";
    private final String submerged_glass_id = "[[2,20,0,0],[2,20,20,0],[2,20,20,0],[2,20,20,0],[2,20,20,0],[2,20,20,0],[2,20,0,0],[71,71,0,0],[2,0,20,0],[2,0,0,0]]";
    private final String submerged_glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,15,0,0],[0,0,0,0]]";
    private final String raised_id = "[[85,5,5,5],[126,5,102,5],[85,5,5,5],[126,5,102,5],[85,5,5,5],[126,5,102,5],[85,5,5,5],[126,71,71,5],[126,0,0,5],[0,0,0,68]]";
    private final String raised_data = "[[0,0,0,0],[8,0,0,0],[0,0,0,0],[8,0,0,0],[0,0,0,0],[8,0,0,0],[0,0,0,0],[8,2,8,0],[8,0,0,0],[0,0,0,4]]";
    private final String police_id = "[[35,35,35,0],[35,35,35,0],[35,35,35,0],[35,35,35,0],[35,35,35,0],[35,35,35,0],[35,35,35,0],[71,71,35,0],[0,0,35,50],[0,0,68,0]]";
    private final String police_data = "[[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[11,11,11,0],[0,8,11,0],[0,0,11,5],[0,0,4,0]]";
    private final String plain_id = "[[159,159,159,0],[159,159,159,0],[159,159,159,0],[159,159,159,0],[159,159,159,0],[159,159,159,0],[159,159,159,0],[71,71,159,0],[0,0,159,0],[0,0,0,0]]";
    private final String nether_id = "[[114,114,114,0],[114,114,114,0],[114,114,114,0],[114,114,114,0],[114,114,114,0],[114,114,114,0],[114,114,114,0],[71,71,114,0],[0,0,112,89],[0,0,0,0]]";
    private final String nether_data = "[[2,6,2,0],[2,6,2,0],[2,6,2,0],[1,5,1,0],[3,7,3,0],[3,7,3,0],[3,7,3,0],[0,8,0,0],[0,0,0,0],[0,0,0,0]]";
    private final String jungle_id = "[[48,48,4,0],[48,4,48,67],[4,4,48,0],[4,48,4,67],[48,4,48,0],[48,4,48,67],[4,4,48,0],[71,71,4,67],[0,0,4,4],[0,0,106,0]]";
    private final String jungle_ice_id = "[[79,79,79,0],[79,79,79,79],[79,79,79,0],[79,79,79,79],[79,79,79,0],[79,79,79,79],[79,79,79,0],[71,71,79,79],[0,0,0,79],[0,0,106,0]]";
    private final String jungle_ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,8,0]]";
    private final String jungle_glass_id = "[[20,20,20,0],[20,20,20,20],[20,20,20,0],[20,20,20,20],[20,20,20,0],[20,20,20,20],[20,20,20,0],[71,71,20,20],[0,0,0,20],[0,0,106,0]]";
    private final String jungle_glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,8,0]]";
    private final String jungle_data = "[[0,0,0,0],[0,0,0,2],[0,0,0,0],[0,0,0,1],[0,0,0,0],[0,0,0,3],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,8,0]]";
    private final String ice_id = "[[79,79,79,0],[79,79,79,0],[79,79,79,0],[79,79,79,0],[79,79,79,0],[79,79,79,0],[79,79,79,0],[71,71,79,0],[0,0,35,0],[0,0,68,0]]";
    private final String ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String glass_id = "[[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[71,71,20,0],[0,0,20,0],[0,0,68,0]]";
    private final String glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,8,0,0],[0,0,0,0],[0,0,4,0]]";
    private final String flower_id = "[[0,0,0,0],[0,0,0,35],[0,0,0,0],[0,0,0,35],[0,0,0,0],[0,0,0,35],[0,0,0,0],[64,64,0,35],[35,35,35,89],[0,0,0,0]]";
    private final String flower_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[13,13,13,0],[0,0,0,0]]";
    private final String flower_ice_id = "[[0,0,0,0],[0,0,0,79],[0,0,0,0],[0,0,0,79],[0,0,0,0],[0,0,0,79],[0,0,0,0],[64,64,0,79],[79,79,79,79],[0,0,0,0]]";
    private final String flower_ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,0,0,0],[0,0,0,0]]";
    private final String flower_glass_id = "[[0,0,0,0],[0,0,0,20],[0,0,0,0],[0,0,0,20],[0,0,0,0],[0,0,0,28],[0,0,0,0],[64,64,0,20],[20,20,20,20],[0,0,0,0]]";
    private final String flower_glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,0,0,0],[0,0,0,0]]";
    private final String factory_data = "[[8,8,8,0],[8,8,8,0],[8,8,8,0],[8,8,8,0],[8,8,8,0],[8,8,8,0],[8,8,8,0],[0,8,8,0],[0,0,8,0],[0,0,0,0]]";
    private final String desert_id = "[[24,35,24,44],[35,24,35,128],[24,35,24,44],[35,24,35,128],[24,35,24,44],[35,24,35,128],[24,35,24,44],[71,71,24,128],[0,0,24,24],[0,0,0,0]]";
    private final String desert_data = "[[0,1,0,1],[1,1,1,2],[0,1,0,1],[1,1,1,1],[0,1,0,1],[1,1,1,3],[0,1,0,1],[0,8,1,0],[0,0,1,0],[0,0,0,0]]";
    private final String column_id = "[[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[71,71,109,44],[0,0,4,44],[0,0,68,0]]";
    private final String column_data = "[[2,6,2,5],[2,6,2,5],[2,6,2,5],[1,5,1,5],[3,7,3,5],[3,7,3,5],[3,7,3,5],[0,8,0,5],[0,0,0,5],[0,0,4,0]]";
    private final String chalice_id = "[[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[71,71,156,155],[155,155,155,9],[0,0,0,0]]";
    private final String chalice_data = "[[7,0,6,0],[7,0,6,0],[7,0,5,0],[7,0,5,0],[7,0,7,0],[7,0,7,0],[7,0,7,0],[2,8,4,0],[0,0,0,0],[0,0,0,0]]";
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> yellowsub = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> windmill = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> well = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> well_ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> well_glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> village = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> tent_ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> tent_glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> tent = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> telephone = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> taller_ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> taller_glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> taller = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> swamp = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> submerged = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> submerged_ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> submerged_glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> raised = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> police = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> nether = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> jungle_ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> jungle_glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> jungle = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> flower = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> flower_ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> flower_glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> factory = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> desert = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> column = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> chalice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final List<Integer> problemBlocks = Arrays.asList(new Integer[]{64, 67, 68, 71, 96, 106, 109, 114, 128, 156});

    public TARDISChameleonPreset() {
    }

    public void makePresets() {
        for (TARDISConstants.COMPASS d : TARDISConstants.COMPASS.values()) {
            chalice.put(d, buildTARDISChameleonColumn(d, chalice_id, chalice_data, false));
            column.put(d, buildTARDISChameleonColumn(d, column_id, column_data, false));
            desert.put(d, buildTARDISChameleonColumn(d, desert_id, desert_data, false));
            factory.put(d, buildTARDISChameleonColumn(d, plain_id, factory_data, false));
            flower.put(d, buildTARDISChameleonColumn(d, flower_id, flower_data, false));
            flower_glass.put(d, buildTARDISChameleonColumn(d, flower_glass_id, flower_glass_data, false));
            flower_ice.put(d, buildTARDISChameleonColumn(d, flower_ice_id, flower_ice_data, false));
            glass.put(d, buildTARDISChameleonColumn(d, glass_id, glass_data, false));
            ice.put(d, buildTARDISChameleonColumn(d, ice_id, ice_data, false));
            jungle.put(d, buildTARDISChameleonColumn(d, jungle_id, jungle_data, false));
            jungle_glass.put(d, buildTARDISChameleonColumn(d, jungle_glass_id, jungle_glass_data, false));
            jungle_ice.put(d, buildTARDISChameleonColumn(d, jungle_ice_id, jungle_ice_data, false));
            nether.put(d, buildTARDISChameleonColumn(d, nether_id, nether_data, false));
            police.put(d, buildTARDISChameleonColumn(d, police_id, police_data, false));
            raised.put(d, buildTARDISChameleonColumn(d, raised_id, raised_data, false));
            submerged.put(d, buildTARDISChameleonColumn(d, submerged_id, submerged_data, true));
            submerged_ice.put(d, buildTARDISChameleonColumn(d, submerged_ice_id, submerged_ice_data, true));
            submerged_glass.put(d, buildTARDISChameleonColumn(d, submerged_glass_id, submerged_glass_data, true));
            swamp.put(d, buildTARDISChameleonColumn(d, swamp_id, swamp_data, false));
            taller.put(d, buildTARDISChameleonColumn(d, taller_id, taller_data, false));
            taller_glass.put(d, buildTARDISChameleonColumn(d, taller_glass_id, taller_glass_data, false));
            taller_ice.put(d, buildTARDISChameleonColumn(d, taller_ice_id, taller_ice_data, false));
            telephone.put(d, buildTARDISChameleonColumn(d, telephone_id, telephone_data, false));
            tent.put(d, buildTARDISChameleonColumn(d, tent_id, tent_data, false));
            tent_glass.put(d, buildTARDISChameleonColumn(d, tent_glass_id, tent_glass_data, false));
            tent_ice.put(d, buildTARDISChameleonColumn(d, tent_ice_id, tent_ice_data, false));
            village.put(d, buildTARDISChameleonColumn(d, village_id, village_data, false));
            well.put(d, buildTARDISChameleonColumn(d, well_id, well_data, false));
            well_glass.put(d, buildTARDISChameleonColumn(d, well_glass_id, well_glass_data, false));
            well_ice.put(d, buildTARDISChameleonColumn(d, well_ice_id, well_ice_data, false));
            windmill.put(d, buildTARDISChameleonColumn(d, windmill_id, windmill_data, true));
            yellowsub.put(d, buildTARDISChameleonColumn(d, yellowsub_id, yellowsub_data, false));
        }
    }

    private TARDISChameleonColumn buildTARDISChameleonColumn(TARDISConstants.COMPASS d, String id, String data, boolean assyemtric) {
        TARDISChameleonColumn tcc = new TARDISChameleonColumn();
        if (d.equals(TARDISConstants.COMPASS.EAST)) {
            tcc.setId(getIntArrayFromJSON(id));
            tcc.setData(getByteArrayFromJSON(data));
        } else {
            int[][] id_arr = rotate2DIntArray(getIntArrayFromJSON(id), d, assyemtric);
            byte[][] data_arr = rotate2DByteArray(getByteArrayFromJSON(data), d, assyemtric);
            tcc.setId(id_arr);
            tcc.setData(convertData(id_arr, data_arr, d));
        }
        return tcc;
    }

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of ints
     */
    private int[][] getIntArrayFromJSON(String js) {
        int[][] preset = new int[10][4];
        JSONArray json = new JSONArray(js);
        for (int col = 0; col < 10; col++) {
            JSONArray jsoncol = json.getJSONArray(col);
            for (int block = 0; block < 4; block++) {
                preset[col][block] = jsoncol.getInt(block);
            }
        }
        return preset;
    }

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of bytes
     */
    private byte[][] getByteArrayFromJSON(String js) {
        byte[][] preset = new byte[10][4];
        JSONArray json = new JSONArray(js);
        for (byte col = 0; col < 10; col++) {
            JSONArray jsoncol = json.getJSONArray(col);
            for (byte block = 0; block < 4; block++) {
                preset[col][block] = jsoncol.getByte(block);
            }
        }
        return preset;
    }

    private int[][] rotate2DIntArray(int[][] id, TARDISConstants.COMPASS d, boolean assymetric) {
        switch (d) {
            case NORTH:
                int[] zero_s = id[0];
                int[] one_s = id[1];
                int[] two_s = id[2];
                int[] three_s = id[3];
                int[] four_s = id[4];
                int[] five_s = id[5];
                int[] six_s = id[6];
                int[] seven_s = id[7];
                id[0] = two_s;
                id[1] = three_s;
                id[2] = four_s;
                id[3] = five_s;
                id[4] = six_s;
                id[5] = seven_s;
                id[6] = zero_s;
                id[7] = one_s;
                return id;
            case WEST:
                if (assymetric) {
                    int[] two_w = id[2];
                    int[] four_w = id[4];
                    id[2] = id[0];
                    id[0] = two_w;
                    id[4] = id[6];
                    id[6] = four_w;
                }
                int[] three_w = id[3];
                id[3] = id[7];
                id[7] = three_w;
                return id;
            default:
                int[] zero_n = id[0];
                int[] one_n = id[1];
                int[] two_n = id[2];
                int[] three_n = id[3];
                int[] four_n = id[4];
                int[] five_n = id[5];
                int[] six_n = id[6];
                int[] seven_n = id[7];
                id[0] = six_n;
                id[1] = seven_n;
                id[2] = zero_n;
                id[3] = one_n;
                id[4] = two_n;
                id[5] = three_n;
                id[6] = four_n;
                id[7] = five_n;
                return id;
        }
    }

    private byte[][] rotate2DByteArray(byte[][] id, TARDISConstants.COMPASS d, boolean assymetric) {
        switch (d) {
            case NORTH:
                byte[] zero_s = id[0];
                byte[] one_s = id[1];
                byte[] two_s = id[2];
                byte[] three_s = id[3];
                byte[] four_s = id[4];
                byte[] five_s = id[5];
                byte[] six_s = id[6];
                byte[] seven_s = id[7];
                id[0] = two_s;
                id[1] = three_s;
                id[2] = four_s;
                id[3] = five_s;
                id[4] = six_s;
                id[5] = seven_s;
                id[6] = zero_s;
                id[7] = one_s;
                return id;
            case WEST:
                if (assymetric) {
                    byte[] two_w = id[2];
                    byte[] four_w = id[4];
                    id[2] = id[0];
                    id[0] = two_w;
                    id[4] = id[6];
                    id[6] = four_w;
                }
                byte[] three_w = id[3];
                id[3] = id[7];
                id[7] = three_w;
                return id;
            default:
                byte[] zero_n = id[0];
                byte[] one_n = id[1];
                byte[] two_n = id[2];
                byte[] three_n = id[3];
                byte[] four_n = id[4];
                byte[] five_n = id[5];
                byte[] six_n = id[6];
                byte[] seven_n = id[7];
                id[0] = six_n;
                id[1] = seven_n;
                id[2] = zero_n;
                id[3] = one_n;
                id[4] = two_n;
                id[5] = three_n;
                id[6] = four_n;
                id[7] = five_n;
                return id;
        }
    }

    private byte[][] convertData(int[][] id, byte[][] data, TARDISConstants.COMPASS d) {
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                if (problemBlocks.contains(id[col][block])) {
                    switch (id[col][block]) {
                        case 64: // wood door
                        case 71: // iron door
                            if (data[col][block] < 8) {
                                // data is either a 0 or a 2
                                switch (d) {
                                    case SOUTH:
                                        if (data[col][block] == (byte) 0) {
                                            data[col][block] = (byte) 1;
                                        } else {
                                            data[col][block] = (byte) 3;
                                        }
                                        break;
                                    case WEST:
                                        if (data[col][block] == (byte) 0) {
                                            data[col][block] = (byte) 2;
                                        } else {
                                            data[col][block] = (byte) 0;
                                        }
                                        break;
                                    default:
                                        if (data[col][block] == (byte) 0) {
                                            data[col][block] = (byte) 3;
                                        } else {
                                            data[col][block] = (byte) 1;
                                        }
                                        break;
                                }
                            }
                            break;
                        case 68: // wall sign
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 2;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 5;
                                    break;
                                default:
                                    data[col][block] = (byte) 3;
                                    break;
                            }
                            break;
                        case 67: // cobble stair
                        case 109: // smooth stair
                        case 114: // nether brick stair
                        case 128: // sandstone stair
                        case 156: // quartz stair
                            switch (d) {
                                case SOUTH:
                                    switch (data[col][block]) {
                                        case 0:
                                            data[col][block] = (byte) 2;
                                            break;
                                        case 1:
                                            data[col][block] = (byte) 3;
                                            break;
                                        case 2:
                                            data[col][block] = (byte) 1;
                                            break;
                                        case 3:
                                            data[col][block] = (byte) 0;
                                            break;
                                        case 4:
                                            data[col][block] = (byte) 6;
                                            break;
                                        case 5:
                                            data[col][block] = (byte) 7;
                                            break;
                                        case 6:
                                            data[col][block] = (byte) 5;
                                            break;
                                        case 7:
                                            data[col][block] = (byte) 4;
                                            break;
                                    }
                                    break;
                                case WEST:
                                    if (col == 3 || col == 7) {
                                        switch (data[col][block]) {
                                            case 0:
                                                data[col][block] = (byte) 1;
                                                break;
                                            case 1:
                                                data[col][block] = (byte) 0;
                                                break;
                                            case 4:
                                                data[col][block] = (byte) 5;
                                                break;
                                            case 5:
                                                data[col][block] = (byte) 4;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                default:
                                    switch (data[col][block]) {
                                        case 0:
                                            data[col][block] = (byte) 3;
                                            break;
                                        case 1:
                                            data[col][block] = (byte) 2;
                                            break;
                                        case 2:
                                            data[col][block] = (byte) 0;
                                            break;
                                        case 3:
                                            data[col][block] = (byte) 1;
                                            break;
                                        case 4:
                                            data[col][block] = (byte) 7;
                                            break;
                                        case 5:
                                            data[col][block] = (byte) 6;
                                            break;
                                        case 6:
                                            data[col][block] = (byte) 4;
                                            break;
                                        case 7:
                                            data[col][block] = (byte) 5;
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case 96: // trapdoor
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 4;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 3;
                                    break;
                                default:
                                    data[col][block] = (byte) 1;
                                    break;
                            }
                        default: // vine
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 1;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 2;
                                    break;
                                default:
                                    data[col][block] = (byte) 4;
                                    break;
                            }
                            break;
                    }
                }
            }
        }
        return data;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getColumn() {
        return column;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getPolice() {
        return police;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getDesert() {
        return desert;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getGlass() {
        return glass;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getIce() {
        return ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getJungle() {
        return jungle;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTaller_glass() {
        return taller_glass;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTent_glass() {
        return tent_glass;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getJungle_glass() {
        return jungle_glass;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTaller_ice() {
        return taller_ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTent_ice() {
        return tent_ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getJungle_ice() {
        return jungle_ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getNether() {
        return nether;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getFactory() {
        return factory;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getSwamp() {
        return swamp;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTaller() {
        return taller;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTent() {
        return tent;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getVillage() {
        return village;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getYellowsub() {
        return yellowsub;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getTelephone() {
        return telephone;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getSubmerged() {
        return submerged;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getSubmerged_ice() {
        return submerged_ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getSubmerged_glass() {
        return submerged_glass;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getFlower() {
        return flower;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getFlower_ice() {
        return flower_ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getFlower_glass() {
        return flower_glass;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getChalice() {
        return chalice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getWindmill() {
        return windmill;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getRaised() {
        return raised;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getWell() {
        return well;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getWell_ice() {
        return well_ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getWell_glass() {
        return well_glass;
    }
}
