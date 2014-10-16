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

import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISChatPaginator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public enum CMDS {

    ADD(ChatColor.AQUA + "Adding companions" + ChatColor.RESET + " To allow other players to travel with you in the TARDIS you need to add them to your companions list To do this type: " + ChatColor.GOLD + "/TARDIS add [player]" + ChatColor.RESET + " To remove a companion from the list, type: " + ChatColor.GOLD + "/TARDIS remove [player]"),
    ADMIN(ChatColor.AQUA + "TARDIS admin commands" + ChatColor.RESET + " Arguments " + ChatColor.GOLD + "/tardisadmin key [Material]" + ChatColor.RESET + " - set the Material used as the TARDIS key. Default: GOLD_NUGGET. " + ChatColor.GOLD + "/tardisadmin tp_radius [x]" + ChatColor.RESET + " - set the maximum distance (in blocks) you can time travel in the TARDIS. Default: 256 " + ChatColor.GOLD + "/tardisadmin default_world [true|false]" + ChatColor.RESET + " - set whether the (inner) TARDIS is created in a specific world. Default: false " + ChatColor.GOLD + "/tardisadmin default_world_name [world]" + ChatColor.RESET + " - set the default world the (inner) TARDIS is created in. " + ChatColor.GOLD + "/tardisadmin include_default_world [true|false]" + ChatColor.RESET + " - set whether the default world is included in random time travel destinations. Default: false " + ChatColor.GOLD + "/tardisadmin exclude [world]" + ChatColor.RESET + " - excludes the specified world from random time travel destinations. " + ChatColor.GOLD + "/tardisadmin sfx [true|false]" + ChatColor.RESET + " - set whether a sound effects play inside the TARDIS. Default: true, but a player can also set their own preference if they wish. " + ChatColor.GOLD + "/tardisadmin use_worldguard [true|false]" + ChatColor.RESET + " - set whether WorldGuard should be used to protect the inner TARDIS. Default: true. " + ChatColor.GOLD + "/tardisadmin respect_worldguard [true|false]" + ChatColor.RESET + " - set whether travelling respects WorldGuard regions created by the WorldGuard other plugins. Default: true. " + ChatColor.GOLD + "/tardisadmin nether [true|false]" + ChatColor.RESET + " - set whether players can time travel to the NETHER. Default: false. " + ChatColor.GOLD + "/tardisadmin the_end [true|false]" + ChatColor.RESET + " - set whether players can time travel to THE END. Default: false. " + ChatColor.GOLD + "/tardisadmin land_on_water [true|false]" + ChatColor.RESET + " - set whether the TARDIS will land on water in NORMAL worlds. Default: true. " + ChatColor.GOLD + "/tardisadmin name_tardis [true|false]" + ChatColor.RESET + " - set whether the Police Box will will have the player's name on it. Default: false. To set an Artron Energy Recharger to the beacon you are looking at, type " + ChatColor.GOLD + "/tardisadmin recharger [name]" + ChatColor.RESET + " Where [name] is a what you want to call the recharger."),
    AREA("TARDIS area commands " + ChatColor.GOLD + "/tardisarea start [name]" + ChatColor.RESET + " - type this to define the starting corner of a preset admin area. " + ChatColor.GOLD + "/tardisarea end" + ChatColor.RESET + " - type this to define the ending corner of a preset admin area. " + ChatColor.GOLD + "/tardisarea show [name]" + ChatColor.RESET + " - type this to show the corners of the admin area. " + ChatColor.GOLD + "/tardisarea remove [name]" + ChatColor.RESET + " - type this to delete a preset admin area."),
    ARTRON("Artron Energy The wood button on the TARDIS console controls the Artron Energy Capacitor. If the button is right-clicked, then the Artron levels are updated. Clicking with a Nether Star puts the capacitor to maximum. Clicking with the TARDIS key initialises the capacitor by spawning a charged creeper inside it and sets the level to 50%. Clicking while sneaking transfers player Artron Energy into the capacitor. If the button is just right-clicked, it displays the current capacitor level as percentage of full."),
    BIND("Binding saved locations to blocks To bind a saved location, the home location or set a block to hide or rebuild the TARDIS Police Box, use the " + ChatColor.GOLD + "/tardisbind [bind type] [what_to_bind]" + ChatColor.RESET + " command . First place the block where you want it to be, then type the command. You will be asked to click the block so that the TARDIS matrix knows of its location. To bind a saved location - /tardisbind save [name] - use the saved destination name (as it appears in the /tardis list saves command) as the second argument. To bind a command - /tardisbind cmd [command] - use either 'hide', 'rebuild' or 'home'. To bind a player - /tardisbind player [name] - use the player's name. To bind a TARDIS area - /tardisbind area [name] - use the area name (as it appears in the /tardis list areas command). To remove a bound block, use the command: " + ChatColor.GOLD + "/tardisbind remove [name]"),
    CHAMELEON(ChatColor.AQUA + "The TARDIS Chameleon Circuit" + ChatColor.RESET + " You can make the TARDIS Police Box blend in with its surroundings by turning on the Chameleon Circuit. To do this by command, type: " + ChatColor.GOLD + "/TARDIS chameleon [on|off]" + ChatColor.RESET + " Otherwise just right-click the Chameleon Circuit sign to toggle it on or off If you created your TARDIS using a previous verion of the plugin, you can also just place a sign anywhere, and use the " + ChatColor.GREEN + "/tardis update chameleon" + ChatColor.RESET + " command to add it to the TARDIS database."),
    COMMANDS(ChatColor.AQUA + "TARDIS help" + ChatColor.RESET + " Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command. Type " + ChatColor.GOLD + "/TARDIS help create|delete|timetravel" + ChatColor.RESET + " for instructions on creating and removing a TARDIS and how to time travel. Commands " + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + " - list saved time travel destinations, TARDIS companions, Artron Energy Rechargers or admin defined areas. " + ChatColor.GOLD + "/TARDIS save [name]" + ChatColor.RESET + " - save the current location of the Police Box. " + ChatColor.GOLD + "/TARDIS removesave [name]" + ChatColor.RESET + " - delete a saved destination. " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " - show the co-ordinates of a lost TARDIS. " + ChatColor.GOLD + "/TARDIS add" + ChatColor.RESET + " - add a TARDIS companion. " + ChatColor.GOLD + "/TARDIS remove" + ChatColor.RESET + " - remove a TARDIS companion. " + ChatColor.GOLD + "/TARDIS update" + ChatColor.RESET + " - update the special block positions in a modified TARDIS interior. " + ChatColor.GOLD + "/TARDIS bind" + ChatColor.RESET + " - bind saved locations and some commands to blocks in the TARDIS interior. " + ChatColor.GOLD + "/tardistravel" + ChatColor.RESET + " - set the time travel destination to co-ordinates, a player's location, a saved destination, or to an admin defined area. " + ChatColor.GOLD + "/TARDIS rebuild" + ChatColor.RESET + " - rebuild a busted TARDIS Police Box. " + ChatColor.GOLD + "/TARDIS chameleon" + ChatColor.RESET + " - turn the Chameleon Circuit on or off. " + ChatColor.GOLD + "/tardisprefs sfx" + ChatColor.RESET + " - turn TARDIS sound effects on or off. " + ChatColor.GOLD + "/tardisprefs quotes" + ChatColor.RESET + " - turn TARDIS quotes on or off. " + ChatColor.GOLD + "/TARDIS setdest" + ChatColor.RESET + " - save the block you are looking at as a destination. " + ChatColor.GOLD + "/TARDIS home" + ChatColor.RESET + " - change saved TARDIS home location to the block you are looking at. " + ChatColor.GOLD + "/TARDIS hide" + ChatColor.RESET + " - hide the TARDIS police box - use /tardis rebuild to bring it back. " + ChatColor.GOLD + "/TARDIS direction" + ChatColor.RESET + " - change the direction the TARDIS police box is facing. " + ChatColor.GOLD + "/TARDIS comehere" + ChatColor.RESET + " - Make the TARDIS come to the block you are looking at. " + ChatColor.GOLD + "/TARDIS namekey" + ChatColor.RESET + " - rename the TARDIS key. " + ChatColor.GOLD + "/TARDIS version" + ChatColor.RESET + " - check the version of the TARDIS plugin and CraftBukkit on the server. " + ChatColor.GOLD + "/TARDIS room [type]" + ChatColor.RESET + " - Add rooms to the TARDIS."),
    CREATE(ChatColor.AQUA + "Creating a TARDIS" + ChatColor.RESET + " You create a TARDIS by creating and placing a " + ChatColor.GOLD + "TARDIS Seed block." + ChatColor.RESET + " To see a recipe for the seed block, type " + ChatColor.GOLD + "/tardisrecipe tardis [type]" + ChatColor.RESET + " - where [type] is the kind of TARDIS you want to create. Once you have a seed block placed, right-click it with your TARDIS key (by default a GOLD_NUGGET). To enter the TARDIS, right-click the door with your TARDIS key to open it, then just walk in."),
    DELETE(ChatColor.AQUA + "Removing a TARDIS" + ChatColor.RESET + " To remove your TARDIS, " + ChatColor.GOLD + "break the 'POLICE BOX' wall sign" + ChatColor.RESET + " on the front of the TARDIS. " + ChatColor.RED + "WARNING:" + ChatColor.RESET + " You will lose any items you have stored in your TARDIS chests, and any saved time travel destinations."),
    FIND(ChatColor.AQUA + "Finding the TARDIS" + ChatColor.RESET + " Simply type " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " To display the world name and x, y, z co-ordinates of the last saved location of your TARDIS."),
    HIDE(ChatColor.AQUA + "Hide the TARDIS" + ChatColor.RESET + " To temporarily hide the TARDIS type: " + ChatColor.GOLD + "/TARDIS hide" + ChatColor.RESET + " To bring it back, type /tardis rebuild"),
    HOME(ChatColor.AQUA + "Change the TARDIS home location" + ChatColor.RESET + " To change the location the TARDIS calls home, look at a block and type: " + ChatColor.GOLD + "/TARDIS home"),
    LIST(ChatColor.AQUA + "TARDIS Lists" + ChatColor.RESET + " Type " + ChatColor.GOLD + "/TARDIS list saves" + ChatColor.RESET + " to list the destinations saved in the TARDIS console. Type " + ChatColor.GOLD + "/TARDIS list companions" + ChatColor.RESET + " to list players who you have added as TARDIS companions Type " + ChatColor.GOLD + "/TARDIS list areas" + ChatColor.RESET + " to list admin defined TARDIS areas (like landing pads and airports) Type " + ChatColor.GOLD + "/TARDIS list rechargers" + ChatColor.RESET + " to list Artron Energy Rechargers"),
    REBUILD(ChatColor.AQUA + "Rebuilding the TARDIS" + ChatColor.RESET + " Sometimes you may need to rebuild the TARDIS Police Box for example if the server crashes. To rebuild it, type: " + ChatColor.GOLD + "/TARDIS rebuild" + ChatColor.RESET + " The plugin will retrieve the last saved location and rebuild the TARDIS in that spot"),
    RECIPE(""),
    REMOVESAVE(ChatColor.AQUA + "Deleting time travel destinations" + ChatColor.RESET + " To delete a destination, type " + ChatColor.GOLD + "/TARDIS removesave [name]" + ChatColor.RESET + " Where [name] is the destination to delete."),
    ROOM(ChatColor.AQUA + "Add rooms to the TARDIS" + ChatColor.RESET + " To expand the TARDIS interior, you can add rooms. To do this you first need to add a passage way, then the room type that you require. To add rooms type: " + ChatColor.GOLD + "/TARDIS room [room type]" + ChatColor.RESET + " The room types are: passage, arboretum, bedroom, kitchen, library, pool, vault and empty. You then need to seed the growing of the room by placing the appropriate seed block for the room where you want the door to the room to be. After clicking the seed block with the TARDIS key, the room will be grown."),
    SAVE(ChatColor.AQUA + "Saving time travel destinations" + ChatColor.RESET + " To save the current TARDIS destination (where the Police Box is located), type " + ChatColor.GOLD + "/TARDIS save [name]" + ChatColor.RESET + " Where [name] is a what you want to call the destination."),
    SETDEST(ChatColor.AQUA + "Setting destinations" + ChatColor.RESET + " To set and save a TARDIS destination to the block you are looking at, type " + ChatColor.GOLD + "/TARDIS setdest [name]" + ChatColor.RESET + " Where [name] is a what you want to call the destination. Use the /tardistravel dest [name] command to travel to the specified destination."),
    SFX(ChatColor.AQUA + "TARDIS sound effects" + ChatColor.RESET + " By default, TARDIS sound effects are enabled and play while you are inside the TARDIS You can toggle the sounds on or off by typing: " + ChatColor.GOLD + "/tardisprefs sfx [on|off]" + ChatColor.RESET + " If the admin has disabled sound effects, this command will have no effect."),
    TIMETRAVEL(ChatColor.AQUA + "Time travelling in the TARDIS" + ChatColor.RESET + " You can time travel in the TARDIS by changing the delay settings of the redstone repeaters on the TARDIS console. The repeater closest to the door controls the kind of world you will travel to - the 1-tick delay setting selects a random world type, the 2 tick delay setting selects NORMAL worlds, the 3-tick setting selects NETHER worlds, and the 4-tick setting selects THE END worlds. After changing the repeater settings, you then click the stone button at the rear of the TARDIS console. When exiting the TARDIS (right-click the door with your TARDIS key - by default a GOLD_NUGGET) you will time travel to a random destination."),
    TRAVEL(ChatColor.AQUA + "Travel commands" + ChatColor.RESET + " You can set your next destination using the command: " + ChatColor.GOLD + "/tardistravel" + ChatColor.RESET + " To travel to specfic co-ordinates, type: " + ChatColor.GOLD + "/tardistravel [world] [x] [y] [z]" + ChatColor.RESET + " To travel to a players location, type: " + ChatColor.GOLD + "/tardistravel [player]" + ChatColor.RESET + " To travel to the 'home' location, type: " + ChatColor.GOLD + "/tardistravel home" + ChatColor.RESET + " You must run these commands from inside the TARDIS. To travel to a saved destination, type: " + ChatColor.GOLD + "/tardistravel dest [name]" + ChatColor.RESET + " You must run these commands from inside the TARDIS. You must run these commands from inside the TARDIS."),
    UPDATE(ChatColor.AQUA + "Modifying the TARDIS interior" + ChatColor.RESET + " To update the position of special blocks in the TARDIS ie. the door, button and repeaters. First move the block, then type: " + ChatColor.GOLD + "/TARDIS update [door|button|world-repeater|x-repeater|z-repeater|y-repeater|chameleon|save-sign]" + ChatColor.RESET + " You will be asked to click the block in its new position 'world-repeater' is the one closest to the door With your back to the door the 'x-repeater' is the one on the right With your back to the door the 'z-repeater' is the one on the left 'y-repeater' is the one at the back above the button"),
    VERSION(ChatColor.AQUA + "Show the version of TARDIS and CraftBukkit you are running " + ChatColor.GOLD + "/TARDIS version");
    private final String help;

    private CMDS(String help) {
        this.help = help;
    }

    public String getHelp() {
        return help;
    }

    public static CMDS fromString(String name) {
        return getEnumFromString(CMDS.class, name);
    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException ex) {
                TARDIS.plugin.debug(ex.getMessage());
            }
        }
        return null;
    }

    public static void send(Player p, String message) {
        if (message.length() > TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
            String[] multiline = TARDISChatPaginator.wordWrap(message, TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
            p.sendMessage(multiline);
        } else {
            p.sendMessage(message);
        }
    }
}
