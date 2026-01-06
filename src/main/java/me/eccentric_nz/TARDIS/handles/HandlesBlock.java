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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.custommodels.keys.DiskVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public enum HandlesBlock {

    // paper
    // command
    COMEHERE(HandlesCategory.COMMAND, "/tardis comehere command", List.of(Component.text("Make the TARDIS"), Component.text("travel to you")), HandlesVariant.HANDLES_COMMAND_COMEHERE.getKey()),
    HIDE(HandlesCategory.COMMAND, "/tardis hide command", List.of(Component.text("Make the TARDIS"), Component.text("exterior invisible")), HandlesVariant.HANDLES_COMMAND_HIDE.getKey()),
    REBUILD(HandlesCategory.COMMAND, "/tardis rebuild command", List.of(Component.text("Rebuild the"), Component.text("TARDIS exterior")), HandlesVariant.HANDLES_COMMAND_REBUILD.getKey()),
    SCAN(HandlesCategory.COMMAND, "TARDIS scan command", List.of(Component.text("Scan the environment"), Component.text("outside the TARDIS")), HandlesVariant.HANDLES_COMMAND_SCAN.getKey()),
    TAKE_OFF(HandlesCategory.COMMAND, "TARDIS take off command", List.of(Component.text("Make the TARDIS enter"), Component.text("the Time Vortex")), HandlesVariant.HANDLES_COMMAND_TAKE_OFF.getKey()),
    TRAVEL(HandlesCategory.COMMAND, "/tardistravel command", List.of(Component.text("Make the TARDIS"), Component.text("travel somewhere")), HandlesVariant.HANDLES_COMMAND_TRAVEL.getKey()),
    LAND(HandlesCategory.COMMAND, "TARDIS land command", List.of(Component.text("Make the TARDIS exit"), Component.text("the Time Vortex")), HandlesVariant.HANDLES_COMMAND_LAND.getKey()),
    // control
    FOR(HandlesCategory.CONTROL, "FOR loop", List.of(Component.text("Use to start a loop"), Component.text("with a counter")), HandlesVariant.HANDLES_CONTROL_FOR.getKey()),
    TO(HandlesCategory.CONTROL, "TO", List.of(Component.text("Use with a FOR loop"), Component.text("to specify the number"), Component.text("of loops")), HandlesVariant.HANDLES_CONTROL_TO.getKey()),
    DO(HandlesCategory.CONTROL, "DO", List.of(Component.text("Use after an"), Component.text("IF or FOR loop")), HandlesVariant.HANDLES_CONTROL_DO.getKey()),
    END(HandlesCategory.CONTROL, "END", List.of(Component.text("Use to finish"), Component.text("a conditional or"), Component.text("loop statement")), HandlesVariant.HANDLES_CONTROL_END.getKey()),
    IF(HandlesCategory.CONTROL, "IF", List.of(Component.text("Check whether a"), Component.text("condition is"), Component.text("true or false")), HandlesVariant.HANDLES_CONTROL_IF.getKey()),
    ELSE(HandlesCategory.CONTROL, "ELSE", List.of(Component.text("Use to run actions"), Component.text("if an IF is false")), HandlesVariant.HANDLES_CONTROL_ELSE.getKey()),
    ELSE_IF(HandlesCategory.CONTROL, "ELSE IF", List.of(Component.text("Check another condition")), HandlesVariant.HANDLES_CONTROL_ELSE_IF.getKey()),
    BREAK(HandlesCategory.CONTROL, "BREAK", List.of(Component.text("Use to break"), Component.text("out of a loop")), HandlesVariant.HANDLES_CONTROL_BREAK.getKey()),
    // event
    MATERIALISE(HandlesCategory.EVENT, "TARDIS materialisation event", null, HandlesVariant.HANDLES_EVENT_MATERIALISE.getKey()),
    DEMATERIALISE(HandlesCategory.EVENT, "TARDIS dematerialisation event", null, HandlesVariant.HANDLES_EVENT_DEMATERIALISE.getKey()),
    ARTRON(HandlesCategory.EVENT, "Artron Level event", List.of(Component.text("Use with Operator"), Component.text("and Number blocks")), HandlesVariant.HANDLES_EVENT_ARTRON.getKey()),
    DEATH(HandlesCategory.EVENT, "Time Lord Death event", null, HandlesVariant.HANDLES_EVENT_DEATH.getKey()),
    ENTER(HandlesCategory.EVENT, "TARDIS Entry event", null, HandlesVariant.HANDLES_EVENT_ENTER.getKey()),
    EXIT(HandlesCategory.EVENT, "TARDIS Exit event", null, HandlesVariant.HANDLES_EVENT_EXIT.getKey()),
    HADS(HandlesCategory.EVENT, "TARDIS H.A.D.S event", null, HandlesVariant.HANDLES_EVENT_HADS.getKey()),
    SIEGE_ON(HandlesCategory.EVENT, "Siege Mode On event", null, HandlesVariant.HANDLES_EVENT_SIEGE_ON.getKey()),
    SIEGE_OFF(HandlesCategory.EVENT, "Siege Mode Off event", null, HandlesVariant.HANDLES_EVENT_SIEGE_OFF.getKey()),
    LOG_OUT(HandlesCategory.EVENT, "Player log out event", null, HandlesVariant.HANDLES_EVENT_LOG_OUT.getKey()),
    // number
    ZERO(HandlesCategory.NUMBER, "0", List.of(Component.text("The number zero")), HandlesVariant.HANDLES_NUMBER_ZERO.getKey()),
    ONE(HandlesCategory.NUMBER, "1", List.of(Component.text("The number one")), HandlesVariant.HANDLES_NUMBER_ONE.getKey()),
    TWO(HandlesCategory.NUMBER, "2", List.of(Component.text("The number two")), HandlesVariant.HANDLES_NUMBER_TWO.getKey()),
    THREE(HandlesCategory.NUMBER, "3", List.of(Component.text("The number three")), HandlesVariant.HANDLES_NUMBER_THREE.getKey()),
    FOUR(HandlesCategory.NUMBER, "4", List.of(Component.text("The number four")), HandlesVariant.HANDLES_NUMBER_FOUR.getKey()),
    FIVE(HandlesCategory.NUMBER, "5", List.of(Component.text("The number five")), HandlesVariant.HANDLES_NUMBER_FIVE.getKey()),
    SIX(HandlesCategory.NUMBER, "6", List.of(Component.text("The number six")), HandlesVariant.HANDLES_NUMBER_SIX.getKey()),
    SEVEN(HandlesCategory.NUMBER, "7", List.of(Component.text("The number seven")), HandlesVariant.HANDLES_NUMBER_SEVEN.getKey()),
    EIGHT(HandlesCategory.NUMBER, "8", List.of(Component.text("The number eight")), HandlesVariant.HANDLES_NUMBER_EIGHT.getKey()),
    NINE(HandlesCategory.NUMBER, "9", List.of(Component.text("The number nine")), HandlesVariant.HANDLES_NUMBER_NINE.getKey()),
    COIN(HandlesCategory.NUMBER, "Flip a coin", List.of(Component.text("Returns a true"), Component.text("or false value")), HandlesVariant.HANDLES_NUMBER_COIN.getKey()),
    RANDOM(HandlesCategory.NUMBER, "Random number", List.of(Component.text("Generate a random number"), Component.text("between 0 and the"), Component.text("number that follows")), HandlesVariant.HANDLES_NUMBER_RANDOM.getKey()),
    // operator
    ASSIGNMENT(HandlesCategory.OPERATOR, "Assignment operator", List.of(Component.text("Used to assign a"), Component.text("value to a variable")), HandlesVariant.HANDLES_OPERATOR_ASSIGNMENT.getKey()),
    ADDITION(HandlesCategory.OPERATOR, "Addition operator", List.of(Component.text("Use to add two"), Component.text("numbers together")), HandlesVariant.HANDLES_OPERATOR_ADDITION.getKey()),
    SUBTRACTION(HandlesCategory.OPERATOR, "Subtraction operator", List.of(Component.text("Use to subtract one"), Component.text("number from anther")), HandlesVariant.HANDLES_OPERATOR_SUBTRACTION.getKey()),
    MULTIPLICATION(HandlesCategory.OPERATOR, "Multiplication operator", List.of(Component.text("Use to multiply two"), Component.text("numbers together")), HandlesVariant.HANDLES_OPERATOR_MULTIPLICATION.getKey()),
    DIVISION(HandlesCategory.OPERATOR, "Division operator", List.of(Component.text("Use to divide one"), Component.text("number by another")), HandlesVariant.HANDLES_OPERATOR_DIVISION.getKey()),
    AND(HandlesCategory.OPERATOR, "AND operator", List.of(Component.text("Use to check"), Component.text("both conditions"), Component.text("are is true")), HandlesVariant.HANDLES_OPERATOR_AND.getKey()),
    OR(HandlesCategory.OPERATOR, "OR operator", List.of(Component.text("Use to check"), Component.text("if either"), Component.text("condition is true")), HandlesVariant.HANDLES_OPERATOR_OR.getKey()),
    EQUALS(HandlesCategory.OPERATOR, "Equality operator", List.of(Component.text("Checks if two"), Component.text("objects are equal")), HandlesVariant.HANDLES_OPERATOR_EQUALS.getKey()),
    NOT_EQUAL(HandlesCategory.OPERATOR, "Not equal operator", List.of(Component.text("Checks if two objects"), Component.text("are not the same")), HandlesVariant.HANDLES_OPERATOR_NOT_EQUAL.getKey()),
    LESS_THAN(HandlesCategory.OPERATOR, "Less than operator", List.of(Component.text("Checks if one number"), Component.text("is smaller than another")), HandlesVariant.HANDLES_OPERATOR_LESS_THAN.getKey()),
    LESS_THAN_EQUAL(HandlesCategory.OPERATOR, "Less than or equal to operator", List.of(Component.text("Checks if one number"), Component.text("is smaller than or"), Component.text("equal to another")), HandlesVariant.HANDLES_OPERATOR_LESS_THAN_EQUAL.getKey()),
    GREATER_THAN(HandlesCategory.OPERATOR, "Greater than operator", List.of(Component.text("Checks if one number"), Component.text("is bigger than another")), HandlesVariant.HANDLES_OPERATOR_GREATER_THAN.getKey()),
    GREATER_THAN_EQUAL(HandlesCategory.OPERATOR, "Greater than or equal to operator", List.of(Component.text("Checks if one number"), Component.text("is bigger than or"), Component.text("equal to another")), HandlesVariant.HANDLES_OPERATOR_GREATER_THAN_EQUAL.getKey()),
    MODULO(HandlesCategory.OPERATOR, "Modulo operator", List.of(Component.text("Gets the remainder"), Component.text("left over after"), Component.text("dividing two numbers")), HandlesVariant.HANDLES_OPERATOR_MODULO.getKey()),
    // selector
    TIME_LORD(HandlesCategory.SELECTOR, "Target Time Lord", null, HandlesVariant.HANDLES_SELECTOR_TIME_LORD.getKey()),
    COMPANIONS(HandlesCategory.SELECTOR, "Target companions", null, HandlesVariant.HANDLES_SELECTOR_COMPANIONS.getKey()),
    TARDIS(HandlesCategory.SELECTOR, "The TARDIS", null, HandlesVariant.HANDLES_SELECTOR_TARDIS.getKey()),
    DOOR(HandlesCategory.SELECTOR, "TARDIS Door", List.of(Component.text("Use with an"), Component.text("Open, Close"), Component.text("Lock or Unlock block")), HandlesVariant.HANDLES_SELECTOR_DOOR.getKey()),
    LIGHTS(HandlesCategory.SELECTOR, "TARDIS Lights", List.of(Component.text("Use with an"), Component.text("On or Off block")), HandlesVariant.HANDLES_SELECTOR_LIGHTS.getKey()),
    POWER(HandlesCategory.SELECTOR, "TARDIS Power action", List.of(Component.text("Use with a Show,"), Component.text("On or Off block")), HandlesVariant.HANDLES_SELECTOR_POWER.getKey()),
    SIEGE(HandlesCategory.SELECTOR, "Siege Mode action", List.of(Component.text("Use with an"), Component.text("On or Off block")), HandlesVariant.HANDLES_SELECTOR_SIEGE.getKey()),
    // variable
    VARIABLE(HandlesCategory.VARIABLE, "Variable", List.of(Component.text("A generic container"), Component.text("to hold a value")), HandlesVariant.HANDLES_VARIABLE_VARIABLE.getKey()),
    X(HandlesCategory.VARIABLE, "X coordinate", List.of(Component.text("Use with a number"), Component.text("to specify a location")), HandlesVariant.HANDLES_VARIABLE_X.getKey()),
    Y(HandlesCategory.VARIABLE, "Y coordinate", List.of(Component.text("Use with a number"), Component.text("to specify a location")), HandlesVariant.HANDLES_VARIABLE_Y.getKey()),
    Z(HandlesCategory.VARIABLE, "Z coordinate", List.of(Component.text("Use with a number"), Component.text("to specify a location")), HandlesVariant.HANDLES_VARIABLE_Z.getKey()),
    OPEN(HandlesCategory.VARIABLE, "Open Door action", null, HandlesVariant.HANDLES_VARIABLE_OPEN.getKey()),
    CLOSE(HandlesCategory.VARIABLE, "Close Door action", null, HandlesVariant.HANDLES_VARIABLE_CLOSE.getKey()),
    LOCK(HandlesCategory.VARIABLE, "Lock Door action", null, HandlesVariant.HANDLES_VARIABLE_LOCK.getKey()),
    UNLOCK(HandlesCategory.VARIABLE, "Unlock Door action", null, HandlesVariant.HANDLES_VARIABLE_UNLOCK.getKey()),
    ON(HandlesCategory.VARIABLE, "On action", null, HandlesVariant.HANDLES_VARIABLE_ON.getKey()),
    OFF(HandlesCategory.VARIABLE, "Off action", null, HandlesVariant.HANDLES_VARIABLE_OFF.getKey()),
    SHOW(HandlesCategory.VARIABLE, "Show Artron Levels action", null, HandlesVariant.HANDLES_VARIABLE_SHOW.getKey()),
    REDSTONE(HandlesCategory.VARIABLE, "Send Redstone signal", List.of(Component.text("Use to power the block"), Component.text("Handles is placed on")), HandlesVariant.HANDLES_VARIABLE_REDSTONE.getKey()),
    HOME(HandlesCategory.VARIABLE, "Home Location", null, HandlesVariant.HANDLES_VARIABLE_HOME.getKey()),
    RECHARGER(HandlesCategory.VARIABLE, "Travel to recharger", null, HandlesVariant.HANDLES_VARIABLE_RECHARGER.getKey()),
    // bowls
    // buttons
    CONTROL(HandlesCategory.BUTTON, "Show control blocks", null, GuiVariant.CONTROL.getKey()),
    OPERATOR(HandlesCategory.BUTTON, "Show operator blocks", null, GuiVariant.OPERATOR.getKey()),
    VAR(HandlesCategory.BUTTON, "Show variable blocks", null, GuiVariant.VAR.getKey()),
    NUMBER(HandlesCategory.BUTTON, "Show number blocks", null, GuiVariant.NUMBER.getKey()),
    EVENT(HandlesCategory.BUTTON, "Show event blocks", null, GuiVariant.EVENT.getKey()),
    COMMAND(HandlesCategory.BUTTON, "Show command blocks", null, GuiVariant.COMMAND.getKey()),
    SELECTOR(HandlesCategory.BUTTON, "Show selector blocks", null, GuiVariant.SELECTOR.getKey()),
    PROGRAMS(HandlesCategory.BUTTON, "Show saved programs", null, GuiVariant.HANDLES_DISK.getKey()),
    SAVE(HandlesCategory.BUTTON, "Save the program to disk", null, GuiVariant.SAVE.getKey()),
    LEFT(HandlesCategory.BUTTON, "Scroll left", null, GuiVariant.LEFT.getKey()),
    RIGHT(HandlesCategory.BUTTON, "Scroll right", null, GuiVariant.RIGHT.getKey()),
    // music disks
    AREA_DISK(HandlesCategory.DISK, "Area Storage Disk", null, DiskVariant.AREA_DISK.getKey()),
    BIOME_DISK(HandlesCategory.DISK, "Biome Storage Disk", null, DiskVariant.BIOME_DISK.getKey()),
    PLAYER_DISK(HandlesCategory.DISK, "Player Storage Disk", null, DiskVariant.PLAYER_DISK.getKey()),
    SAVE_DISK(HandlesCategory.DISK, "Save Storage Disk", null, DiskVariant.SAVE_DISK.getKey());

    public final static HashMap<String, HandlesBlock> BY_NAME = new HashMap<>();
    private final static List<HandlesBlock> commands = new ArrayList<>();
    private final static List<HandlesBlock> controls = new ArrayList<>();
    private final static List<HandlesBlock> events = new ArrayList<>();
    private final static List<HandlesBlock> numbers = new ArrayList<>();
    private final static List<HandlesBlock> operators = new ArrayList<>();
    private final static List<HandlesBlock> selectors = new ArrayList<>();
    private final static List<HandlesBlock> variables = new ArrayList<>();
    private final static List<HandlesBlock> buttons = new ArrayList<>();

    static {
        for (HandlesBlock block : values()) {
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

    private final HandlesCategory category;
    private final String displayName;
    private final List<Component> lore;
    private final NamespacedKey model;

    HandlesBlock(HandlesCategory category, String displayName, List<Component> lore, NamespacedKey model) {
        this.category = category;
        this.displayName = displayName;
        this.lore = lore;
        this.model = model;
    }

    public static List<HandlesBlock> getCommands() {
        return commands;
    }

    public static List<HandlesBlock> getControls() {
        return controls;
    }

    public static List<HandlesBlock> getEvents() {
        return events;
    }

    public static List<HandlesBlock> getNumbers() {
        return numbers;
    }

    public static List<HandlesBlock> getOperators() {
        return operators;
    }

    public static List<HandlesBlock> getSelectors() {
        return selectors;
    }

    public static List<HandlesBlock> getVariables() {
        return variables;
    }

    public static List<HandlesBlock> getButtons() {
        return buttons;
    }

    public HandlesCategory getCategory() {
        return category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Component> getLore() {
        return lore;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
