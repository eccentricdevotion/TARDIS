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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.DiskVariant;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public enum TARDISHandlesBlock {

    // paper
    // command
    COMEHERE(TARDISHandlesCategory.COMMAND, "/tardis comehere command", Arrays.asList("Make the TARDIS", "travel to you"), HandlesVariant.HANDLES_COMMAND_COMEHERE.getKey()),
    HIDE(TARDISHandlesCategory.COMMAND, "/tardis hide command", Arrays.asList("Make the TARDIS", "exterior invisible"), HandlesVariant.HANDLES_COMMAND_HIDE.getKey()),
    REBUILD(TARDISHandlesCategory.COMMAND, "/tardis rebuild command", Arrays.asList("Rebuild the", "TARDIS exterior"), HandlesVariant.HANDLES_COMMAND_REBUILD.getKey()),
    SCAN(TARDISHandlesCategory.COMMAND, "TARDIS scan command", Arrays.asList("Scan the environment", "outside the TARDIS"), HandlesVariant.HANDLES_COMMAND_SCAN.getKey()),
    TAKE_OFF(TARDISHandlesCategory.COMMAND, "TARDIS take off command", Arrays.asList("Make the TARDIS enter", "the Time Vortex"), HandlesVariant.HANDLES_COMMAND_TAKE_OFF.getKey()),
    TRAVEL(TARDISHandlesCategory.COMMAND, "/tardistravel command", Arrays.asList("Make the TARDIS", "travel somewhere"), HandlesVariant.HANDLES_COMMAND_TRAVEL.getKey()),
    LAND(TARDISHandlesCategory.COMMAND, "TARDIS land command", Arrays.asList("Make the TARDIS exit", "the Time Vortex"), HandlesVariant.HANDLES_COMMAND_LAND.getKey()),
    // control
    FOR(TARDISHandlesCategory.CONTROL, "FOR loop", Arrays.asList("Use to start a loop", "with a counter"), HandlesVariant.HANDLES_CONTROL_FOR.getKey()),
    TO(TARDISHandlesCategory.CONTROL, "TO", Arrays.asList("Use with a FOR loop", "to specify the number", "of loops"), HandlesVariant.HANDLES_CONTROL_TO.getKey()),
    DO(TARDISHandlesCategory.CONTROL, "DO", Arrays.asList("Use after an", "IF or FOR loop"), HandlesVariant.HANDLES_CONTROL_DO.getKey()),
    END(TARDISHandlesCategory.CONTROL, "END", Arrays.asList("Use to finish", "a conditional or", "loop statement"), HandlesVariant.HANDLES_CONTROL_END.getKey()),
    IF(TARDISHandlesCategory.CONTROL, "IF", Arrays.asList("Check whether a", "condition is", "true or false"), HandlesVariant.HANDLES_CONTROL_IF.getKey()),
    ELSE(TARDISHandlesCategory.CONTROL, "ELSE", Arrays.asList("Use to run actions", "if an IF is false"), HandlesVariant.HANDLES_CONTROL_ELSE.getKey()),
    ELSE_IF(TARDISHandlesCategory.CONTROL, "ELSE IF", List.of("Check another condition"), HandlesVariant.HANDLES_CONTROL_ELSE_IF.getKey()),
    BREAK(TARDISHandlesCategory.CONTROL, "BREAK", Arrays.asList("Use to break", "out of a loop"), HandlesVariant.HANDLES_CONTROL_BREAK.getKey()),
    // event
    MATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS materialisation event", null, HandlesVariant.HANDLES_EVENT_MATERIALISE.getKey()),
    DEMATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS dematerialisation event", null, HandlesVariant.HANDLES_EVENT_DEMATERIALISE.getKey()),
    ARTRON(TARDISHandlesCategory.EVENT, "Artron Level event", Arrays.asList("Use with Operator", "and Number blocks"), HandlesVariant.HANDLES_EVENT_ARTRON.getKey()),
    DEATH(TARDISHandlesCategory.EVENT, "Time Lord Death event", null, HandlesVariant.HANDLES_EVENT_DEATH.getKey()),
    ENTER(TARDISHandlesCategory.EVENT, "TARDIS Entry event", null, HandlesVariant.HANDLES_EVENT_ENTER.getKey()),
    EXIT(TARDISHandlesCategory.EVENT, "TARDIS Exit event", null, HandlesVariant.HANDLES_EVENT_EXIT.getKey()),
    HADS(TARDISHandlesCategory.EVENT, "TARDIS H.A.D.S event", null, HandlesVariant.HANDLES_EVENT_HADS.getKey()),
    SIEGE_ON(TARDISHandlesCategory.EVENT, "Siege Mode On event", null, HandlesVariant.HANDLES_EVENT_SIEGE_ON.getKey()),
    SIEGE_OFF(TARDISHandlesCategory.EVENT, "Siege Mode Off event", null, HandlesVariant.HANDLES_EVENT_SIEGE_OFF.getKey()),
    LOG_OUT(TARDISHandlesCategory.EVENT, "Player log out event", null, HandlesVariant.HANDLES_EVENT_LOG_OUT.getKey()),
    // number
    ZERO(TARDISHandlesCategory.NUMBER, "0", List.of("The number zero"), HandlesVariant.HANDLES_NUMBER_ZERO.getKey()),
    ONE(TARDISHandlesCategory.NUMBER, "1", List.of("The number one"), HandlesVariant.HANDLES_NUMBER_ONE.getKey()),
    TWO(TARDISHandlesCategory.NUMBER, "2", List.of("The number two"), HandlesVariant.HANDLES_NUMBER_TWO.getKey()),
    THREE(TARDISHandlesCategory.NUMBER, "3", List.of("The number three"), HandlesVariant.HANDLES_NUMBER_THREE.getKey()),
    FOUR(TARDISHandlesCategory.NUMBER, "4", List.of("The number four"), HandlesVariant.HANDLES_NUMBER_FOUR.getKey()),
    FIVE(TARDISHandlesCategory.NUMBER, "5", List.of("The number five"), HandlesVariant.HANDLES_NUMBER_FIVE.getKey()),
    SIX(TARDISHandlesCategory.NUMBER, "6", List.of("The number six"), HandlesVariant.HANDLES_NUMBER_SIX.getKey()),
    SEVEN(TARDISHandlesCategory.NUMBER, "7", List.of("The number seven"), HandlesVariant.HANDLES_NUMBER_SEVEN.getKey()),
    EIGHT(TARDISHandlesCategory.NUMBER, "8", List.of("The number eight"), HandlesVariant.HANDLES_NUMBER_EIGHT.getKey()),
    NINE(TARDISHandlesCategory.NUMBER, "9", List.of("The number nine"), HandlesVariant.HANDLES_NUMBER_NINE.getKey()),
    COIN(TARDISHandlesCategory.NUMBER, "Flip a coin", Arrays.asList("Returns a true", "or false value"), HandlesVariant.HANDLES_NUMBER_COIN.getKey()),
    RANDOM(TARDISHandlesCategory.NUMBER, "Random number", Arrays.asList("Generate a random number", "between 0 and the", "number that follows"), HandlesVariant.HANDLES_NUMBER_RANDOM.getKey()),
    // operator
    ASSIGNMENT(TARDISHandlesCategory.OPERATOR, "Assignment operator", Arrays.asList("Used to assign a", "value to a variable"), HandlesVariant.HANDLES_OPERATOR_ASSIGNMENT.getKey()),
    ADDITION(TARDISHandlesCategory.OPERATOR, "Addition operator", Arrays.asList("Use to add two", "numbers together"), HandlesVariant.HANDLES_OPERATOR_ADDITION.getKey()),
    SUBTRACTION(TARDISHandlesCategory.OPERATOR, "Subtraction operator", Arrays.asList("Use to subtract one", "number from anther"), HandlesVariant.HANDLES_OPERATOR_SUBTRACTION.getKey()),
    MULTIPLICATION(TARDISHandlesCategory.OPERATOR, "Multiplication operator", Arrays.asList("Use to multiply two", "numbers together"), HandlesVariant.HANDLES_OPERATOR_MULTIPLICATION.getKey()),
    DIVISION(TARDISHandlesCategory.OPERATOR, "Division operator", Arrays.asList("Use to divide one", "number by another"), HandlesVariant.HANDLES_OPERATOR_DIVISION.getKey()),
    AND(TARDISHandlesCategory.OPERATOR, "AND operator", Arrays.asList("Use to check", "both conditions", "are is true"), HandlesVariant.HANDLES_OPERATOR_AND.getKey()),
    OR(TARDISHandlesCategory.OPERATOR, "OR operator", Arrays.asList("Use to check", "if either", "condition is true"), HandlesVariant.HANDLES_OPERATOR_OR.getKey()),
    EQUALS(TARDISHandlesCategory.OPERATOR, "Equality operator", Arrays.asList("Checks if two", "objects are equal"), HandlesVariant.HANDLES_OPERATOR_EQUALS.getKey()),
    NOT_EQUAL(TARDISHandlesCategory.OPERATOR, "Not equal operator", Arrays.asList("Checks if two objects", "are not the same"), HandlesVariant.HANDLES_OPERATOR_NOT_EQUAL.getKey()),
    LESS_THAN(TARDISHandlesCategory.OPERATOR, "Less than operator", Arrays.asList("Checks if one number", "is smaller than another"), HandlesVariant.HANDLES_OPERATOR_LESS_THAN.getKey()),
    LESS_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Less than or equal to operator", Arrays.asList("Checks if one number", "is smaller than or", "equal to another"), HandlesVariant.HANDLES_OPERATOR_LESS_THAN_EQUAL.getKey()),
    GREATER_THAN(TARDISHandlesCategory.OPERATOR, "Greater than operator", Arrays.asList("Checks if one number", "is bigger than another"), HandlesVariant.HANDLES_OPERATOR_GREATER_THAN.getKey()),
    GREATER_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Greater than or equal to operator", Arrays.asList("Checks if one number", "is bigger than or", "equal to another"), HandlesVariant.HANDLES_OPERATOR_GREATER_THAN_EQUAL.getKey()),
    MODULO(TARDISHandlesCategory.OPERATOR, "Modulo operator", Arrays.asList("Gets the remainder", "left over after", "dividing two numbers"), HandlesVariant.HANDLES_OPERATOR_MODULO.getKey()),
    // selector
    TIME_LORD(TARDISHandlesCategory.SELECTOR, "Target Time Lord", null, HandlesVariant.HANDLES_SELECTOR_TIME_LORD.getKey()),
    COMPANIONS(TARDISHandlesCategory.SELECTOR, "Target companions", null, HandlesVariant.HANDLES_SELECTOR_COMPANIONS.getKey()),
    TARDIS(TARDISHandlesCategory.SELECTOR, "The TARDIS", null, HandlesVariant.HANDLES_SELECTOR_TARDIS.getKey()),
    DOOR(TARDISHandlesCategory.SELECTOR, "TARDIS Door", Arrays.asList("Use with an", "Open, Close", "Lock or Unlock block"), HandlesVariant.HANDLES_SELECTOR_DOOR.getKey()),
    LIGHTS(TARDISHandlesCategory.SELECTOR, "TARDIS Lights", Arrays.asList("Use with an", "On or Off block"), HandlesVariant.HANDLES_SELECTOR_LIGHTS.getKey()),
    POWER(TARDISHandlesCategory.SELECTOR, "TARDIS Power action", Arrays.asList("Use with a Show,", "On or Off block"), HandlesVariant.HANDLES_SELECTOR_POWER.getKey()),
    SIEGE(TARDISHandlesCategory.SELECTOR, "Siege Mode action", Arrays.asList("Use with an", "On or Off block"), HandlesVariant.HANDLES_SELECTOR_SIEGE.getKey()),
    // variable
    VARIABLE(TARDISHandlesCategory.VARIABLE, "Variable", Arrays.asList("A generic container", "to hold a value"), HandlesVariant.HANDLES_VARIABLE_VARIABLE.getKey()),
    X(TARDISHandlesCategory.VARIABLE, "X coordinate", Arrays.asList("Use with a number", "to specify a location"), HandlesVariant.HANDLES_VARIABLE_X.getKey()),
    Y(TARDISHandlesCategory.VARIABLE, "Y coordinate", Arrays.asList("Use with a number", "to specify a location"), HandlesVariant.HANDLES_VARIABLE_Y.getKey()),
    Z(TARDISHandlesCategory.VARIABLE, "Z coordinate", Arrays.asList("Use with a number", "to specify a location"), HandlesVariant.HANDLES_VARIABLE_Z.getKey()),
    OPEN(TARDISHandlesCategory.VARIABLE, "Open Door action", null, HandlesVariant.HANDLES_VARIABLE_OPEN.getKey()),
    CLOSE(TARDISHandlesCategory.VARIABLE, "Close Door action", null, HandlesVariant.HANDLES_VARIABLE_CLOSE.getKey()),
    LOCK(TARDISHandlesCategory.VARIABLE, "Lock Door action", null, HandlesVariant.HANDLES_VARIABLE_LOCK.getKey()),
    UNLOCK(TARDISHandlesCategory.VARIABLE, "Unlock Door action", null, HandlesVariant.HANDLES_VARIABLE_UNLOCK.getKey()),
    ON(TARDISHandlesCategory.VARIABLE, "On action", null, HandlesVariant.HANDLES_VARIABLE_ON.getKey()),
    OFF(TARDISHandlesCategory.VARIABLE, "Off action", null, HandlesVariant.HANDLES_VARIABLE_OFF.getKey()),
    SHOW(TARDISHandlesCategory.VARIABLE, "Show Artron Levels action", null, HandlesVariant.HANDLES_VARIABLE_SHOW.getKey()),
    REDSTONE(TARDISHandlesCategory.VARIABLE, "Send Redstone signal", Arrays.asList("Use to power the block", "Handles is placed on"), HandlesVariant.HANDLES_VARIABLE_REDSTONE.getKey()),
    HOME(TARDISHandlesCategory.VARIABLE, "Home Location", null, HandlesVariant.HANDLES_VARIABLE_HOME.getKey()),
    RECHARGER(TARDISHandlesCategory.VARIABLE, "Travel to recharger", null, HandlesVariant.HANDLES_VARIABLE_RECHARGER.getKey()),
    // bowls
    // buttons
    CONTROL(TARDISHandlesCategory.BUTTON, "Show control blocks", null, GuiVariant.CONTROL.getKey()),
    OPERATOR(TARDISHandlesCategory.BUTTON, "Show operator blocks", null, GuiVariant.OPERATOR.getKey()),
    VAR(TARDISHandlesCategory.BUTTON, "Show variable blocks", null, GuiVariant.VAR.getKey()),
    NUMBER(TARDISHandlesCategory.BUTTON, "Show number blocks", null, GuiVariant.NUMBER.getKey()),
    EVENT(TARDISHandlesCategory.BUTTON, "Show event blocks", null, GuiVariant.EVENT.getKey()),
    COMMAND(TARDISHandlesCategory.BUTTON, "Show command blocks", null, GuiVariant.COMMAND.getKey()),
    SELECTOR(TARDISHandlesCategory.BUTTON, "Show selector blocks", null, GuiVariant.SELECTOR.getKey()),
    PROGRAMS(TARDISHandlesCategory.BUTTON, "Show saved programs", null, GuiVariant.HANDLES_DISK.getKey()),
    SAVE(TARDISHandlesCategory.BUTTON, "Save the program to disk", null, GuiVariant.SAVE.getKey()),
    LEFT(TARDISHandlesCategory.BUTTON, "Scroll left", null, GuiVariant.LEFT.getKey()),
    RIGHT(TARDISHandlesCategory.BUTTON, "Scroll right", null, GuiVariant.RIGHT.getKey()),
    // music disks
    AREA_DISK(TARDISHandlesCategory.DISK, "Area Storage Disk", null, DiskVariant.AREA_DISK.getKey()),
    BIOME_DISK(TARDISHandlesCategory.DISK, "Biome Storage Disk", null, DiskVariant.BIOME_DISK.getKey()),
    PLAYER_DISK(TARDISHandlesCategory.DISK, "Player Storage Disk", null, DiskVariant.PLAYER_DISK.getKey()),
    SAVE_DISK(TARDISHandlesCategory.DISK, "Save Storage Disk", null, DiskVariant.SAVE_DISK.getKey());

    public final static HashMap<String, TARDISHandlesBlock> BY_NAME = new HashMap<>();
    private final static List<TARDISHandlesBlock> commands = new ArrayList<>();
    private final static List<TARDISHandlesBlock> controls = new ArrayList<>();
    private final static List<TARDISHandlesBlock> events = new ArrayList<>();
    private final static List<TARDISHandlesBlock> numbers = new ArrayList<>();
    private final static List<TARDISHandlesBlock> operators = new ArrayList<>();
    private final static List<TARDISHandlesBlock> selectors = new ArrayList<>();
    private final static List<TARDISHandlesBlock> variables = new ArrayList<>();
    private final static List<TARDISHandlesBlock> buttons = new ArrayList<>();

    static {
        for (TARDISHandlesBlock block : values()) {
            switch (block.getCategory()) {
                case BUTTON -> buttons.add(block);
                case COMMAND -> commands.add(block);
                case CONTROL -> controls.add(block);
                case EVENT -> events.add(block);
                case NUMBER -> numbers.add(block);
                case OPERATOR -> operators.add(block);
                case SELECTOR -> selectors.add(block);
                case VARIABLE -> variables.add(block);
                default -> {
                }
            }
            BY_NAME.put(block.displayName, block);
        }
    }

    private final TARDISHandlesCategory category;
    private final String displayName;
    private final List<String> lore;
    private final NamespacedKey model;

    TARDISHandlesBlock(TARDISHandlesCategory category, String displayName, List<String> lore, NamespacedKey model) {
        this.category = category;
        this.displayName = displayName;
        this.lore = lore;
        this.model = model;
    }

    public static List<TARDISHandlesBlock> getCommands() {
        return commands;
    }

    public static List<TARDISHandlesBlock> getControls() {
        return controls;
    }

    public static List<TARDISHandlesBlock> getEvents() {
        return events;
    }

    public static List<TARDISHandlesBlock> getNumbers() {
        return numbers;
    }

    public static List<TARDISHandlesBlock> getOperators() {
        return operators;
    }

    public static List<TARDISHandlesBlock> getSelectors() {
        return selectors;
    }

    public static List<TARDISHandlesBlock> getVariables() {
        return variables;
    }

    public static List<TARDISHandlesBlock> getButtons() {
        return buttons;
    }

    public TARDISHandlesCategory getCategory() {
        return category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
