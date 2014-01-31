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
package me.eccentric_nz.TARDIS.enumeration;

/**
 *
 * @author eccentric_nz
 */
public enum MESSAGE {

    EMERGENCY("The world the TARDIS was in could not be found, attempting emergency relocation!"),
    NO_PERMS("You do not have permission to do that!"),
    NOT_OWNER("You are not the Timelord or Companion for this TARDIS!"),
    NO_TARDIS("You have not created a TARDIS yet!"),
    NO_CURRENT("Could not get current TARDIS location!"),
    NOT_ENOUGH_ENERGY("The TARDIS does not have enough Artron Energy to make this trip!"),
    NOT_ENOUGH_ZERO_ENERGY("The TARDIS does not have enough Artron Energy for the Zero Room!"),
    NOT_IN_TARDIS("You are not inside your TARDIS. You need to be to use this feature!"),
    NO_PB_IN_TARDIS("You cannot bring the Police Box here because you are inside a TARDIS!"),
    NO_PB_IN_WORLD("You cannot bring the TARDIS Police Box to this world"),
    NOT_A_TIMELORD("You are not a Timelord. You need to create a TARDIS first before using this command!"),
    NOT_WHILE_TRAVELLING("You cannot set a destination while the TARDIS is travelling!"),
    NOT_WHILE_MAT("You cannot do that while the TARDIS is materialising!"),
    NOT_IN_ZERO("You cannot do that in the Zero Room!"),
    NOT_VALID_NAME("That doesn't appear to be a valid username."),
    CONFIG_UPDATED("The config was updated!"),
    NO_MAT_CIRCUIT("The Materialisation Circuit is missing from the console!"),
    NO_MEM_CIRCUIT("The Memory Circuit is missing from the console!"),
    ISO_ON("The isomorphic security lockout has been engaged... Hands off the controls!"),
    LOST_IN_VORTEX("The TARDIS is still travelling... you would get lost in the time vortex!"),
    TOO_FEW_ARGS("Too few command arguments!"),
    MUST_BE_PLAYER("You must be a player to run this command!"),
    NOT_VALID_MATERIAL("That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");

    String text;

    public String getText() {
        return text;
    }

    private MESSAGE(String text) {
        this.text = text;
    }
}
