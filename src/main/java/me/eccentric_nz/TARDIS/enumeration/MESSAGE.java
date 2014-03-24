/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

/**
 *
 * @author eccentric_nz
 */
public enum MESSAGE {

    ANTIBUILD("You can't do that because the TARDIS has placed this area in a stasis field!"),
    ANTIBUILD_TIMELORD("You can't do that because %s has locked the TARDIS in a perpetual state of relativistic stability!"),
    CHAMELEON_SET("Chameleon Preset set to "),
    CONFIG_UPDATED("The config was updated!"),
    COULD_NOT_FIND_NAME("Could not find a player with that name!"),
    COULD_NOT_FIND_ROOM("Could not find a room with that name!"),
    EMERGENCY("The world the TARDIS was in could not be found, attempting emergency relocation!"),
    ISO_ON("The isomorphic security lockout has been engaged... Hands off the controls!"),
    LOST_IN_VORTEX("The TARDIS is still travelling... you would get lost in the time vortex!"),
    MUST_BE_PLAYER("You must be a player to run this command!"),
    NOT_A_TIMELORD("You are not a Timelord. You need to create a TARDIS first before using this command!"),
    NOT_ENOUGH_ENERGY("The TARDIS does not have enough Artron Energy to make this trip!"),
    NOT_ENOUGH_ZERO_ENERGY("The TARDIS does not have enough Artron Energy for the Zero Room!"),
    NOT_IN_TARDIS("You are not inside your TARDIS. You need to be to use this feature!"),
    NOT_IN_ZERO("You cannot do that in the Zero Room!"),
    NOT_OWNER("You are not the Timelord for this TARDIS!"),
    NOT_VALID_MATERIAL("That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html"),
    NOT_VALID_NAME("That doesn't appear to be a valid username."),
    NOT_WHILE_MAT("You cannot do that while the TARDIS is materialising!"),
    NOT_WHILE_TRAVELLING("You cannot set a destination while the TARDIS is travelling!"),
    NO_CURRENT("Could not get current TARDIS location!"),
    NO_MAT_CIRCUIT("The Materialisation Circuit is missing from the console!"),
    NO_MEM_CIRCUIT("The Memory Circuit is missing from the console!"),
    NO_MORE_SPOTS("All available parking spots are taken in this area!"),
    NO_PB_IN_TARDIS("You cannot bring the Police Box here because you are inside a TARDIS!"),
    NO_PB_IN_WORLD("You cannot bring the TARDIS Police Box to this world"),
    NO_PERMS("You do not have permission to do that!"),
    NO_TARDIS("You have not created a TARDIS yet!"),
    TOO_FEW_ARGS("Too few command arguments!");

    String text;

    public String getText() {
        return text;
    }

    private MESSAGE(String text) {
        this.text = text;
    }
}
