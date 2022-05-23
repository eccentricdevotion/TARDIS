/*
 * Copyright (C) 2022 eccentric_nz
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

import java.util.*;

/**
 * @author eccentric_nz
 */
public enum TARDISHandlesBlock {

    // paper
    // command
    COMEHERE(TARDISHandlesCategory.COMMAND, "/tardis comehere command", Arrays.asList("Make the TARDIS", "travel to you"), 1),
    HIDE(TARDISHandlesCategory.COMMAND, "/tardis hide command", Arrays.asList("Make the TARDIS", "exterior invisible"), 2),
    REBUILD(TARDISHandlesCategory.COMMAND, "/tardis rebuild command", Arrays.asList("Rebuild the", "TARDIS exterior"), 3),
    SCAN(TARDISHandlesCategory.COMMAND, "TARDIS scan command", Arrays.asList("Scan the environment", "outside the TARDIS"), 4),
    TAKE_OFF(TARDISHandlesCategory.COMMAND, "TARDIS take off command", Arrays.asList("Make the TARDIS enter", "the Time Vortex"), 5),
    TRAVEL(TARDISHandlesCategory.COMMAND, "/tardistravel command", Arrays.asList("Make the TARDIS", "travel somewhere"), 6),
    LAND(TARDISHandlesCategory.COMMAND, "TARDIS land command", Arrays.asList("Make the TARDIS exit", "the Time Vortex"), 7),
    // control
    FOR(TARDISHandlesCategory.CONTROL, "FOR loop", Arrays.asList("Use to start a loop", "with a counter"), 8),
    TO(TARDISHandlesCategory.CONTROL, "TO", Arrays.asList("Use with a FOR loop", "to specify the number", "of loops"), 9),
    DO(TARDISHandlesCategory.CONTROL, "DO", Arrays.asList("Use after an", "IF or FOR loop"), 10),
    END(TARDISHandlesCategory.CONTROL, "END", Arrays.asList("Use to finish", "a conditional or", "loop statement"), 11),
    IF(TARDISHandlesCategory.CONTROL, "IF", Arrays.asList("Check whether a", "condition is", "true or false"), 12),
    ELSE(TARDISHandlesCategory.CONTROL, "ELSE", Arrays.asList("Use to run actions", "if an IF is false"), 13),
    ELSE_IF(TARDISHandlesCategory.CONTROL, "ELSE IF", Collections.singletonList("Check another condition"), 14),
    BREAK(TARDISHandlesCategory.CONTROL, "BREAK", Arrays.asList("Use to break", "out of a loop"), 15),
    // event
    MATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS materialisation event", null, 16),
    DEMATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS dematerialisation event", null, 17),
    ARTRON(TARDISHandlesCategory.EVENT, "Artron Level event", Arrays.asList("Use with Operator", "and Number blocks"), 18),
    DEATH(TARDISHandlesCategory.EVENT, "Time Lord Death event", null, 19),
    ENTER(TARDISHandlesCategory.EVENT, "TARDIS Entry event", null, 20),
    EXIT(TARDISHandlesCategory.EVENT, "TARDIS Exit event", null, 21),
    HADS(TARDISHandlesCategory.EVENT, "TARDIS H.A.D.S event", null, 22),
    SIEGE_ON(TARDISHandlesCategory.EVENT, "Siege Mode On event", null, 23),
    SIEGE_OFF(TARDISHandlesCategory.EVENT, "Siege Mode Off event", null, 24),
    LOG_OUT(TARDISHandlesCategory.EVENT, "Player log out event", null, 25),
    // number
    ZERO(TARDISHandlesCategory.NUMBER, "0", Collections.singletonList("The number zero"), 26),
    ONE(TARDISHandlesCategory.NUMBER, "1", Collections.singletonList("The number one"), 27),
    TWO(TARDISHandlesCategory.NUMBER, "2", Collections.singletonList("The number two"), 28),
    THREE(TARDISHandlesCategory.NUMBER, "3", Collections.singletonList("The number three"), 29),
    FOUR(TARDISHandlesCategory.NUMBER, "4", Collections.singletonList("The number four"), 30),
    FIVE(TARDISHandlesCategory.NUMBER, "5", Collections.singletonList("The number five"), 31),
    SIX(TARDISHandlesCategory.NUMBER, "6", Collections.singletonList("The number six"), 32),
    SEVEN(TARDISHandlesCategory.NUMBER, "7", Collections.singletonList("The number seven"), 33),
    EIGHT(TARDISHandlesCategory.NUMBER, "8", Collections.singletonList("The number eight"), 34),
    NINE(TARDISHandlesCategory.NUMBER, "9", Collections.singletonList("The number nine"), 35),
    COIN(TARDISHandlesCategory.NUMBER, "Flip a coin", Arrays.asList("Returns a true", "or false value"), 36),
    RANDOM(TARDISHandlesCategory.NUMBER, "Random number", Arrays.asList("Generate a random number", "between 0 and the", "number that follows"), 37),
    // operator
    ASSIGNMENT(TARDISHandlesCategory.OPERATOR, "Assignment operator", Arrays.asList("Used to assign a", "value to a variable"), 38),
    ADDITION(TARDISHandlesCategory.OPERATOR, "Addition operator", Arrays.asList("Use to add two", "numbers together"), 39),
    SUBTRACTION(TARDISHandlesCategory.OPERATOR, "Subtraction operator", Arrays.asList("Use to subtract one", "number from anther"), 40),
    MULTIPLICATION(TARDISHandlesCategory.OPERATOR, "Multiplication operator", Arrays.asList("Use to multiply two", "numbers together"), 41),
    DIVISION(TARDISHandlesCategory.OPERATOR, "Division operator", Arrays.asList("Use to divide one", "number by another"), 42),
    AND(TARDISHandlesCategory.OPERATOR, "AND operator", Arrays.asList("Use to check", "both conditions", "are is true"), 43),
    OR(TARDISHandlesCategory.OPERATOR, "OR operator", Arrays.asList("Use to check", "if either", "condition is true"), 44),
    EQUALS(TARDISHandlesCategory.OPERATOR, "Equality operator", Arrays.asList("Checks if two", "objects are equal"), 45),
    NOT_EQUAL(TARDISHandlesCategory.OPERATOR, "Not equal operator", Arrays.asList("Checks if two objects", "are not the same"), 46),
    LESS_THAN(TARDISHandlesCategory.OPERATOR, "Less than operator", Arrays.asList("Checks if one number", "is smaller than another"), 47),
    LESS_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Less than or equal to operator", Arrays.asList("Checks if one number", "is smaller than or", "equal to another"), 48),
    GREATER_THAN(TARDISHandlesCategory.OPERATOR, "Greater than operator", Arrays.asList("Checks if one number", "is bigger than another"), 49),
    GREATER_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Greater than or equal to operator", Arrays.asList("Checks if one number", "is bigger than or", "equal to another"), 50),
    MODULO(TARDISHandlesCategory.OPERATOR, "Modulo operator", Arrays.asList("Gets the remainder", "left over after", "dividing two numbers"), 51),
    // selector
    TIME_LORD(TARDISHandlesCategory.SELECTOR, "Target Time Lord", null, 52),
    COMPANIONS(TARDISHandlesCategory.SELECTOR, "Target companions", null, 53),
    TARDIS(TARDISHandlesCategory.SELECTOR, "The TARDIS", null, 54),
    DOOR(TARDISHandlesCategory.SELECTOR, "TARDIS Door", Arrays.asList("Use with an", "Open, Close", "Lock or Unlock block"), 55),
    LIGHTS(TARDISHandlesCategory.SELECTOR, "TARDIS Lights", Arrays.asList("Use with an", "On or Off block"), 56),
    POWER(TARDISHandlesCategory.SELECTOR, "TARDIS Power action", Arrays.asList("Use with a Show,", "On or Off block"), 57),
    SIEGE(TARDISHandlesCategory.SELECTOR, "Siege Mode action", Arrays.asList("Use with an", "On or Off block"), 58),
    // variable
    VARIABLE(TARDISHandlesCategory.VARIABLE, "Variable", Arrays.asList("A generic container", "to hold a value"), 59),
    X(TARDISHandlesCategory.VARIABLE, "X coordinate", Arrays.asList("Use with a number", "to specify a location"), 60),
    Y(TARDISHandlesCategory.VARIABLE, "Y coordinate", Arrays.asList("Use with a number", "to specify a location"), 61),
    Z(TARDISHandlesCategory.VARIABLE, "Z coordinate", Arrays.asList("Use with a number", "to specify a location"), 62),
    OPEN(TARDISHandlesCategory.VARIABLE, "Open Door action", null, 63),
    CLOSE(TARDISHandlesCategory.VARIABLE, "Close Door action", null, 64),
    LOCK(TARDISHandlesCategory.VARIABLE, "Lock Door action", null, 65),
    UNLOCK(TARDISHandlesCategory.VARIABLE, "Unlock Door action", null, 66),
    ON(TARDISHandlesCategory.VARIABLE, "On action", null, 67),
    OFF(TARDISHandlesCategory.VARIABLE, "Off action", null, 68),
    SHOW(TARDISHandlesCategory.VARIABLE, "Show Artron Levels action", null, 69),
    REDSTONE(TARDISHandlesCategory.VARIABLE, "Send Redstone signal", Arrays.asList("Use to power the block", "Handles is placed on"), 70),
    HOME(TARDISHandlesCategory.VARIABLE, "Home Location", null, 71),
    RECHARGER(TARDISHandlesCategory.VARIABLE, "Travel to recharger", null, 72),
    // bowls
    // buttons
    CONTROL(TARDISHandlesCategory.BUTTON, "Show control blocks", null, 47),
    OPERATOR(TARDISHandlesCategory.BUTTON, "Show operator blocks", null, 66),
    VAR(TARDISHandlesCategory.BUTTON, "Show variable blocks", null, 83),
    NUMBER(TARDISHandlesCategory.BUTTON, "Show number blocks", null, 65),
    EVENT(TARDISHandlesCategory.BUTTON, "Show event blocks", null, 52),
    COMMAND(TARDISHandlesCategory.BUTTON, "Show command blocks", null, 44),
    SELECTOR(TARDISHandlesCategory.BUTTON, "Show selector blocks", null, 76),
    PROGRAMS(TARDISHandlesCategory.BUTTON, "Show saved programs", null, 69),
    SAVE(TARDISHandlesCategory.BUTTON, "Save the program to disk", null, 74),
    LEFT(TARDISHandlesCategory.BUTTON, "Scroll left", null, 60),
    RIGHT(TARDISHandlesCategory.BUTTON, "Scroll right", null, 73),
    // music disks
    AREA_DISK(TARDISHandlesCategory.DISK, "Area Storage Disk", null, 10000001),
    BIOME_DISK(TARDISHandlesCategory.DISK, "Biome Storage Disk", null, 10000001),
    PLAYER_DISK(TARDISHandlesCategory.DISK, "Player Storage Disk", null, 10000001),
    SAVE_DISK(TARDISHandlesCategory.DISK, "Save Storage Disk", null, 10000001);

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
                case BUTTON:
                    buttons.add(block);
                    break;
                case COMMAND:
                    commands.add(block);
                    break;
                case CONTROL:
                    controls.add(block);
                    break;
                case EVENT:
                    events.add(block);
                    break;
                case NUMBER:
                    numbers.add(block);
                    break;
                case OPERATOR:
                    operators.add(block);
                    break;
                case SELECTOR:
                    selectors.add(block);
                    break;
                case VARIABLE:
                    variables.add(block);
                    break;
                default:
                    break;
            }
            BY_NAME.put(block.displayName, block);
        }
    }

    private final TARDISHandlesCategory category;
    private final String displayName;
    private final List<String> lore;
    private final int customModelData;

    TARDISHandlesBlock(TARDISHandlesCategory category, String displayName, List<String> lore, int customModelData) {
        this.category = category;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
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

    public int getCustomModelData() {
        return customModelData;
    }
}
