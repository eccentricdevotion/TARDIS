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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.update.TARDISUpdateableCategory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.HashSet;
import java.util.Set;

public enum Updateable {

    ADVANCED(true, true, Set.of(Material.MUSHROOM_STEM, Material.JUKEBOX, Material.BARRIER), TARDISUpdateableCategory.INTERFACES, "TARDIS Advanced Console"),
    ALLAY(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Allay room"),
    ARS(true, true, Tag.SIGNS.getValues(), TARDISUpdateableCategory.INTERFACES, "Architectural Reconfiguration System"),
    ARTRON(true, true, TARDISUpdateableCategory.CONTROLS, "Artron Energy Capacitor button"),
    BACK(true, true, TARDISUpdateableCategory.CONTROLS, "Previous Location button"),
    BACKDOOR(false, false, Set.of(Material.IRON_DOOR), TARDISUpdateableCategory.OTHERS, "TARDIS back door"),
    BAMBOO(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Bamboo room"),
    BEACON(false, false, true, TARDISUpdateableCategory.LOCATIONS, "The block used to obstruct and turn off the TARDIS beacon"),
    BELL(true, true, TARDISUpdateableCategory.OTHERS, "TARDIS Cloister bell button"),
    BIRDCAGE(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Birdcage room"),
    BUTTON(true, true, TARDISUpdateableCategory.CONTROLS, "Random Location button"),
    CHAMELEON(true, true, Tag.SIGNS.getValues(), TARDISUpdateableCategory.INTERFACES, "Chameleon Circuit"),
    CHARGING_SENSOR(false, false, Set.of(Material.REDSTONE_BLOCK), TARDISUpdateableCategory.SENSORS, "Senses if the TARDIS is recharging"),
    CONDENSER(true, true, Set.of(Material.CHEST), TARDISUpdateableCategory.OTHERS, "Artron Energy Condenser"),
    CONSOLE_LAMP_SWITCH(false, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.CONTROLS, "Changes the light level of the Console Lamp"),
    CONTROL(true, true, Tag.ALL_SIGNS.getValues(), TARDISUpdateableCategory.CONTROLS, "TARDIS Control Centre Menu"),
    CREEPER(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Artron Charged Creeper"),
    DIRECTION(false, true, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.INTERFACES, "Direction item frame"),
    DISPENSER(true, true, Set.of(Material.DISPENSER), TARDISUpdateableCategory.OTHERS, "Custard Cream dispenser"),
    DOOR(false, true, Set.of(Material.IRON_DOOR), TARDISUpdateableCategory.OTHERS, "TARDIS Interior Door"),
    EPS(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Emergency Programme One"),
    EXTERIOR_LAMP(true, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.CONTROLS, "Exterior Lamp Light Level switch"),
    FARM(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Farm room"),
    FLIGHT(true, true, TARDISUpdateableCategory.CONTROLS, "Flight Mode button"),
    FLIGHT_SENSOR(false, false, Set.of(Material.REDSTONE_BLOCK), TARDISUpdateableCategory.SENSORS, "Senses if the TARDIS is in flight"),
    FORCEFIELD(true, true, TARDISUpdateableCategory.OTHERS, "TARDIS Force Field button"),
    FRAME(false, true, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.OTHERS, "Chameleon item frame"),
    FUEL(false, false, Set.of(Material.CHEST, Material.TRAPPED_CHEST), TARDISUpdateableCategory.LOCATIONS, "Smelter room fuel chest"),
    GENERATOR(false, true, Set.of(Material.FLOWER_POT), TARDISUpdateableCategory.INTERFACES, "Sonic Generator"),
    HANDBRAKE(true, true, Set.of(Material.LEVER), TARDISUpdateableCategory.CONTROLS, "Handbrake"),
    HANDBRAKE_SENSOR(false, false, Set.of(Material.REDSTONE_BLOCK), TARDISUpdateableCategory.SENSORS, "Senses when the TARDIS's handbrake is toggled"),
    HINGE(false, false, TARDISUpdateableCategory.OTHERS, "Set the side a door hinge is on"),
    HUTCH(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Rabbit Hutch room"),
    IGLOO(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Igloo room"),
    IISTUBIL(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Camel iistubil room"),
    INFO(true, true, Tag.SIGNS.getValues(), TARDISUpdateableCategory.INTERFACES, "TARDIS Information System"),
    KEYBOARD(true, false, Tag.SIGNS.getValues(), TARDISUpdateableCategory.OTHERS, "Keyboard Input sign"),
    LAVA(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Lava room"),
    LIGHT(true, true, TARDISUpdateableCategory.OTHERS, "Console Light switch"),
    LIGHT_LEVEL(true, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.CONTROLS, "Interior Light Level switch"),
    MALFUNCTION_SENSOR(false, false, Set.of(Material.REDSTONE_BLOCK), TARDISUpdateableCategory.SENSORS, "Senses if the TARDIS has malfunctioned"),
    MAP(false, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.OTHERS, "TARDIS Scanner Map"),
    MONITOR(false, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.OTHERS, "TARDIS Monitor"),
    MONITOR_FRAME(false, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.OTHERS, "Monitor Frame"),
    MUSHROOMS(false, false, true, TARDISUpdateableCategory.OTHERS, "Convert old custom mushroom blocks to TARDIS Item Displays"),
    PEN(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Sniffer Pen room"),
    POWER_SENSOR(false, false, Set.of(Material.REDSTONE_BLOCK), TARDISUpdateableCategory.SENSORS, "Senses when the TARDIS's power changes"),
    RAIL(false, false, Tag.FENCES.getValues(), TARDISUpdateableCategory.LOCATIONS, "Rail room entry point"),
    RELATIVITY_DIFFERENTIATOR(true, false, Set.of(Material.COMPARATOR), TARDISUpdateableCategory.CONTROLS, "Relativity Differentiator"),
    ROTOR(false, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.OTHERS, "Time Rotor item frame"),
    SAVE_SIGN(true, true, Tag.SIGNS.getValues(), TARDISUpdateableCategory.INTERFACES, "Saved locations and TARDIS areas"),
    SCANNER(true, true, TARDISUpdateableCategory.OTHERS, "Exterior Scanner button"),
    SIEGE(true, false, TARDISUpdateableCategory.INTERFACES, "Siege Mode button"),
    SMELT(false, false, Set.of(Material.CHEST, Material.TRAPPED_CHEST), TARDISUpdateableCategory.LOCATIONS, "Smelter room drop chest"),
    SONIC_DOCK(false, false, Set.of(Material.ITEM_FRAME), TARDISUpdateableCategory.OTHERS, "Sonic Screwdriver Dock"),
    STABLE(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Horse Stable room"),
    STALL(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Llama Stall room"),
    STORAGE(true, false, Set.of(Material.MUSHROOM_STEM, Material.NOTE_BLOCK, Material.BARRIER), TARDISUpdateableCategory.INTERFACES, "Disk Storage Container"),
    TELEPATHIC(true, true, Set.of(Material.DAYLIGHT_DETECTOR), TARDISUpdateableCategory.INTERFACES, "Telepathic Circuit"),
    TELEVISION(true, false, Set.of(Material.BARRIER), TARDISUpdateableCategory.INTERFACES, "TARDIS Television"),
    TEMPORAL(true, true, Tag.SIGNS.getValues(), TARDISUpdateableCategory.INTERFACES, "Temporal Relocation"),
    TERMINAL(true, true, Tag.SIGNS.getValues(), TARDISUpdateableCategory.INTERFACES, "Destination Terminal"),
    THROTTLE(true, true, Set.of(Material.REPEATER), TARDISUpdateableCategory.CONTROLS, "Space Time Throttle"),
    TOGGLE_WOOL(true, true, TARDISUpdateableCategory.OTHERS, "Toggle Black Wool behind door"),
    VAULT(false, false, Set.of(Material.CHEST, Material.TRAPPED_CHEST, Material.BARREL), TARDISUpdateableCategory.LOCATIONS, "Vault room drop chest"),
    VILLAGE(false, false, true, TARDISUpdateableCategory.LOCATIONS, "Village room"),
    WEATHER(true, true, TARDISUpdateableCategory.INTERFACES, "TARDIS Weather Menu"),
    WORLD_REPEATER(false, true, Set.of(Material.REPEATER), TARDISUpdateableCategory.CONTROLS, "World Type selector"),
    X_REPEATER(false, true, Set.of(Material.REPEATER), TARDISUpdateableCategory.CONTROLS, "Random x coordinate setter"),
    Y_REPEATER(false, true, Set.of(Material.REPEATER), TARDISUpdateableCategory.CONTROLS, "Distance multiplier"),
    Z_REPEATER(false, true, Set.of(Material.REPEATER), TARDISUpdateableCategory.CONTROLS, "Random z coordinate setter"),
    ZERO(true, false, TARDISUpdateableCategory.OTHERS, "Zero room transmat button");

    private final boolean control;
    private final boolean secondary;
    private final boolean anyBlock;
    private final Set<Material> materialChoice;
    private final TARDISUpdateableCategory category;
    private final String description;

    Updateable(boolean control, boolean secondary, TARDISUpdateableCategory category, String description) {
        this.control = control;
        this.secondary = secondary;
        anyBlock = false;
        materialChoice = concat();
        this.category = category;
        this.description = description;
    }

    Updateable(boolean control, boolean secondary, boolean anyBlock, TARDISUpdateableCategory category, String description) {
        this.control = control;
        this.secondary = secondary;
        this.anyBlock = anyBlock;
        materialChoice = Set.of(Material.SPAWNER);
        this.category = category;
        this.description = description;
    }

    Updateable(boolean control, boolean secondary, Set<Material> materialChoice, TARDISUpdateableCategory category, String description) {
        this.control = control;
        this.secondary = secondary;
        anyBlock = false;
        this.materialChoice = materialChoice;
        this.category = category;
        this.description = description;
    }

    public String getName() {
        if (this == Updateable.TOGGLE_WOOL) {
            return "toggle_wool";
        } else {
            return TARDISStringUtils.toDashedLowercase(toString());
        }
    }

    public boolean isControl() {
        return control;
    }

    public boolean isSecondary() {
        return secondary;
    }

    public boolean isAnyBlock() {
        return anyBlock;
    }

    public Set<Material> getMaterialChoices() {
        return materialChoice;
    }

    public TARDISUpdateableCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean usesItemFrame() {
        switch (this) {
            case DIRECTION, FRAME, MAP, MONITOR, MONITOR_FRAME, SONIC_DOCK -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private Set<Material> concat() {
        Set<Material> choices = new HashSet<>();
        choices.addAll(Tag.BUTTONS.getValues());
        choices.addAll(Tag.PRESSURE_PLATES.getValues());
        choices.addAll(Tag.WALL_SIGNS.getValues());
        choices.add(Material.COMPARATOR);
        choices.add(Material.REPEATER);
        return choices;
    }
}
