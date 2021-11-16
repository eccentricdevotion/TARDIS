package me.eccentric_nz.TARDIS.enumeration;

public enum Theme {

    SIXTY_THREE("theme_63", 2620),
    ZERO_FIVE("theme_05", 1340),
    TWENTY_TWENTY("theme_20", 2080),
    RANDOM("theme", 2620);

    String filename;
    long length;

    Theme(String filename, long length) {
        this.filename = filename;
        this.length = length;
    }

    public String getFilename() {
        return filename;
    }

    public long getLength() {
        return length;
    }
}
