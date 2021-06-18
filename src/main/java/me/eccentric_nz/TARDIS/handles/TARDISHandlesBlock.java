/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.handles;

import java.util.*;

/**
 * @author eccentric_nz
 */
public enum TardisHandlesBlock {

    // paper
    // command
    COMEHERE(TardisHandlesCategory.COMMAND, "/tardis comehere command", Arrays.asList("Make the TARDIS", "travel to you"), 1),
    HIDE(TardisHandlesCategory.COMMAND, "/tardis hide command", Arrays.asList("Make the TARDIS", "exterior invisible"), 2),
    REBUILD(TardisHandlesCategory.COMMAND, "/tardis rebuild command", Arrays.asList("Rebuild the", "TARDIS exterior"), 3),
    SCAN(TardisHandlesCategory.COMMAND, "TARDIS scan command", Arrays.asList("Scan the environment", "outside the TARDIS"), 4),
    TAKE_OFF(TardisHandlesCategory.COMMAND, "TARDIS take off command", Arrays.asList("Make the TARDIS enter", "the Time Vortex"), 5),
    TRAVEL(TardisHandlesCategory.COMMAND, "/tardistravel command", Arrays.asList("Make the TARDIS", "travel somewhere"), 6),
    LAND(TardisHandlesCategory.COMMAND, "TARDIS land command", Arrays.asList("Make the TARDIS exit", "the Time Vortex"), 7), // control
    FOR(TardisHandlesCategory.CONTROL, "FOR loop", Arrays.asList("Use to start a loop", "with a counter"), 8),
    TO(TardisHandlesCategory.CONTROL, "TO", Arrays.asList("Use with a FOR loop", "to specify the number", "of loops"), 9),
    DO(TardisHandlesCategory.CONTROL, "DO", Arrays.asList("Use after an", "IF or FOR loop"), 10),
    END(TardisHandlesCategory.CONTROL, "END", Arrays.asList("Use to finish", "a conditional or", "loop statement"), 11),
    IF(TardisHandlesCategory.CONTROL, "IF", Arrays.asList("Check whether a", "condition is", "true or false"), 12),
    ELSE(TardisHandlesCategory.CONTROL, "ELSE", Arrays.asList("Use to run actions", "if an IF is false"), 13),
    ELSE_IF(TardisHandlesCategory.CONTROL, "ELSE IF", Collections.singletonList("Check another condition"), 14),
    BREAK(TardisHandlesCategory.CONTROL, "BREAK", Arrays.asList("Use to break", "out of a loop"), 15), // event
    MATERIALISE(TardisHandlesCategory.EVENT, "TARDIS materialisation event", null, 16),
    DEMATERIALISE(TardisHandlesCategory.EVENT, "TARDIS dematerialisation event", null, 17),
    ARTRON(TardisHandlesCategory.EVENT, "Artron Level event", Arrays.asList("Use with Operator", "and Number blocks"), 18),
    DEATH(TardisHandlesCategory.EVENT, "Time Lord Death event", null, 19),
    ENTER(TardisHandlesCategory.EVENT, "TARDIS Entry event", null, 20),
    EXIT(TardisHandlesCategory.EVENT, "TARDIS Exit event", null, 21),
    HADS(TardisHandlesCategory.EVENT, "TARDIS H.A.D.S event", null, 22),
    SIEGE_ON(TardisHandlesCategory.EVENT, "Siege Mode On event", null, 23),
    SIEGE_OFF(TardisHandlesCategory.EVENT, "Siege Mode Off event", null, 24),
    LOG_OUT(TardisHandlesCategory.EVENT, "Player log out event", null, 25), // number
    ZERO(TardisHandlesCategory.NUMBER, "0", Collections.singletonList("The number zero"), 26),
    ONE(TardisHandlesCategory.NUMBER, "1", Collections.singletonList("The number one"), 27),
    TWO(TardisHandlesCategory.NUMBER, "2", Collections.singletonList("The number two"), 28),
    THREE(TardisHandlesCategory.NUMBER, "3", Collections.singletonList("The number three"), 29),
    FOUR(TardisHandlesCategory.NUMBER, "4", Collections.singletonList("The number four"), 30),
    FIVE(TardisHandlesCategory.NUMBER, "5", Collections.singletonList("The number five"), 31),
    SIX(TardisHandlesCategory.NUMBER, "6", Collections.singletonList("The number six"), 32),
    SEVEN(TardisHandlesCategory.NUMBER, "7", Collections.singletonList("The number seven"), 33),
    EIGHT(TardisHandlesCategory.NUMBER, "8", Collections.singletonList("The number eight"), 34),
    NINE(TardisHandlesCategory.NUMBER, "9", Collections.singletonList("The number nine"), 35),
    COIN(TardisHandlesCategory.NUMBER, "Flip a coin", Arrays.asList("Returns a true", "or false value"), 36),
    RANDOM(TardisHandlesCategory.NUMBER, "Random number", Arrays.asList("Generate a random number", "between 0 and the", "number that follows"), 37), // operator
    ASSIGNMENT(TardisHandlesCategory.OPERATOR, "Assignment operator", Arrays.asList("Used to assign a", "value to a variable"), 38),
    ADDITION(TardisHandlesCategory.OPERATOR, "Addition operator", Arrays.asList("Use to add two", "numbers together"), 39),
    SUBTRACTION(TardisHandlesCategory.OPERATOR, "Subtraction operator", Arrays.asList("Use to subtract one", "number from anther"), 40),
    MULTIPLICATION(TardisHandlesCategory.OPERATOR, "Multiplication operator", Arrays.asList("Use to multiply two", "numbers together"), 41),
    DIVISION(TardisHandlesCategory.OPERATOR, "Division operator", Arrays.asList("Use to divide one", "number by another"), 42),
    AND(TardisHandlesCategory.OPERATOR, "AND operator", Arrays.asList("Use to check", "both conditions", "are is true"), 43),
    OR(TardisHandlesCategory.OPERATOR, "OR operator", Arrays.asList("Use to check", "if either", "condition is true"), 44),
    EQUALS(TardisHandlesCategory.OPERATOR, "Equality operator", Arrays.asList("Checks if two", "objects are equal"), 45),
    NOT_EQUAL(TardisHandlesCategory.OPERATOR, "Not equal operator", Arrays.asList("Checks if two objects", "are not the same"), 46),
    LESS_THAN(TardisHandlesCategory.OPERATOR, "Less than operator", Arrays.asList("Checks if one number", "is smaller than another"), 47),
    LESS_THAN_EQUAL(TardisHandlesCategory.OPERATOR, "Less than or equal to operator", Arrays.asList("Checks if one number", "is smaller than or", "equal to another"), 48),
    GREATER_THAN(TardisHandlesCategory.OPERATOR, "Greater than operator", Arrays.asList("Checks if one number", "is bigger than another"), 49),
    GREATER_THAN_EQUAL(TardisHandlesCategory.OPERATOR, "Greater than or equal to operator", Arrays.asList("Checks if one number", "is bigger than or", "equal to another"), 50),
    MODULO(TardisHandlesCategory.OPERATOR, "Modulo operator", Arrays.asList("Gets the remainder", "left over after", "dividing two numbers"), 51), // selector
    TIME_LORD(TardisHandlesCategory.SELECTOR, "Target Time Lord", null, 52),
    COMPANIONS(TardisHandlesCategory.SELECTOR, "Target companions", null, 53),
    TARDIS(TardisHandlesCategory.SELECTOR, "The TARDIS", null, 54),
    DOOR(TardisHandlesCategory.SELECTOR, "TARDIS Door", Arrays.asList("Use with an", "Open, Close", "Lock or Unlock block"), 55),
    LIGHTS(TardisHandlesCategory.SELECTOR, "TARDIS Lights", Arrays.asList("Use with an", "On or Off block"), 56),
    POWER(TardisHandlesCategory.SELECTOR, "TARDIS Power action", Arrays.asList("Use with a Show,", "On or Off block"), 57),
    SIEGE(TardisHandlesCategory.SELECTOR, "Siege Mode action", Arrays.asList("Use with an", "On or Off block"), 58), // variable
    VARIABLE(TardisHandlesCategory.VARIABLE, "Variable", Arrays.asList("A generic container", "to hold a value"), 59),
    X(TardisHandlesCategory.VARIABLE, "X coordinate", Arrays.asList("Use with a number", "to specify a location"), 60),
    Y(TardisHandlesCategory.VARIABLE, "Y coordinate", Arrays.asList("Use with a number", "to specify a location"), 61),
    Z(TardisHandlesCategory.VARIABLE, "Z coordinate", Arrays.asList("Use with a number", "to specify a location"), 62),
    OPEN(TardisHandlesCategory.VARIABLE, "Open Door action", null, 63),
    CLOSE(TardisHandlesCategory.VARIABLE, "Close Door action", null, 64),
    LOCK(TardisHandlesCategory.VARIABLE, "Lock Door action", null, 65),
    UNLOCK(TardisHandlesCategory.VARIABLE, "Unlock Door action", null, 66),
    ON(TardisHandlesCategory.VARIABLE, "On action", null, 67),
    OFF(TardisHandlesCategory.VARIABLE, "Off action", null, 68),
    SHOW(TardisHandlesCategory.VARIABLE, "Show Artron Levels action", null, 69),
    REDSTONE(TardisHandlesCategory.VARIABLE, "Send Redstone signal", Arrays.asList("Use to power the block", "Handles is placed on"), 70),
    HOME(TardisHandlesCategory.VARIABLE, "Home Location", null, 71),
    RECHARGER(TardisHandlesCategory.VARIABLE, "Travel to recharger", null, 72), // bowls
    // buttons
    CONTROL(TardisHandlesCategory.BUTTON, "Show control blocks", null, 47),
    OPERATOR(TardisHandlesCategory.BUTTON, "Show operator blocks", null, 66),
    VAR(TardisHandlesCategory.BUTTON, "Show variable blocks", null, 83),
    NUMBER(TardisHandlesCategory.BUTTON, "Show number blocks", null, 65),
    EVENT(TardisHandlesCategory.BUTTON, "Show event blocks", null, 52),
    COMMAND(TardisHandlesCategory.BUTTON, "Show command blocks", null, 44),
    SELECTOR(TardisHandlesCategory.BUTTON, "Show selector blocks", null, 76),
    PROGRAMS(TardisHandlesCategory.BUTTON, "Show saved programs", null, 69),
    SAVE(TardisHandlesCategory.BUTTON, "Save the program to disk", null, 74),
    LEFT(TardisHandlesCategory.BUTTON, "Scroll left", null, 60),
    RIGHT(TardisHandlesCategory.BUTTON, "Scroll right", null, 73), // music disks
    AREA_DISK(TardisHandlesCategory.DISK, "Area Storage Disk", null, 10000001),
    BIOME_DISK(TardisHandlesCategory.DISK, "Biome Storage Disk", null, 10000001),
    PLAYER_DISK(TardisHandlesCategory.DISK, "Player Storage Disk", null, 10000001),
    SAVE_DISK(TardisHandlesCategory.DISK, "Save Storage Disk", null, 10000001);

    public final static HashMap<String, TardisHandlesBlock> BY_NAME = new HashMap<>();
    private final static List<TardisHandlesBlock> commands = new ArrayList<>();
    private final static List<TardisHandlesBlock> controls = new ArrayList<>();
    private final static List<TardisHandlesBlock> events = new ArrayList<>();
    private final static List<TardisHandlesBlock> numbers = new ArrayList<>();
    private final static List<TardisHandlesBlock> operators = new ArrayList<>();
    private final static List<TardisHandlesBlock> selectors = new ArrayList<>();
    private final static List<TardisHandlesBlock> variables = new ArrayList<>();
    private final static List<TardisHandlesBlock> buttons = new ArrayList<>();

    static {
        for (TardisHandlesBlock block : values()) {
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

    private final TardisHandlesCategory category;
    private final String displayName;
    private final List<String> lore;
    private final int customModelData;

    TardisHandlesBlock(TardisHandlesCategory category, String displayName, List<String> lore, int customModelData) {
        this.category = category;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
    }

    public static List<TardisHandlesBlock> getCommands() {
        return commands;
    }

    public static List<TardisHandlesBlock> getControls() {
        return controls;
    }

    public static List<TardisHandlesBlock> getEvents() {
        return events;
    }

    public static List<TardisHandlesBlock> getNumbers() {
        return numbers;
    }

    public static List<TardisHandlesBlock> getOperators() {
        return operators;
    }

    public static List<TardisHandlesBlock> getSelectors() {
        return selectors;
    }

    public static List<TardisHandlesBlock> getVariables() {
        return variables;
    }

    public static List<TardisHandlesBlock> getButtons() {
        return buttons;
    }

    public TardisHandlesCategory getCategory() {
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
