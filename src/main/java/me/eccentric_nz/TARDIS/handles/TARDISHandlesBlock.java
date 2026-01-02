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
public enum TARDISHandlesBlock {

    // paper
    // command
    COMEHERE(TARDISHandlesCategory.COMMAND, "/tardis comehere command", List.of(Component.text("Make the TARDIS"), Component.text("travel to you")), HandlesVariant.HANDLES_COMMAND_COMEHERE.getKey()),
    HIDE(TARDISHandlesCategory.COMMAND, "/tardis hide command", List.of(Component.text("Make the TARDIS"), Component.text("exterior invisible")), HandlesVariant.HANDLES_COMMAND_HIDE.getKey()),
    REBUILD(TARDISHandlesCategory.COMMAND, "/tardis rebuild command", List.of(Component.text("Rebuild the"), Component.text("TARDIS exterior")), HandlesVariant.HANDLES_COMMAND_REBUILD.getKey()),
    SCAN(TARDISHandlesCategory.COMMAND, "TARDIS scan command", List.of(Component.text("Scan the environment"), Component.text("outside the TARDIS")), HandlesVariant.HANDLES_COMMAND_SCAN.getKey()),
    TAKE_OFF(TARDISHandlesCategory.COMMAND, "TARDIS take off command", List.of(Component.text("Make the TARDIS enter"), Component.text("the Time Vortex")), HandlesVariant.HANDLES_COMMAND_TAKE_OFF.getKey()),
    TRAVEL(TARDISHandlesCategory.COMMAND, "/tardistravel command", List.of(Component.text("Make the TARDIS"), Component.text("travel somewhere")), HandlesVariant.HANDLES_COMMAND_TRAVEL.getKey()),
    LAND(TARDISHandlesCategory.COMMAND, "TARDIS land command", List.of(Component.text("Make the TARDIS exit"), Component.text("the Time Vortex")), HandlesVariant.HANDLES_COMMAND_LAND.getKey()),
    // control
    FOR(TARDISHandlesCategory.CONTROL, "FOR loop", List.of(Component.text("Use to start a loop"), Component.text("with a counter")), HandlesVariant.HANDLES_CONTROL_FOR.getKey()),
    TO(TARDISHandlesCategory.CONTROL, "TO", List.of(Component.text("Use with a FOR loop"), Component.text("to specify the number"), Component.text("of loops")), HandlesVariant.HANDLES_CONTROL_TO.getKey()),
    DO(TARDISHandlesCategory.CONTROL, "DO", List.of(Component.text("Use after an"), Component.text("IF or FOR loop")), HandlesVariant.HANDLES_CONTROL_DO.getKey()),
    END(TARDISHandlesCategory.CONTROL, "END", List.of(Component.text("Use to finish"), Component.text("a conditional or"), Component.text("loop statement")), HandlesVariant.HANDLES_CONTROL_END.getKey()),
    IF(TARDISHandlesCategory.CONTROL, "IF", List.of(Component.text("Check whether a"), Component.text("condition is"), Component.text("true or false")), HandlesVariant.HANDLES_CONTROL_IF.getKey()),
    ELSE(TARDISHandlesCategory.CONTROL, "ELSE", List.of(Component.text("Use to run actions"), Component.text("if an IF is false")), HandlesVariant.HANDLES_CONTROL_ELSE.getKey()),
    ELSE_IF(TARDISHandlesCategory.CONTROL, "ELSE IF", List.of(Component.text("Check another condition")), HandlesVariant.HANDLES_CONTROL_ELSE_IF.getKey()),
    BREAK(TARDISHandlesCategory.CONTROL, "BREAK", List.of(Component.text("Use to break"), Component.text("out of a loop")), HandlesVariant.HANDLES_CONTROL_BREAK.getKey()),
    // event
    MATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS materialisation event", null, HandlesVariant.HANDLES_EVENT_MATERIALISE.getKey()),
    DEMATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS dematerialisation event", null, HandlesVariant.HANDLES_EVENT_DEMATERIALISE.getKey()),
    ARTRON(TARDISHandlesCategory.EVENT, "Artron Level event", List.of(Component.text("Use with Operator"), Component.text("and Number blocks")), HandlesVariant.HANDLES_EVENT_ARTRON.getKey()),
    DEATH(TARDISHandlesCategory.EVENT, "Time Lord Death event", null, HandlesVariant.HANDLES_EVENT_DEATH.getKey()),
    ENTER(TARDISHandlesCategory.EVENT, "TARDIS Entry event", null, HandlesVariant.HANDLES_EVENT_ENTER.getKey()),
    EXIT(TARDISHandlesCategory.EVENT, "TARDIS Exit event", null, HandlesVariant.HANDLES_EVENT_EXIT.getKey()),
    HADS(TARDISHandlesCategory.EVENT, "TARDIS H.A.D.S event", null, HandlesVariant.HANDLES_EVENT_HADS.getKey()),
    SIEGE_ON(TARDISHandlesCategory.EVENT, "Siege Mode On event", null, HandlesVariant.HANDLES_EVENT_SIEGE_ON.getKey()),
    SIEGE_OFF(TARDISHandlesCategory.EVENT, "Siege Mode Off event", null, HandlesVariant.HANDLES_EVENT_SIEGE_OFF.getKey()),
    LOG_OUT(TARDISHandlesCategory.EVENT, "Player log out event", null, HandlesVariant.HANDLES_EVENT_LOG_OUT.getKey()),
    // number
    ZERO(TARDISHandlesCategory.NUMBER, "0", List.of(Component.text("The number zero")), HandlesVariant.HANDLES_NUMBER_ZERO.getKey()),
    ONE(TARDISHandlesCategory.NUMBER, "1", List.of(Component.text("The number one")), HandlesVariant.HANDLES_NUMBER_ONE.getKey()),
    TWO(TARDISHandlesCategory.NUMBER, "2", List.of(Component.text("The number two")), HandlesVariant.HANDLES_NUMBER_TWO.getKey()),
    THREE(TARDISHandlesCategory.NUMBER, "3", List.of(Component.text("The number three")), HandlesVariant.HANDLES_NUMBER_THREE.getKey()),
    FOUR(TARDISHandlesCategory.NUMBER, "4", List.of(Component.text("The number four")), HandlesVariant.HANDLES_NUMBER_FOUR.getKey()),
    FIVE(TARDISHandlesCategory.NUMBER, "5", List.of(Component.text("The number five")), HandlesVariant.HANDLES_NUMBER_FIVE.getKey()),
    SIX(TARDISHandlesCategory.NUMBER, "6", List.of(Component.text("The number six")), HandlesVariant.HANDLES_NUMBER_SIX.getKey()),
    SEVEN(TARDISHandlesCategory.NUMBER, "7", List.of(Component.text("The number seven")), HandlesVariant.HANDLES_NUMBER_SEVEN.getKey()),
    EIGHT(TARDISHandlesCategory.NUMBER, "8", List.of(Component.text("The number eight")), HandlesVariant.HANDLES_NUMBER_EIGHT.getKey()),
    NINE(TARDISHandlesCategory.NUMBER, "9", List.of(Component.text("The number nine")), HandlesVariant.HANDLES_NUMBER_NINE.getKey()),
    COIN(TARDISHandlesCategory.NUMBER, "Flip a coin", List.of(Component.text("Returns a true"), Component.text("or false value")), HandlesVariant.HANDLES_NUMBER_COIN.getKey()),
    RANDOM(TARDISHandlesCategory.NUMBER, "Random number", List.of(Component.text("Generate a random number"), Component.text("between 0 and the"), Component.text("number that follows")), HandlesVariant.HANDLES_NUMBER_RANDOM.getKey()),
    // operator
    ASSIGNMENT(TARDISHandlesCategory.OPERATOR, "Assignment operator", List.of(Component.text("Used to assign a"), Component.text("value to a variable")), HandlesVariant.HANDLES_OPERATOR_ASSIGNMENT.getKey()),
    ADDITION(TARDISHandlesCategory.OPERATOR, "Addition operator", List.of(Component.text("Use to add two"), Component.text("numbers together")), HandlesVariant.HANDLES_OPERATOR_ADDITION.getKey()),
    SUBTRACTION(TARDISHandlesCategory.OPERATOR, "Subtraction operator", List.of(Component.text("Use to subtract one"), Component.text("number from anther")), HandlesVariant.HANDLES_OPERATOR_SUBTRACTION.getKey()),
    MULTIPLICATION(TARDISHandlesCategory.OPERATOR, "Multiplication operator", List.of(Component.text("Use to multiply two"), Component.text("numbers together")), HandlesVariant.HANDLES_OPERATOR_MULTIPLICATION.getKey()),
    DIVISION(TARDISHandlesCategory.OPERATOR, "Division operator", List.of(Component.text("Use to divide one"), Component.text("number by another")), HandlesVariant.HANDLES_OPERATOR_DIVISION.getKey()),
    AND(TARDISHandlesCategory.OPERATOR, "AND operator", List.of(Component.text("Use to check"), Component.text("both conditions"), Component.text("are is true")), HandlesVariant.HANDLES_OPERATOR_AND.getKey()),
    OR(TARDISHandlesCategory.OPERATOR, "OR operator", List.of(Component.text("Use to check"), Component.text("if either"), Component.text("condition is true")), HandlesVariant.HANDLES_OPERATOR_OR.getKey()),
    EQUALS(TARDISHandlesCategory.OPERATOR, "Equality operator", List.of(Component.text("Checks if two"), Component.text("objects are equal")), HandlesVariant.HANDLES_OPERATOR_EQUALS.getKey()),
    NOT_EQUAL(TARDISHandlesCategory.OPERATOR, "Not equal operator", List.of(Component.text("Checks if two objects"), Component.text("are not the same")), HandlesVariant.HANDLES_OPERATOR_NOT_EQUAL.getKey()),
    LESS_THAN(TARDISHandlesCategory.OPERATOR, "Less than operator", List.of(Component.text("Checks if one number"), Component.text("is smaller than another")), HandlesVariant.HANDLES_OPERATOR_LESS_THAN.getKey()),
    LESS_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Less than or equal to operator", List.of(Component.text("Checks if one number"), Component.text("is smaller than or"), Component.text("equal to another")), HandlesVariant.HANDLES_OPERATOR_LESS_THAN_EQUAL.getKey()),
    GREATER_THAN(TARDISHandlesCategory.OPERATOR, "Greater than operator", List.of(Component.text("Checks if one number"), Component.text("is bigger than another")), HandlesVariant.HANDLES_OPERATOR_GREATER_THAN.getKey()),
    GREATER_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Greater than or equal to operator", List.of(Component.text("Checks if one number"), Component.text("is bigger than or"), Component.text("equal to another")), HandlesVariant.HANDLES_OPERATOR_GREATER_THAN_EQUAL.getKey()),
    MODULO(TARDISHandlesCategory.OPERATOR, "Modulo operator", List.of(Component.text("Gets the remainder"), Component.text("left over after"), Component.text("dividing two numbers")), HandlesVariant.HANDLES_OPERATOR_MODULO.getKey()),
    // selector
    TIME_LORD(TARDISHandlesCategory.SELECTOR, "Target Time Lord", null, HandlesVariant.HANDLES_SELECTOR_TIME_LORD.getKey()),
    COMPANIONS(TARDISHandlesCategory.SELECTOR, "Target companions", null, HandlesVariant.HANDLES_SELECTOR_COMPANIONS.getKey()),
    TARDIS(TARDISHandlesCategory.SELECTOR, "The TARDIS", null, HandlesVariant.HANDLES_SELECTOR_TARDIS.getKey()),
    DOOR(TARDISHandlesCategory.SELECTOR, "TARDIS Door", List.of(Component.text("Use with an"), Component.text("Open, Close"), Component.text("Lock or Unlock block")), HandlesVariant.HANDLES_SELECTOR_DOOR.getKey()),
    LIGHTS(TARDISHandlesCategory.SELECTOR, "TARDIS Lights", List.of(Component.text("Use with an"), Component.text("On or Off block")), HandlesVariant.HANDLES_SELECTOR_LIGHTS.getKey()),
    POWER(TARDISHandlesCategory.SELECTOR, "TARDIS Power action", List.of(Component.text("Use with a Show,"), Component.text("On or Off block")), HandlesVariant.HANDLES_SELECTOR_POWER.getKey()),
    SIEGE(TARDISHandlesCategory.SELECTOR, "Siege Mode action", List.of(Component.text("Use with an"), Component.text("On or Off block")), HandlesVariant.HANDLES_SELECTOR_SIEGE.getKey()),
    // variable
    VARIABLE(TARDISHandlesCategory.VARIABLE, "Variable", List.of(Component.text("A generic container"), Component.text("to hold a value")), HandlesVariant.HANDLES_VARIABLE_VARIABLE.getKey()),
    X(TARDISHandlesCategory.VARIABLE, "X coordinate", List.of(Component.text("Use with a number"), Component.text("to specify a location")), HandlesVariant.HANDLES_VARIABLE_X.getKey()),
    Y(TARDISHandlesCategory.VARIABLE, "Y coordinate", List.of(Component.text("Use with a number"), Component.text("to specify a location")), HandlesVariant.HANDLES_VARIABLE_Y.getKey()),
    Z(TARDISHandlesCategory.VARIABLE, "Z coordinate", List.of(Component.text("Use with a number"), Component.text("to specify a location")), HandlesVariant.HANDLES_VARIABLE_Z.getKey()),
    OPEN(TARDISHandlesCategory.VARIABLE, "Open Door action", null, HandlesVariant.HANDLES_VARIABLE_OPEN.getKey()),
    CLOSE(TARDISHandlesCategory.VARIABLE, "Close Door action", null, HandlesVariant.HANDLES_VARIABLE_CLOSE.getKey()),
    LOCK(TARDISHandlesCategory.VARIABLE, "Lock Door action", null, HandlesVariant.HANDLES_VARIABLE_LOCK.getKey()),
    UNLOCK(TARDISHandlesCategory.VARIABLE, "Unlock Door action", null, HandlesVariant.HANDLES_VARIABLE_UNLOCK.getKey()),
    ON(TARDISHandlesCategory.VARIABLE, "On action", null, HandlesVariant.HANDLES_VARIABLE_ON.getKey()),
    OFF(TARDISHandlesCategory.VARIABLE, "Off action", null, HandlesVariant.HANDLES_VARIABLE_OFF.getKey()),
    SHOW(TARDISHandlesCategory.VARIABLE, "Show Artron Levels action", null, HandlesVariant.HANDLES_VARIABLE_SHOW.getKey()),
    REDSTONE(TARDISHandlesCategory.VARIABLE, "Send Redstone signal", List.of(Component.text("Use to power the block"), Component.text("Handles is placed on")), HandlesVariant.HANDLES_VARIABLE_REDSTONE.getKey()),
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
    private final List<Component> lore;
    private final NamespacedKey model;

    TARDISHandlesBlock(TARDISHandlesCategory category, String displayName, List<Component> lore, NamespacedKey model) {
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

    public List<Component> getLore() {
        return lore;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
