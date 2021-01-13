/*
 * Copyright by FisheyLP, Version 1.3 (12.08.16)
 *
 * https://github.com/FisheyLP/TableGenerator
 * https://www.spigotmc.org/threads/help-making-a-chat-table-based.170306/
 *
 */
package me.eccentric_nz.TARDIS.messaging;

public class TableGeneratorCustomFont extends TableGenerator {

    private static final char char1 = '\uf821';

    public TableGeneratorCustomFont(Alignment... alignments) {
        super(char1, alignments);
    }
}
