package me.eccentric_nz.TARDIS.database.converters;


public record SQLTable(String table, String id, String column) {

    public SQLTable(String table, String id) {
        this(table, id, "location");
    }
}

